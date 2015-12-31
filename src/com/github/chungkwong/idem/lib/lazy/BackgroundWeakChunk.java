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
package com.github.chungkwong.idem.lib.lazy;
import static com.github.chungkwong.idem.global.MiscUtilities.THREAD_POOL;
import java.lang.ref.*;
import java.util.concurrent.*;
/**
 *
 * @author kwong
 */
public class BackgroundWeakChunk<T> implements Chunk<T>,Runnable{
	Callable<T> proc;
	SoftReference<T> ref;
	Exception ex;
	boolean notnull=true;
	public BackgroundWeakChunk(Callable<T> proc){
		this.proc=proc;
		THREAD_POOL.submit(this);
	}
	@Override
	public synchronized T get() throws Exception{
		T val=getAndSet();
		if(ex!=null)
			throw ex;
		return val;
	}
	@Override
	public void run(){
		getAndSet();
	}
	private synchronized T getAndSet(){
		T val=ref==null?null:ref.get();
		if(val==null&&notnull)
			val=getAndSet();
		return val;
	}
	private T callAndSet(){
		ex=null;
		T val=null;
		try{
			val=proc.call();
		}catch(Exception ex){
			this.ex=ex;
		}
		notnull=val!=null;
		ref=new SoftReference(val);
		return val;
	}
}