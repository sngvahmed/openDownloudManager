package org.downloadManger.gui;

import org.apache.commons.io.FilenameUtils;
import org.downloadManger.ActionListner.AddDownloudMenuActionListner;
import org.downloadManger.downloader.DownloadFile;
import utills.*;
import utills.Image;

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
    private ImageIcon iconBackground , iconButton;

    public AddDownloudMenu(){
        setTitle("Downloud Info");

        WindowsLabelInit();

        url = new JTextField();
        url.setBounds(20,20,700,30);

        DownloudButtonInit();

        window.add(url);
        window.add(download);

        setContentPane(window);
        configurFrame();
        ListnerHandler();
    }

    private void DownloudButtonInit() {
        download = new JButton();
        iconButton = new ImageIcon(Image.HOME_DOWNLOUD);
        download.setIcon(iconButton);
        download.setBounds(300,70,120,30);

    }

    private void WindowsLabelInit() {
        iconBackground = new ImageIcon(utills.Image.BACKGROUND);
        window = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(iconBackground.getImage(), 0, 0, null);
                super.paintComponent(g);
            }
        };
        window.setOpaque(false);
        window.setLayout(null);
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

