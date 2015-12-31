package com.github.chungkwong.idem.gui.widget;
import javax.swing.*;
public interface Chooser<T>{
	JComponent getComponent();
	T getValue();
}