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
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class OpenDialog extends JPanel implements ListSelectionListener{
	private final JLabel msg;
	private final InputStream in;
	private final Object src;
	private final JList<String> list;
	public OpenDialog(String msg,InputStream in,Object src){
		this.msg=new JLabel(msg);
		this.in=in;
		this.src=src;
		this.list=new JList(new TreeSet(Registry.LOADERS.keySet()).toArray());
		list.setCellRenderer(new DefaultListCellRenderer(){
			@Override
			public Component getListCellRendererComponent(JList<?> list,Object value,int index,boolean isSelected,boolean cellHasFocus){
				return super.getListCellRendererComponent(list,UILanguageManager.getDefaultTranslation(value.toString()),index,isSelected,cellHasFocus);
			}

		});
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		if(src!=null)
			add(new JLabel(src.toString()));
		add(new JLabel(msg));
		list.addListSelectionListener(this);
		add(list);
	}
	@Override
	public void valueChanged(ListSelectionEvent e){
		if(in!=null){
			try{
				DataObject obj=Registry.LOADERS.get(list.getSelectedValue()).loadDataObject(in,src);
				MainFrame.MAIN_FRAME.getWindowRecent().setDataObject(obj);
			}catch(Exception ex){
				msg.setText(UILanguageManager.getDefaultTranslation("FAIL_TO_OPEN"));
			}
		}else
			MainFrame.MAIN_FRAME.getWindowRecent().setDataObject(Registry.LOADERS.get(list.getSelectedValue()).newDataObject());
	}
}
