package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		File file = new File("json.json");
		FileInputStream fileInputStream = new FileInputStream(file);
		JSONArrayIterator jsonArrayIterator = new JSONArrayIterator(fileInputStream);
		
		String obj;
		
		while ((obj = jsonArrayIterator.nextJSONObject()) != null)
			System.out.println(">>> " + obj);
	}

}
