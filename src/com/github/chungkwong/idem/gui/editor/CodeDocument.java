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
public class CodeDocument extends AbstractDocument{
	private static final AttributeSet EMPTY_ATTRIBUTE_SET=new SimpleAttributeSet();
	Element root;
	public CodeDocument(ContextFreeGrammar grammar){
		super(new GapContent());
		writeLock();
		//root=createBranchElement(root,EMPTY_ATTRIBUTE_SET);
		root=createLeafElement(null,EMPTY_ATTRIBUTE_SET,0,0);
//root=createBranchElement(null,EMPTY_ATTRIBUTE_SET);
		writeUnlock();
	}
	@Override
	public Element getDefaultRootElement(){
		return root;
	}
	@Override
	public Element getParagraphElement(int arg0){
		Element element=root;
		while(element instanceof BranchElement)
			element=element.getElement(element.getElementIndex(arg0));
		return element;
	}
}
