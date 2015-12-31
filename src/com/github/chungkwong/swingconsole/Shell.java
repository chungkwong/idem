/* Shell.java
 * =========================================================================
 * This file is originally part of the SwingConsole Project
 *
 * Copyright (C) 2015 Chan Chung Kwong
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 */
package com.github.chungkwong.swingconsole;
/**
 * A interface that a shell
 */
public interface Shell{
	/**
	 * Check if the input is valid
	 * @param line input
	 * @return result
	 */
	public boolean acceptLine(String line);
	/**
	 * Evaluate the last input accepted
	 * @return output
	 */
	public String evaluate();
	/**
	 * Search for all possible auto complete
	 * @return all auto-completion
	 */
	public java.util.List<String> getHints(String prefix);
}