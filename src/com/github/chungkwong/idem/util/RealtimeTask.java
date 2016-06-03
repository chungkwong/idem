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
package com.github.chungkwong.idem.util;
import java.util.logging.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class RealtimeTask implements Runnable{
	private final Runnable task;
	private final boolean isDaemon;
	private final int priority;
	private final Object barrier=new Object();
	private transient boolean await=false;
	private Thread thread;
	public RealtimeTask(Runnable task,boolean isDaemon,int priority){
		this.task=task;
		this.isDaemon=isDaemon;
		this.priority=priority;
	}
	public RealtimeTask(Runnable task){
		this.task=task;
		this.isDaemon=true;
		this.priority=Thread.NORM_PRIORITY;
	}
	public void invoke(){
		synchronized(barrier){
			if(thread==null){
				thread=new Thread(this);
				thread.setDaemon(isDaemon);
				thread.setPriority(priority);
				thread.start();
			}
			await=true;
			barrier.notifyAll();
		}
	}
	public void stop(){
		synchronized(barrier){
			if(thread!=null){
				thread.interrupt();
				thread=null;
			}
		}
	}
	@Override
	public void run(){
		while(!Thread.interrupted()){
			try{
				synchronized(barrier){
					while(!await)
						barrier.wait();
					await=false;
				}
				task.run();
			}catch(InterruptedException ex){

			}catch(Exception ex){
				Logger.getGlobal().throwing("RealtimeTask","run",ex);
			}
		}
	}

}