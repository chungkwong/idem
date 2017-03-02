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
import com.github.chungkwong.idem.util.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class NaiveParser implements Parser{
	public static final boolean DEBUG=false;
	public static final ParserFactory FACTORY=(g)->new NaiveParser(g);
	private final ContextFreeGrammar grammar;
	private final boolean nullable;
	public NaiveParser(ContextFreeGrammar grammar){
		this.grammar=normalize(grammar);
		if(DEBUG)
			System.out.println(this.grammar);
		this.nullable=this.grammar.getRules().stream().anyMatch((rule)->rule.getMember().length==0);
	}
	private static ContextFreeGrammar normalize(ContextFreeGrammar grammar){
		NonTerminal start=new NonTerminal("$0");
		List<ProductionRule> rules=new ArrayList<>(grammar.getRules());
		rules.add(new ProductionRule(start,new Symbol[]{grammar.getStartSymbol()},(v)->v[0]));
		removeNullRules(rules);
		removeEqualRule(rules);
		rewriteRules(rules);
		return new ContextFreeGrammar(start,rules);
	}
	private static void removeNullRules(List<ProductionRule> rules){
		while(true){
			NonTerminal cand=null;
			Iterator<ProductionRule> iter=rules.iterator();
			while(iter.hasNext()){
				ProductionRule curr=iter.next();
				if(curr.getMember().length==0&&!curr.getTarget().getID().equals("$0")){
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
					ProductionRule rule=rules.get(i);
					Symbol[] comp=rule.getMember();
					for(int j=0;j<comp.length;j++){
						if(comp[i].equals(cand)){
							Symbol[] reducedComp=arrayDelete(j,comp);
							int k=j;
							rules.add(new ProductionRule(rule.getTarget(),reducedComp,
									(a)->rule.apply((SymbolInstance[])arrayInsert(k,a)).getSemanticValue()));
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
	private static void removeEqualRule(List<ProductionRule> rules){
		while(true){
			NonTerminal to=null,from=null;
			Iterator<ProductionRule> iter=rules.iterator();
			while(iter.hasNext()){
				ProductionRule curr=iter.next();
				if(curr.getMember().length==1&&curr.getMember()[0]instanceof NonTerminal){
					to=curr.getTarget();
					from=(NonTerminal)curr.getMember()[0];
					iter.remove();
					break;
				}
			}
			if(to==null)
				break;
			int len=rules.size();
			for(int i=0;i<len;i++){
				ProductionRule rule=rules.get(i);
				if(rule.getTarget().equals(from))
					rules.add(new ProductionRule(to,rule.getMember(),rule.getAction()));
			}
		}
	}
	private static void rewriteRules(List<ProductionRule> rules){
		int count=0;
		ListIterator<ProductionRule> iter=rules.listIterator();
		while(iter.hasNext()){
			ProductionRule rule=iter.next();
			Symbol[] comp=rule.getMember();
			int len=comp.length;
			if(len>2){
				iter.remove();
				NonTerminal prev;
				NonTerminal curr=new NonTerminal("$"+(++count));
				iter.add(new ProductionRule(rule.getTarget(),new Symbol[]{comp[0],curr},(p)->{
					((Object[])p[1])[0]=p[0];
					return rule.getAction().apply((Object[])p[1]);
				}));
				for(int i=1;i<len-2;i++){
					prev=curr;
					curr=new NonTerminal("$"+(++count));
					int k=i;
					iter.add(new ProductionRule(prev,new Symbol[]{comp[i],curr},(p)->{
						((Object[])p[1])[k]=p[0];
						return p[1];
					}));
				}
				iter.add(new ProductionRule(curr,new Symbol[]{comp[len-2],comp[len-1]},(p)->{
					Object[] array=new Object[len];
					array[len-2]=p[0];
					array[len-1]=p[1];
					return array;
				}));
			}
		}
	}
	@Override
	public Object parse(Lex lex){
		List<Token> tokens=new ArrayList<>();
		lex.forEachRemaining((t)->tokens.add(t));
		int n=tokens.size();
		if(nullable&&n==0)
			return null;
		SymbolSet[][] table=new SymbolSet[n][n];
		for(int i=0;i<n;i++){
			Terminal type=tokens.get(i).getType();
			Object val=tokens.get(i).getValue();
			addSymbolInstance(i,i,new SymbolInstance(type,val),table);
			for(ProductionRule rule:grammar.getRules())
				if(rule.getMember().length==1&&rule.getMember()[0].equals(type))
					addSymbolInstance(i,i,new SymbolInstance(rule.getTarget(),val),table);
		}
		if(DEBUG)
			printTable(table);
		for(int l=2;l<=n;l++){
			for(int i=0;i<=n-l;i++){
				int j=i+l-1;
				for(int k=i;k<j;k++)
					for(ProductionRule rule:grammar.getRules()){
						SymbolInstance first,second;
						if(rule.getMember().length==2&&(first=containsSymbol(i,k,rule.getMember()[0],table))!=null&&
								(second=containsSymbol(k+1,j,rule.getMember()[1],table))!=null){
							addSymbolInstance(i,j,rule.apply(first,second),table);
						}
					}
			}
			if(DEBUG)
				printTable(table);
		}
		return table[0][n-1].contains(grammar.getStartSymbol()).getSemanticValue();
	}
	private static void addSymbolInstance(int i,int j,SymbolInstance instance,SymbolSet[][] table){
		if(table[i][j]==null)
			table[i][j]=new Singleton(instance);
		else
			table[i][j]=table[i][j].addSymbol(instance);
	}
	private static SymbolInstance containsSymbol(int i,int j,Symbol symbol,SymbolSet[][] table){
		if(table[i][j]==null)
			return null;
		else
			return table[i][j].contains(symbol);
	}
	private static void printTable(SymbolSet[][] table){
		int n=table.length;
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				System.out.print(table[i][j]);
				System.out.print('\t');
			}
			System.out.println();
		}
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
		@Override
		public String toString(){
			return instance.toString();
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
			return instances.stream().filter((s)->s.getSymbol().equals(symbol)).findAny().orElse(null);
		}
		@Override
		public String toString(){
			return instances.toString();
		}
	}
	public static void main(String[] args){
		NonTerminal start=new NonTerminal("START");
		Terminal word=new Terminal("WORD"),number=new Terminal("NUMBER"),other=new Terminal("OTHER");
		ArrayList<ProductionRule> rules=new ArrayList<>();
		rules.add(new ProductionRule(start,new Symbol[]{word,other,number},
				(a)->a[0].toString().substring(Integer.parseInt(a[2].toString()))));
		ContextFreeGrammar cfg=new ContextFreeGrammar(start,rules);
		NaiveParser parser=new NaiveParser(cfg);
		RegularExpressionLexFactory LexFactory=new RegularExpressionLexFactory();
		LexFactory.addTokenType("WORD","[a-zA-Z]+");
		LexFactory.addTokenType("NUMBER","[0-9]+");
		LexFactory.addTokenType("OTHER","[^a-zA-Z0-9]");

		System.out.println(parser.parse(LexFactory.createLex(new IntCheckPointIterator("abcd-2".codePoints().iterator()))));
	}
}