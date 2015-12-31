/*
 * Copyright (C) 2015 kwong
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
package com.github.chungkwong.idem.lib;
import java.util.*;
/**
 * A data structure representing partition of objects
 */
public final class Partition<T> implements Cloneable{
	HashMap<T,Node<T>> nodes;
	/**
	 * Construct a Partition without any set
	 */
	public Partition(){
		nodes=new HashMap<>();
	}
	private Partition(HashMap<T,Node<T>> nodes){
		this.nodes=nodes;
	}
	/**
	 * Make a new set containing exactly one element
	 * @param obj the new element
	 */
	public void makeSet(T obj){
		if(!nodes.containsKey(obj))
			nodes.put(obj,new Node());
	}
	/**
	 * Find the root of the set containing a element
	 * @param obj the element
	 */
	public T findRoot(T obj){
		Stack<Node<T>> stack=new Stack<>();
		while(true){
			Node<T> node=nodes.get(obj);
			if(node.parent==null)
				break;
			stack.push(node);
			obj=node.parent;
		}
		while(!stack.empty())
			stack.pop().parent=obj;
		return obj;
	}
	/**
	 * Link two sets
	 * @param obj1 root of the frist set
	 * @param obj2 root of the second set
	 */
	private void link(T obj1,T obj2){
		if(obj1.equals(obj2))
			return;
		Node<T> node1=nodes.get(obj1),node2=nodes.get(obj2);
		int rank1=node1.rank,rank2=node2.rank;
		if(rank1>rank2){
			node2.parent=obj1;
		}else{
			node1.parent=obj2;
			if(rank1==rank2)
				node2.rank=rank1+1;
		}
	}
	/**
	 * Combine two sets
	 * @param obj1 an element contained in the first set
	 * @param obj2 an element contained in the second set
	 */
	public void union(T obj1,T obj2){
		link(findRoot(obj1),findRoot(obj2));
	}
	/**
	 * Check if a element is root
	 * @param obj the index the element
	 */
	public boolean isRoot(T obj){
		return nodes.get(obj).parent==null;
	}
	/*public String toStringNoSingle(){
		return nodes.entrySet().stream().filter((entry)->entry.getValue().parent==null)
				.map((entry)->entry.getValue().parent+"<-"+entry.getKey())
				.collect(Collectors.joining("\n"));
	}*/
	@Override
	public String toString(){
		StringBuilder buf=new StringBuilder();
		for(Map.Entry<T,Node<T>> entry:nodes.entrySet()){
			if(entry.getValue().parent==null)
				buf.append(entry.getKey()).append('\n');
			else
				buf.append(entry.getValue().parent).append("<-").append(entry.getKey()).append('\n');
		}
		return buf.toString();
	}
	@Override
	public Partition<T> clone(){
		Partition<T> copy=new Partition<>((HashMap<T,Node<T>>)nodes.clone());
		for(Map.Entry<T,Node<T>> entry:copy.nodes.entrySet())
			entry.setValue(entry.getValue().clone());
		return copy;
	}
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Partition))
			return false;
		Partition o=(Partition)obj;
		return o.nodes.keySet().equals(nodes.keySet())&&
				nodes.keySet().stream().allMatch((e)->o.findRoot(e).equals(findRoot(e)));
	}
}
class Node<T>{
	T parent=null;
	int rank=0;
	public Node(){
	}
	public Node(T parent,int rank){
		this.parent=parent;
		this.rank=rank;
	}
	@Override
	public Node<T> clone(){
		return new Node<>(parent,rank);
	}
}