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

/**
 * Lexical error
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class LexicalException extends RuntimeException{
	/**
	 * Creates a new instance of <code>LexicalException</code> without detail message.
	 */
	public LexicalException() {
	}
	/**
	 * Constructs an instance of <code>LexicalException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public LexicalException(String msg) {
		super(msg);
	}
	/**
	 * Constructs an instance of <code>LexicalException</code> to repack other exception.
	 * @param cause 
	 */
	public LexicalException(Throwable cause){
		super(cause);
	}
}
