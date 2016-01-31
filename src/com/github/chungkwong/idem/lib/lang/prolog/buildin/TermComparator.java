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
package com.github.chungkwong.idem.lib.lang.prolog.buildin;
import com.github.chungkwong.idem.lib.lang.prolog.*;
import java.math.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class TermComparator implements java.util.Comparator<Term>{
	public static final TermComparator INSTANCE=new TermComparator();
	private static final int VARIABLE=0,REAL=1,INTEGER=2,ATOM=3,COMPOUND=4;
	@Override
	public int compare(Term o1,Term o2){
		int p1=getTypePriority(o1),p2=getTypePriority(o2);
		if(p1!=p2)
			return p1-p2;
		switch(p1){
			case VARIABLE:
				return o1.toString().compareTo(o2.toString());
			case REAL:case INTEGER:
				return ((Comparable)((Atom)o1).getValue()).compareTo(((Atom)o2).getValue());
			case ATOM:
				{
					Object v1=((Atom)o1).getValue(),v2=((Atom)o2).getValue();
					if(v1==null){
						if(v2==null)
							return 0;
						else
							return -1;
					}else{
						if(v2==null)
							return 1;
						else
							return v1.toString().compareTo(v2.toString());
					}
				}
			case COMPOUND:
				{
					CompoundTerm c1=(CompoundTerm)o1,c2=(CompoundTerm)o2;
					int diff=c1.getArguments().size()-c2.getArguments().size();
					if(diff!=0)
						return diff;
					diff=c1.getFunctor().toString().compareTo(c2.getFunctor().toString());
					if(diff!=0)
						return diff;
					int len=c1.getArguments().size();
					for(int i=0;i<len;i++){
						diff=compare(c1.getArguments().get(i),c2.getArguments().get(i));
						if(diff!=0)
							return diff;
					}
					return 0;
				}
			default:
				assert false;
				return 0;
		}
	}
	private int getTypePriority(Term t){
		if(t instanceof Variable)
			return VARIABLE;
		if(t instanceof Atom){
			Object val=((Atom)t).getValue();
			if(val instanceof BigDecimal)
				return REAL;
			else if(val instanceof BigInteger)
				return INTEGER;
			else
				return ATOM;
		}
		return COMPOUND;
	}
}
