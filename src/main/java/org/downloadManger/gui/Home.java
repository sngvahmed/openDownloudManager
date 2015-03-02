package org.downloadManger.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.downloadManger.ActionListner.HomeActionListner;
import org.springframework.stereotype.Component;

import utills.Image;

@Component("Home")
public class Home extends JFrame {


    private static final long serialVersionUID = 1L;
    private JPanel window;
    private JButton download;
    private HomeActionListner homeActionListner;
    public static JTable table;
    public static DefaultTableModel tableModel;
    private ImageIcon iconBackground , iconButton;

    String[] columnNames = { "File name", "Size", "Status", "Time left",
            "Transfare rate", "progress", "Downloaded" };

    public Home() {
        configureMenuBar(this);
        constructGui(); // GUI Button & Jlabel
        configurFrame(); // Frame Resizing
        constructDownloadList(); // Table initlization
        homeActionListner = new HomeActionListner(download);
        download.addActionListener(homeActionListner);
    }

    private void constructGui() {
        setTitle("ODM");

        // jlabel initilazer
        WindowsLabelInit();

        // downloud button initalizer
        DownloudButtonInit();

        window.add(download);
        setContentPane(window);
    }

    private void constructDownloadList() {

        table = new JTable();
        table.setRowHeight(25);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);
        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 100, 860, 300);
        // Add the scroll pane to this panel.
        window.add(scrollPane);

        tableModel = (DefaultTableModel) table.getModel();

        tableModel.setColumnIdentifiers(columnNames);

        centerTableCells();

        tableModel.fireTableDataChanged();
    }

    private void centerTableCells() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columnNames.length; i++)
            Home.table.getColumnModel().getColumn(i)
                    .setCellRenderer(centerRenderer);
    }

    private void configurFrame() {
        setResizable(false);
        getContentPane().setBackground(new Color(240, 240, 240));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setVisible(true);
        setWindowPosition();

    }

    private void setWindowPosition() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = getWidth();
        int height = getHeight();
        int x = (int) ((dimension.getWidth() - width) / 2);
        int y = (int) ((dimension.getHeight() - height) / 2);
        setLocation(x, y);
    }

    private void DownloudButtonInit() {
        download = new JButton();
        iconButton = new ImageIcon(Image.HOME_DOWNLOUD);
        download.setIcon(iconButton);
        download.setBounds(20, 20, 140, 50);

    }

    private void WindowsLabelInit() {
        iconBackground = new ImageIcon(Image.BACKGROUND);
        window = new JPanel(){
            public void paintComponent(Graphics g) {
                g.drawImage(iconBackground.getImage(), 0, 0, null);
                super.paintComponent(g);
            }
        };
        window.setOpaque(false);
        window.setLayout(null);
    }

    private void configureMenuBar(JFrame frame) {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        // Create the menu bar.
        menuBar = new JMenuBar();

        // Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);

        // a group of JMenuItems
        menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
                ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        menu.add(menuItem);

        menuItem = new JMenuItem("Both text and iconBackground", new ImageIcon(
                "images/middle.gif"));
        menuItem.setMnemonic(KeyEvent.VK_B);
        menu.add(menuItem);

        menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menu.add(menuItem);

        // a group of radio button menu items
        menu.addSeparator();
        ButtonGroup group = new ButtonGroup();
        rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Another one");
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        // a group of check box menu items
        menu.addSeparator();
        cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
        cbMenuItem.setMnemonic(KeyEvent.VK_C);
        menu.add(cbMenuItem);

        cbMenuItem = new JCheckBoxMenuItem("Another one");
        cbMenuItem.setMnemonic(KeyEvent.VK_H);
        menu.add(cbMenuItem);

        // a submenu
        menu.addSeparator();
        submenu = new JMenu("A submenu");
        submenu.setMnemonic(KeyEvent.VK_S);

        menuItem = new JMenuItem("An item in the submenu");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
                ActionEvent.ALT_MASK));
        submenu.add(menuItem);

        menuItem = new JMenuItem("Another item");
        submenu.add(menuItem);
        menu.add(submenu);

        // Build second menu in the menu bar.
        menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");
        menuBar.add(menu);

        frame.setJMenuBar(menuBar);
    }
}
