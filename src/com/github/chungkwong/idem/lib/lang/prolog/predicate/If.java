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
package com.github.chungkwong.idem.lib.lang.prolog.predicate;
import com.github.chungkwong.idem.lib.lang.prolog.*;
/**
 *
 * @author kwong
 */
public class If  extends ControlConstruct{
	public static final If IF=new If();
	private If(){}
	private static final Predicate pred=new Predicate("->",2);
	@Override
	public void firstexecute(Processor exec){
		throw new UnsupportedOperationException("Not supported yet.");
	}
	@Override
	public void reexecute(Processor exec){}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}
