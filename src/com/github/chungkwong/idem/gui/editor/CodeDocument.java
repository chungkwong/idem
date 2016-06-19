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
import javax.swing.text.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class CodeDocument extends DefaultStyledDocument{
	private static final AttributeSet EMPTY_ATTRIBUTE_SET=new SimpleAttributeSet();
	static final String TOKEN_TYPE="$token_type";
	public CodeDocument(ContextFreeGrammar grammar){
		/*writeLock();
		try{
			//super(new GapContent());
			//writeLock();
			//root=createBranchElement(root,EMPTY_ATTRIBUTE_SET);
			//root=createLeafElement(null,EMPTY_ATTRIBUTE_SET,0,0);
			insertString(0,"hello",new SimpleAttributeSet());
		}catch(BadLocationException ex){
			Logger.getLogger(CodeDocument.class.getName()).log(Level.SEVERE,null,ex);
		}
		//Element f=createLeafElement(getDefaultRootElement(),new SimpleAttributeSet(),0,2);
		//Element s=createLeafElement(getDefaultRootElement(),new SimpleAttributeSet(),2,6);
		//StyleConstants.setComponent((MutableAttributeSet)f.getAttributes(),new JButton("..."));
		//((AbstractDocument.BranchElement)getDefaultRootElement()).replace(0,1,new Element[]{f,s});
		writeUnlock();
//root=createBranchElement(null,EMPTY_ATTRIBUTE_SET);
		//writeUnlock();*/
	}
}
