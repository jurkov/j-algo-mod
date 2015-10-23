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
package org.jalgo.module.am0c0.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.activity.InvalidActivityException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.core.Editor;
import org.jalgo.module.am0c0.core.Transformator;
import org.jalgo.module.am0c0.core.Transformator.TransformationState;
import org.jalgo.module.am0c0.gui.jeditor.JEditor;
import org.jalgo.module.am0c0.gui.jeditor.JEditor.ExInteger;
import org.jalgo.module.am0c0.gui.jeditor.JEditor.LineType;
import org.jalgo.module.am0c0.gui.jeditor.jedit.tokenmarker.CTransTokenMarker;
import org.jalgo.module.am0c0.model.AddressException;
import org.jalgo.module.am0c0.model.CodeObject;
import org.jalgo.module.am0c0.model.am0.AM0Program;
import org.jalgo.module.am0c0.model.c0.ast.Statement;
import org.jalgo.module.am0c0.model.c0.trans.C0TransProgram;
import org.jalgo.module.am0c0.model.c0.trans.SymbolException;
import org.jalgo.module.am0c0.model.c0.trans.SymbolTable;

/**
 * This class shows the transformation in the JAlgo module C0/AM0.
 * 
 * @author Max Leuth&auml;user
 * @author Felix Schmitt
 */
public class TransformationView extends View {
	private static final long serialVersionUID = 1L;
	private JButton backToEditorButton, stepBackButton, applyButton, stepToEndButton, simulateButton;
	private JButton matchButton, replaceToAm0Button;
	private JSplitPane hSplitPane, vSplitPane;
	private JPanel leftPanel, rightPanel, buttonPanel, allButtons, buttonPanelTrans, buttonPanelLinear;
	private JTable symbolTable;
	private DefaultTableModel symbolTableModel;
	private JEditorPane previewText, ruleText;
	private JEditor codeEditor;
	private ButtonHandler buttonHandler;
	private JScrollPane ruleScrollPane, previewScrollPane;
	private Transformator transController;

	private int markingStart;
	private int markingEnd;
	private int iStartnumber;
	private ExInteger markingIndex;

	/**
	 * see {@link View}
	 * 
	 * @param transController
	 *            the {@link Transformator} associated with this
	 *            {@link TransformationView}
	 */
	public TransformationView(Transformator transController) {
		markingStart = -1;
		markingEnd = -1;
		iStartnumber = 1;
		markingIndex = new ExInteger(-1);

		initComponents();
		initComponentAttributs();
		attachButtonHandler();
		add(vSplitPane);
		this.transController = transController;
	}

	@Override
	/**
	 * creates and initializes components, attaches handlers and sets attributes
	 */
	protected void initComponents() {
		buttonHandler = new ButtonHandler();
		leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());

		rightPanel = new JPanel();

		buttonHandler = new ButtonHandler();
		String[] x = { Messages.getString("am0c0", "TransformationView.1"), Messages.getString("am0c0", "TransformationView.3") }; //$NON-NLS-1$ //$NON-NLS-2$
		String[][] y = { { "", "" } }; //$NON-NLS-1$ //$NON-NLS-2$
		symbolTable = new JTable();
		symbolTable.setEnabled(false);
		symbolTableModel = new DefaultTableModel(y, x);
		symbolTable.setModel(symbolTableModel);

		ruleText = new JEditorPane();
		ruleText.setFont(GuiConstants.STANDARDFONT_SERIF);

		ruleScrollPane = getScrollPane(ruleText);

