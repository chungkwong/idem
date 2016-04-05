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
public class SimpleTreeNode<T> extends Node<T> implements MutableTreeNode{
	private T val;
	private TreeNode parent;
	private final List<TreeNode> childs;
	public SimpleTreeNode(T val,TreeNode parent,List<TreeNode> childs){
		this.val=val;
		this.parent=parent;
		this.childs=childs;
	}
	@Override
	public T getValue(){
		return val;
	}
	@Override
	public TreeNode getParent(){
		return parent;
	}
	@Override
	public Iterator<TreeNode> getChildIterator(){
		return childs.iterator();
	}
	@Override
	public void insert(MutableTreeNode child,int index){
		childs.add(index,child);
	}
	@Override
	public void remove(int index){
		childs.remove(index);
	}
	@Override
	public void remove(MutableTreeNode node){
		childs.remove(node);
	}
	@Override
	public void setUserObject(Object object){
		this.val=(T)object;
	}
	@Override
	public void removeFromParent(){
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	@Override
	public void setParent(MutableTreeNode newParent){
		this.parent=newParent;
	}
	@Override
	public TreeNode getChildAt(int childIndex){
		return childs.get(childIndex);
	}
	@Override
	public int getChildCount(){
		return childs.size();
	}
	@Override
	public int getIndex(TreeNode node){
		return childs.indexOf(node);
	}
	@Override
	public boolean getAllowsChildren(){
		return childs!=null;
	}
	@Override
	public boolean isLeaf(){
		return childs.isEmpty();
	}
	@Override
	public Enumeration children(){
		return Collections.enumeration(childs);
	}
}