package org.downloadManger.downloader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

import com.github.axet.wget.DirectMultipart;
import com.github.axet.wget.WGet;
import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.DownloadInfo.Part;
import com.github.axet.wget.info.DownloadInfo.Part.States;
import com.github.axet.wget.info.ex.DownloadMultipartError;

@Component("DownloadFile")
public class DownloadFile extends Thread {

	private File savedFilePath;
	private URL link;
	AtomicBoolean stop = new AtomicBoolean(false);
	DownloadInfo info;
	long last;

	public DownloadFile() {

	}

	public void setParams(File savedFilePath, URL link) {
		this.link = link;
		this.savedFilePath = savedFilePath;
	}

	@Override
	public void run() {
		download();
	}

	private boolean download() {

		try {
			Thread notify = new Thread() {

				@Override
				public void run() {
					// notify app or save download state
					// you can extract information from DownloadInfo info;
					switch (info.getState()) {
					case EXTRACTING:
					case EXTRACTING_DONE:
					case DONE:
						System.out.println(info.getState());
						break;
					case RETRYING:
						System.out.println(info.getState() + " "
								+ info.getDelay());
						break;
					case DOWNLOADING:
						long now = System.currentTimeMillis();
						if (now - 1000 > last) {
							last = now;

							String parts = "";
							for (Part p : info.getParts()) {
								if (p.getState().equals(States.DOWNLOADING)) {
									parts += String.format("Part#%d(%.2f) ",
											p.getNumber(), p.getCount()
													/ (float) p.getLength());
								}
							}

							System.out.println(String.format("%.2f %s",
									info.getCount() / (float) info.getLength(),
									parts));
						}
						break;
					default:
						break;
					}
				}
			};
			// <dependency>
			// <groupId>com.github.axet</groupId>
			// <artifactId>wget</artifactId>
			// <version>1.2.12</version>
			// <scope>compile</scope>
			// </dependency>
			
//			<plugin>
//	        <artifactId>maven-compiler-plugin</artifactId>
//	        <version>2.3.2</version>
//	        <configuration>
//	          <source>1.6</source>
//	          <target>1.6</target>
//	          <encoding>UTF-8</encoding>
//	        </configuration>
//	      </plugin>
			
			// choise file
			URL url = link;
			// initialize url information object
			info = new DownloadInfo(url);
			// extract infromation from the web
			info.extract(stop, notify);
			// enable multipart donwload
			info.enableMultipart();
			// Choise target file
			File target = savedFilePath;
			// create wget downloader
			WGet w = new WGet(info, target);
			w.updateDirectboolSize(250);
			// will blocks until download finishes
			w.download(stop, notify);
		} catch (DownloadMultipartError e) {
			for (Part p : e.getInfo().getParts()) {
				Throwable ee = p.getException();
				if (ee != null)
					ee.printStackTrace();
			}
			return false;
		} catch (RuntimeException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
		return true;

	}

}
/*
 * InputStream in = new BufferedInputStream(link.openStream());
 * ByteArrayOutputStream out = new ByteArrayOutputStream(); byte[] buf = new
 * byte[1024]; int n = 0; while (-1 != (n = in.read(buf))) { out.write(buf, 0,
 * n); } out.close(); in.close(); byte[] response = out.toByteArray();
 * 
 * FileOutputStream fos = new FileOutputStream(savedFilePath);
 * fos.write(response); fos.close(); // End download code return true;
 */