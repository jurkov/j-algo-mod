/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.am0c0.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jalgo.module.am0c0.model.am0.MachineConfiguration;
import org.jalgo.module.am0c0.model.c0.ast.C0AST;

/**
 * History to store already calculated elements like
 * {@link MachineConfiguration} or {@link C0AST}.
 * 
 * @author Max Leuth&auml;user
 */
public class AM0History<T> implements Iterator<T> {
	private List<T> history;
	private int currentIndex;

	public AM0History() {
		history = new ArrayList<T>();
		currentIndex = 0;
	}

	/**
	 * @return true if there is one more element in the history, false
	 *         otherwise.
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return currentIndex < history.size();
	}

	/**
	 * @return true if there is one more previous element in the history, false
	 *         otherwise.
	 * 
	 */
	public boolean hasPrevious() {
		return currentIndex > 0;
	}

	/**
	 * @return the next element in the history if this exists, <b>null</b>
	 *         otherwise.
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public T next() {
		if (hasNext()) {
			currentIndex++;
			return history.get(currentIndex);
		}
		return null;
	}

	/**
	 * @return the previous element in the history if this exists, <b>null</b>
	 *         otherwise.
	 * 
	 */
	public T previous() {
		if (hasPrevious()) {
			currentIndex--;
			return history.get(currentIndex);
		}
		return null;
	}

	@Override
	public void remove() {
		throw new AbstractMethodError(
				"Calling this method is not allowed for this class.");
	}

	/**
	 * @return the current size of the history.
	 */
	public int getCount() {
		return history.size();
	}

	/**
	 * @return the current index.
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * Add an object of type T to the history.
	 * 
	 * @param m
	 *            Element of type T which should be added.
	 * @throws IllegalArgumentException
	 */
	public void add(T m) throws IllegalArgumentException {
		if (m != null) {
			currentIndex++;
			for (int killIndex = currentIndex; killIndex < history.size(); killIndex++) {
				history.remove(killIndex);
			}
			history.add(m);
		} else
			throw new IllegalArgumentException(
					"Null arguments are not allowed here!");
	}

	/**
	 * Deletes all entries in the history.
	 */
	public void clear() {
		history.clear();
	}

	/**
	 * @return The object which is the last one in this history.
	 */
	public T getLastElement() {
		return history.get(history.size() - 1);
	}

	/**
	 * @param s
	 *            Step to check.
	 * @return true if this step exists already in the history.
	 */
	public boolean stepExists(int s) {
		return history.size() > s;
	}

	/**
	 * @param step
	 * @return the object at this step in the history.
	 */
	public T getAtStep(int step) {
		return history.get(step);
	}
}
