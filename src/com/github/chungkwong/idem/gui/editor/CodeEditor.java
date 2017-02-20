/*
 * Copyright (C) 2016,2017 Chan Chung Kwong <1m02math@126.com>
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
public class CodeEditor extends JPanel{
	private final JTree tree;
	private final JEditorPane editor=new JEditorPane();
	private final JScrollPane jsp=new JScrollPane(editor);
	private final LineNumberSideBar lineHeader=new LineNumberSideBar();
	private final StyleScheme scheme=new StyleScheme();
	private boolean editing=true;
	public CodeEditor(){
		super(new BorderLayout());
		initScheme();
		jsp.getVerticalScrollBar().addAdjustmentListener((e)->lineHeader.repaint());
		add(jsp,BorderLayout.CENTER);
		add(lineHeader,BorderLayout.WEST);
		editor.setEditorKit(new javax.swing.text.StyledEditorKit());
		editor.setEditable(true);
		this.tree=new JTree((AbstractDocument.AbstractElement)editor.getDocument().getDefaultRootElement());
		editor.getDocument().addUndoableEditListener((e)->((DefaultTreeModel)tree.getModel()).setRoot(
				(AbstractDocument.AbstractElement)editor.getDocument().getDefaultRootElement()));
		editor.getDocument().addUndoableEditListener((e)->lineHeader.repaint());
		editor.getDocument().addUndoableEditListener((e)->{
			if(editing){
				editing=false;
				scheme.updateStyle((StyledDocument)editor.getDocument());
				editing=true;
			}
		});
	}
	public JTree getStructureTree(){
		return tree;
	}
	private void initScheme(){
		SimpleAttributeSet italic=new SimpleAttributeSet();
		StyleConstants.setItalic(italic,true);
		scheme.addTokenType("NUM","[0-9]+",italic);
		SimpleAttributeSet bold=new SimpleAttributeSet();
		StyleConstants.setBold(bold,true);
		scheme.addTokenType("LETTER","[a-zA-Z]+",bold);
		SimpleAttributeSet red=new SimpleAttributeSet();
		StyleConstants.setForeground(red,Color.RED);
		scheme.addTokenType("OTHER","[^0-9a-zA-Z]+",red);
	}
	public static void main(String[] args){
		JFrame f=new JFrame("Test");
		CodeEditor editor=new CodeEditor();
		f.add(new JScrollPane(editor),BorderLayout.CENTER);
		f.add(new JScrollPane(editor.getStructureTree()),BorderLayout.WEST);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		editor.requestFocusInWindow();
	}
	class LineNumberSideBar extends JComponent{
		private Dimension dimCache=new Dimension(1,1);
		public LineNumberSideBar(){
			setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
		}
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			BoxView root=(BoxView)editor.getUI().getRootView(editor).getView(0);
			int numberWidth=g.getFontMetrics().stringWidth(Integer.toString(root.getViewCount()+1));
			int numberHeight=g.getFontMetrics().getHeight();
			if(dimCache.getWidth()!=numberWidth){
				dimCache=new Dimension(numberWidth,(int)getVisibleRect().getHeight());
				setPreferredSize(dimCache);
				setMinimumSize(dimCache);
				invalidate();
				CodeEditor.this.validate();
			}
			Rectangle visibleRect=jsp.getViewport().getViewRect();
			int startOffset=editor.viewToModel(visibleRect.getLocation());
			int endOffset=editor.viewToModel(new Point(visibleRect.x,visibleRect.y+visibleRect.height));
			int startLine=root.getViewIndex(startOffset,Position.Bias.Forward);
			int endLine=root.getViewIndex(endOffset,Position.Bias.Forward);
			for(int i=startLine;i<=endLine;i++){
				Rectangle bounds=root.getChildAllocation(i,visibleRect).getBounds();
				g.drawString(Integer.toString(i+1),0,bounds.y+numberHeight-2*visibleRect.y);
			}
		}
	}
}
