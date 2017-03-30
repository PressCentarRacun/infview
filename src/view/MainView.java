
package view;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import constants.Constants;
import controller.MainController;
import model.Entity;
import net.miginfocom.swing.MigLayout;
import view.tree.TreeView;

import java.awt.BorderLayout;
import java.util.*;

public class MainView extends JFrame{
	
   private static MainView instance;
   private TreeView treeView;
   private ToolBarView toolBarView;
   private MenuBarView menuBarView;
   private DesktopView desktopView;
   private MainController controller;

	public static MainView getInstance() {
		if (instance == null) {
			instance = new MainView();
			instance.initialize();
		}
		return instance;
	}
	
	private void initialize() {
		setLookAndFeel();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("InfView");
		this.setSize(Constants.WINDOW_SIZE);
		this.setLocationRelativeTo(null);
		this.setLayout(new MigLayout("fill", "0[100, grow 30]5[700, grow 70]0", "0[grow 10]0[grow 90]0"));
		// Adds the menu bar.
		this.menuBarView = new MenuBarView();
		//this.setJMenuBar(menuBarView);

		// Adds the tool bar.
		this.toolBarView = new ToolBarView();
		this.add(this.toolBarView, "wrap, span 2 1, grow");

		// Adds the tree view.
		this.treeView = new TreeView();
		this.add(this.treeView, "grow");

		// Adds the desktop view.
		this.desktopView = new DesktopView();
		this.add(this.desktopView, "grow");

		// Attaches the listeners.
		this.controller = new MainController(this);
	}
	
	private void setLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					SwingUtilities.updateComponentTreeUI(this);
					pack();
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to
			// another look and feel.
		}
	}
	
	public void doTableOpen(Entity entity) {
		desktopView.getTopPanel().addTab(entity);
		desktopView.getBottomPanel().addTab(entity);
		System.out.println("otvaram " + entity.getName() + "...");
	}
}