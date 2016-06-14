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
package com.github.chungkwong.idem.lib.lang.common.lex;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class RegularExpression{
	private static final HashMap<Integer,CharacterSet> SHORT_CHARACTER_TYPE=new HashMap<>();
	private static final HashMap<String,CharacterSet> LONG_CHARACTER_TYPE=new HashMap<>();
	private static final HashMap<String,Byte> UNICODE_CATEGORY=new HashMap<>();
	private static final HashMap<String,CharacterSet> BINARY_PROPERTY=new HashMap<>();
	private static final RegularExpression WILDCARD=new CharRegularExpression(CharacterSetFactory.createWildcardCharacterSet());
	private static final RegularExpression EMPTY=new EmptyRegularExpression();
	static{
		SHORT_CHARACTER_TYPE.put((int)'\\',CharacterSetFactory.createSingletonCharacterSet('\\'));
		SHORT_CHARACTER_TYPE.put((int)'t',CharacterSetFactory.createSingletonCharacterSet('\t'));
		SHORT_CHARACTER_TYPE.put((int)'n',CharacterSetFactory.createSingletonCharacterSet('\n'));
		SHORT_CHARACTER_TYPE.put((int)'r',CharacterSetFactory.createSingletonCharacterSet('\r'));
		SHORT_CHARACTER_TYPE.put((int)'f',CharacterSetFactory.createSingletonCharacterSet('\f'));
		SHORT_CHARACTER_TYPE.put((int)'a',CharacterSetFactory.createSingletonCharacterSet('\u0007'));
		SHORT_CHARACTER_TYPE.put((int)'e',CharacterSetFactory.createSingletonCharacterSet('\u001B'));
		CharacterSet digit=CharacterSetFactory.createRangeCharacterSet('0','9');
		SHORT_CHARACTER_TYPE.put((int)'d',digit);
		SHORT_CHARACTER_TYPE.put((int)'D',CharacterSetFactory.createComplementCharacterSet(digit));
		CharacterSet hspace=CharacterSetFactory.createUnionCharacterSet(
				CharacterSetFactory.createRangeCharacterSet(0x2000,0x200A),
				CharacterSetFactory.createEnumCharacterSet(' ','\t',0xA0,0x1680,0x180E,0x202F,0x205F,0x3000));
		SHORT_CHARACTER_TYPE.put((int)'h',hspace);
		SHORT_CHARACTER_TYPE.put((int)'H',CharacterSetFactory.createComplementCharacterSet(hspace));
		CharacterSet whitespace=CharacterSetFactory.createEnumCharacterSet(' ','\t',0x0B,'\n','\f','\r');
		SHORT_CHARACTER_TYPE.put((int)'s',whitespace);
		SHORT_CHARACTER_TYPE.put((int)'S',CharacterSetFactory.createComplementCharacterSet(whitespace));
		CharacterSet vspace=CharacterSetFactory.createEnumCharacterSet('\n',0x0B,'\f','\r',0x85,0x2028,0x2029);
		SHORT_CHARACTER_TYPE.put((int)'v',vspace);
		SHORT_CHARACTER_TYPE.put((int)'V',CharacterSetFactory.createComplementCharacterSet(vspace));
		CharacterSet word=CharacterSetFactory.createUnionCharacterSet(
				CharacterSetFactory.createRangeCharacterSet('a','z'),
				CharacterSetFactory.createRangeCharacterSet('A','Z'),digit);
		SHORT_CHARACTER_TYPE.put((int)'w',word);
		SHORT_CHARACTER_TYPE.put((int)'W',CharacterSetFactory.createComplementCharacterSet(word));
		CharacterSet lower=CharacterSetFactory.createRangeCharacterSet('a','z');
		LONG_CHARACTER_TYPE.put("Lower",lower);
		CharacterSet upper=CharacterSetFactory.createRangeCharacterSet('A','Z');
		LONG_CHARACTER_TYPE.put("Upper",upper);
		LONG_CHARACTER_TYPE.put("ASCII",CharacterSetFactory.createRangeCharacterSet(0x0,0x1F));
		LONG_CHARACTER_TYPE.put("Alpha",CharacterSetFactory.createUnionCharacterSet(lower,upper));
		LONG_CHARACTER_TYPE.put("Digit",digit);
		LONG_CHARACTER_TYPE.put("Alnum",CharacterSetFactory.createUnionCharacterSet(lower,upper,digit));
		CharacterSet punct=CharacterSetFactory.createEnumCharacterSet('!','\"','#','$','%','&','\'','(',')','*','+',',','-','.','/',':',';','<','=','>','?','@','[','\\',']','^','_','`','{','|','}','~');
		LONG_CHARACTER_TYPE.put("Punct",punct);
		LONG_CHARACTER_TYPE.put("Graph",CharacterSetFactory.createUnionCharacterSet(lower,upper,digit,punct));
		CharacterSet print=CharacterSetFactory.createUnionCharacterSet(lower,upper,digit,punct,
				CharacterSetFactory.createSingletonCharacterSet(0x20));
		LONG_CHARACTER_TYPE.put("Print",print);
		LONG_CHARACTER_TYPE.put("Blank",CharacterSetFactory.createEnumCharacterSet(' ','\t'));
		CharacterSet cntrl=CharacterSetFactory.createUnionCharacterSet(
				CharacterSetFactory.createRangeCharacterSet(0x0,0x1F),
				CharacterSetFactory.createSingletonCharacterSet(0x7F));
		LONG_CHARACTER_TYPE.put("Cntrl",cntrl);
		CharacterSet xdigit=CharacterSetFactory.createUnionCharacterSet(digit,
				CharacterSetFactory.createRangeCharacterSet('a','f'),
				CharacterSetFactory.createRangeCharacterSet('A','F'));
		LONG_CHARACTER_TYPE.put("XDigit",xdigit);
		LONG_CHARACTER_TYPE.put("Space",whitespace);
		UNICODE_CATEGORY.put("Cn",Character.UNASSIGNED);
		UNICODE_CATEGORY.put("Lu",Character.UPPERCASE_LETTER);
		UNICODE_CATEGORY.put("Ll",Character.LOWERCASE_LETTER);
		UNICODE_CATEGORY.put("Lt",Character.TITLECASE_LETTER);
		UNICODE_CATEGORY.put("Lm",Character.MODIFIER_LETTER);
		UNICODE_CATEGORY.put("Lo",Character.OTHER_LETTER);
		UNICODE_CATEGORY.put("Mn",Character.NON_SPACING_MARK);
		UNICODE_CATEGORY.put("Me",Character.ENCLOSING_MARK);
		UNICODE_CATEGORY.put("Mc",Character.COMBINING_SPACING_MARK);
		UNICODE_CATEGORY.put("Nd",Character.DECIMAL_DIGIT_NUMBER);
		UNICODE_CATEGORY.put("Nl",Character.LETTER_NUMBER);
		UNICODE_CATEGORY.put("No",Character.OTHER_NUMBER);
		UNICODE_CATEGORY.put("Zs",Character.SPACE_SEPARATOR);
		UNICODE_CATEGORY.put("Zl",Character.LINE_SEPARATOR);
		UNICODE_CATEGORY.put("Zp",Character.PARAGRAPH_SEPARATOR);
		UNICODE_CATEGORY.put("Cc",Character.CONTROL);
		UNICODE_CATEGORY.put("Cf",Character.FORMAT);
		UNICODE_CATEGORY.put("Co",Character.PRIVATE_USE);
		UNICODE_CATEGORY.put("Cs",Character.SURROGATE);
		UNICODE_CATEGORY.put("Pd",Character.DASH_PUNCTUATION);
		UNICODE_CATEGORY.put("Ps",Character.START_PUNCTUATION);
		UNICODE_CATEGORY.put("Pe",Character.END_PUNCTUATION);
		UNICODE_CATEGORY.put("Pc",Character.CONNECTOR_PUNCTUATION);
		UNICODE_CATEGORY.put("Po",Character.OTHER_PUNCTUATION);
		UNICODE_CATEGORY.put("Sm",Character.MATH_SYMBOL);
		UNICODE_CATEGORY.put("Sc",Character.CURRENCY_SYMBOL);
		UNICODE_CATEGORY.put("Sk",Character.MODIFIER_SYMBOL);
		UNICODE_CATEGORY.put("So",Character.OTHER_SYMBOL);
		UNICODE_CATEGORY.put("Pi",Character.INITIAL_QUOTE_PUNCTUATION);
		UNICODE_CATEGORY.put("Pf",Character.FINAL_QUOTE_PUNCTUATION);
		BINARY_PROPERTY.put("Alphabetic",Character::isAlphabetic);
		BINARY_PROPERTY.put("Ideographic",Character::isIdeographic);
		BINARY_PROPERTY.put("Letter",Character::isLetter);
		BINARY_PROPERTY.put("Lowercase",Character::isLowerCase);
		BINARY_PROPERTY.put("Uppercase",Character::isUpperCase);
		BINARY_PROPERTY.put("Titlecase",Character::isTitleCase);
		BINARY_PROPERTY.put("Punctuation",(c)->{
			int ct=Character.getType(c);
			return ct==Character.CONNECTOR_PUNCTUATION||ct==Character.DASH_PUNCTUATION||
					ct==Character.START_PUNCTUATION||ct==Character.END_PUNCTUATION||ct==Character.OTHER_PUNCTUATION||
					ct==Character.INITIAL_QUOTE_PUNCTUATION||ct==Character.FINAL_QUOTE_PUNCTUATION;
		});
		BINARY_PROPERTY.put("Control",(c)->Character.getType(c)==Character.CONTROL);
		BINARY_PROPERTY.put("White_Space",Character::isWhitespace);
		BINARY_PROPERTY.put("Digit",Character::isDigit);
		BINARY_PROPERTY.put("Hex_Digit",(c)->Character.isDigit(c)||(c>=0x0030&&c<=0x0039)||(c>=0x0041&&c<=0x0046)||
				(c>=0x0061&&c<=0x0066)||(c>=0xFF10&&c<=0xFF19)||(c>=0xFF21&&c<=0xFF26)||(c>=0xFF41&&c<=0xFF46));
		BINARY_PROPERTY.put("Join_Control",(c)->(c==0x200C||c==0x200D));
		BINARY_PROPERTY.put("Noncharacter_Code_Point",c->(c&0xFFFE)==0xFFFE||(c>=0xFDD0&&c<=0xFDEF));
		BINARY_PROPERTY.put("Assigned",(c)->Character.getType(c)!=Character.UNASSIGNED);
	}
	/*
	 \p{Punct}	Punctuation: One of !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
	 \p{Graph}	A visible character: [\p{Alnum}\p{Punct}]
	 \p{Print}	A printable character: [\p{Graph}\x20]
	 \p{Blank}	A space or a tab: [ \t]
	 \p{Cntrl}	A control character: [\x00-\x1F\x7F]
	 \p{XDigit}	A hexadecimal digit: [0-9a-fA-F]
	 \p{Space}
	 */
	public static RegularExpression parseRegularExpression(String regex){
		return nextRegularExpression(new CodePointBuffer(regex.codePoints().toArray()));
	}
	private static RegularExpression nextRegularExpression(CodePointBuffer buf){
		ArrayList<RegularExpression> union=new ArrayList<>();
		ArrayList<RegularExpression> concat=new ArrayList<>();
		while(!buf.isEOF()&&buf.peek()!=')'){
			RegularExpression term=nextPrimaryRegularExpression(buf);
			if(!buf.isEOF()){
				int c=buf.peek();
				switch(c){
					case '{':
						buf.skip();
						term=modifyByQuantity(buf,term);
						break;
					case '?':
						buf.skip();
						term=new UnionRegularExpression(Arrays.asList(EMPTY,term));
						break;
					case '+':
						buf.skip();
						term=new ConcatRegularExpression(Arrays.asList(term,new StarRegularExpression(term)));
						break;
				}
			}
			concat.add(term);
			if(!buf.isEOF()&&buf.peek()=='|'){
				buf.skip();
				union.add(createConcatRegularExpression(concat));
				concat.clear();
			}
		}
		union.add(createConcatRegularExpression(concat));
		return createUnionRegularExpression(union);
	}
	private static RegularExpression modifyByQuantity(CodePointBuffer buf,RegularExpression regex){
		int min=nextInteger(buf);
		RegularExpression begin=createConcatRegularExpression(Collections.nCopies(min,regex));
		if(buf.peek()==','){
			buf.skip();
			if(buf.peek()=='}'){
				regex=createConcatRegularExpression(Arrays.asList(begin,new StarRegularExpression(regex)));
			}else{
				int max=nextInteger(buf);
				RegularExpression tmp=EMPTY;
				List<RegularExpression> copy=Collections.nCopies(max-min,regex);
				List<RegularExpression> union=new ArrayList<>(max-min+1);
				for(int i=min;i<=max;i++){
					union.add(createConcatRegularExpression(copy.subList(0,i-min)));
				}
				regex=createConcatRegularExpression(Arrays.asList(begin,createUnionRegularExpression(union)));
			}
		}else{
			regex=begin;
		}
		buf.eat('}');
		return regex;
	}
	private static int nextInteger(CodePointBuffer buf){
		int i=0;
		while(Character.isDigit(buf.peek()))
			i=i*10+Character.forDigit(buf.read(),10);
		return i;
	}
	private static RegularExpression nextPrimaryRegularExpression(CodePointBuffer buf){
		switch(buf.read()){
			case '(':
				return nextCapturedRegularExpression(buf);
			case '.':
				return nextWildcardRegularExpression(buf);
			case '[':
				return new CharRegularExpression(nextRangeCharacterSet(buf));
			case '\\':
				return new CharRegularExpression(nextTypedCharacterSet(buf));
			default:
				return new CharRegularExpression(nextSingletonCharacterSet(buf));
		}
	}
	private static CharacterSet nextTypedCharacterSet(CodePointBuffer buf){
		int prefix=buf.read();
		CharacterSet type=SHORT_CHARACTER_TYPE.get(prefix);
		if(type!=null){
			return type;
		}
		if(prefix=='p'){
			buf.eat('{');
			String id=nextCurlyToken(buf);
			type=LONG_CHARACTER_TYPE.get(id);
			if(type!=null){
				return type;
			}
			return nextPropertyCharacterSet(id);
		}
		return nextEscapeCharacterSet(buf,prefix);
	}
	private static CharacterSet nextPropertyCharacterSet(String id){
		String script=removePrefix(id,"Is","script=","sc=");
		if(script!=null){
			try{
				return (c)->Character.UnicodeScript.of(c).equals(Character.UnicodeScript.forName(script));
			}catch(IllegalArgumentException ex){
			}
		}
		String block=removePrefix(id,"In","block=","blk=");
		if(block!=null){
			try{
				return (c)->Character.UnicodeBlock.of(c).equals(Character.UnicodeBlock.forName(block));
			}catch(IllegalArgumentException ex){
			}
		}
		String spec=removePrefix(id,"Is");
		if(spec!=null){
			CharacterSet set=BINARY_PROPERTY.get(spec);
			if(set!=null)
				return set;
		}else
			spec=id;
		Byte cat=UNICODE_CATEGORY.get(spec);
		if(cat!=null){
			int category=cat;
			return (c)->Character.getType(c)==category;
		}
		throw new IllegalStateException("Unknown type: "+id);
	}
	private static String removePrefix(String id,String... prefixs){
		for(String prefix:prefixs){
			if(id.startsWith(prefix)){
				return id.substring(prefix.length());
			}
		}
		return null;
	}
	private static CharacterSet nextEscapeCharacterSet(CodePointBuffer buf,int prefix){
		int code=prefix;
		switch(prefix){
			case '0':
				code=nextOctInteger(buf);
				break;
			case 'c':
				code=buf.read()^64;
				break;
			case 'x':
				if(buf.peek()=='{'){
					buf.read();
					code=Integer.parseInt(nextCurlyToken(buf),16);
				}else{
					code=RegularExpression.nextInteger(buf,2,prefix);
				}
				break;
			case 'u':
				code=RegularExpression.nextInteger(buf,4,prefix);
				break;
		}
		return CharacterSetFactory.createSingletonCharacterSet(code);
	}
	private static int nextInteger(CodePointBuffer buf,int length,int base){
		int number=0;
		for(int i=0;i<length;i++){
			number=number*base+Character.forDigit(buf.read(),base);
		}
		return number;
	}
	private static int nextOctInteger(CodePointBuffer buf){
		int number=buf.read();
		int length=number<=3?2:1;
		for(int i=0;i<length;i++){
			int d=buf.peek();
			if(d>='0'&&d<='7'){
				number=number*8+(d-'0');
				buf.skip();
			}else{
				break;
			}
		}
		return number;
	}
	private static String nextCurlyToken(CodePointBuffer buf){
		int start=buf.offset;
		int count=0;
		while(buf.read()!='}'){
			++count;
		}
		return new String(buf.codepoints,start,count);
	}
	private static RegularExpression nextCapturedRegularExpression(CodePointBuffer buf){
		RegularExpression captured=nextRegularExpression(buf);
		buf.eat(')');
		return captured;
	}
	private static CharacterSet nextRangeCharacterSet(CodePointBuffer buf){
		boolean complement=false;
		if(buf.peek()=='^'){
			buf.skip();
			complement=true;
		}
		List<CharacterSet> union=new ArrayList();
		while(buf.peek()!=']'){
			CharacterSet term=null;
			switch(buf.read()){
				case '\\':
					term=nextTypedCharacterSet(buf);
					break;
				case '[':
					term=nextRangeCharacterSet(buf);
					break;
				default:
					term=nextSingletonCharacterSet(buf);
					break;
			}
			union.add(term);
			if(buf.peek()=='&'&&buf.codepoints[buf.offset+1]=='&'){
				buf.skip();buf.skip();
				CharacterSet and=CharacterSetFactory.createIntersectionCharacterSet(CharacterSetFactory.createUnionCharacterSet(union.toArray(new CharacterSet[0])),nextRangeCharacterSet(buf));
				union.clear();
				union.add(and);
				break;
			}
		}
		buf.eat(']');
		CharacterSet set=CharacterSetFactory.createUnionCharacterSet(union.toArray(new CharacterSet[0]));
		if(complement)
			set=CharacterSetFactory.createComplementCharacterSet(set);
		return set;
	}
	private static RegularExpression nextWildcardRegularExpression(CodePointBuffer buf){
		return WILDCARD;
	}
	private static CharacterSet nextSingletonCharacterSet(CodePointBuffer buf){
		return CharacterSetFactory.createSingletonCharacterSet(nextSingletonCharacter(buf));
	}
	private static int nextSingletonCharacter(CodePointBuffer buf){
		return buf.codepoints[buf.offset-1];
	}
	private static class CodePointBuffer extends RegularExpression{
		final int[] codepoints;
		int offset=0;
		public CodePointBuffer(int[] codepoints){
			this.codepoints=codepoints;
		}
		public boolean isEOF(){
			return offset>=codepoints.length;
		}
		public int read(){
			return codepoints[offset++];
		}
		public void skip(){
			++offset;
		}
		public int peek(){
			return codepoints[offset];
		}
		public void eat(char c){
			int in=read();
			if(in!=c){
				throw new IllegalStateException("Expected "+c+" but met "+in);
			}
		}
	}
	private static class EmptyRegularExpression extends RegularExpression{
		public EmptyRegularExpression(){

		}
	}
	private static class CharRegularExpression extends RegularExpression{
		final CharacterSet charSet;
		public CharRegularExpression(CharacterSet charSet){
			this.charSet=charSet;
		}
	}
	private static RegularExpression createUnionRegularExpression(List<RegularExpression> choices){
		switch(choices.size()){
			case 0:return EMPTY;
			case 1:return choices.get(0);
			default:return new UnionRegularExpression(choices);
		}
	}
	private static class UnionRegularExpression extends RegularExpression{
		final List<RegularExpression> choices;
		public UnionRegularExpression(List<RegularExpression> choices){
			this.choices=choices;
		}
	}
	private static RegularExpression createConcatRegularExpression(List<RegularExpression> choices){
		switch(choices.size()){
			case 0:return EMPTY;
			case 1:return choices.get(0);
			default:return new ConcatRegularExpression(choices);
		}
	}
	private static class ConcatRegularExpression extends RegularExpression{
		final List<RegularExpression> parts;
		public ConcatRegularExpression(List<RegularExpression> parts){
			this.parts=parts;
		}
	}
	private static RegularExpression createStarRegularExpression(RegularExpression base){
		return new StarRegularExpression(base);
	}
	private static class StarRegularExpression extends RegularExpression{
		final RegularExpression base;
		public StarRegularExpression(RegularExpression base){
			this.base=base;
		}
	}
}
