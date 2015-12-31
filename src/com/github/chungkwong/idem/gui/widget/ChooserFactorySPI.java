package com.github.chungkwong.idem.gui.widget;
import java.util.*;
public final class ChooserFactorySPI{
	static ChooserFactorySPI defaultSPI=new ChooserFactorySPI();
	HashMap<Class,ChooserFactory> factories=new HashMap<>();
	static{
		registerDefaultChooserFactory(new ColorChooserFactory());
	}
	public ChooserFactorySPI(){

	}
	public void registerChooserFactory(Class cls,ChooserFactory factory){
		factories.put(cls,factory);
	}
	public ChooserFactory getFactory(Class cls){
		return factories.get(cls);
	}
	public static void registerDefaultChooserFactory(ChooserFactory factory){
		defaultSPI.registerChooserFactory(factory.getType(),factory);
	}
	public static ChooserFactory getDefaultFactory(Class cls){
		return defaultSPI.getFactory(cls);
	}
	public static void setDefaultSPI(ChooserFactorySPI spi){
		defaultSPI=spi;
	}
}