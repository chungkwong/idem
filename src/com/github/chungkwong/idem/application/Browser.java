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
package com.github.chungkwong.idem.application;
import com.github.chungkwong.idem.global.*;
import com.github.chungkwong.idem.gui.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;

/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Browser extends JPanel implements DataObject,ActionListener,HyperlinkListener{
	private final JEditorPane page=new JEditorPane();
	private final JComboBox in=new JComboBox();
	private Thread curr=null;
	private String url;
	public Browser(){
		setLayout(new BorderLayout());
		in.setEditable(true);
		in.addActionListener(this);
		add(in,BorderLayout.NORTH);
		page.setEditable(false);
		page.addHyperlinkListener(this);
		add(new JScrollPane(page),BorderLayout.CENTER);
	}
	public Browser(InputStream in,Object src) throws IOException{
		this();
		page.read(in,src);
	}
	@Override
	public void actionPerformed(ActionEvent e){
		url=(String)in.getSelectedItem();
		if(curr!=null)
			try{
				curr.interrupt();
			}catch(Exception ex){
				Log.LOG.throwing("Browser","actionPerformed",ex);
			}
		curr=new Thread(()->{
			try{page.setPage(url);in.insertItemAt(url,0);}catch(Exception ex){Log.LOG.throwing("Browser","",ex);}
		});
		curr.start();
	}
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e){
		if(e.getEventType()!=HyperlinkEvent.EventType.ACTIVATED)return;
		if(e instanceof HTMLFrameHyperlinkEvent){
			HTMLFrameHyperlinkEvent evt=(HTMLFrameHyperlinkEvent)e;
			HTMLDocument doc=(HTMLDocument)page.getDocument();
			doc.processHTMLFrameHyperlinkEvent(evt);
		}else{
			url=e.getURL().toString();
			in.setSelectedItem(url);
		}
	}
	@Override
	public String getDescription(){
		return UILanguageManager.getDefaultTranslation("Browser");
	}
	@Override
	public JComponent createDefaultView(){
		return this;
	}
}