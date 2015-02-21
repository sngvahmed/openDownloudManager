package org.downloadManger.gui;

import java.util.Vector;

import com.github.axet.wget.info.URLInfo.States;

public class DownloadListNotifier {

	public static void notifyState(States downloadState, int fileNumber) {
		Home.tableModel.setValueAt(downloadState, fileNumber, 2);
		Home.tableModel.fireTableDataChanged();
	}

	public static void notifyProgress(long numOfBytesDownloadedBytes,
			long fileLength, int fileNumber) {
		int progress = (int) ((numOfBytesDownloadedBytes / (float) fileLength) * 100);
		Home.tableModel.setValueAt(Integer.toString(progress) + " %",
				fileNumber, 5);
		Home.tableModel.fireTableDataChanged();

	}

	public static void notifyTranfareRate(long numOfBytesDownloadedBytes,
			long previouseBytes, int fileNumber) {
		String tranfareRate = String.format("%.2f KB/Sec",
				(double) (numOfBytesDownloadedBytes - previouseBytes) / 1024.0);
		Home.tableModel.setValueAt(tranfareRate, fileNumber, 4);
		Home.tableModel.fireTableDataChanged();
	}

	public static void notifyNewDownloadRecord(Vector<Object> rowData) {
		Home.tableModel.addRow(rowData);
		Home.tableModel.fireTableDataChanged();
	}

	public static void notifyFileSize(long fileLength, int fileNumber) {
		Double doublefileSize = (double) ((double) fileLength / (double) 1024.0 / 1024.0);
		String fileSize = String.format("%.2f", doublefileSize) + " MB";
		Home.tableModel.setValueAt(fileSize, fileNumber, 1);
	}

	public static void notifyTimeLeft(long numOfBytesDownloadedBytes,
			long fileLength, long previouseBytes, int fileNumber) {
		double transfareRateOfKiloBytes = (double) (numOfBytesDownloadedBytes - previouseBytes) / 1024.0;
		double remainedSizeOfKiloBytes = (double) ((double) (fileLength - numOfBytesDownloadedBytes) / (double) 1024.0);
		String fileSize = String.format("%.2f",
				(remainedSizeOfKiloBytes / transfareRateOfKiloBytes) / 60)
				+ " Minutes";
		Home.tableModel.setValueAt(fileSize, fileNumber, 3);
	}

	public static void notifyDownloaded(long numOfBytesDownloadedBytes,
			int fileNumber) {

		String downloadedSize = String.format("%.2f MB",
				((double)numOfBytesDownloadedBytes / 1024.0 / 1024.0));
		Home.tableModel.setValueAt(downloadedSize, fileNumber, 6);
	}
}
