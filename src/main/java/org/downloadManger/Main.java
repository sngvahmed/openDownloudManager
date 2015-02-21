package org.downloadManger;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		app.getBean("Home");
		app.close();
	}

}
