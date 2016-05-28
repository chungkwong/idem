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
package com.github.chungkwong.idem.lib.lang.common.lex;
import com.github.chungkwong.idem.lib.*;
import java.util.*;
import java.util.stream.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class DFA{
	private static final State FAILED=new State(false);
	private State init;
	public DFA(State init){
		this.init=init;
	}
	public State run(IntStream input){
		return run(input,init);
	}
	public State run(IntStream input,State start){
		if(input.peek(null)){
			
		}
	}
	public boolean isAccepted(IntStream input){
		return run(input).isAcceptedState();
	}
	public boolean isAccepted(IntStream input,State start){
		return run(input,start).isAcceptedState();
	}
	public static class State{
		private boolean acceptedState;
		private List<Pair<CharacterSet,State>> transitionTable=new LinkedList<>();
		public State(boolean acceptedState){
			this.acceptedState=acceptedState;
		}
		public boolean isAcceptedState(){
			return acceptedState;
		}
		public void setAcceptedState(boolean acceptedState){
			this.acceptedState=acceptedState;
		}
		public void addTransition(CharacterSet set,State next,boolean checkOverlap){
			if(checkOverlap&&transitionTable.stream().anyMatch((pair)->
					CharacterSetFactory.createIntersectionCharacterSet(pair.getFirst(),set).stream().findAny().isPresent())){
				throw new AmbiguousException();
			}
			transitionTable.add(new Pair<>(set,next));
		}
		public State nextState(int codePoint){
			Optional<Pair<CharacterSet,State>> found=transitionTable.stream().filter((pair)->pair.getFirst().contains(codePoint)).findFirst();
			return found.isPresent()?found.get().getSecond():FAILED;
		}
	}
}