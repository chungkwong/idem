/*
 * Copyright (C) 2017 Chan Chung Kwong <1m02math@126.com>
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
package com.github.chungkwong.idem.lib.lang.common.parser;
import com.github.chungkwong.idem.lib.lang.common.lex.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class NaiveParser implements Parser{
	public static final ParserFactory FACTORY=(g)->new NaiveParser(g);
	private final ContextFreeGrammar grammar;
	private final boolean nullable;
	private NaiveParser(ContextFreeGrammar grammar){
		this.grammar=normalize(grammar);
		this.nullable=this.grammar.getRules().stream().allMatch((rule)->rule.getMember().length==0);
	}
	private static ContextFreeGrammar normalize(ContextFreeGrammar grammar){
		int id=0;
		NonTerminal start=new NonTerminal("$"+id);
		List<ProductionRule> rules=new ArrayList<>();
		rules.add(new ProductionRule(start,new Symbol[]{grammar.getStartSymbol()},(v)->v[0]));

		return new ContextFreeGrammar(start,rules);
	}
	private static void removeNullRules(List<ProductionRule> rules){
		while(true){
			NonTerminal cand=null;
			Iterator<ProductionRule> iter=rules.iterator();
			while(iter.hasNext()){
				ProductionRule curr=iter.next();
				if(curr.getMember().length==0){
					cand=curr.getTarget();
					iter.remove();
					break;
				}
			}
			if(cand==null)
				break;
			boolean changed=true;
			while(changed){
				changed=false;
				for(int i=0;i<rules.size();i++){
					Symbol[] comp=rules.get(i).getMember();
					for(int j=0;j<comp.length;j++){
						if(comp[i].equals(cand)){
							Symbol[] reducedComp=arrayDelete(j,comp);
							//rules.add(new ProductionRule(rules.get(i).getTarget(),comp,(a)->rules.get(i).apply((SymbolInstance[])arrayInsert(j,a))));
							break;
						}
					}
				}
			}
		}
	}
	private static <T> T[] arrayDelete(int index,T[] array){
		Object[] deleted=new Object[array.length-1];
		System.arraycopy(array,0,deleted,0,index);
		System.arraycopy(array,index+1,deleted,index,array.length-index-1);
		return (T[])deleted;
	}
	private static <T> T[] arrayInsert(int index,T[] array){
		Object[] inserted=new Object[array.length+1];
		System.arraycopy(array,0,inserted,0,index);
		System.arraycopy(array,index,inserted,index+1,array.length-index);
		return (T[])inserted;
	}
	@Override
	public Object parse(Lex lex){
		List<Token> tokens=new ArrayList<>();
		lex.forEachRemaining((t)->tokens.add(t));
		int n=tokens.size();
		SymbolSet[][] table=new SymbolSet[n][n];
		for(int i=0;i<n;i++){
			Terminal type=tokens.get(i).getType();
			Object val=tokens.get(i).getValue();
			for(ProductionRule rule:grammar.getRules())
				if(rule.getMember().length==1&&rule.getMember()[0].equals(rule))
					addSymbolInstance(i,i,new SymbolInstance(type,val),table);
		}
		for(int l=2;l<n;l++){
			for(int i=0;i<n-l;i++){
				int j=i+l-1;
				for(int k=i;k<j;k++)
					for(ProductionRule rule:grammar.getRules()){
						SymbolInstance first,second;
						if(rule.getMember().length==2&&(first=containsSymbol(i,k,rule.getMember()[0],table))!=null&&
								(second=containsSymbol(k+1,j,rule.getMember()[1],table))!=null)
							addSymbolInstance(i,j,new SymbolInstance(rule.getTarget(),rule.apply(first,second)),table);
					}
			}
		}
		return table[0][n-1].contains(grammar.getStartSymbol()).getSemanticValue();
	}
	private static void addSymbolInstance(int i,int j,SymbolInstance instance,SymbolSet[][] table){
		if(table[i][j]==null)
			table[i][j]=new Singleton(instance);
		else
			table[i][j].addSymbol(instance);
	}
	private static SymbolInstance containsSymbol(int i,int j,Symbol symbol,SymbolSet[][] table){
		if(table[i][j]==null)
			return null;
		else
			return table[i][j].contains(symbol);
	}
	private interface SymbolSet{
		SymbolSet addSymbol(SymbolInstance s);
		SymbolInstance contains(Symbol symbol);
	}
	private static class Singleton implements SymbolSet{
		private final SymbolInstance instance;
		public Singleton(SymbolInstance instance){
			this.instance=instance;
		}
		@Override
		public SymbolSet addSymbol(SymbolInstance s){
			return new Pack(instance).addSymbol(s);
		}
		@Override
		public SymbolInstance contains(Symbol symbol){
			return instance.getSymbol().equals(symbol)?instance:null;
		}
	}
	private static class Pack implements SymbolSet{
		private final List<SymbolInstance> instances=new LinkedList<>();
		public Pack(SymbolInstance instance){
			this.instances.add(instance);
		}
		@Override
		public SymbolSet addSymbol(SymbolInstance s){
			instances.add(s);
			return this;
		}
		@Override
		public SymbolInstance contains(Symbol symbol){
			return instances.stream().filter((s)->s.equals(symbol)).findAny().orElse(null);
		}
	}
}
