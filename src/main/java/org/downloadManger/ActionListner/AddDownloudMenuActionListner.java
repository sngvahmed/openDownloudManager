package org.downloadManger.ActionListner;

import org.apache.commons.io.FilenameUtils;
import org.downloadManger.downloader.DownloadFile;
import org.downloadManger.gui.Home;
import org.downloadManger.gui.ProgressFrame;
import org.downloadManger.gui.ProgressFrameNotifier;
import utills.Message;
import utills.Service;
import utills.Uiutills;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sngv on 27/02/15.
 */
public class AddDownloudMenuActionListner implements ActionListener {
    private JButton startDownloud;
    private JTextField url;
    private Component frame;

    public AddDownloudMenuActionListner(JButton download , JTextField url , Component frame) {
        this.startDownloud = download;
        this.url = url;
        this.frame = frame;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startDownloud){
            JFileChooser fileChooser;
            fileChooser = new JFileChooser();
            URL actualUrl = null;
            try {
                actualUrl = new URL(url.getText());
            } catch (MalformedURLException e1) {
                Uiutills.showDialog("Invalid url", Message.URL_ERROR,
                        JOptionPane.ERROR_MESSAGE);
                return ;
            }
            String fileName = "" , extension = "";

            fileName = FilenameUtils.getBaseName(actualUrl.getFile());
            extension = FilenameUtils.getExtension(actualUrl.toString());

            FileNameExtensionFilter filter;

            try {
                fileName = FilenameUtils.getBaseName(actualUrl.getFile());
                extension = FilenameUtils.getExtension(actualUrl.toString());
                filter = new FileNameExtensionFilter(extension, extension);
                fileChooser.setFileFilter(filter);
                fileChooser.setSelectedFile(new File(fileName));
            }catch (Exception exception){
                System.out.println(exception.toString());
            }

            int fileChecker = fileChooser.showSaveDialog(frame);

            if(fileChecker == JFileChooser.APPROVE_OPTION){
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                ProgressFrame progressFrame = new ProgressFrame();
                ProgressFrameNotifier progressFrameNotifier = new ProgressFrameNotifier(progressFrame);
                String nameAfterCheck = Service.nameFile(fileChooser.getSelectedFile());
                DownloadFile downloadFile = new DownloadFile(progressFrameNotifier,new File(nameAfterCheck), actualUrl,
                        Home.tableModel.getRowCount());
                downloadFile.start();
                frame.setVisible(false);
            }else {
                Uiutills.showDialog("Invalid url", Message.FILE_NOT_EXISTS,
                        JOptionPane.ERROR_MESSAGE);            }
        }
    }
}
