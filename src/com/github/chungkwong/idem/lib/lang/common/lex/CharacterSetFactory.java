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
import java.util.stream.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class CharacterSetFactory{
	public static CharacterSet createRangeCharacterSet(int begin,int end){
		return new RangeCharacterSet(begin,end);
	}
	public static CharacterSet createBlockCharacterSet(Character.UnicodeBlock block){
		return new BlockCharacterSet(block);
	}
	public static CharacterSet createUnionCharacterSet(CharacterSet set1,CharacterSet set2){
		return new UnionCharacterSet(set1,set2);
	}
	public static CharacterSet createIntersectionCharacterSet(CharacterSet set1,CharacterSet set2){
		return new IntersectionCharacterSet(set1,set2);
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
		private final CharacterSet set1,set2;
		private IntersectionCharacterSet(CharacterSet set1,CharacterSet set2){
			this.set1=set1;
			this.set2=set2;
		}
		@Override
		public boolean contains(int codePoint){
			return set1.contains(codePoint)&&set2.contains(codePoint);
		}
		@Override
		public IntStream stream(){
			return set1.stream().filter((c)->set2.contains(c));
		}
	}
	private static class UnionCharacterSet implements CharacterSet{
		private final CharacterSet set1,set2;
		private UnionCharacterSet(CharacterSet set1,CharacterSet set2){
			this.set1=set1;
			this.set2=set2;
		}
		@Override
		public boolean contains(int codePoint){
			return set1.contains(codePoint)||set2.contains(codePoint);
		}
	}
}