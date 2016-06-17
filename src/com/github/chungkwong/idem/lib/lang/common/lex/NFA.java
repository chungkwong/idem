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
import java.util.stream.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public final class NFA{
	private final State init, accept;
	public NFA(){
		this.init=createState();
		this.accept=createState();
	}
	public StateSet run(IntCheckPointIterator input){
		return run(input,init);
	}
	public StateSet run(IntCheckPointIterator input,State start){
		StateSet set=new StateSet(start);
		StateSet lastAccept=null;
		StringBuilder buf=new StringBuilder();
		input.startPreread();
		while(!set.isEmpty()&&input.hasNext()){
			set.next(input.nextInt());
			if(set.contains(accept)){
				for(int c:input.endPrereadForward())
					buf.appendCodePoint(c);
				lastAccept=set.clone();
				input.startPreread();
			}
		}
		input.endPrereadBackward();
		String text=buf.toString();
		return lastAccept;
	}
	public boolean isAccepted(IntCheckPointIterator input){
		return isAccepted(input,init);
	}
	public boolean isAccepted(IntCheckPointIterator input,State start){
		return run(input,start).contains(accept);
	}
	public State getInitState(){
		return init;
	}
	public State getAcceptState(){
		return accept;
	}
	public State createState(){
		State state=new State();
		return state;
	}
	public void prepareForRun(){
		findLambdaClosure();
	}
	public void findLambdaClosure(){
		boolean changed=true;
		while(changed){
			changed=false;
			for(State state:getStates()){
				for(State next:state.lambdaTransitionTable){
					for(State nextnext:next.lambdaTransitionTable){
						changed|=!state.lambdaTransitionTable.contains(nextnext);
						state.lambdaTransitionTable.add(next);
					}
				}
			}
		}
	}
	private Collection<State> getStates(){
		HashSet<State> states=new HashSet<>();
		Stack<State> tosearch=new Stack<>();
		states.add(init);
		tosearch.push(init);
		while(!tosearch.isEmpty()){
			State state=tosearch.pop();
			Set<State> found=state.lambdaTransitionTable.stream().filter((s)->!states.contains(s)).collect(Collectors.toSet());
			states.addAll(found);
			tosearch.addAll(found);
			found=state.transitionTable.stream().map((p)->p.getSecond()).filter((s)->!states.contains(s)).collect(Collectors.toSet());
			states.addAll(found);
			tosearch.addAll(found);
		}
		return states;
	}
	public DFA toDFA(){
		Collection<State> states=getStates();
		DFA dfa=new DFA(null);
		DFA.State state=new DFA.State();


		return dfa;
	}
	public static class State{
		private final List<com.github.chungkwong.idem.lib.Pair<CharacterSet,State>> transitionTable=new LinkedList<>();
		private final HashSet<State> lambdaTransitionTable=new HashSet<>();
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
		public StateSet(){
		}
		public StateSet(State start){
			set.add(start);
			findClosure();
		}
		public boolean contains(State state){
			return set.contains(state);
		}
		public boolean isEmpty(){
			return set.isEmpty();
		}
		public void next(int codePoint){
			set.stream().forEach((state)->state.nextState(codePoint,spare));
			HashSet<State> tmp=set;
			set=spare;
			spare=tmp;
			spare.clear();
			findClosure();
		}
		private void findClosure(){
			set.stream().forEach((state)->{spare.addAll(state.lambdaTransitionTable);});
			set.addAll(spare);
			spare.clear();
		}
		@Override
		protected StateSet clone(){
			StateSet copy=new StateSet();
			copy.set.addAll(set);
			return copy;
		}
		@Override
		public boolean equals(Object obj){
			return obj!=null&&obj instanceof StateSet&&((StateSet)obj).set.equals(set);
		}
		@Override
		public int hashCode(){
			int hash=7;
			hash=89*hash+Objects.hashCode(this.set);
			return hash;
		}

	}
}