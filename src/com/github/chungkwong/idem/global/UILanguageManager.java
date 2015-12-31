package com.github.chungkwong.idem.global;
import java.util.*;
public class UILanguageManager{
	static final String path="com.github.chungkwong.idem.resources.PHARSE";
	static UILanguageManager defaultUILanguageManager=new UILanguageManager(ResourceBundle.getBundle(path));
	ResourceBundle translation;
	/**
	 * Initialize Environment
	 */
	public UILanguageManager(ResourceBundle translation){
		this.translation=translation;
	}
	/**
	 * Get the local name corresponding to a code
	 * @param code the code
	 * @return the local name
	 */
	public String getTranslation(String code){
		return translation.getString(code);
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
