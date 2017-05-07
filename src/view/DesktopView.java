/***********************************************************************
 * Module:  DesktopView.java
 * Author:  Random
 * Purpose: Defines the Class DesktopView
 ***********************************************************************/

package view;

import java.awt.Component;

import javax.swing.JPanel;

import controller.DesktopController;
import net.miginfocom.swing.MigLayout;
import model.indextree.Node;
import view.indextree.IndexTreeView;

public class DesktopView extends JPanel {
	private DesktopController desktopController;
	private TabbedTables mainTable;
	private TabbedTables detailsTable;
	
	public DesktopView() {
		this.setLayout(new MigLayout("fill", "5[]5", "5[100, grow 10]5[100, grow 10]5"));
		mainTable = new TabbedTables(true);
		detailsTable = new TabbedTables(false);
		this.add(mainTable, "grow, wrap");
	}
	
	public TabbedTables getMainTable() {
		return mainTable;
	}
	
	public TabbedTables getDetailsTable() {
		return detailsTable;
	}
	
	public void attachDetailsTable() {
		System.out.println(this.getComponentCount());
		this.add(detailsTable, "grow");
		this.repaint();
	}
	
	public void detachDetailsTable() {
		for(Component c : this.getComponents()) {
			System.out.println("Finding");
			if(c == detailsTable) {
				System.out.println("Found");
				this.remove(c);
				this.repaint();
			}
		}
	}
	
	public void attachIndexTree(Node node) {
		detachIndexTree();
		this.add(new IndexTreeView(node), "grow");
		this.repaint();
	}
	
	public void detachIndexTree() {
		System.out.println("Seeking");
		for(Component c : this.getComponents()) {
			if(c instanceof IndexTreeView) {
				this.remove(c);
				System.out.println("Destroying");
				this.repaint();
			}
		}		
	}

}