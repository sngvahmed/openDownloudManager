package org.downloadManger.downloader;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

import org.downloadManger.gui.DownloadListNotifier;
import org.downloadManger.gui.ProgressFrame;
import org.downloadManger.gui.ProgressFrameNotifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import utills.Message;
import utills.Uiutills;

import com.github.axet.wget.WGet;
import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.DownloadInfo.Part;
import com.github.axet.wget.info.DownloadInfo.Part.States;
import com.github.axet.wget.info.ex.DownloadMultipartError;

public class DownloadFile extends Thread {

	private File savedFilePath;
	private AtomicBoolean stop = new AtomicBoolean(false);
	private DownloadInfo info;
	private int fileNumber;
	private URL url;
	private ProgressFrameNotifier progressFrameNotifier;

	public DownloadFile(ProgressFrameNotifier progressFrameNotifier,
			File savedFilePath, URL link, int fileNumber) {
		this.setSavedFilePath(savedFilePath);
		this.setFileNumber(fileNumber);
		this.setUrl(link);
		this.progressFrameNotifier = progressFrameNotifier;
		// initialize url information object
		setInfo(new DownloadInfo(link));
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
			con = (HttpURLConnection) getUrl().openConnection();

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
				DownloadInfoCalculator.calcProgress(1, 1), getFileNumber());
		DownloadListNotifier
				.notifyTranfareRate(
						DownloadInfoCalculator.calcTransfareRate(0, 0),
						getFileNumber());
		DownloadListNotifier.notifyTimeLeft(
				DownloadInfoCalculator.calcTimeLeft(1, 1, 0), getFileNumber());
		if (getInfo().getLength() == null) {
			DownloadListNotifier.notifyDownloaded(-1, getFileNumber());
		} else
			DownloadListNotifier
					.notifyDownloaded(DownloadInfoCalculator
							.calcDownloadedSize(getInfo().getLength()),
							getFileNumber());

		progressFrameNotifier.notifyTitle(DownloadInfoCalculator.calcProgress(
				info.getCount(), info.getLength()));
		progressFrameNotifier.notifyProgressBar(DownloadInfoCalculator
				.calcProgress(info.getCount(), info.getLength()));
		progressFrameNotifier.notifyUrl(url.toString());
		progressFrameNotifier.notifyStatusTable(info.getState().toString(), 0,
				1);

		progressFrameNotifier.notifyStatusTable(
				String.format("%.2f",
						DownloadInfoCalculator.calcFileSize(info.getLength()))
						+ " MB", 1, 1);
		progressFrameNotifier.notifyStatusTable(
				String.format("%.2f", DownloadInfoCalculator
						.calcDownloadedSize(info.getCount()))
						+ " MB ( "
						+ DownloadInfoCalculator.calcProgress(info.getCount(),
								info.getLength()) + "% )", 2, 1);
		progressFrameNotifier.notifyStatusTable(
				String.format("%.2f",
						DownloadInfoCalculator.calcTransfareRate(0, 0))
						+ " MB", 3, 1);
		progressFrameNotifier.notifyStatusTable(
				String.format("%.2f",
						DownloadInfoCalculator.calcTimeLeft(1, 1, 0))
						+ " Minutes", 4, 1);
		progressFrameNotifier.notifyStatusTable(
				Boolean.toString(info.resume(info)), 5, 1);

		if (info.multipart())
			progressFrameNotifier.notifyPartsTable(info.getParts());
		else {
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

	}

	private void createDownloadListRow() {
		Vector<Object> rowData = new Vector<Object>();
		rowData.add(getSavedFilePath().getName());
		rowData.add("");
		rowData.add("");
		rowData.add("");
		rowData.add("");
		rowData.add("");
		DownloadListNotifier.notifyNewDownloadRecord(rowData);
	}

	private void download() {
		DownloadNotificationThread notify = new DownloadNotificationThread(
				progressFrameNotifier, getInfo(), url, getFileNumber());

		try {
			System.out.println("multipart");
			downloadMultiPart(notify);
			
		} catch (DownloadMultipartError e) {
			System.out.println("single part");
			downloadSinglePart(notify);
		} catch (RuntimeException e) {
			System.out.println("single part");
			downloadSinglePart(notify);
		} catch (Exception e) {
			Message m = null;
			m.setMessage(e.getMessage());
			Uiutills.showDialog("Error", m, JOptionPane.ERROR_MESSAGE);
		}

	}

	private void downloadSinglePart(DownloadNotificationThread notify) {

		URL url = this.getUrl();
		File target = getSavedFilePath();
		WGet w = new WGet(url, target);
		setInfo(w.getInfo());
		notify.setInfo(getInfo());
		DownloadListNotifier.notifyFileSize(
				DownloadInfoCalculator.calcFileSize(getInfo().getLength()),
				getFileNumber());
		w.download(getStop(), notify);
	}

	private void downloadMultiPart(DownloadNotificationThread notify) {
		// extract infromation from the web
		getInfo().extract(getStop(), notify);
		// enable multipart donwload
		getInfo().enableMultipart();

		DownloadListNotifier.notifyFileSize(
				DownloadInfoCalculator.calcFileSize(getInfo().getLength()),
				getFileNumber());
		// Choise target file
		File target = getSavedFilePath();
		// create wget downloader
		WGet w = new WGet(getInfo(), target);
		w.updateDirectboolSize(7);
		w.download(getStop(), notify);
	}

	/*
	 * Setters and getters
	 */

	public File getSavedFilePath() {
		return savedFilePath;
	}

	public void setSavedFilePath(File savedFilePath) {
		this.savedFilePath = savedFilePath;
	}

	public AtomicBoolean getStop() {
		return stop;
	}

	public void setStop(AtomicBoolean stop) {
		this.stop = stop;
	}

	public DownloadInfo getInfo() {
		return info;
	}

	public void setInfo(DownloadInfo info) {
		this.info = info;
	}

	public int getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
}
