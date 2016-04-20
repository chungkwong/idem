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
import com.github.chungkwong.idem.global.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.FocusManager;
import javax.swing.*;
import javax.swing.plaf.basic.*;
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
	public boolean isWindowSingle(){
		return component instanceof WindowSingle;
	}
	public WindowSingle getWindowSingle(){
		return (WindowSingle)component;
	}
	public void split(int orientation){
		if(component instanceof WindowSingle){
			remove(component);
			WindowSingle first=(WindowSingle)component;
			WindowSingle second=new WindowSingle(first.getDataObject());
			JSplitPane component=new JSplitPane(orientation,WindowGroup.create(first),WindowGroup.create(second));
			component.setUI(new MySplitPaneUI(component));
			component.setOneTouchExpandable(true);
			component.setDividerSize(10);
			add(component,BorderLayout.CENTER);
			validate();
			component.setDividerLocation(0.5);
			component.setResizeWeight(0.5);
			if(getParent() instanceof JSplitPane){
				((BasicSplitPaneUI)((JSplitPane)getParent()).getUI()).getDivider().repaint();
			}
			getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_1,InputEvent.CTRL_DOWN_MASK),"merge");
			FocusManager.getCurrentManager().getFocusOwner().requestFocusInWindow();
			this.component=component;
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
	//private static final SplitPaneUI SPLITPANE_UI=new MySplitPaneUI();
	private static class MySplitPaneUI extends BasicSplitPaneUI{
		public MySplitPaneUI(JSplitPane pane){
			super();
			splitPane=pane;
			divider=new MySplitPaneDivider(this);
			//divider.setBorder(new Border());
		}

		private static class MySplitPaneDivider extends BasicSplitPaneDivider
				implements MouseListener,MouseMotionListener{
			int leftEnd=0,rightEnd=0;
			public MySplitPaneDivider(BasicSplitPaneUI ui){
				super(ui);
				addMouseListener(this);
				addMouseMotionListener(this);
				ui.setLastDragLocation(splitPane.getLastDividerLocation());
			}
			@Override
			protected JButton createLeftOneTouchButton(){
				return super.createLeftOneTouchButton();
			}
			@Override
			protected JButton createRightOneTouchButton(){
				return null;
			}
			@Override
			public void paint(Graphics g){
				Graphics2D g2d=(Graphics2D)g;
				if(orientation==JSplitPane.HORIZONTAL_SPLIT)
					paintHorizontal(g2d);
				else
					paintVertical(g2d);
			}
			private boolean isIcon(int x,int y){
				int cord=orientation==JSplitPane.VERTICAL_SPLIT?x:y;
				return cord<=rightEnd;
			}
			private void setCursor(MouseEvent e){
				if(isIcon(e.getX(),e.getY()))
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				else
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}
			private void paintVertical(Graphics2D g2d){
				g2d.setColor(Color.BLACK);
				int offset=0;
				int ascent=g2d.getFontMetrics().getAscent();
				int descent=g2d.getFontMetrics().getDescent();
				int height=ascent+descent;
				double scale=(dividerSize+0.0)/height;
				g2d.scale(scale,scale);
				String descriptionL=((WindowGroup)splitPane.getLeftComponent()).isWindowSingle()?
						((WindowGroup)splitPane.getLeftComponent()).getWindowSingle().getDataObject().getDescription()
						:UILanguageManager.getDefaultTranslation("WINDOW_GROUP");
				String descriptionR=((WindowGroup)splitPane.getRightComponent()).isWindowSingle()?
						((WindowGroup)splitPane.getRightComponent()).getWindowSingle().getDataObject().getDescription()
						:UILanguageManager.getDefaultTranslation("WINDOW_GROUP");
				g2d.fillPolygon(new int[]{0,ascent/2,ascent},new int[]{ascent,0,ascent},3);
				offset+=ascent;
				g2d.drawString(descriptionL,offset,ascent);
				offset+=g2d.getFontMetrics().stringWidth(descriptionL);
				leftEnd=(int)(offset*scale);
				g2d.fillPolygon(new int[]{offset,offset+ascent/2,offset+ascent},new int[]{0,ascent,0},3);
				offset+=ascent;
				g2d.drawString(descriptionR,offset,ascent);
				offset+=g2d.getFontMetrics().stringWidth(descriptionR);
				rightEnd=(int)(offset*scale);
			}
			private void paintHorizontal(Graphics2D g2d){
				g2d.setColor(Color.BLACK);
				int offset=0;
				int ascent=g2d.getFontMetrics().getAscent();
				int descent=g2d.getFontMetrics().getDescent();
				int height=ascent+descent;
				double scale=(dividerSize+0.0)/height;
				g2d.scale(scale,scale);
				String descriptionL=((WindowGroup)splitPane.getLeftComponent()).isWindowSingle()?
						((WindowGroup)splitPane.getLeftComponent()).getWindowSingle().getDataObject().getDescription()
						:UILanguageManager.getDefaultTranslation("WINDOW_GROUPllllllllllll");
				String descriptionR=((WindowGroup)splitPane.getRightComponent()).isWindowSingle()?
						((WindowGroup)splitPane.getRightComponent()).getWindowSingle().getDataObject().getDescription()
						:UILanguageManager.getDefaultTranslation("WINDOW_GROUPllllllllllll");
				g2d.translate(0,g2d.getFontMetrics().stringWidth(descriptionR+descriptionL)+2*ascent);
				g2d.rotate(-Math.PI/2);
				g2d.fillPolygon(new int[]{0,ascent/2,ascent},new int[]{0,ascent,0},3);
				offset+=ascent;
				g2d.drawString(descriptionR,offset,ascent);
				offset+=g2d.getFontMetrics().stringWidth(descriptionR);
				leftEnd=(int)(offset*scale);
					g2d.fillPolygon(new int[]{offset,offset+ascent/2,offset+ascent},new int[]{ascent,0,ascent},3);
				offset+=ascent;
				g2d.drawString(descriptionL,offset,ascent);
				offset+=g2d.getFontMetrics().stringWidth(descriptionL);
				rightEnd=(int)(offset*scale);
				leftEnd=rightEnd-leftEnd;
			}
			@Override
			public void paintComponents(Graphics arg0){

			}
			@Override
			public void mouseClicked(MouseEvent e){
				int cord=orientation==JSplitPane.VERTICAL_SPLIT?e.getX():e.getY();
				int dividerLocation=splitPane.getLastDividerLocation();
				int minLocation=splitPane.getMinimumDividerLocation();
				int maxLocation=splitPane.getMaximumDividerLocation();
				if(cord<=leftEnd){
					if(dividerLocation<=minLocation)
						splitPane.setDividerLocation(splitPaneUI.getLastDragLocation());
					else
						splitPane.setDividerLocation(0.0);
				}else if(cord<=rightEnd){
					if(dividerLocation>=maxLocation)
						splitPane.setDividerLocation(splitPaneUI.getLastDragLocation());
					else
						splitPane.setDividerLocation(1.0);
				}
			}
			@Override
			public void mousePressed(MouseEvent e){

			}
			@Override
			public void mouseReleased(MouseEvent e){

			}
			@Override
			public void mouseEntered(MouseEvent e){
				setCursor(e);
			}
			@Override
			public void mouseExited(MouseEvent e){

			}
			@Override
			public void mouseDragged(MouseEvent e){
				splitPaneUI.setLastDragLocation(orientation==JSplitPane.VERTICAL_SPLIT?e.getX():e.getY());
			}
			@Override
			public void mouseMoved(MouseEvent e){
				setCursor(e);
			}

		}
	}
}
