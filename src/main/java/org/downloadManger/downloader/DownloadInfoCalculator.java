package org.downloadManger.downloader;


public class DownloadInfoCalculator {

	public DownloadInfoCalculator() {

	}

	/**
	 * calculates the progress of downloading file
	 * 
	 * @param numOfBytesDownloadedBytes
	 * @param fileLength
	 * @return progress in percentage
	 */
	public static int calcProgress(long numOfBytesDownloadedBytes,
			long fileLength) {
		int progress = (int) ((numOfBytesDownloadedBytes / (float) fileLength) * 100);
		return progress;
	}

	/**
	 * Calculate the transfer rate of the internet connection that perform the
	 * download
	 * 
	 * @param numOfBytesDownloadedBytes
	 * @param previouseBytes
	 * @return Transfer rate in kilobytes per second
	 */
	public static double calcTransfareRate(long numOfBytesDownloadedBytes,
			long previouseBytes) {
		double tranfareRate = (double) (numOfBytesDownloadedBytes - previouseBytes) / 1024.0;
		return tranfareRate;
	}

	/**
	 * Calculate file size in megabytes
	 * 
	 * @param fileLength
	 * @return file size in megabytes
	 */
	public static double calcFileSize(long fileLength) {
		double doublefileSize = (double) ((double) fileLength / (double) 1024.0 / 1024.0);
		return doublefileSize;
	}

	/**
	 * calculates time left of downloading file
	 * 
	 * @param numOfBytesDownloadedBytes
	 * @param fileLength
	 * @param previouseBytes
	 * @return time left of downloading file in minutes
	 */
	public static double calcTimeLeft(long numOfBytesDownloadedBytes,
			long fileLength, long previouseBytes) {
		double transfareRateOfKiloBytes = (double) (numOfBytesDownloadedBytes - previouseBytes) / 1024.0;
		double remainedSizeOfKiloBytes = (double) ((double) (fileLength - numOfBytesDownloadedBytes) / (double) 1024.0);
		double timeLeft = (remainedSizeOfKiloBytes / transfareRateOfKiloBytes) / 60;
		return timeLeft;
	}
	/**
	 * calculates downloaded size until now in megabytes
	 * @param numOfBytesDownloadedBytes
	 * @return downloaded size until now in megabytes
	 */
	public static double calcDownloadedSize(long numOfBytesDownloadedBytes) {
		double downloadedSize = (double) numOfBytesDownloadedBytes / 1024.0 / 1024.0;
		return downloadedSize;
	}
}
