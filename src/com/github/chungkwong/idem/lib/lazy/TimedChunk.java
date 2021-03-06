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
public class TimedChunk<T> implements Chunk<T>{
	private final Chunk<T> chunk;
	public TimedChunk(Chunk<T> chunk){
		this.chunk=chunk;
	}
	@Override
	public T get(){
		return chunk.get();
	}
	public T get(long duration,TimeUnit unit){
		try{
			return THREAD_POOL.submit(()->chunk.get()).get(duration,unit);
		}catch(InterruptedException|ExecutionException|TimeoutException ex){
			throw new RuntimeException(ex);
		}
	}
}
