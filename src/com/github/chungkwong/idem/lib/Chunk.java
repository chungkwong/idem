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
package com.github.chungkwong.idem.lib;
import static com.github.chungkwong.idem.global.Log.LOG;
import java.lang.ref.*;
import java.util.concurrent.*;
import java.util.logging.*;
/**
 *
 * @author kwong
 */
public class Chunk<T>{
	static ExecutorService pool=Executors.newCachedThreadPool();
	Future<T> future;
	SoftReference<T> ref;
	public Chunk(Callable<T> callable){
		future=pool.submit(callable);
	}
	private T getCached(){
		return ref!=null?ref.get():null;
	}
	public T get(){
		T obj=getCached();
		if(obj==null)
			try{
				obj=future.get();
				ref=new SoftReference(obj);
			}catch(Exception ex){
				LOG.log(Level.WARNING,null,ex);
			}
		return obj;
	}
	public T get(long timeout,TimeUnit unit){
		T obj=getCached();
		if(obj==null)
			try{
				obj=future.get(timeout,unit);
				ref=new SoftReference(obj);
			}catch(Exception ex){
				LOG.log(Level.WARNING,null,ex);
			}
		return obj;
	}
	public static void main(String[] args)throws Exception{
		new Chunk<String>(()->{Thread.sleep(5000);System.out.println("scad");return "good";}).get(3,TimeUnit.SECONDS);
		System.out.println("good");
	}
}
