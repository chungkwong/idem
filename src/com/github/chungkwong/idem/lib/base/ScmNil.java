package com.github.chungkwong.idem.lib.base;

public final class ScmNil implements ScmPairOrNil{
	public static final ScmNil NIL=new ScmNil();
	private ScmNil(){
	}
	public String toString(){
		return "Nil";
	}
}
