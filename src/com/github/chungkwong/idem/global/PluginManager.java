/*
 * Copyright (C) 2015 kwong
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
package com.github.chungkwong.idem.global;
import static com.github.chungkwong.idem.global.Log.LOG;
import java.util.*;
import java.util.logging.*;
/**
 *
 * @author kwong
 */
public class PluginManager{
	static final List<Plugin> registry=new ArrayList<>();
	static final String DEFAULT_LIST_SERVER="http://www.github.com/";
	public static boolean registerPlugin(Plugin plugin){
		try{
			plugin.install();
			registry.add(plugin);
		}catch(Exception ex){
			LOG.log(Level.WARNING,null,ex);
			return false;
		}
		return true;
	}
	public static boolean deregisterPlugin(Plugin plugin){
		try{
			plugin.uninstall();
			registry.remove(plugin);
		}catch(Exception ex){
			LOG.log(Level.WARNING,null,ex);
			return false;
		}
		return true;
	}
	public static void loadDefaultPlugins(){

	}
	public static List<Plugin> getLoadedPlugins(){
		return registry;
	}
	public static List<PluginSpec> getDownloadedPlugins(){
		List<PluginSpec> plugins=new ArrayList<>();

		return plugins;
	}
	public static List<PluginSpec> getDownloadablePlugins(){
		List<PluginSpec> plugins=new ArrayList<>();
		String server=PreferenceManager.PREFERENCE.get("PLUGIN_LIST_SERVER",DEFAULT_LIST_SERVER);
		
		return plugins;
	}
}
