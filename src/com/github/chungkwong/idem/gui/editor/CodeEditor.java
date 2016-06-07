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
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.tree.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class CodeEditor extends JEditorPane{
	JTree tree;
	public CodeEditor(){
		setEditorKit(CodeEditorKit.DEFAULT_CODE_EDITOR_KIT);
		//setEditorKit(new javax.swing.text.StyledEditorKit());
		//setEditorKit(new javax.swing.text.DefaultEditorKit());
		setEditable(true);
		this.tree=new JTree((AbstractDocument.AbstractElement)getDocument().getDefaultRootElement());
		getDocument().addUndoableEditListener((e)->((DefaultTreeModel)tree.getModel()).setRoot(
				(AbstractDocument.AbstractElement)getDocument().getDefaultRootElement()));
	}
	public JTree getStructureTree(){
		return tree;
	}
	public static void main(String[] args){
		JFrame f=new JFrame("Test");
		CodeEditor editor=new CodeEditor();
		f.add(new JScrollPane(editor),BorderLayout.CENTER);
		f.add(new JScrollPane(editor.getStructureTree()),BorderLayout.WEST);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}