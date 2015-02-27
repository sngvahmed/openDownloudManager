package org.downloadManger.gui;

import org.apache.commons.io.FilenameUtils;
import org.downloadManger.ActionListner.AddDownloudMenuActionListner;
import org.downloadManger.downloader.DownloadFile;
import utills.Message;
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
public class AddDownloudMenu extends JFrame{

    private JPanel window;
    private JTextField url;
    private JButton download;
    private AddDownloudMenuActionListner addDownloudListner;

    public AddDownloudMenu(){
        setTitle("Downloud Info");

        window = new JPanel();
        window.setLayout(null);

        url = new JTextField();
        url.setBounds(20,20,700,30);

        download = new JButton("Download");
        download.setBounds(300,70,120,30);

        window.add(url);
        window.add(download);

        setContentPane(window);
        configurFrame();
        ListnerHandler();
    }

    public void ListnerHandler(){

        addDownloudListner = new AddDownloudMenuActionListner(download,url,AddDownloudMenu.this);
        download.addActionListener(addDownloudListner);

    }

    private void configurFrame() {
        setResizable(false);
        getContentPane().setBackground(new Color(240, 240, 240));
        setSize(740, 150);
        setVisible(true);
        setWindowPosition();
    }

    private void setWindowPosition() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = getWidth();
        int height = getHeight();
        int x = (int) ((dimension.getWidth() - width) / 2);
        int y = (int) ((dimension.getHeight() - height) / 2) - 100;
        setLocation(x, y);
    }

}

