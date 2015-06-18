/*
 * j-Algo - a visualization tool for algorithm runs, especially useful for
 * students and lecturers of computer science. j-Algo is written in Java and
 * thus platform independent. Development is supported by Technische Universität
 * Dresden.
 *
 * Copyright (C) 2004-2008 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jalgo.module.heapsort;

import java.net.URL;

import org.jalgo.main.util.Messages;

/**
 * Utility class for resources and localisation.
 * 
 * @author mbue
 */
public final class Util {

	private static final String main = "main";
	private static final String module = "heapsort";
	
	public static String getString(String key) {
		return Messages.getString(module, key);
	}
	
	public static URL getMainResourceURL(String key) {
		return Messages.getResourceURL(main, key);
	}
	
	public static URL getResourceURL(String key) {
		return Messages.getResourceURL(module, key);
	}

}
