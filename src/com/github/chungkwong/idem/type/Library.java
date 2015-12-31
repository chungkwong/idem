package com.github.chungkwong.idem.type;

public abstract class Library{
	int[] version;
	public abstract String getName();
	public int[] getVersion(){
		return version;
	}
	public abstract void register();

}
