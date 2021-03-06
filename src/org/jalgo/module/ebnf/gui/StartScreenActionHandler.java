/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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

/* Created on 15.04.2005 */
package org.jalgo.module.ebnf.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.module.ebnf.MainController;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;

/**
 * The class <code>WelcomeScreenActionHandler</code> is an event handler for
 * the <code>StartScreen</code> class. It handles button clicks and mouse
 * events for rollover mechanism.
 * 
 * @author Tom Kazimiers
 */
public class StartScreenActionHandler implements ActionListener, MouseListener {

	private MainController maincontroller;
	private StartScreen screen;

	/**
	 * Constructs a <code>WelcomeScreenActionHandler</code> object with the
	 * given references.
	 * 
	 * @param gui the <code>GUIController</code> instance
	 * @param screen the <code>WelcomeScreen</code> instance, for which this
	 *            event handler is used
	 */
	public StartScreenActionHandler(MainController gui, org.jalgo.module.ebnf.gui.StartScreen screen) {
		this.maincontroller = gui;
		this.screen = screen;
	}

	/**
	 * Handles button clicks.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("load")) //$NON-NLS-1$
			JAlgoGUIConnector.getInstance().showOpenDialog(true, true);
		else if (e.getActionCommand().equals("startEbnfInput")) { //$NON-NLS-1$
			maincontroller.setEbnfInputMode(false);
		}
		else if (e.getActionCommand().equals("startSynDiaInput")) { //$NON-NLS-1$
			maincontroller.setSynDiaInputMode(new SynDiaSystem("S"));
			screen.setButtonsEnabled(false);
		}

		((StartButton)e.getSource()).setSelected(false);
		screen.setDescription(null);
	}

	/**
	 * Causes the event source button to highlight and to display its
	 * description string on screen.
	 */
	public void mouseEntered(MouseEvent e) {
		StartButton source = (StartButton)e.getSource();
		if (!source.isEnabled()) return;
		source.setSelected(true);
		screen.setDescription(source.getDescription());
	}

	/**
	 * Causes the event source button to be displayed normally and to remove the
	 * description string from the screen.
	 */
	public void mouseExited(MouseEvent e) {
		((StartButton)e.getSource()).setSelected(false);
		screen.setDescription(null);
	}

	/**
	 * This method has no effect.
	 */
	public void mouseClicked(MouseEvent e) {
	// This method has no effect
	}

	/**
	 * This method has no effect.
	 */
	public void mousePressed(MouseEvent e) {
	// This method has no effect
	}

	/**
	 * This method has no effect.
	 */
	public void mouseReleased(MouseEvent e) {
	// This method has no effect
	}
}