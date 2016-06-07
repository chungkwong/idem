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
package com.github.chungkwong.idem.gui.editor;
import javax.swing.*;
import javax.swing.text.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class CodeEditorKit extends StyledEditorKit{
	public static final CodeEditorKit DEFAULT_CODE_EDITOR_KIT=new CodeEditorKit(GrammarProvider.getGrammar(),"text/scheme");
	private final ContextFreeGrammar cfg;
	private final String mime;
	private final Action[] actions=new Action[]{};
	public CodeEditorKit(ContextFreeGrammar cfg,String mime){
		this.cfg=cfg;
		this.mime=mime;
	}
	@Override
	public void install(JEditorPane c){
		super.install(c);
		//c.setHighlighter(new CodeHighlighter());
	}

	public ContextFreeGrammar getCfg(){
		return cfg;
	}
	@Override
	public String getContentType(){
		return mime;
	}
	@Override
	public ViewFactory getViewFactory(){
		return new CodeViewFactory();
	}
	/*@Override
	public Action[] getActions(){
		return actions;
	}*/
	/*@Override
	public Caret createCaret(){
		return new DefaultCaret();
	}*/
	@Override
	public Document createDefaultDocument(){
		//return new DefaultStyledDocument();
		return new CodeDocument(cfg);
	}
}