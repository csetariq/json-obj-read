package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class JSONArrayIterator {
	private BufferedReader bufferedReader;

	public JSONArrayIterator(InputStream inStream) throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("US-ASCII")));
		skipUntilDataArray();
	}
	
	public String nextJSONObject() throws IOException {
		
		StringBuilder stringBuilder = new StringBuilder();
		char currChar = skipWhiteSpaces();
		
		if (currChar == '{') {
			stringBuilder.append(currChar);
			while ((currChar = (char) bufferedReader.read()) != '}' && currChar != -1)
				stringBuilder.append(currChar);
			
			if (currChar == -1)
				return null;
			
			stringBuilder.append(currChar);
			return stringBuilder.toString();
		}
		return null;
	}
	
	public void skipUntilDataArray() throws IOException {
		String key = null;
		
		while ((key = nextKey()) != null && !"data".equals(key))
			;
		
		if ("data".equals(key)) {
			char lastReadChar = skipWhiteSpaces();
			if (lastReadChar == '[')
				return;
			else
				throw new IOException("didnt see JSONArray");
				
		} else
			throw new IOException("reached EOF before seeing data");
	}
	
	public char skipWhiteSpaces() throws IOException {
		char c;
		while (isWhiteSpace(c = (char) bufferedReader.read()))
			;
		return c;
	}
	
	private boolean isWhiteSpace(char c) {
		switch(c) {
		case ' ':
		case '\t':
		case '\n':
		case '\r':
		case '\f':
		case ':':
		case ',':
			return true;
		default:
			return false;
		}
	}
	
	public String nextKey() throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		boolean quoteStart = false;
		boolean quoteEnd = false;
		
		char c;
		while ((c = (char) bufferedReader.read()) != '"' && c != -1)
			;
		
		if (c == '"')
			quoteStart = true;
		
		if (quoteStart)
			while ((c = (char) bufferedReader.read()) != '"' && c != -1)
				stringBuilder.append(c);
		
		if (c == '"')
			quoteEnd = true;
		
		return (quoteEnd) ?
			stringBuilder.toString() :
			null;
	}
}
