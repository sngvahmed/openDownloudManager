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
			if (now - 1000 > last) {
				last = now;

				String parts = "";
				for (Part p : info.getParts()) {
					if (p.getState().equals(States.DOWNLOADING)) {
						parts += String.format("Part#%d(%.2f) ", p.getNumber(),
								p.getCount() / (float) p.getLength());
					}
				}

				System.out.println(String.format("%.2f %s", info.getCount()
						/ (float) info.getLength(), parts));

				DownloadListNotifier.notifyProgress(info.getCount(),
						info.getLength(), fileNumber);
				DownloadListNotifier.notifyTranfareRate(info.getCount(),
						previouseBytes, fileNumber);
				DownloadListNotifier.notifyTimeLeft(info.getCount(),
						info.getLength(), previouseBytes, fileNumber);
				DownloadListNotifier.notifyDownloaded(info.getCount(), fileNumber);
				previouseBytes = info.getCount();

			}
			break;
		default:
			break;
		}

	}
}
