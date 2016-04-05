package com.github.chungkwong.idem.global;
import java.util.*;
public class UILanguageManager{
	private static final String path="com.github.chungkwong.idem.resources.PHARSE";
	private static UILanguageManager defaultUILanguageManager=new UILanguageManager(ResourceBundle.getBundle(path));
	private final ResourceBundle bundle;
	/**
	 * Initialize Environment
	 * @param bundle
	 */
	public UILanguageManager(ResourceBundle bundle){
		this.bundle=bundle;
	}
	/**
	 * Get the local name corresponding to a code
	 * @param code the code
	 * @return the local name
	 */
	public String getTranslation(String code){
		try{
			return bundle.getString(code);
		}catch(MissingResourceException ex){
			Log.LOG.throwing("UILanguageManager","getTranslation",ex);
			return code;
		}
	}
	/**
	 * Get the local name corresponding to a code by default
	 * @param code the code
	 * @return the local name
	 */
	public static String getDefaultTranslation(String code){
		return defaultUILanguageManager.getTranslation(code);
	}
	public static void setDefaultUILanguageManager(UILanguageManager manager){
		defaultUILanguageManager=manager;
	}
}
