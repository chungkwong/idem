package com.github.chungkwong.idem.gui.widget;
import java.awt.*;
public final class ColorChooserFactory implements ChooserFactory<Color>{
	public ColorChooserFactory(){
	}
	public Chooser<Color> getChooser(Color init){
		return new ColorChooser(init);
	}
	public Chooser<Color> getChooser(){
		return new ColorChooser();
	}
	public Class getType(){
		return Color.class;
	}
}