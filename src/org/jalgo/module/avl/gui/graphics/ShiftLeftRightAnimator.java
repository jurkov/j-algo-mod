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

/* Created on 09.06.2005 */
package org.jalgo.module.avl.gui.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.lang.Math;

import javax.swing.JOptionPane;

import org.jalgo.module.avl.datastructure.Node;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.gui.Settings;

/**
 * @author Alexander Claus, Sebastian Pape
 */
public class ShiftLeftRightAnimator
extends Animator {

	private final PaintArea pa;
	private final Graphics2D offG;
	private final Node pivot;
	private final int diff;
	
	private Rectangle boundingRect;

	public ShiftLeftRightAnimator(PaintArea pa, Graphics2D offG, Node pivot, int diff) {
		this.pa = pa;
		this.offG = offG;
		this.pivot = pivot;
		this.diff = diff;
		boundingRect = new Rectangle();
		boundingRect.x = pa.getXFor(SearchTree.getMinimumNode(pivot))
			- NODE_DIAMETER[Settings.getDisplayMode()] / 2 - 1;
		boundingRect.y = pa.getYFor(pivot)
			- NODE_DIAMETER[Settings.getDisplayMode()] / 2 - 1;
		boundingRect.width = pa.getXFor(SearchTree.getMaximumNode(pivot))
			- pa.getXFor(SearchTree.getMinimumNode(pivot))
			+ NODE_DIAMETER[Settings.getDisplayMode()] + 2;
		boundingRect.height = (pivot.getHeight() - 1)
			* Y_DIST_NODES[Settings.getDisplayMode()]
			+ NODE_DIAMETER[Settings.getDisplayMode()] + 2;
	}

	public void run() {

		int source_x = boundingRect.x/2;
        int source_y = boundingRect.y/2;
        int source_width = boundingRect.width;
        int source_height = boundingRect.height;
        int dx = boundingRect.x;
        int dy = 0;
        
        //JOptionPane.showMessageDialog(null,source_x + " " + source_y + " " + boundingRect.width + " " + boundingRect.height);
        //offG.setColor(Color.RED);
		//offG.fillRect(source_x, source_y, source_width, source_height);
        //pa.drawTree(pivot, 0, 0);
		
        if(diff>0)
        {
        	for (int y = 0; y <= diff; y++) 
        	{
    			pa.drawWhiteBackground();
    			pa.drawTree(pivot, -diff+y, 0);
        		pa.repaint();
        		try {
        			Thread.sleep(30);
        		}
        		catch (InterruptedException ex) {
        			ex.printStackTrace();
        		}
        	}
        }
        else
        {
        	for (int y = 0; y <= -diff; y++) 
        	{
    			pa.drawWhiteBackground();;
    			pa.drawTree(pivot, -diff-y, 0);
        		pa.repaint();
        		try {
        			Thread.sleep(30);
        		}
        		catch (InterruptedException ex) {
        			ex.printStackTrace();
        		}
        	}
        }
		animStopped();
	}
}