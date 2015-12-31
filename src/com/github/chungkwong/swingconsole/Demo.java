/* Demo.java
 * =========================================================================
 * This file is originally part of the SwingConsole Project
 *
 * Copyright (C) 2015 Chan Chung Kwong
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 */
package com.github.chungkwong.swingconsole;
import java.awt.*;
import javax.swing.*;
/**
 * A simple example intended to show the use of SwingConsole
 */
public final class Demo{
	public static void main(String[] args) throws Exception{
		JFrame f=new JFrame("Console");
		f.add(new SwingConsole(new ShellDemo()),BorderLayout.CENTER);//the use of SwingConsole can be so simple
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}