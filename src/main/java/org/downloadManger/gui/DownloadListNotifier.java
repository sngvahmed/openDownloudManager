package org.downloadManger.gui;

import java.util.Vector;

import com.github.axet.wget.info.URLInfo.States;

public class DownloadListNotifier {

	public static void notifyState(States downloadState, int fileNumber) {
		Home.tableModel.setValueAt(downloadState, fileNumber, 2);
		Home.tableModel.fireTableDataChanged();
	}

	public static void notifyProgress(int progress, int fileNumber) {
		Home.tableModel.setValueAt(Integer.toString(progress) + " %",
				fileNumber, 5);
		Home.tableModel.fireTableDataChanged();

	}

	public static void notifyTranfareRate(double transferRate, int fileNumber) {
		String tr = String.format("%.2f KB/Sec", transferRate);
		Home.tableModel.setValueAt(tr, fileNumber, 4);
		Home.tableModel.fireTableDataChanged();
	}

	public static void notifyNewDownloadRecord(Vector<Object> rowData) {
		Home.tableModel.addRow(rowData);
		Home.tableModel.fireTableDataChanged();
	}

	public static void notifyFileSize(double fileSize, int fileNumber) {
		String fz = String.format("%.2f", fileSize) + " MB";
		Home.tableModel.setValueAt(fz, fileNumber, 1);
	}

	public static void notifyTimeLeft(double timeLeft, int fileNumber) {

		String tl = String.format("%.2f", timeLeft) + " Minutes";
		Home.tableModel.setValueAt(tl, fileNumber, 3);
	}

	public static void notifyDownloaded(double downloadedSize, int fileNumber) {
		
		if(downloadedSize == -1){
			Home.tableModel.setValueAt("Unknown", fileNumber, 6);
			return ;
		}
		String dz = String.format("%.2f MB", downloadedSize);
		Home.tableModel.setValueAt(dz, fileNumber, 6);
	}
}
