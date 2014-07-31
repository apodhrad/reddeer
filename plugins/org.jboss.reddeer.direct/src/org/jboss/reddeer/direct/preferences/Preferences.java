package org.jboss.reddeer.direct.preferences;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

/**
 * This class provides core support for Eclipse preferences.
 * 
 * @author apodhrad
 * 
 */
public class Preferences {

	/**
	 * Returns value of plugin/key, if it doesn't exist ti will return null.
	 * 
	 * @param plugin
	 *            plugin
	 * @param key
	 *            key
	 * @return value of plugin/key
	 */
	public static String get(String plugin, String key) {
		return get(plugin, key, null);
	}

	/**
	 * 
	 * Returns value of plugin/key, if it doesn't exist ti will return a given
	 * defualt value.
	 * 
	 * @param plugin
	 *            plugin
	 * @param key
	 *            key
	 * @param defaultValue
	 *            default value
	 * @return value of plugin/key
	 */
	public static String get(String plugin, String key, String defaultValue) {
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(plugin);
		return prefs.get(key, defaultValue);
	}

	public static void put(String plugin, String key, String value) {
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(plugin);
		prefs.put(key, value);
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			throw new RuntimeException("Cannot store preferences for " + plugin + "/" + key + "='" + value + "'", e);
		}
	}
}
