package com.github.chungkwong.idem.gui.widget;
import java.awt.*;
public final class FontChooserFactory implements ChooserFactory<Font>{
	public FontChooserFactory(){
	}
	public Chooser<Font> getChooser(Font init){
		return new FontChooser(init);
	}
	public Chooser<Font> getChooser(){
		return new FontChooser();
	}
	public Class getType(){
		return Font.class;
	}
}