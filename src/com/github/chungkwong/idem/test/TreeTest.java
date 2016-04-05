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
package com.github.chungkwong.idem.test;
import com.github.chungkwong.idem.lib.lazy.*;
import com.github.chungkwong.idem.util.*;
import com.github.chungkwong.idem.util.Node;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class TreeTest extends JPanel{
	JTree tree;
	public TreeTest(){
		setLayout(new BorderLayout());
		tree=new JTree(new FileNode(File.listRoots()[0]));
		add(new JScrollPane(tree),BorderLayout.CENTER);
	}

	public static void main(String[] args){
		JFrame f=new JFrame("Tree");
		f.add(new TreeTest(),BorderLayout.CENTER);//the use of SwingConsole can be so simple
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	class FileNode extends LazyTreeNode<File>{
		private final File file;
		public FileNode(File file){
			super(file,null,new UnbufferedChunk<>(new ChildProvider(file)));
			this.file=file;
		}
		@Override
		public String toString(){
			return file.getName();
		}
	}
	class ChildProvider implements Callable<Iterator<? extends Node>>{
		private final File file;
		public ChildProvider(File file){
			this.file=file;
		}
		@Override
		public Iterator<? extends Node> call() throws Exception{
			File[] childs=file.listFiles();
			if(childs!=null)
				return Arrays.asList(childs).stream().map((f)->new FileNode(f)).collect(Collectors.toList()).iterator();
			else
				return Collections.emptyIterator();
		}
	}
}
