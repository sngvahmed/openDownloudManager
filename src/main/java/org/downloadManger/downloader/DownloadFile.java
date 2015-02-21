package org.downloadManger.downloader;

import java.io.File;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import org.downloadManger.gui.DownloadListNotifier;
import org.springframework.stereotype.Component;

import com.github.axet.wget.WGet;
import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.DownloadInfo.Part;
import com.github.axet.wget.info.ex.DownloadMultipartError;

@Component("DownloadFile")
public class DownloadFile extends Thread {

	private File savedFilePath;
	private AtomicBoolean stop = new AtomicBoolean(false);
	private DownloadInfo info;
	private int fileNumber;
	private URL url;

	public DownloadFile() {

	}

	public DownloadFile(File savedFilePath, URL link, int fileNumber) {
		this.savedFilePath = savedFilePath;
		this.fileNumber = fileNumber;
		this.url = link;
		// initialize url information object
		info = new DownloadInfo(link);
	}

	@Override
	public void run() {
		createDownloadListRow();
		download();
	}

	private void createDownloadListRow() {
		Vector<Object> rowData = new Vector<Object>();
		rowData.add(savedFilePath.getName());
		rowData.add("");
		rowData.add("");
		rowData.add("");
		rowData.add("");
		rowData.add("");
		DownloadListNotifier.notifyNewDownloadRecord(rowData);
	}

	private boolean download() {

		try {
			DownloadNotificationThread notify = new DownloadNotificationThread(
					info, fileNumber);

			downloadMultiPart(notify);

		} catch (DownloadMultipartError e) {
			for (Part p : e.getInfo().getParts()) {
				Throwable ee = p.getException();
				if (ee != null)
					ee.printStackTrace();
			}
			return false;
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;

	}

	private void downloadMultiPart(DownloadNotificationThread notify) {
		// extract infromation from the web
		info.extract(stop, notify);
		// enable multipart donwload
		info.enableMultipart();

		DownloadListNotifier.notifyFileSize(info.getLength(), fileNumber);

		// Choise target file
		File target = savedFilePath;
		// create wget downloader
		WGet w = new WGet(info, target);
		// will blocks until download finishes
		w.updateDirectboolSize(7);
		w.download(stop, notify);
	}
}
