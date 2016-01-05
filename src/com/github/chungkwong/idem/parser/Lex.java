/*
 * Copyright (C) 2015 Chan Chung Kwong
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 */

package com.github.chungkwong.idem.parser;
import com.github.chungkwong.idem.lib.base.*;
import com.github.chungkwong.idem.math.*;
import java.io.*;
import java.math.*;
import java.util.*;
/**
 * A hand written lexical analyzer for Scheme(the intended target is R7RS small language)
 */
public final class Lex{
	public static final int EOF=-1,NONE=-2;
	public static final int ALARM='\u0007',BACKSPACE='\u0008',TABULATION='\u0009',LINEFEED='\n',NEWLINE='\n'
		,LINE_TABULATION='\u000B',FORM_FEED='\u000C',CARRIAGE_RETURN='\r',ESCAPE='\u001B',SPACE='\u0020'
		,DELETE='\u007F',NEXT_LINE='\u0085',LINE_SEPARATOR='\u2028',PARAGRAPH_SEPARATOR='\u2029';
	public static final HashMap<String,Integer> name2char=new HashMap<>();
	public static final HashMap<Integer,Integer> mem2char=new HashMap<>();
	public static final HashSet<Integer> delimiter=new HashSet<>();
	public static final HashSet<Integer> idInitial=new HashSet<>(),idSubsequent=new HashSet<>();
	//public static final HashSet<Character> expMarker=new HashSet<Character>();//R6RS only
	public static final HashSet<Byte> idInitialType=new HashSet<>(),idSubsequentType=new HashSet<>();
	Reader in;
	boolean foldingCase=false;
	int ugetc=NONE;//look forward charcater, NONE if none
	HashMap<String,Object> datums=new HashMap<>();
	static{
		name2char.put("null",(int)'\0');
		name2char.put("alarm",ALARM);
		name2char.put("backspace",BACKSPACE);
		name2char.put("tab",TABULATION);
		name2char.put("linefeed",LINEFEED);
		name2char.put("newline",NEWLINE);
		name2char.put("vtab",LINE_TABULATION);
		name2char.put("page",FORM_FEED);
		name2char.put("return",CARRIAGE_RETURN);
		name2char.put("escape",ESCAPE);
		name2char.put("space",SPACE);
		name2char.put("delete",DELETE);
		mem2char.put((int)'a',ALARM);
		mem2char.put((int)'b',BACKSPACE);
		mem2char.put((int)'t',TABULATION);
		mem2char.put((int)'n',LINEFEED);
		mem2char.put((int)'v',LINE_TABULATION);
		mem2char.put((int)'f',FORM_FEED);
		mem2char.put((int)'r',CARRIAGE_RETURN);
		mem2char.put((int)'\"',(int)'\"');
		mem2char.put((int)'\\',(int)'\\');
		mem2char.put((int)'|',(int)'|');
		delimiter.add((int)'(');
		delimiter.add((int)')');
		delimiter.add((int)'[');//for R6RS
		delimiter.add((int)']');//for R6RS
		delimiter.add((int)'\"');
		delimiter.add((int)'|');
		delimiter.add((int)';');
		idInitial.add((int)'!');
		idInitial.add((int)'$');
		idInitial.add((int)'%');
		idInitial.add((int)'&');
		idInitial.add((int)'*');
		idInitial.add((int)'/');
		idInitial.add((int)':');
		idInitial.add((int)'<');
		idInitial.add((int)'=');
		idInitial.add((int)'>');
		idInitial.add((int)'?');
		idInitial.add((int)'^');
		idInitial.add((int)'_');
		idInitial.add((int)'~');
		idInitialType.add(Character.UPPERCASE_LETTER);
		idInitialType.add(Character.LOWERCASE_LETTER);
		idInitialType.add(Character.TITLECASE_LETTER);
		idInitialType.add(Character.MODIFIER_LETTER);
		idInitialType.add(Character.OTHER_LETTER);
		idInitialType.add(Character.NON_SPACING_MARK);
		idInitialType.add(Character.LETTER_NUMBER);
		idInitialType.add(Character.OTHER_NUMBER);
		idInitialType.add(Character.DASH_PUNCTUATION);
		idInitialType.add(Character.CONNECTOR_PUNCTUATION);
		idInitialType.add(Character.OTHER_PUNCTUATION);
		idInitialType.add(Character.CURRENCY_SYMBOL);
		idInitialType.add(Character.MATH_SYMBOL);
		idInitialType.add(Character.MODIFIER_SYMBOL);
		idInitialType.add(Character.OTHER_SYMBOL);
		idInitialType.add(Character.PRIVATE_USE);
		idSubsequent.addAll(idInitial);
		idSubsequent.add((int)'+');
		idSubsequent.add((int)'-');
		idSubsequent.add((int)'.');
		idSubsequent.add((int)'@');
		idSubsequentType.addAll(idInitialType);
		idSubsequentType.add(Character.DECIMAL_DIGIT_NUMBER);
		idSubsequentType.add(Character.COMBINING_SPACING_MARK);
		idSubsequentType.add(Character.ENCLOSING_MARK);
		/*expMarker.add('e');
		expMarker.add('s');
		expMarker.add('f');
		expMarker.add('d');
		expMarker.add('l');
		expMarker.add('E');
		expMarker.add('S');
		expMarker.add('F');
		expMarker.add('D');
		expMarker.add('L');*/
	}
	/**
	 * Construct a lexical analyzer to analysis a piece of code from a Reader
	 * @param in the source
	 */
	public Lex(Reader in){
		this.in=in;
	}
	/**
	 * Construct a lexical analyzer to analysis a piece of code as String
	 * @param code the source
	 */
	public Lex(String code){
		in=new StringReader(code);
	}
	/**
	 * Collect all remaining token
	 */
	public ArrayList<Object> getRemainingTokens()throws IOException{
		ArrayList<Object> tokens=new ArrayList<Object>();
		Object next=nextToken();
		while(next!=null){
			tokens.add(next);
			next=nextToken();
		}
		return tokens;
	}
	/**
	 * Get the next token
	 * @return the token or null if the end of the code is reached
	 */
	public Object nextToken(){
		try{
  			while(true){
				int c=ugetc;
				if(ugetc!=NONE)
					ugetc=NONE;
				else
					c=in.read();
				if(isWhiteSpace(c))
					continue;
				switch(c){
					case EOF:
						return null;
					case '(':case ')':case '`':case '\''://case '[':case ']':
						return new Token(String.valueOf((char)c));
					case ',':
						ugetc=in.read();
						if(ugetc=='@')
							return new Token(",@");
						else{
							ugetc=NONE;
							return new Token(",");
						}
					case ';':
						eatLineRemaining();
						break;
					case '#':
						c=in.read();
						switch(c){
							case '|':
								eatNestedComment();
								break;
							case '!':
								foldingCase=in.read()=='f';
								if(foldingCase)
									in.skip(8);//#!fold-case
								else
									in.skip(11);//#!no-fold-case
								break;
							case ';':
								return new Token("#;");
							case '\\':
								return nextCharacter();
							case 't':case 'T':
								ugetc=in.read();
								if(ugetc=='r'){
									ugetc=NONE;
									in.skip(3);//true
								}
								return Boolean.TRUE;
							case 'f':case 'F':
								ugetc=in.read();
								if(ugetc=='a'){
									ugetc=NONE;
									in.skip(4);//false
								}
								return Boolean.FALSE;
							case '(':
								return new Token("#(");
							case 'u':
								if(in.read()=='8'&&in.read()=='(')
									return new Token("#u8(");
							/*case '\'':
								return new Token("#\'");
							case '`':
								return new Token("#`");
							case ',':
								if(curr+1<len&&code.charAt(curr+1)=='@'){
									++curr;
									return new Token("#,@");
								}else
									return new Token("#,");
							case 'v':
								if(curr+3<=len&&code.substring(curr,curr+3).equals("u8(")){
									curr+=3;
									return new Token("#vu8(");
								}
								break;*/
							default:
								if(Character.isLetter(c)){
									ugetc=c;
									return nextNumber('#');
									}
									return nextLabel();
						}
						break;
					case '\"':
						return nextString();
					case '|':
						return nextVerbatimIdentifer();
					default:
						return nextIdentiferOrNumber(c);
				}
			}
		}catch(IOException ex){
			return new RuntimeException("Exception while reading token");
		}
	}
	static final boolean isWhiteSpace(int c){
		switch(c){
			case TABULATION:
			case LINEFEED:
			case CARRIAGE_RETURN:
			case LINE_TABULATION:
			case FORM_FEED:
			case NEXT_LINE:
				return true;
		}
		int type=Character.getType(c);
		if(type==Character.SPACE_SEPARATOR||type==Character.LINE_SEPARATOR||type==Character.PARAGRAPH_SEPARATOR)
			return true;
		return false;
	}
	static final boolean isDelimiter(int c){
		return delimiter.contains(c)||isWhiteSpace(c);
	}
	void eatLineRemaining()throws IOException{
		int c=in.read();
		while(c!=EOF&&c!=LINEFEED&&c!=CARRIAGE_RETURN&&c!=NEXT_LINE&&c!=LINE_SEPARATOR&&c!=PARAGRAPH_SEPARATOR)
			c=in.read();
	}
	void eatNestedComment()throws IOException{
		int lv=1;
		int ch;
		while((ch=in.read())!=EOF){
			if(ch=='#'&&in.read()=='|'){
				++lv;
			}else if(ch=='|'&&in.read()=='#'){
				--lv;
				if(lv==0)
					break;
			}
		}
	}
	char nextCharacter()throws IOException{
		String cname=untilNextDelimiter();
		if(cname.length()==1)
			return cname.charAt(0);
		else if(cname.charAt(0)=='x'){//&&cname.length()>1
			ugetc=NONE;//forget the ';' after the hex
			return Character.toChars(Integer.valueOf(cname.substring(1),16))[0];
		}else{
			if(foldingCase)
				cname=ScmString.toFoldingCase(cname);
			return (char)(int)name2char.get(cname);
		}
	}
	String untilNextDelimiter()throws IOException{
		StringBuilder buf=new StringBuilder();
		buf.append((char)in.read());
		int c;
		while((c=in.read())!=EOF&&!isDelimiter(c)&&c!='#'){
			buf.append((char)c);
		}
		ugetc=c;
		return buf.toString();
	}
	DatumLabel nextLabel()throws IOException{
		StringBuilder buf=new StringBuilder();
		while(true){
			int c=in.read();
			if(c=='=')
				return new DatumLabelSrc(buf.toString());
			else if(c=='#')
				return new DatumLabelRef(buf.toString());
			else if(Character.isDigit(c))
				buf.append((char)c);
			else
				throw new RuntimeException("Illegal label format");
		}
	}
	String nextString() throws IOException{
		StringBuilder str=new StringBuilder();
		int c=in.read();
		while(true){
			if(c=='\\'){
				c=in.read();
				if(mem2char.containsKey(c)){
					str.append((char)(int)mem2char.get(c));
					c=in.read();
				}else if(c=='x'){
					int point=0;
					while((c=in.read())!=';')
						point=point*16+Character.digit(c,16);
					str.append((char)point);
					c=in.read();
				}else{
					c=eatLineEndingInString(c);
				}
			}else if(c=='\"'){
				break;
			}else{
				str.append((char)c);
				c=in.read();
			}
		}
		return str.toString();
	}
	int eatLineEndingInString(int c)throws IOException{
		while(c!=EOF&&isIntraLineSpace(c))
			c=in.read();
		switch(c){
			case LINEFEED:
			case NEXT_LINE:
			case LINE_SEPARATOR:
				c=in.read();
				break;
			case CARRIAGE_RETURN:
				c=in.read();
				if(c==LINEFEED||c==NEXT_LINE)
					c=in.read();
				break;
			default:
				throw new RuntimeException("Illegal string format");
		}
		while(c!=EOF&&isIntraLineSpace(c))
			c=in.read();
		return c;
	}
	static final boolean isIntraLineSpace(int c){
		return c==TABULATION||Character.getType(c)==Character.SPACE_SEPARATOR;
	}
	String nextVerbatimIdentifer()throws IOException{
		int c=in.read();
		StringBuilder buf=new StringBuilder();
		while(c!='|'&&c!=EOF){
			if(c=='\\'){
				c=nextHexOrMem(buf);
			}else{
				buf.append((char)c);
				c=in.read();
			}
		}
		return foldingCase?ScmString.toFoldingCase(buf.toString()):buf.toString();
	}
	int nextHexOrMem(StringBuilder buf)throws IOException{
		int c=in.read();
		if(mem2char.containsKey((char)c))
			return mem2char.get((char)c);
		else if(c=='x'){
			int point=0;
			while((c=in.read())!=';'){
				point=point*16+Character.digit(c,16);
			}
			buf.append((char)point);
			return c;
		}else{
			return c;
		}
	}
	Object nextIdentiferOrNumber(int prefix)throws IOException{
		if(isInitial(prefix))
			return nextIdentifer(prefix);
		if(prefix=='.'){
			ugetc=in.read();
			if(ugetc==EOF||isDelimiter(ugetc)){
				return new Token(".");
			}
			if(ugetc=='.'||ugetc=='+'||ugetc=='-'||ugetc=='@'||isInitial(ugetc))
				return nextIdentifer(prefix);
		}else if(prefix=='+'||prefix=='-'){
			ugetc=in.read();
			if(ugetc==EOF||isDelimiter(ugetc)){
				return Character.toString((char)prefix);
			}
			//TODO support of identifier likes +.(.)
			if(ugetc=='+'||ugetc=='-'||ugetc=='@'||isInitial(ugetc))
				return nextIdentifer(prefix);
		}
		return nextNumber(prefix);
	}
	String nextIdentifer(int prefix)throws IOException{
		StringBuilder str=new StringBuilder();
		str.append((char)prefix);
		if(ugetc!=NONE)
			prefix=ugetc;
		else
			prefix=in.read();
		while(prefix!=EOF&&isSubsequent(prefix)){
			str.append((char)prefix);
			prefix=in.read();
		}
		ugetc=prefix;
		return foldingCase?ScmString.toFoldingCase(str.toString()):str.toString();
	}
	static final boolean isInitial(int c){
		return (c>='a'&&c<='z')||(c>='A'&&c<='Z')||idInitial.contains(c)||(c>127&&idInitialType.contains(Character.getType(c)));
	}
	static final boolean isSubsequent(int c){
		return (c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c>='0'&&c<='9')||idSubsequent.contains(c)||(c>127&&idSubsequentType.contains(Character.getType(c)));
	}
	Object nextNumber(int prefix)throws IOException{
		int base=10;
		byte exact=0;//1 mean exact, -1 means inexact,0 means not mentioned
		if(prefix=='#'){
			for(int i=0;i<2;i++){
				switch(ugetc){
					case 'i':case 'I':
						exact=-1;
						break;
					case 'e':case 'E':
						exact=1;
						break;
					case 'b':case 'B':
						base=2;
						break;
					case 'o':case 'O':
						base=8;
						break;
					case 'd':case 'D':
						base=10;
						break;
					case 'x':case 'X':
						base=16;
						break;
				}
				prefix=in.read();
				if(prefix=='#')
					ugetc=in.read();
				else
					break;
			}
			ugetc=NONE;
		}
		Object real=nextReal(prefix,exact,base);
		if(ugetc==NONE){
			prefix=in.read();
		}else{
			prefix=ugetc;
			ugetc=NONE;
		}
		switch(prefix){
			case 'i':
				return new Tuple(BigInteger.ZERO,real);
			case '@':
				return new Tuple(real,nextReal(in.read(),exact,base));
			case '+':
			case '-':
				ugetc=in.read();
				Object imag=nextReal(prefix,exact,base);
				ugetc=NONE;
				return new Tuple(real,imag);
			default:
				ugetc=prefix;
				return real;
		}
	}
	Object nextReal(int prefix,byte exact,int base)throws IOException{
		boolean neg=false;
		if(prefix=='+'){
			prefix=ugetc;
			ugetc=NONE;
		}else if(prefix=='-'){
			neg=true;
			prefix=ugetc;
			ugetc=NONE;
		}
		if(prefix=='i'){
			ugetc=prefix;
			return neg?BigInteger.ONE.negate():BigInteger.ONE;
		}
		BigInteger num=BigInteger.ZERO,b=BigInteger.valueOf(base);
		DigitVerifier digitCheck=DigitVerifier.getDigitVerifier(base);
		while(digitCheck.verify(prefix)){
			num=num.multiply(b).add(BigInteger.valueOf(Character.digit(prefix,base)));
			prefix=in.read();
		}
		BigDecimal real=null;
		if(prefix=='.'){
			BigDecimal ratio=BigDecimal.ONE.divide(BigDecimal.valueOf(base)),pos=ratio;
			real=new BigDecimal(num);
			prefix=ugetc!=NONE?ugetc:in.read();
			ugetc=NONE;
			while(digitCheck.verify(prefix)){
				real=BigDecimal.valueOf(Character.digit(prefix,base)).multiply(pos).add(real);
				pos=pos.multiply(ratio);
				prefix=in.read();
			}
		}else if(prefix=='/'){
			BigInteger dorm=BigInteger.ZERO;
			prefix=in.read();
			while(digitCheck.verify(prefix)){
				dorm=dorm.multiply(b).add(BigInteger.valueOf(Character.digit(prefix,base)));
				prefix=in.read();
			}
			ugetc=prefix;
			return new Rational(num,dorm);
		}else if(prefix=='i'){
			in.skip(4);//inf.0
			return neg?Double.NEGATIVE_INFINITY:Double.POSITIVE_INFINITY;
		}else if(prefix=='n'){
			in.skip(4);//nan.0
			return neg?-Double.NaN:Double.NaN;
		}
		if(prefix=='e'){
			if(real==null)
				real=new BigDecimal(num);
			prefix=in.read();
			boolean negexp=false;
			if(prefix=='+'){
				prefix=in.read();
			}else if(prefix=='-'){
				negexp=true;
				prefix=in.read();
			}
			int exp=0;
			while(Character.isDigit(prefix)){
				exp=exp*10+Character.digit(prefix,10);
				prefix=in.read();
			}
			real=real.movePointRight(negexp?-exp:exp);
		}
		ugetc=prefix;
		if(real==null)
			return neg?num.negate():num;
		else
			return neg?real.negate():real;
	}
	public static void main(String[] args)throws IOException{
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		String s=null;
		while((s=in.readLine())!=null){
			System.out.println(new Lex(s).getRemainingTokens());
		}
	}
}
class DigitVerifier{
	BitSet digits=new BitSet(128);
	static final DigitVerifier[] loaded=new DigitVerifier[37];
	private DigitVerifier(int base){
		if(base<2||base>36)
			throw new RuntimeException();
		digits.set('0','0'+Math.min(10,base));
		if(base>10){
			digits.set('a','a'+(base-10));
			digits.set('A','A'+(base-10));
		}
	}
	public static DigitVerifier getDigitVerifier(int base){
		if(loaded[base]==null)
			loaded[base]=new DigitVerifier(base);
		return loaded[base];
	}
	public boolean verify(int c){
		if(c>127||c<0)
			return false;
		return digits.get(c);
	}
}
