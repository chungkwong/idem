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
import com.github.chungkwong.idem.lib.lazy.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class LazyTreeNode<T> extends Node<T>{
	private T val;
	private Node parent;
	private Chunk<Iterator<? extends Node>> childs;
	public LazyTreeNode(T val,Node parent,Chunk<Iterator<? extends Node>> childs){
		this.val=val;
		this.parent=parent;
		this.childs=childs;
	}
	@Override
	public T getValue(){
		return val;
	}
	@Override
	public Node<?> getParent(){
		return parent;
	}
	@Override
	public Iterator<? extends Node> getChildIterator(){
		return childs==null?Collections.emptyIterator():childs.get();
	}
	@Override
	public boolean getAllowsChildren(){
		return childs!=null;
	}
}