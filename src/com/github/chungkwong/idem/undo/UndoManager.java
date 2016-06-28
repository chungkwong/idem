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
import com.github.chungkwong.idem.util.*;
import java.util.*;
import java.util.stream.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class UndoManager{
	private final List<UndoableEvent> events;
	private ListIterator<UndoableEvent> iter;
	public UndoManager(){
		this(new LinkedList<>());
	}
	public UndoManager(List<UndoableEvent> events){
		this.events=events;
		iter=events.listIterator(events.size());
	}
	public void addEvent(UndoableEvent e){
		if(iter.hasNext()){
			events.addAll(new ReversedList<>(events.subList(iter.nextIndex(),events.size())).stream().map(
					(ev)->getReverseUndoableEvent(ev)).collect(Collectors.toList()));
			iter=events.listIterator(events.size());
		}
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
		iter.next().redo();
	}
	public List<UndoableEvent> getUndoList(){
		return new ReversedList<>(events.subList(0,iter.nextIndex()));
	}
	public List<UndoableEvent> getRedoList(){
		return events.subList(iter.nextIndex(),events.size());
	}
	private static UndoableEvent getReverseUndoableEvent(UndoableEvent event){
		return event instanceof ReverseUndoableEvent?((ReverseUndoableEvent)event).getRawEvent()
				:new ReverseUndoableEvent(event);
	}
	private static class ReverseUndoableEvent implements UndoableEvent{
		private final UndoableEvent rawEvent;
		public ReverseUndoableEvent(UndoableEvent rawEvent){
			this.rawEvent=rawEvent;
		}
		@Override
		public void undo(){
			rawEvent.redo();
		}
		@Override
		public void redo(){
			rawEvent.undo();
		}
		@Override
		public String getDescription(){
			return "Undo:"+rawEvent.getDescription();
		}
		public UndoableEvent getRawEvent(){
			return rawEvent;
		}
	}
}
