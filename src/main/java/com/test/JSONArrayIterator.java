package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Stack;

public class JSONArrayIterator {
	private BufferedReader bufferedReader;

	public JSONArrayIterator(InputStream inStream) throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("US-ASCII")));
		skipUntilDataArray();
	}
	
	public String nextJSONObject() throws IOException {
		Stack<Character> stack = new Stack<Character>();
		StringBuilder stringBuilder = new StringBuilder();
		boolean insideQuote = false;
		int currChar = skipWhiteSpaces();
		
		if (currChar == '{' && !insideQuote) {
			stack.push((char) currChar);
			stringBuilder.append((char)currChar);
			while (true) {
				currChar = (char) bufferedReader.read();
				if (currChar == -1)
					return null;
				
				if (isLineBreak(currChar))
					continue;
				
				if (currChar == '"')
					insideQuote = !insideQuote;
				
				if (currChar == '{' && !insideQuote)
					stack.push('{');
				
				stringBuilder.append((char)currChar);
				
				if (currChar == '}' && !insideQuote)
						stack.pop();
				
				if (stack.isEmpty())
					break;
			}
			return stringBuilder.toString();
		}
		return null;
	}
	
	public void skipUntilDataArray() throws IOException {
		String key = null;
		
		while ((key = nextKey()) != null && !"data".equals(key))
			;
		
		if ("data".equals(key)) {
			int lastReadChar = skipWhiteSpaces();
			if (lastReadChar == '[')
				return;
			else
				throw new IOException("didnt see JSONArray");
				
		} else
			throw new IOException("reached EOF before seeing data");
	}
	
	public int skipWhiteSpaces() throws IOException {
		int c;
		while (isWhiteSpace(c = bufferedReader.read()))
			;
		return c;
	}
	
	private boolean isLineBreak(int c) {
		return c == '\r' || c == '\n';
	}
	
	private boolean isWhiteSpace(int c) {
		switch(c) {
		case ' ':
		case '\t':
		case '\n':
		case '\r':
		case '\f':
		case ':':
		case ',':
			return true;
		case -1:
		default:
			return false;
		}
	}
	
	public String nextKey() throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		boolean quoteStart = false;
		boolean quoteEnd = false;
		
		int c;
		while ((c = bufferedReader.read()) != '"' && c != -1) {
//			System.out.println((char)c);
		}
//		System.out.println(">> " + (char)c);
		
		if (c == '"')
			quoteStart = true;
		
		if (quoteStart)
			while ((c = bufferedReader.read()) != '"' && c != -1) {
//				System.out.println("inquote " + (char)c);
				stringBuilder.append((char)c);
			}
		
//		System.out.println("<<< " + (char)c);
		if (c == '"')
			quoteEnd = true;
		
		return (quoteEnd) ?
			stringBuilder.toString() :
			null;
	}
}
