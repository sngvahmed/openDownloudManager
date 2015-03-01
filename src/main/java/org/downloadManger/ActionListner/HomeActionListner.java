package org.downloadManger.ActionListner;

import org.downloadManger.gui.AddDownloudMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sngv on 27/02/15.
 */
public class HomeActionListner implements ActionListener {

    private JButton addDownloud;

    public HomeActionListner(JButton download) {
        this.addDownloud = download;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addDownloud){
            AddDownloudMenu downloudInfoDialog = new AddDownloudMenu();
            downloudInfoDialog.setVisible(true);
        }
    }
}
