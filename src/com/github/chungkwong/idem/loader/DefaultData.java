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
package com.github.chungkwong.idem.loader;
import com.github.chungkwong.idem.gui.*;
import java.io.*;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class DefaultData implements DataObject{
	private final String msg;
	private final InputStream in;
	private final Object src;
	public DefaultData(String msg){
		this.msg=msg;
		this.in=null;
		this.src=null;
	}
	public DefaultData(String msg,InputStream in,Object src){
		this.msg=msg;
		this.in=in;
		this.src=src;
	}
	@Override
	public JComponent createDefaultView(){
		return new OpenDialog(msg,in,src);
	}
	@Override
	public String getDescription(){
		return "Choose";
	}

}