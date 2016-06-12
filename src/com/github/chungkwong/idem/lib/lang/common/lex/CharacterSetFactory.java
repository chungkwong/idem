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
import java.util.stream.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class CharacterSetFactory{
	private static final CharacterSet WILDCARD_CHARACTER_SET=(c)->true;
	public static CharacterSet createWildcardCharacterSet(){
		return WILDCARD_CHARACTER_SET;
	}
	public static CharacterSet createSingletonCharacterSet(int codepoint){
		return new RangeCharacterSet(codepoint,codepoint);
	}
	public static CharacterSet createRangeCharacterSet(int begin,int end){
		return new RangeCharacterSet(begin,end);
	}
	public static CharacterSet createEnumCharacterSet(int... codePoints){
		return new EnumCharacterSet(codePoints);
	}
	public static CharacterSet createBlockCharacterSet(Character.UnicodeBlock block){
		return new BlockCharacterSet(block);
	}
	public static CharacterSet createUnionCharacterSet(CharacterSet... set){
		return new UnionCharacterSet(set);
	}
	public static CharacterSet createIntersectionCharacterSet(CharacterSet... set){
		return new IntersectionCharacterSet(set);
	}
	public static CharacterSet createComplementCharacterSet(CharacterSet set){
		return new ComplementCharacterSet(set);
	}
	private static class EnumCharacterSet implements CharacterSet{
		private final int[] codePoints;
		public EnumCharacterSet(int... codePoints){
			this.codePoints=codePoints;
		}
		@Override
		public boolean contains(int codePoint){
			return Arrays.stream(codePoints).anyMatch((c)->c==codePoint);
		}
		@Override
		public IntStream stream(){
			return Arrays.stream(codePoints);
		}
	}
	private static class RangeCharacterSet implements CharacterSet{
		private final int begin,end;
		public RangeCharacterSet(int begin,int end){
			this.begin=begin;
			this.end=end;
		}
		@Override
		public boolean contains(int codePoint){
			return codePoint>=begin&&codePoint<=end;
		}
		@Override
		public IntStream stream(){
			return IntStream.range(begin,end+1);
		}
	}
	private static class BlockCharacterSet implements CharacterSet{
		private final Character.UnicodeBlock block;
		private BlockCharacterSet(Character.UnicodeBlock block){
			this.block=block;
		}
		@Override
		public boolean contains(int codePoint){
			return Character.UnicodeBlock.of(codePoint).equals(block);
		}
	}
	private static class IntersectionCharacterSet implements CharacterSet{
		private final CharacterSet[] set;
		private IntersectionCharacterSet(CharacterSet... set){
			this.set=set;
		}
		@Override
		public boolean contains(int codePoint){
			return Arrays.stream(set).allMatch((set)->set.contains(codePoint));
		}
		@Override
		public IntStream stream(){
			return set[0].stream().filter(
					(c)->Arrays.stream(set,1,set.length).allMatch((set)->set.contains(c)));
		}
	}
	private static class UnionCharacterSet implements CharacterSet{
		private final CharacterSet[] set;
		private UnionCharacterSet(CharacterSet... set){
			this.set=set;
		}
		@Override
		public boolean contains(int codePoint){
			return Arrays.stream(set).anyMatch((set)->set.contains(codePoint));
		}
		@Override
		public IntStream stream(){
			return Arrays.stream(set).flatMapToInt(CharacterSet::stream);
		}
	}
	private static class ComplementCharacterSet implements CharacterSet{
		private final CharacterSet set;
		private ComplementCharacterSet(CharacterSet set){
			this.set=set;
		}
		@Override
		public boolean contains(int codePoint){
			return !set.contains(codePoint);
		}
	}
}