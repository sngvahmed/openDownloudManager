package org.downloadManger.downloader;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

import org.downloadManger.gui.DownloadListNotifier;
import org.springframework.stereotype.Component;

import utills.Message;
import utills.Uiutills;

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

		if (!isValidUrl()) {
			Uiutills.showDialog("Invalid url", Message.URL_ERROR,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		createDownloadListRow();
		download();
		// invoke this method because of time delay in notify thread
		// purpose : because it did not update the value in list after dwonload
		// finish
		notifyListwithEndOfDownload();
	}

	private boolean isValidUrl() {
		// check if url is exist
		HttpURLConnection con;
		try {
			con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("HEAD");
			System.out.println(con.getContentType());
			if (con.getContentType() == null
					|| con.getResponseCode() != HttpURLConnection.HTTP_OK
					|| con.getContentType().indexOf("text/html") != -1) {
				throw new MalformedURLException();
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private void notifyListwithEndOfDownload() {
		DownloadListNotifier.notifyProgress(
				DownloadInfoCalculator.calcProgress(1, 1), fileNumber);
		DownloadListNotifier.notifyTranfareRate(
				DownloadInfoCalculator.calcTransfareRate(0, 0), fileNumber);
		DownloadListNotifier.notifyTimeLeft(
				DownloadInfoCalculator.calcTimeLeft(1, 1, 0), fileNumber);
		if (info.getLength() == null) {
			DownloadListNotifier.notifyDownloaded(-1, fileNumber);
		} else
			DownloadListNotifier
					.notifyDownloaded(DownloadInfoCalculator
							.calcDownloadedSize(info.getLength()), fileNumber);
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

	private void download() {

		try {
			DownloadNotificationThread notify = new DownloadNotificationThread(
					info, fileNumber);
			if (info.multipart())
				downloadMultiPart(notify);
			else
				downloadSinglePart(notify);

		} catch (DownloadMultipartError e) {
			Message m = null;
			m.setMessage(e.getMessage());
			Uiutills.showDialog("Error", m, JOptionPane.ERROR_MESSAGE);
		} catch (RuntimeException e) {
			Message m = null;
			m.setMessage(e.getMessage());
			Uiutills.showDialog("Error", m, JOptionPane.ERROR_MESSAGE);		} catch (Exception e) {
			Message m = null;
			m.setMessage(e.getMessage());
			Uiutills.showDialog("Error", m, JOptionPane.ERROR_MESSAGE);
		}

	}

	private void downloadSinglePart(DownloadNotificationThread notify) {

		URL url = this.url;
		File target = new File("download/file");
		WGet w = new WGet(url, target);
		info = w.getInfo();
		notify.setInfo(info);
		DownloadListNotifier.notifyFileSize(
				DownloadInfoCalculator.calcFileSize(info.getLength()),
				fileNumber);
		w.download(stop, notify);
	}

	private void downloadMultiPart(DownloadNotificationThread notify) {
		// extract infromation from the web
		info.extract(stop, notify);
		// enable multipart donwload
		info.enableMultipart();

		DownloadListNotifier.notifyFileSize(
				DownloadInfoCalculator.calcFileSize(info.getLength()),
				fileNumber);
		// Choise target file
		File target = savedFilePath;
		// create wget downloader
		WGet w = new WGet(info, target);
		w.updateDirectboolSize(7);
		w.download(stop, notify);
	}
}
