/*
 * Copyright (C) 2015 Chan Chung Kwong <1m02math@126.com>
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
import java.util.concurrent.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class BackgroundChunk<T> implements Chunk<T>{
	Future<T> future;
	public BackgroundChunk(Callable<T> proc){
		future=THREAD_POOL.submit(proc);
	}
	@Override
	public synchronized T get(){
		try{
			return future.get();
		}catch(InterruptedException|ExecutionException ex){
			throw new RuntimeException(ex);
		}
	}

}
