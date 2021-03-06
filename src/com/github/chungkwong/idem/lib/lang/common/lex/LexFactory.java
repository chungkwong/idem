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
import com.github.chungkwong.idem.lib.lang.common.parser.*;
import com.github.chungkwong.idem.util.*;
import java.util.*;
import java.util.function.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 * @param <C> Enum of token type
 */
public interface LexFactory{
	Collection<Terminal> getAllTokenType();
	void addTokenType(Terminal type,String regex,Function<String,Object> converter);
	default void addTokenType(String type,String regex){
		addTokenType(new Terminal(type),regex,(t)->t);
	}
	Lex createLex(IntCheckPointIterator src);
}