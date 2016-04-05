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
package com.github.chungkwong.idem.util;
import java.util.*;
import javax.swing.tree.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public abstract class Node<T> implements TreeNode{
	@Override
	public abstract TreeNode getParent();
	@Override
	public abstract boolean getAllowsChildren();
	public abstract T getValue();
	public abstract Iterator<? extends TreeNode> getChildIterator();
	@Override
	public TreeNode getChildAt(int childIndex){
		return (TreeNode)IteratorHelper.get(getChildIterator(),childIndex);
	}
	@Override
	public int getChildCount(){
		return IteratorHelper.length(getChildIterator());
	}
	@Override
	public int getIndex(TreeNode node){
		return IteratorHelper.indexOf(getChildIterator(),node);
	}
	@Override
	public boolean isLeaf(){
		return IteratorHelper.isEmpty(getChildIterator());
	}
	@Override
	public Enumeration children(){
		return IteratorHelper.toEnumeration(getChildIterator());
	}
	@Override
	public String toString(){
		return getValue().toString();
	}
}