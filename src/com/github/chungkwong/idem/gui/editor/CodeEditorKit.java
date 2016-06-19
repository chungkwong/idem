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
import java.awt.event.*;
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
	private final Action[] actions=new Action[]{
		new SelectDownwardAction(),new SelectUpwardAction()
	};
	public CodeEditorKit(ContextFreeGrammar cfg,String mime){
		this.cfg=cfg;
		this.mime=mime;
	}
	@Override
	public void install(JEditorPane c){
		super.install(c);
		c.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl D"),actions[0]);
		c.getActionMap().put(actions[0],actions[0]);
		c.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl U"),actions[1]);
		c.getActionMap().put(actions[1],actions[1]);
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
	@Override
	public Action[] getActions(){
		return TextAction.augmentList(super.getActions(),actions);
	}
	/*@Override
	public Caret createCaret(){
		return new DefaultCaret();
	}*/
	@Override
	public Document createDefaultDocument(){
		//return new DefaultStyledDocument();
		return new CodeDocument(cfg);
	}
	static void setSelection(JTextComponent editor,int offset){
		editor.setSelectionEnd(offset);
		editor.setSelectionStart(offset);
	}
	static void setSelection(JTextComponent editor,int start,int end){
		editor.setCaretPosition(start);
		editor.setSelectionEnd(end);
		editor.setSelectionStart(start);
	}
	static boolean isInclude(int parentStart,int parentEnd,int childStart,int childEnd){
		return parentStart<=childStart&&childEnd<=parentEnd;
	}
	static boolean isIncludeProperly(int parentStart,int parentEnd,int childStart,int childEnd){
		return (parentStart<=childStart&&childEnd<parentEnd)||
				(parentStart<childStart&&childEnd<=parentEnd);
	}
	static Element getCurrentElementUp(JTextComponent editor){
		int selectionStart=editor.getSelectionStart();
		int selectionEnd=editor.getSelectionEnd();
		Element prev=editor.getDocument().getDefaultRootElement();
		while(!prev.isLeaf()){
			Element curr=prev.getElement(prev.getElementIndex(selectionStart));
			if(!isIncludeProperly(curr.getStartOffset(),curr.getEndOffset(),selectionStart,selectionEnd))
				break;
			prev=curr;
		}
		return prev;
	}
	static Element getCurrentElementDown(JTextComponent editor){
		int selectionStart=editor.getSelectionStart();
		int selectionEnd=editor.getSelectionEnd();
		Element element=editor.getDocument().getDefaultRootElement();
		while(!element.isLeaf()){
			element=element.getElement(element.getElementIndex(selectionStart));
			if(!isInclude(element.getStartOffset(),element.getEndOffset(),selectionStart,selectionEnd)){
				return element;
			}
		}
		return null;
	}
	static class SelectDownwardAction extends StyledTextAction{
		public SelectDownwardAction(){
			super("Narrow selection");
		}
		@Override
		public void actionPerformed(ActionEvent e){
			JEditorPane editor=getEditor(e);
			Element element=getCurrentElementDown(editor);
			if(element!=null)
				setSelection(editor,element.getStartOffset(),element.getEndOffset());
			else
				setSelection(editor,editor.getSelectionStart(),editor.getSelectionStart());
		}
	}
	static class SelectUpwardAction extends StyledTextAction{
		public SelectUpwardAction(){
			super("Expand selection");
		}
		@Override
		public void actionPerformed(ActionEvent e){
			JEditorPane editor=getEditor(e);
			Element element=getCurrentElementUp(editor);
			setSelection(editor,element.getStartOffset(),element.getEndOffset());
		}
	}
	static class SelectForwardAction extends StyledTextAction{
		public SelectForwardAction(){
			super("Select forward");
		}
		@Override
		public void actionPerformed(ActionEvent e){
			JEditorPane editor=getEditor(e);
			Element element=getCurrentElementUp(editor);
			setSelection(editor,element.getStartOffset(),element.getEndOffset());
		}
	}
}