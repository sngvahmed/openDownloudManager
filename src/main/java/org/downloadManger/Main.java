package org.downloadManger;

import org.springframework.context.support.ClassPathXmlApplicationContext;


import javax.swing.*;
import java.awt.*;

public class Main {


    private static ImageIcon icon;
    private static JLabel label;

	public static void main(String[] args) {

		ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		app.getBean("Home");
		app.close();
	}

}