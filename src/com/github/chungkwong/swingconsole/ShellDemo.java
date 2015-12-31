/* ShellDemo.java
 * =========================================================================
 * This file is originally part of the SwingConsole Project
 *
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
package com.github.chungkwong.swingconsole;
import static com.github.chungkwong.idem.global.Log.LOG;
import java.io.*;
import java.util.*;
import java.util.logging.*;
/**
 * A example shell
 */
public final class ShellDemo implements Shell{
	Runtime runtime=Runtime.getRuntime();
	String result;
	static String lineSeparator=System.getProperty("line.separator");
	public ShellDemo(){
	}
	public boolean acceptLine(String line){
		try{
			Process p=runtime.exec(line);
			StringBuilder buf=new StringBuilder();
			String l=null;
			BufferedReader out=new BufferedReader(new InputStreamReader(p.getInputStream(),"UTF-8"));
			while((l=out.readLine())!=null){
				buf.append(l);
				buf.append(lineSeparator);
			}
			out.close();
			result=buf.toString();
			return true;
		}catch(Exception ex){
			LOG.log(Level.WARNING,null,ex);
		}
		return false;
	}
	public String evaluate(){
		return result;
	}
	public java.util.List<String> getHints(String prefix){
		ArrayList<String> lst=new ArrayList<String>();
		lst.add("ls");
		lst.add("pwd");
		lst.add("uname");
		lst.add(prefix);
		return lst;
	}
}