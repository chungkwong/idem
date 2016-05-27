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
	public static CharacterSet createUnionCharacterSet(CharacterSet set1,CharacterSet set2){
		return (c)->set1.contains(c)||set2.contains(c);
	}
	public static CharacterSet createIntersectionCharacterSet(CharacterSet set1,CharacterSet set2){
		return (c)->set1.contains(c)&&set2.contains(c);
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
		public BlockCharacterSet(Character.UnicodeBlock block){
			this.block=block;
		}
		@Override
		public boolean contains(int codePoint){
			return Character.UnicodeBlock.of(codePoint).equals(block);
		}
	}
}