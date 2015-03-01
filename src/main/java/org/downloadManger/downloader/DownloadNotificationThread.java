package org.downloadManger.downloader;

import java.awt.List;
import java.net.URL;
import java.util.ArrayList;

import org.downloadManger.gui.DownloadListNotifier;
import org.downloadManger.gui.ProgressFrameNotifier;

import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.DownloadInfo.Part;
import com.github.axet.wget.info.DownloadInfo.Part.States;

public class DownloadNotificationThread extends Thread {

	private DownloadInfo info;
	private URL url;
	private int fileNumber;
	private long previouseBytes = 0;
	private long last;
	private ProgressFrameNotifier progressFrameNotifier;

	public DownloadNotificationThread(
			ProgressFrameNotifier progressFrameNotifier, DownloadInfo info,
			URL url, int fileNumber) {
		this.info = info;
		this.fileNumber = fileNumber;
		this.progressFrameNotifier = progressFrameNotifier;
		this.url = url;
	}

	public void setInfo(DownloadInfo info) {
		this.info = info;
	}

	@Override
	public void run() {
		// notify app or save download state
		// you can extract information from DownloadInfo info;
		DownloadListNotifier.notifyState(info.getState(), fileNumber);

		switch (info.getState()) {

		case DONE:
			System.out.println(info.getState());
			break;
		case RETRYING:
			System.out.println(info.getState() + " " + info.getDelay());
			break;
		case DOWNLOADING:
			long now = System.currentTimeMillis();
			if (now - 500 > last) {
				last = now;
				progressFrameNotifier.notifyTitle(DownloadInfoCalculator
						.calcProgress(info.getCount(), info.getLength()));
				progressFrameNotifier.notifyProgressBar(DownloadInfoCalculator
						.calcProgress(info.getCount(), info.getLength()));
				progressFrameNotifier.notifyUrl(url.toString());
				progressFrameNotifier.notifyStatusTable(info.getState()
						.toString(), 0, 1);

				progressFrameNotifier.notifyStatusTable(
						String.format("%.2f", DownloadInfoCalculator
								.calcFileSize(info.getLength()))
								+ " MB", 1, 1);
				progressFrameNotifier.notifyStatusTable(
						String.format("%.2f", DownloadInfoCalculator
								.calcDownloadedSize(info.getCount()))
								+ " MB ( "
								+ DownloadInfoCalculator.calcProgress(
										info.getCount(), info.getLength())
								+ "% )", 2, 1);
				progressFrameNotifier.notifyStatusTable(
						String.format(
								"%.2f",
								DownloadInfoCalculator.calcTransfareRate(
										info.getCount(), previouseBytes))
								+ " MB", 3, 1);
				progressFrameNotifier.notifyStatusTable(
						String.format("%.2f", DownloadInfoCalculator
								.calcTimeLeft(info.getCount(),
										info.getLength(), previouseBytes))
								+ " Minutes", 4, 1);
				progressFrameNotifier.notifyStatusTable(
						Boolean.toString(info.resume(info)), 5, 1);
				
				if (info.multipart()){
					progressFrameNotifier.notifyPartsTable(info.getParts());
				}else{
					// create single part download 
					Part part = new Part();
					part.setStart(0);
					part.setEnd(info.getLength());
					part.setCount(0);
					part.setState(States.DOWNLOADING);
					part.setCount(info.getCount());
					ArrayList<Part> parts = new ArrayList<Part>();
					parts.add(part);
					progressFrameNotifier.notifyPartsTable(parts);
				}
				DownloadListNotifier.notifyProgress(DownloadInfoCalculator
						.calcProgress(info.getCount(), info.getLength()),
						fileNumber);
				DownloadListNotifier.notifyTranfareRate(
						DownloadInfoCalculator.calcTransfareRate(
								info.getCount(), previouseBytes), fileNumber);
				DownloadListNotifier.notifyTimeLeft(DownloadInfoCalculator
						.calcTimeLeft(info.getCount(), info.getLength(),
								previouseBytes), fileNumber);
				DownloadListNotifier.notifyDownloaded(DownloadInfoCalculator
						.calcDownloadedSize(info.getCount()), fileNumber);
				previouseBytes = info.getCount();

			}
			break;
		default:
			break;
		}

	}
}
