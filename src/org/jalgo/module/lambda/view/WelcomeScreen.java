package org.jalgo.module.lambda.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;

public class WelcomeScreen extends JPanel implements ActionListener, MouseListener {
	static final long serialVersionUID = 1L;
	
	private GUIController gui;
	
	private WelcomeScreenButton newButton;
	private WelcomeScreenButton loadButton;
	
	private JLabel descLabel;

	public static final Color WELCOME_BACKGROUND_COLOR = new Color(238, 173, 14);
	
	public WelcomeScreen(GUIController gui) {
		this.gui = gui;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		descLabel = new JLabel();
		descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		descLabel.setFont(new Font("Arial", Font.BOLD, 17));
		
		newButton = new WelcomeScreenButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.welcomeNew")), 
											new ImageIcon(Messages.getResourceURL("lambda", "Icon.welcomeNewOver")),
											Messages.getString("lambda", "wlc.new"));
		loadButton = new WelcomeScreenButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.welcomeLoad")),
											new ImageIcon(Messages.getResourceURL("lambda", "Icon.welcomeLoadOver")),
											Messages.getString("lambda", "wlc.load"));
		
		newButton.addActionListener(this);
		loadButton.addActionListener(this);
		newButton.addMouseListener(this);
		loadButton.addMouseListener(this);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(WELCOME_BACKGROUND_COLOR);
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(newButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(loadButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		
		add(Box.createVerticalStrut(150));
		add(buttonPane);
		add(Box.createVerticalStrut(50));
		add(descLabel);
	}

	//@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(newButton)) {
			gui.installWorkScreen();
		}
		
		if(e.getSource().equals(loadButton)) {
			JAlgoGUIConnector.getInstance().showOpenDialog(true, true);			
		}
	}
	
	//@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mouseEntered(MouseEvent e) {
		descLabel.setText(((WelcomeScreenButton)e.getSource()).getDescText());
		((WelcomeScreenButton)e.getSource()).setSelected(true);
	}

	//@Override
	public void mouseExited(MouseEvent e) {
		descLabel.setText("");
		((WelcomeScreenButton)e.getSource()).setSelected(false);
	}

	//@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * paint function to set background
	 */
	protected void paintComponent(Graphics g) {
		g.setColor(WELCOME_BACKGROUND_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
