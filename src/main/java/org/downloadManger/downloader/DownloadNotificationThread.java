package org.downloadManger.downloader;

import org.downloadManger.gui.DownloadListNotifier;
import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.DownloadInfo.Part;
import com.github.axet.wget.info.DownloadInfo.Part.States;

public class DownloadNotificationThread extends Thread {

	private DownloadInfo info;
	private int fileNumber;
	private long previouseBytes = 0;
	private long last;

	public DownloadNotificationThread(DownloadInfo info, int fileNumber) {
		this.info = info;
		this.fileNumber = fileNumber;
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