		rightPanel.add(getScrollPane(symbolTable));
		rightPanel.add(ruleScrollPane);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(0, 1));
		
		buttonPanelTrans = new JPanel();
		buttonPanelTrans.setLayout(new GridLayout(1, 3));
		
		buttonPanelLinear = new JPanel();
		buttonPanelLinear.setLayout(new GridLayout(1, 2));

		stepBackButton = new JButton(Messages.getString("am0c0", "TransformationView.8")); //$NON-NLS-1$
		stepBackButton.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", GuiConstants.PREVIOUS_ICON))); //$NON-NLS-1$

		applyButton = new JButton(Messages.getString("am0c0", "TransformationView.10")); //$NON-NLS-1$
		applyButton.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", GuiConstants.NEXT_ICON))); //$NON-NLS-1$
		applyButton.setEnabled(false);

		stepToEndButton = new JButton(Messages.getString("am0c0", "TransformationView.12")); //$NON-NLS-1$
		stepToEndButton.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", GuiConstants.LAST_ICON))); //$NON-NLS-1$
		stepToEndButton.setEnabled(false);

		simulateButton = new JButton(Messages.getString("am0c0", "TransformationView.14")); //$NON-NLS-1$
		simulateButton.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", GuiConstants.RUN_ICON))); //$NON-NLS-1$
		
		matchButton = new JButton("Match");
		matchButton.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", GuiConstants.RUN_ICON)));
		
		replaceToAm0Button = new JButton("Replace");
		replaceToAm0Button.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", GuiConstants.LAST_ICON)));
		
		backToEditorButton = new JButton(Messages.getString("am0c0", "TransformationView.16")); //$NON-NLS-1$
		backToEditorButton.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", GuiConstants.BACK_TO_EDITOR_ICON))); //$NON-NLS-1$

		buttonPanelTrans.add(stepBackButton);
		buttonPanelTrans.add(applyButton);
		buttonPanelTrans.add(stepToEndButton);
		
		buttonPanelLinear.add(matchButton);
		buttonPanelLinear.add(replaceToAm0Button);
		
		buttonPanel.add(buttonPanelTrans);
		buttonPanel.add(buttonPanelLinear);
		
		allButtons = new JPanel();
		allButtons.setLayout(new GridLayout(1, 3));
		
		allButtons.add(backToEditorButton, BorderLayout.WEST);
		allButtons.add(new JPanel());
		allButtons.add(simulateButton, BorderLayout.EAST);

		previewText = new JEditorPane();
		previewText.setFont(GuiConstants.STANDARDFONT);

		previewScrollPane = getScrollPane(previewText);

		codeEditor = new JEditor(new Observer() {

			@Override
			public void update(Observable arg0, Object arg1) {
				// modified flag can be ignored here
			}
		}, null);

		codeEditor.setMinimumSize(new Dimension(200, 200));

		setCodeEditorListeners();

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(codeEditor, BorderLayout.CENTER);
		p.add(buttonPanel, BorderLayout.SOUTH);
		
		hSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, p, previewScrollPane);

		leftPanel.add(hSplitPane, BorderLayout.CENTER);
		leftPanel.add(allButtons, BorderLayout.SOUTH);

		vSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPanel, rightPanel);
	}
	
	private void setCodeEditorListeners() {
		codeEditor.getPainter().addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				int line = codeEditor.yToLine(arg0.getY());

				if (transController.getState() != TransformationState.TS_FINISHED) {
					ExInteger startLine = new ExInteger(-1);
					ExInteger tmpIndex = new ExInteger(-1);

					CodeObject codeObject = codeEditor.getCodeFromLine(line, startLine, tmpIndex);

					if (codeObject != null) {
						try {
							if (line >= markingStart && line <= markingEnd) {
								applyMarkedStatement();
							} else {

								if (transController.previewStatement(tmpIndex.getValue())) {
									markingIndex.set(tmpIndex.getValue());
									markingStart = startLine.getValue();
									markingEnd = startLine.getValue() + codeObject.getLinesCount() - 1;
									updateGUI();
								}

							}
						} catch (IllegalArgumentException e) {
							updateGUI();
						}
					} else {

						if (transController.getState() == TransformationState.TS_PREVIEW
								|| transController.getState() == TransformationState.TS_WAITING) {
							transController.setState(TransformationState.TS_WAITING);
						} else {
							transController.setState(TransformationState.TS_WAITING_FIRST);
						}

						updateGUI();
					}
				}
			}
		});
	}

	@Override
	protected void attachButtonHandler() {
		backToEditorButton.addActionListener(buttonHandler);
		stepBackButton.addActionListener(buttonHandler);
		applyButton.addActionListener(buttonHandler);
		stepToEndButton.addActionListener(buttonHandler);
		simulateButton.addActionListener(buttonHandler);
		matchButton.addActionListener(buttonHandler);
		replaceToAm0Button.addActionListener(buttonHandler);

		backToEditorButton.addMouseListener(buttonHandler);
		stepBackButton.addMouseListener(buttonHandler);
		applyButton.addMouseListener(buttonHandler);
		stepToEndButton.addMouseListener(buttonHandler);
		simulateButton.addMouseListener(buttonHandler);		
		matchButton.addMouseListener(buttonHandler);
		replaceToAm0Button.addMouseListener(buttonHandler);
	}

	@Override
	protected void initComponentAttributs() {
		setLayout(new BorderLayout());

		rightPanel.setLayout(new GridLayout(2, 1));
		ruleText.setEditable(false);

		ruleScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray),
				Messages.getString("am0c0", "TransformationView.18"))); //$NON-NLS-1$

		simulateButton.setEnabled(false);

		previewText.setEditable(false);

		previewScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray),
				Messages.getString("am0c0", "TransformationView.19"))); //$NON-NLS-1$

		codeEditor.setTokenMarker(new CTransTokenMarker());
		codeEditor.setEditable(false);
		codeEditor.getPainter().setLineHighlightEnabled(false);
		codeEditor.setCodeHighlightMode(true);

		hSplitPane.setOneTouchExpandable(true);
		hSplitPane.setDividerLocation(400);

		vSplitPane.setOneTouchExpandable(true);
	}

	/**
	 * Update the {@link SymbolTable}.
	 */
	public void updateSymbolTable() {
		while (symbolTableModel.getRowCount() > 0)
			symbolTableModel.removeRow(0);

		if (transController.getSymbolTable().size() == 0) {
			String[] emptyRow = { "", "" }; //$NON-NLS-1$ //$NON-NLS-2$
			symbolTableModel.addRow(emptyRow);
		} else {
			try {
				String[][] tableStr = transController.getSymbolTable().toStringTable();
				for (int i = 0; i < transController.getSymbolTable().size(); i++)
					symbolTableModel.addRow(tableStr[i]);
			} catch (SymbolException e) {
				JOptionPane.showMessageDialog(transController.getView(), e.getMessage(), Messages.getString("am0c0", "TransformationView.4"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Update the model in the {@link Editor}.
	 * 
	 * @param c0TransProgram
	 *            of type {@link C0TransProgram}.
	 */
	public void updateModel(C0TransProgram c0TransProgram) {
		/**
		 * TODO: clear/revert everything here
		 */
		codeEditor.setLineType(LineType.NORMAL);
		codeEditor.updateModel(c0TransProgram);
		updateGUI();
	}

	@Override
	/**
	 * see {@link View}
	 */
	public void setPresentationMode(boolean presentationMode) {
		if (presentationMode) {
			codeEditor.setFont(GuiConstants.PRESENTATIONFONT);
			previewText.setFont(GuiConstants.PRESENTATIONFONT);
			ruleText.setFont(GuiConstants.PRESENTATIONFONT_SERIF);
			symbolTable.setFont(GuiConstants.PRESENTATIONFONT);
		} else {
			codeEditor.setFont(GuiConstants.STANDARDFONT);
			previewText.setFont(GuiConstants.STANDARDFONT);
			ruleText.setFont(GuiConstants.STANDARDFONT_SERIF);
			symbolTable.setFont(GuiConstants.STANDARDFONT);
		}
	}
	
	@Override
	public void setFontSize(int size) {
		Font x = codeEditor.getFont();
		codeEditor.setFont(new Font(x.getName(),x.getStyle(),12+size));
		x = previewText.getFont();
		previewText.setFont(new Font(x.getName(),x.getStyle(),12+size));
		x = ruleText.getFont();
		ruleText.setFont(new Font(x.getName(),x.getStyle(),12+size));
		x = symbolTable.getFont();
		symbolTable.setFont(new Font(x.getName(),x.getStyle(),12+size));
		symbolTable.setRowHeight(28+size);
	}

	private class ButtonHandler implements ActionListener, MouseListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == backToEditorButton) {
				int choice = JOptionPane.NO_OPTION;
				
				if (transController.getState() == TransformationState.TS_FINISHED) {
					choice = JOptionPane.showConfirmDialog(transController.getView(), Messages.getString("am0c0", "TransformationView.25"), //$NON-NLS-1$
						Messages.getString("am0c0", "TransformationView.26"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
				}
				if (choice == JOptionPane.YES_OPTION) {
					try {
						AM0Program am0program = transController.getAM0Program();
						transController.getController().getEditor().setAM0Program(am0program);
						// this must be done to remove the model from JEditor and make it editable
						transController.getController().getEditor().getView().getJEditor().setLineType(LineType.NORMAL);
						transController.getController().getEditor().getView().getJEditor().updateModel(null);
					} catch (AddressException arg) {
						JOptionPane.showMessageDialog(transController.getView(), arg
							.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					}
				}
				transController.getController().showEditor();
			}

			if (e.getSource() == applyButton) {
				if (transController.getState() != TransformationState.TS_PREVIEW
						&& transController.getState() != TransformationState.TS_PREVIEW_FIRST) {
					JOptionPane.showMessageDialog(transController.getView(),
							"Transformator should be in state PREVIEW or PREVIEW_FIRST.", "Error", //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.ERROR_MESSAGE);
					throw new IllegalStateException("Transformator should be in state PREVIEW."); //$NON-NLS-1$
				}

				if (markingStart >= 0 && markingEnd < codeEditor.getLineCount()) {
					try {
						applyMarkedStatement();
					} catch (IllegalArgumentException arg) {
						JOptionPane.showMessageDialog(transController.getView(), arg.getMessage(), Messages.getString("am0c0", "TransformationView.5"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						updateGUI();
					} catch (IllegalStateException arg) {
						JOptionPane.showMessageDialog(transController.getView(), arg.getMessage(), Messages.getString("am0c0", "TransformationView.32"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						updateGUI();
					}
				}
			}

			if (e.getSource() == stepToEndButton) {
				if (transController.getState() == TransformationState.TS_FINISHED)
				{
					codeEditor.setLineType(LineType.NORMAL);
					codeEditor.updateModel(transController.getC0TransProgram());
					matchButton.setEnabled(true);
					throw new IllegalStateException(
							Messages.getString("am0c0", "TransformationView.33")); //$NON-NLS-1$
				}
				
				try {
					applyAllStatements();
				} catch (IllegalArgumentException arg) {
					JOptionPane.showMessageDialog(transController.getView(), arg.getMessage(), Messages.getString("am0c0", "TransformationView.34"), //$NON-NLS-1$
							JOptionPane.ERROR_MESSAGE);
					updateGUI();
				} catch (IllegalStateException arg) {
					JOptionPane.showMessageDialog(transController.getView(), arg.getMessage(), Messages.getString("am0c0", "TransformationView.35"), //$NON-NLS-1$
							JOptionPane.ERROR_MESSAGE);
					updateGUI();
				}

			}

			if (e.getSource() == stepBackButton) {
				try {
					transController.stepBack();
				} catch (InvalidActivityException error) {
					JOptionPane.showMessageDialog(transController.getView(), "There is no previous step!", "Error", //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.ERROR_MESSAGE);
				}
				codeEditor.setLineType(LineType.NORMAL);
				updateSymbolTable();
				updateGUI();
			}

			if (e.getSource() == simulateButton) {
				if (transController.getState() != TransformationState.TS_FINISHED)
					throw new IllegalStateException(
							"Simulation is only possible in state FINISHED. Internal program error!"); //$NON-NLS-1$
				
				try {
					AM0Program am0program = transController.getAM0Program();
					transController.getController().getSimulator().setAM0Program(am0program);
				} catch (AddressException arg) {
					JOptionPane.showMessageDialog(transController.getView(), arg
						.getMessage(), Messages.getString("am0c0", "TransformationView.6"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				}
				transController.getController().showSimulator();
			}
			
			if (e.getSource() == matchButton)
			{
				if (transController.getState() != TransformationState.TS_FINISHED)
					throw new IllegalStateException(
							"Lineaisation is only possible in state FINISHED. Internal program error!");
				
				/*String sStartNumber = JOptionPane.showInputDialog("Please input first line number: ",iStartnumber);
				iStartnumber = Integer.parseInt(sStartNumber);
				
				if(iStartnumber<1)
					iStartnumber = 1;
				
				codeEditor.setFirstLine(iStartnumber);*/
				codeEditor.setLineType(LineType.MATCH);
				codeEditor.updateModel(transController.getC0TransProgram());
				updateGUI();
				
				replaceToAm0Button.setEnabled(true);
			}
			
			if (e.getSource() == replaceToAm0Button)
			{
				if (transController.getState() != TransformationState.TS_FINISHED)
					throw new IllegalStateException(
							"Lineaisation is only possible in state FINISHED. Internal program error!");
				
				try {				
					codeEditor.setLineType(LineType.NORMAL);
					codeEditor.updateModel(transController.getAM0Program());
					
				} catch (AddressException arg) {
					JOptionPane.showMessageDialog(transController.getView(), arg
						.getMessage(), Messages.getString("am0c0", "TransformationView.6"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				}
				
				updateGUI();	
				matchButton.setEnabled(false);
				replaceToAm0Button.setEnabled(false);
				
				//TODO Remove it if not needed anymore
				/*if(replaceToAm0Button.getText() == "Replace")
				{
					//This command activates the linear numbers for jalgo
					codeEditor.setFirstLine(10);
					codeEditor.setLineType(LineType.REPLACE);
					codeEditor.updateModel(transController.getC0TransProgram());
					updateGUI();
					replaceToAm0Button.setText("Undo Replace");
				}
				else
				{
					//This command deactivates the linear numbers for jalgo
					codeEditor.setLineType(LineType.NORMAL);
					codeEditor.updateModel(transController.getC0TransProgram());
					updateGUI();
					replaceToAm0Button.setText("Replace");
					
				}*/
			}
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			if (arg0.getSource() == backToEditorButton) {
				transController.getController().writeOnStatusbar(Messages.getString("am0c0", "TransformationView.40")); //$NON-NLS-1$
			}
			if (arg0.getSource() == stepBackButton) {
				transController.getController().writeOnStatusbar(Messages.getString("am0c0", "TransformationView.41")); //$NON-NLS-1$
			}
			if (arg0.getSource() == applyButton) {
				transController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "TransformationView.42")); //$NON-NLS-1$

			}
			if (arg0.getSource() == stepToEndButton) {
				transController.getController().writeOnStatusbar(Messages.getString("am0c0", "TransformationView.43")); //$NON-NLS-1$
			}
			if (arg0.getSource() == simulateButton) {
				transController.getController().writeOnStatusbar(Messages.getString("am0c0", "TransformationView.44")); //$NON-NLS-1$
			}
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			transController.getController().writeOnStatusbar(""); //$NON-NLS-1$
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}

	/**
	 * Update the whole {@link TransformationView}.
	 */
	protected void updateGUI() {
		switch (transController.getState()) {
		case TS_FINISHED:
			codeEditor.setCodeHighlightMode(false);
			setPreviewText(""); //$NON-NLS-1$
			setRuleText(""); //$NON-NLS-1$
			markingStart = -1;
			markingEnd = -1;
			applyButton.setEnabled(false);
			stepToEndButton.setEnabled(true);
			simulateButton.setEnabled(true);
			stepBackButton.setEnabled(true);
			matchButton.setEnabled(true);
			replaceToAm0Button.setEnabled(false);
			break;
		case TS_PREVIEW:
			codeEditor.setCodeHighlightMode(false);
			applyButton.setEnabled(true);
			stepToEndButton.setEnabled(true);
			simulateButton.setEnabled(false);
			stepBackButton.setEnabled(true);
			matchButton.setEnabled(false);
			replaceToAm0Button.setEnabled(false);
			break;
		case TS_WAITING:
			codeEditor.setCodeHighlightMode(true);
			setPreviewText(""); //$NON-NLS-1$
			setRuleText(""); //$NON-NLS-1$
			markingStart = -1;
			markingEnd = -1;
			applyButton.setEnabled(false);
			stepToEndButton.setEnabled(true);
			simulateButton.setEnabled(false);
			stepBackButton.setEnabled(true);
			matchButton.setEnabled(false);
			replaceToAm0Button.setEnabled(false);
			break;
		case TS_WAITING_FIRST:
			codeEditor.setCodeHighlightMode(true);
			setPreviewText(""); //$NON-NLS-1$
			setRuleText(""); //$NON-NLS-1$
			markingStart = -1;
			markingEnd = -1;
			applyButton.setEnabled(false);
			stepToEndButton.setEnabled(true);
			simulateButton.setEnabled(false);
			stepBackButton.setEnabled(false);
			matchButton.setEnabled(false);
			replaceToAm0Button.setEnabled(false);
			break;
		case TS_PREVIEW_FIRST:
			codeEditor.setCodeHighlightMode(false);
			applyButton.setEnabled(true);
			stepToEndButton.setEnabled(true);
			simulateButton.setEnabled(false);
			stepBackButton.setEnabled(false);
			matchButton.setEnabled(false);
			replaceToAm0Button.setEnabled(false);
			break;
		
		}
		codeEditor.setLineMarker(markingStart, markingEnd);
	}

	/**
	 * Apply a marked {@link Statement} using
	 * {@link TransformationView#updateSymbolTable()} and
	 * {@link TransformationView#updateGUI()}.
	 */
	protected void applyMarkedStatement() {
		if (transController.translateStatement(markingIndex.getValue())) {
			updateSymbolTable();
			updateGUI();
		} else
			applyButton.setEnabled(false);
	}

	/**
	 * Apply all {@link Statement}s in one single step.
	 */
	protected void applyAllStatements() throws IllegalStateException {
		transController.translateAll();
		updateSymbolTable();
	}

	/**
	 * Set a new preview text.
	 * 
	 * @param text
	 */
	public void setPreviewText(String text) {
		previewText.setText(text);
		previewText.setCaretPosition(0);
	}

	/**
	 * Set a new rule text.
	 * 
	 * @param text
	 */
	public void setRuleText(String text) {
		ruleText.setText(text);
		ruleText.setCaretPosition(0);
	}
}
