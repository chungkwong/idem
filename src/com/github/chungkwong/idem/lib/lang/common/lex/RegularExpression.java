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
	private static final HashMap<Integer,RegularExpression> SHORT_CHARACTER_TYPE=new HashMap<>();
	private static final HashMap<String,RegularExpression> LONG_CHARACTER_TYPE=new HashMap<>();
	private static final HashMap<String,Byte> UNICODE_CATEGORY=new HashMap<>();
	private static final HashMap<String,CharacterSet> BINARY_PROPERTY=new HashMap<>();
	private static final RegularExpression WILDCARD=new CharRegularExpression(CharacterSetFactory.createWildcardCharacterSet());
	private static final RegularExpression EMPTY=new EmptyRegularExpression();
	static{
		addShortCharacterType('\\',CharacterSetFactory.createSingletonCharacterSet('\\'));
		addShortCharacterType('t',CharacterSetFactory.createSingletonCharacterSet('\t'));
		addShortCharacterType('n',CharacterSetFactory.createSingletonCharacterSet('\n'));
		addShortCharacterType('r',CharacterSetFactory.createSingletonCharacterSet('\r'));
		addShortCharacterType('f',CharacterSetFactory.createSingletonCharacterSet('\f'));
		addShortCharacterType('a',CharacterSetFactory.createSingletonCharacterSet('\u0007'));
		addShortCharacterType('e',CharacterSetFactory.createSingletonCharacterSet('\u001B'));
		CharacterSet digit=CharacterSetFactory.createRangeCharacterSet('0','9');
		addShortCharacterType('d',digit);
		addShortCharacterType('D',CharacterSetFactory.createComplementCharacterSet(digit));
		CharacterSet hspace=CharacterSetFactory.createUnionCharacterSet(
				CharacterSetFactory.createRangeCharacterSet(0x2000,0x200A),
				CharacterSetFactory.createEnumCharacterSet(' ','\t',0xA0,0x1680,0x180E,0x202F,0x205F,0x3000));
		addShortCharacterType('h',hspace);
		addShortCharacterType('H',CharacterSetFactory.createComplementCharacterSet(hspace));
		CharacterSet whitespace=CharacterSetFactory.createEnumCharacterSet(' ','\t',0x0B,'\n','\f','\r');
		addShortCharacterType('s',whitespace);
		addShortCharacterType('S',CharacterSetFactory.createComplementCharacterSet(whitespace));
		CharacterSet vspace=CharacterSetFactory.createEnumCharacterSet('\n',0x0B,'\f','\r',0x85,0x2028,0x2029);
		addShortCharacterType('v',vspace);
		addShortCharacterType('V',CharacterSetFactory.createComplementCharacterSet(vspace));
		CharacterSet word=CharacterSetFactory.createUnionCharacterSet(
				CharacterSetFactory.createRangeCharacterSet('a','z'),
				CharacterSetFactory.createRangeCharacterSet('A','Z'),digit);
		addShortCharacterType('w',word);
		addShortCharacterType('W',CharacterSetFactory.createComplementCharacterSet(word));
		CharacterSet lower=CharacterSetFactory.createRangeCharacterSet('a','z');
		addLongCharacterType("Lower",lower);
		CharacterSet upper=CharacterSetFactory.createRangeCharacterSet('A','Z');
		addLongCharacterType("Upper",upper);
		addLongCharacterType("ASCII",CharacterSetFactory.createRangeCharacterSet(0x0,0x1F));
		addLongCharacterType("Alpha",CharacterSetFactory.createUnionCharacterSet(lower,upper));
		addLongCharacterType("Digit",digit);
		addLongCharacterType("Alnum",CharacterSetFactory.createUnionCharacterSet(lower,upper,digit));
		CharacterSet punct=CharacterSetFactory.createEnumCharacterSet('!','\"','#','$','%','&','\'','(',')','*','+',',','-','.','/',':',';','<','=','>','?','@','[','\\',']','^','_','`','{','|','}','~');
		addLongCharacterType("Punct",punct);
		addLongCharacterType("Graph",CharacterSetFactory.createUnionCharacterSet(lower,upper,digit,punct));
		CharacterSet print=CharacterSetFactory.createUnionCharacterSet(lower,upper,digit,punct,
				CharacterSetFactory.createSingletonCharacterSet(0x20));
		addLongCharacterType("Print",print);
		addLongCharacterType("Blank",CharacterSetFactory.createEnumCharacterSet(' ','\t'));
		CharacterSet cntrl=CharacterSetFactory.createUnionCharacterSet(
				CharacterSetFactory.createRangeCharacterSet(0x0,0x1F),
				CharacterSetFactory.createSingletonCharacterSet(0x7F));
		addLongCharacterType("Cntrl",cntrl);
		CharacterSet xdigit=CharacterSetFactory.createUnionCharacterSet(digit,
				CharacterSetFactory.createRangeCharacterSet('a','f'),
				CharacterSetFactory.createRangeCharacterSet('A','F'));
		addLongCharacterType("XDigit",xdigit);
		addLongCharacterType("Space",whitespace);
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
	private static void addShortCharacterType(char abbr,CharacterSet set){
		SHORT_CHARACTER_TYPE.put((int)abbr,new CharRegularExpression(set));
	}
	private static void addLongCharacterType(String abbr,CharacterSet set){
		LONG_CHARACTER_TYPE.put(abbr,new CharRegularExpression(set));
	}
	public static RegularExpression parseRegularExpression(String regex){
		return parseRegularExpression(new CodePointBuffer(regex.codePoints().toArray()));
	}
	private static RegularExpression parseRegularExpression(CodePointBuffer buf){
		ArrayList<RegularExpression> union=new ArrayList<>();
		ArrayList<RegularExpression> concat=new ArrayList<>();
		while(!buf.isEOF()){
			RegularExpression term=parsePrimaryRegularExpression(buf);
			if(!buf.isEOF()){
				int c=buf.peek();
				switch(c){
					case '{':
						buf.skip();
						term=parseQuantity(buf,term);
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
			}
		}
		union.add(createConcatRegularExpression(concat));
		return createUnionRegularExpression(union);
	}
	private static RegularExpression parseQuantity(CodePointBuffer buf,RegularExpression regex){
		int min=parseInteger(buf);
		RegularExpression begin=createConcatRegularExpression(Collections.nCopies(min,regex));
		if(buf.peek()==','){
			buf.skip();
			if(buf.peek()=='}'){
				regex=createConcatRegularExpression(Arrays.asList(begin,new StarRegularExpression(regex)));
			}else{
				int max=parseInteger(buf);
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
	private static int parseInteger(CodePointBuffer buf){
		int i=0;
		while(Character.isDigit(buf.peek()))
			i=i*10+Character.forDigit(buf.read(),10);
		return i;
	}
	private static RegularExpression parsePrimaryRegularExpression(CodePointBuffer buf){
		switch(buf.read()){
			case '\\':
				return parseCharacterTypeRegularExpression(buf);
			case '(':
				return parseCaughtRegularExpression(buf);
			case '[':
				return parseRangeRegularExpression(buf);
			case '.':
				return parseWildcardRegularExpression(buf);
			default:
				return parseSingletonRegularExpression(buf);
		}
	}
	private static RegularExpression parseCharacterTypeRegularExpression(CodePointBuffer buf){
		int prefix=buf.read();
		RegularExpression type=SHORT_CHARACTER_TYPE.get(prefix);
		if(type!=null){
			return type;
		}
		if(prefix=='p'){
			buf.eat('{');
			String id=parseCurly(buf);
			type=LONG_CHARACTER_TYPE.get(id);
			if(type!=null){
				return type;
			}
			return parsePropertyCharacter(id);
		}
		return parseEscapeCharacter(buf,prefix);
	}
	private static RegularExpression parsePropertyCharacter(String id){
		String script=removePrefix(id,"Is","script=","sc=");
		if(script!=null){
			try{
				return new CharRegularExpression((c)
						->Character.UnicodeScript.of(c).equals(Character.UnicodeScript.forName(script)));
			}catch(IllegalArgumentException ex){
			}
		}
		String block=removePrefix(id,"In","block=","blk=");
		if(block!=null){
			try{
				return new CharRegularExpression((c)
						->Character.UnicodeBlock.of(c).equals(Character.UnicodeBlock.forName(block)));
			}catch(IllegalArgumentException ex){
			}
		}
		String spec=removePrefix(id,"Is");
		if(spec!=null){
			CharacterSet set=BINARY_PROPERTY.get(spec);
			if(set!=null)
				return new CharRegularExpression(set);
		}else
			spec=id;
		Byte cat=UNICODE_CATEGORY.get(spec);
		if(cat!=null){
			int category=cat;
			return new CharRegularExpression((c)->Character.getType(c)==category);
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
	private static RegularExpression parseScriptCharacter(String id){
		return new CharRegularExpression((c)
				->Character.UnicodeBlock.of(c).equals(Character.UnicodeBlock.forName(id)));
	}
	private static RegularExpression parseEscapeCharacter(CodePointBuffer buf,int prefix){
		int code=prefix;
		switch(prefix){
			case '0':
				code=parseOctInteger(buf);
				break;
			case 'x':
				if(buf.peek()=='{'){
					buf.read();
					code=Integer.parseInt(parseCurly(buf),16);
				}else{
					code=parseInteger(buf,2,prefix);
				}
				break;
			case 'u':
				code=parseInteger(buf,4,prefix);
				break;
		}
		return new CharRegularExpression(CharacterSetFactory.createSingletonCharacterSet(code));
	}
	private static int parseInteger(CodePointBuffer buf,int length,int base){
		int number=0;
		for(int i=0;i<length;i++){
			number=number*base+Character.forDigit(buf.read(),base);
		}
		return number;
	}
	private static int parseOctInteger(CodePointBuffer buf){
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
	private static String parseCurly(CodePointBuffer buf){
		int start=buf.offset;
		int count=0;
		while(buf.read()!='}'){
			++count;
		}
		return new String(buf.codepoints,start,count);
	}
	private static RegularExpression parseCaughtRegularExpression(CodePointBuffer buf){
		RegularExpression caught=parseRegularExpression(buf);
		buf.eat(')');
		return caught;
	}
	private static RegularExpression parseRangeRegularExpression(CodePointBuffer buf){
	}
	private static RegularExpression parseWildcardRegularExpression(CodePointBuffer buf){
		return WILDCARD;
	}
	private static RegularExpression parseSingletonRegularExpression(CodePointBuffer buf){
		return new CharRegularExpression(CharacterSetFactory.createSingletonCharacterSet(buf.codepoints[buf.offset-1]));
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
		return choices.size()==1?choices.get(0):new UnionRegularExpression(choices);
	}
	private static class UnionRegularExpression extends RegularExpression{
		final List<RegularExpression> choices;
		public UnionRegularExpression(List<RegularExpression> choices){
			this.choices=choices;
		}
	}
	private static RegularExpression createConcatRegularExpression(List<RegularExpression> choices){
		return choices.size()==1?choices.get(0):new ConcatRegularExpression(choices);
	}
	private static class ConcatRegularExpression extends RegularExpression{
		final List<RegularExpression> parts;
		public ConcatRegularExpression(List<RegularExpression> parts){
			this.parts=parts;
		}
	}
	private static class StarRegularExpression extends RegularExpression{
		final RegularExpression base;
		public StarRegularExpression(RegularExpression base){
			this.base=base;
		}
	}
}
