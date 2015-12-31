package com.github.chungkwong.idem.gui.widget;

interface ChooserFactory<T>{
	Chooser<T> getChooser(T init);
	Chooser<T> getChooser();
	Class getType();
}