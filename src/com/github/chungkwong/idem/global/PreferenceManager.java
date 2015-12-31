package com.github.chungkwong.idem.global;

import java.util.*;
import java.util.prefs.*;
/**
 * Store global settings
 */
public final class PreferenceManager{
	public static final Preferences PREFERENCE=Preferences.userNodeForPackage(PreferenceManager.class);
	static final HashMap<String,PreferenceChangeListener> toNotify=new HashMap<>();
	static{
		PREFERENCE.addPreferenceChangeListener(new PreferenceChangeListener(){
			@Override
			public void preferenceChange(PreferenceChangeEvent evt){
				PreferenceChangeListener listener=toNotify.get(evt.getKey());
				if(listener!=null)
					listener.preferenceChange(evt);
			}
		});
	}
	public static PreferenceChangeListener listenTo(String key,PreferenceChangeListener newListener){
		PreferenceChangeListener oldListener=toNotify.get(key);
		toNotify.put(key,newListener);
		return oldListener;
	}
}
