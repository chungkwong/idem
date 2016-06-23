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
public class CodeViewFactory implements ViewFactory{
	@Override
	public View create(Element elem){
		String kind=elem.getName();
		if(kind.equals(AbstractDocument.ContentElementName)){
			return new LabelView(elem);
		}else if(kind.equals(AbstractDocument.ParagraphElementName)){
			//return new LineWithNumberView(elem);
			return new ParagraphView(elem);
		}else if(kind.equals(AbstractDocument.SectionElementName)){
			return new BoxView(elem,View.Y_AXIS);
		}else if(kind.equals(StyleConstants.ComponentElementName)){
			return new ComponentView(elem);
		}else if(kind.equals(StyleConstants.IconElementName)){
			return new IconView(elem);
		}
		// default to text display
		//return new ComponentView(elem);
		return new LabelView(elem);
	}
	/*Simple but strange way to implement line number
	static class LineWithNumberView extends ParagraphView{
		public LineWithNumberView(Element elem){
			super(elem);
			setFirstLineIndent(50);

		}
		@Override
		public void paint(Graphics g,Shape a){
			g.drawString(Integer.toString(getElement().getParentElement().getElementIndex(getElement().getStartOffset())+1)
					,0,(int)a.getBounds().getY());
			super.paint(g,a); //To change body of generated methods, choose Tools | Templates.
		}

	}*/
}
