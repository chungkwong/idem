/*
 * Copyright (C) 2015 Chan Chung Kwong
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
package com.github.chungkwong.idem.global;
import java.util.logging.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class NativeLogFormatter extends Formatter{
	@Override
	public String format(LogRecord record){
		StringBuilder buf=new StringBuilder();
		buf.append('[').append(record.getLevel().getLocalizedName()).append(']');
		buf.append(record.getThreadID()).append(':');
		buf.append(record.getSourceClassName()).append('.');
		buf.append(record.getSourceMethodName()).append(':');
		buf.append(formatMessage(record)).append('\n');
		if(record.getThrown()!=null)
			buf.append(Log.throwableToString(record.getThrown()));
		return buf.toString();
	}
}