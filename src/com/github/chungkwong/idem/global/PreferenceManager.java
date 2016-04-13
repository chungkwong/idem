package com.github.chungkwong.idem.global;

import java.io.*;
import java.util.prefs.*;
/**
 * Store global settings
 */
public final class PreferenceManager{
	public static final Preferences PREFERENCE=Preferences.userNodeForPackage(PreferenceManager.class);
	public static Preferences getPreference(String path,String defaultPref){
		try{
			if(!Preferences.userRoot().nodeExists(path));//XXXX
				Preferences.importPreferences(PreferenceManager.class.getResourceAsStream(defaultPref));
		}catch(BackingStoreException|IOException|InvalidPreferencesFormatException ex){
			Log.LOG.throwing("Preference","getPreference",ex);
			ex.printStackTrace();
		}
		return PREFERENCE.node(path);
	}
	public static void main(String[] args) throws BackingStoreException{
		PREFERENCE.removeNode();
	}
}
