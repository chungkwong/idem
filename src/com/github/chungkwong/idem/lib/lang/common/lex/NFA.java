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
import com.github.chungkwong.idem.util.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class NFA{
	private final State init, accept;
	public NFA(){
		this.init=new State();
		this.accept=new State();
	}
	public State run(IntCheckPointIterator input){
		return run(input,init);
	}
	public State run(IntCheckPointIterator input,State start){
		StateSet set=input;

		State lastAccept=start;
		return lastAccept;
	}
	public boolean isAccepted(IntCheckPointIterator input){
		return run(input).isAcceptedState();
	}
	public boolean isAccepted(IntCheckPointIterator input,State start){
		return run(input,start).isAcceptedState();
	}
	public State getInitState(){
		return init;
	}
	public State getAcceptState(){
		return init;
	}
	public State createState(){
		return new State();
	}
	public static class State{
		private final List<com.github.chungkwong.idem.lib.Pair<CharacterSet,State>> transitionTable=new LinkedList<>();
		private final List<State> lambdaTransitionTable=new LinkedList<>();
		State(){
		}
		public void addTransition(CharacterSet set,State next,boolean checkOverlap){
			if(checkOverlap&&transitionTable.stream().anyMatch((pair)->
					CharacterSetFactory.createIntersectionCharacterSet(pair.getFirst(),set).stream().findAny().isPresent())){
				throw new AmbiguousException();
			}
			transitionTable.add(new com.github.chungkwong.idem.lib.Pair<>(set,next));
		}
		public void addLambdaTransition(State next){
			lambdaTransitionTable.add(next);
		}
		public void nextState(int codePoint,HashSet<State> result){
			Optional<com.github.chungkwong.idem.lib.Pair<CharacterSet,State>> found=transitionTable.stream().filter((pair)->pair.getFirst().contains(codePoint)).findFirst();
			
		}
	}
	public static class StateSet{
		HashSet<State> set=new HashSet<>(),spare=new HashSet<>();
		public StateSet(State start){
			set.add(start);
		}
		public boolean contains(State state){
			return set.contains(state);
		}
		public void next(int codePoint){

		}
	}
}