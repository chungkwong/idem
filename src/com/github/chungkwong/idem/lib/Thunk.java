package com.github.chungkwong.idem.lib;

public interface Thunk<K,V>{
	V get(K k);
}
