package org.downloadManger;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Main {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		app.getBean("Home");

		app.close();
	}

}
