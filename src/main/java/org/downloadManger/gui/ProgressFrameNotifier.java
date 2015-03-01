package org.downloadManger.gui;

import java.util.List;

import com.github.axet.wget.info.DownloadInfo.Part;

public class ProgressFrameNotifier {

	private ProgressFrame progressFrame;

	public ProgressFrameNotifier(ProgressFrame progressFrame) {
		this.progressFrame = progressFrame;
	}

	public void notifyTitle(int title) {
		progressFrame.setFrameTitle(Integer.toString(title) + "%");
	}
	public void notifyProgressBar(int value) {
		progressFrame.setProgressBarValue(value);
	}
	public void notifyUrl(String url) {
		progressFrame.setUrl(url);
	}
	public void notifyStatusTable(String data , int rowNumber , int colomnNumber) {
		progressFrame.setStatusTableRowData(data, rowNumber, colomnNumber);
	}
	public void notifyPartsTable(List<Part>parts) {
		progressFrame.setPartsTableRowData(parts);
	}
}
