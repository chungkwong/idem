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
import com.github.chungkwong.idem.global.*;
import com.github.chungkwong.idem.gui.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class DefaultLoader implements DataLoader{
	public static DefaultLoader LOADER=new DefaultLoader();
	@Override
	public String toString(){
		return UILanguageManager.getDefaultTranslation("DATA_TYPE");
	}
	@Override
	public Icon getIcon(){
		return null;
	}
	@Override
	public DataObject newDataObject(){
		return new DefaultData(UILanguageManager.getDefaultTranslation("CHOOSE_A_MODE"));
	}
	@Override
	public DataObject loadDataObject(InputStream in,Object src) throws Exception{
		String suffix=null;
		if(src instanceof File)
			suffix=extractSuffix(((File)src).getName());
		else if(src instanceof URL)
			suffix=extractSuffix(((URL)src).getFile());
		if(suffix!=null){
			DataLoader loader=Registry.MIME2LOADER.get(Registry.SUFFIX2MIME.get(suffix,null),null);
			if(loader!=null)
				return loader.loadDataObject(in,src);
		}
		return new DefaultData("NO_SUITABLE_DATALOADER_FOUND",in,src);
	}
	private static String extractSuffix(String file){
		int lastDot=file.lastIndexOf('.');
		if(lastDot!=-1)
			return file.substring(lastDot+1);
		else
			return null;
	}
}