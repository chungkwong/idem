/*
 * Copyright (C) 2016 Chan Chung Kwong <1m02math@126.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.chungkwong.idem.lib.lang.prolog;
import java.io.*;
import java.math.*;
import java.util.*;
/**
 *
 * @author kwong
 */
public class PrologLex{
	PushbackReader in;
	static final String GRAPHIC_TOKEN_CHARACTER="#$&*+-./:<=>?@^~\\";
	public PrologLex(Reader in){
		this.in=new PushbackReader(in,2);
	}
	public PrologLex(String code){
		this(new StringReader(code));
	}
	public ArrayList<Object> getRemainingTokens()throws IOException{
		ArrayList<Object> tokens=new ArrayList<>();
		Object next=nextToken();
		while(next!=null){
			tokens.add(next);
			next=nextToken();
		}
		return tokens;
	}
	private void eatLayoutText()throws IOException{
		while(true){
			int c=in.read();
			switch(c){
				case ' ':case '\r':case '\n':
					break;
				case '%':
					while(c!=-1&&c!='\n'&&c!='\r'){
						c=in.read();
					}
					break;
				case '/':
					c=in.read();
					if(c=='*'){
						bracket:while(true){
							c=in.read();
							if(c==-1)
								return;
							while(c=='*'){
								c=in.read();
								if(c=='/')
									break bracket;
							}
						}
					}else{
						in.unread('/');
						in.unread(c);
						return;
					}
					break;
				default:
					return;
			}
		}
	}
	private char getChar(int base)throws IOException{
		int c=in.read(),ch=0;
		while(c!='\\')
			ch=ch*base+Character.digit(c,base);
		return (char)ch;
	}
	private char getEspaceCharacter() throws IOException{
		int c=in.read();
		switch(c){
			case '\\':case '`':case '\'':case '\"':
				return (char)c;
			case 'a':
				return '\u0007';
			case 'b':
				return '\b';
			case 'f':
				return '\f';
			case 'n':
				return '\n';
			case 'r':
				return '\r';
			case 't':
				return '\t';
			case 'v':
				return '\u000B';
			case 'x':
				return getChar(16);
			case '\n':
			case '\r':
				return '\0';
			default:
				in.unread(c);
				return getChar(8);
		}
	}
	private String getQuotedString(char quote) throws IOException{
		StringBuilder buf=new StringBuilder();
		while(true){
			int c=in.read();
			if(c==quote){
				c=in.read();
				if(c!=quote){
					in.unread(c);
					break;
				}else
					buf.append(quote);
			}else if(c=='\\'){
				char esc=getEspaceCharacter();
				if(esc!='\0')
					buf.append(esc);
			}else if(c==-1)
				break;
			else
				buf.append((char)c);
		}
		return buf.toString();
	}
	private String getGraphicToken(int start) throws IOException{
		StringBuilder buf=new StringBuilder();
		buf.append((char)start);
		while(GRAPHIC_TOKEN_CHARACTER.indexOf(start=in.read())!=-1)
			buf.append((char)start);
		in.unread(start);
		return buf.toString();
	}
	private Variable getVariable() throws IOException{
		int c=in.read();
		if(Character.isAlphabetic(c)||Character.isDigit(c)){
			StringBuilder buf=new StringBuilder();
			buf.append('_');
			do{
				buf.append((char)c);
				c=in.read();
			}while(Character.isAlphabetic(c)||Character.isDigit(c));
			in.unread(c);
			return new Variable(buf.toString());
		}else
			return Variable.WILDCARD;
	}
	private Object getIdentifier(int start) throws IOException{
		StringBuilder buf=new StringBuilder();
		while(Character.isLetter(start)||Character.isDigit(start)){
			buf.append((char)start);
			start=in.read();
		}
		in.unread(start);
		if(Character.isUpperCase(buf.charAt(0))){
			return new Variable(buf.toString());
		}else if(Character.isLowerCase(buf.charAt(0))){
			return buf.toString();
		}else
			throw new LexicalException("Identifier token or named variable expected");
	}
	private BigInteger getInteger(int base) throws IOException{
		StringBuilder buf=new StringBuilder();
		int c=in.read();
		while(Character.digit(c,base)!=-1)
			buf.append((char)c);
		in.unread(c);
		if(buf.length()==0)
			return BigInteger.ZERO;
		else
			return new BigInteger(buf.toString(),base);
	}
	private Number getNumber(int start) throws IOException{
		if(start=='0'){
			start=in.read();
			switch(start){
				case 'b':
					return getInteger(2);
				case 'o':
					return getInteger(8);
				case 'x':
					return getInteger(16);
				case '\'':
					start=in.read();
					if(start=='\'')
						in.read();
					return BigInteger.valueOf(start);
			}
		}
		BigInteger intPart=getInteger(10);
		start=in.read();
		if(start=='.'){
			BigInteger fracPart=getInteger(10);
			BigDecimal val=new BigDecimal(intPart);
			while(val.compareTo(BigDecimal.ONE)>=0)
				val=val.movePointLeft(1);
			val=val.add(new BigDecimal(intPart));
			start=in.read();
			if(start=='E'||start=='e'){
				start=in.read();
				boolean neg=false;
				if(start=='-')
					neg=true;
				else if(start!='+')
					in.unread(start);
				int expPart=getInteger(10).intValue();
				val=neg?val.movePointLeft(expPart):val.movePointRight(expPart);
			}else
				in.unread(start);
			return val;
		}else{
			in.unread(start);
			return intPart;
		}
	}
	public Object nextToken()throws IOException{
		eatLayoutText();
		int c=in.read();
		switch(c){
			case'(':case')':case'[':case ']':case '{':case '}':case '|':case ',':case ';':case '!':
				return String.valueOf(c);
			case '`':
				return getQuotedString('`');
			case '\'':
				return getQuotedString('\'');
			case '\"':
				return getQuotedString('\"');
			case '.':case '#':case '$': case '&':case '*':case '+':case '-':case '/':
			case '<':case '=':case '>':case '?':case '@':case '^':case '~':case '\\'://.
				return getGraphicToken(c);
			case '_':
				return getVariable();
			case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':
				return getNumber(c);
			case -1:
				return null;
			default:
				return getIdentifier(c);
		}
	}
}
class LexicalException extends RuntimeException{
	public LexicalException(String message){
		super(message);
	}
}
