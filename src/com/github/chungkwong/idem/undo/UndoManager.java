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
package com.github.chungkwong.idem.undo;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class UndoManager{
	private final List<UndoableEvent> events;
	private ListIterator<UndoableEvent> iter;
	public UndoManager(List<UndoableEvent> events){
		this.events=events;
		iter=events.listIterator();
	}
	public void addEvent(UndoableEvent e){
		if(iter.hasNext())
			iter=events.listIterator(events.size());
		iter.add(e);
	}
	public boolean canUndo(){
		return iter.hasPrevious();
	}
	public boolean canRedo(){
		return iter.hasNext();
	}
	public void undo(){
		iter.previous().undo();
	}
	public void redo(){
		iter.previous().redo();
	}
}
