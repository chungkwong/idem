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
package com.github.chungkwong.idem.gui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.FocusManager;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class WindowGroup extends JComponent{
	private Component component;
	private WindowGroup(Component component){
		setLayout(new BorderLayout());
		this.component=component;
		add(component,BorderLayout.CENTER);
		getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_H,InputEvent.ALT_DOWN_MASK),"split_h");
		getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_V,InputEvent.ALT_DOWN_MASK),"split_v");
		getActionMap().put("split_h",new SplitAction(JSplitPane.HORIZONTAL_SPLIT));
		getActionMap().put("split_v",new SplitAction(JSplitPane.VERTICAL_SPLIT));
		getActionMap().put("merge",new MergeAction());
	}
	public static WindowGroup create(WindowSingle component){
		return new WindowGroup(component);
	}
	public void split(int orientation){
		if(component instanceof WindowSingle){
			remove(component);
			WindowSingle first=(WindowSingle)component;
			WindowSingle second=first.clone();
			component=new JSplitPane(orientation,WindowGroup.create(first),WindowGroup.create(second));
			((JSplitPane)component).setOneTouchExpandable(true);
			add(component,BorderLayout.CENTER);
			validate();
			((JSplitPane)component).setDividerLocation(0.5);
			getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_1,InputEvent.CTRL_DOWN_MASK),"merge");
			FocusManager.getCurrentManager().getFocusOwner().requestFocusInWindow();
		}
	}
	public void splitVertically(){
		split(JSplitPane.VERTICAL_SPLIT);
	}
	public void splitHorizontally(){
		split(JSplitPane.HORIZONTAL_SPLIT);
	}
	public void retainFirst(){
		if(component instanceof JSplitPane){
			remove(component);
			component=(JComponent)((JSplitPane)component).getLeftComponent();
			add(component,BorderLayout.CENTER);
			getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).remove(KeyStroke.getKeyStroke(KeyEvent.VK_1,InputEvent.CTRL_DOWN_MASK));
			validate();
			FocusManager.getCurrentManager().getFocusOwner().requestFocusInWindow();
		}
	}
	public void retainSecond(){
		if(component instanceof JSplitPane){
			remove(component);
			component=(JComponent)((JSplitPane)component).getRightComponent();
			add(component,BorderLayout.CENTER);
			getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).remove(KeyStroke.getKeyStroke(KeyEvent.VK_1,InputEvent.CTRL_DOWN_MASK));
			validate();
			FocusManager.getCurrentManager().getFocusOwner().requestFocusInWindow();
		}
	}
	class SplitAction extends AbstractAction{
		int orientation;
		public SplitAction(int orientation){
			this.orientation=orientation;
		}
		@Override
		public void actionPerformed(ActionEvent e){
			split(orientation);
		}

	}
	class MergeAction extends AbstractAction{
		public MergeAction(){

		}
		@Override
		public void actionPerformed(ActionEvent e){
			if(component instanceof JSplitPane){
			WindowGroup left=(WindowGroup)((JSplitPane)component).getLeftComponent();
			if(left.component instanceof WindowSingle&&((WindowSingle)left.component).component.isFocusOwner())
				retainFirst();
			else
				retainSecond();
			}
		}

	}
}
