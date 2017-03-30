package view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import model.Entity;
import model.InfTableModel;
import model.Warehouse;
import net.miginfocom.swing.MigLayout;

public class TablePanel extends JPanel {

	// private BottomPanelController bottomPanelController;
	private JTable table;
	private InfTableModel tableModel;
	private Entity entity;

	public TablePanel(Entity entity) {
		this.entity = entity;
		this.setLayout(new MigLayout("fill", "0[]0", "0[]0"));
		initTable();
		add(new JScrollPane(table), "grow");
	}

	private void initTable() {
		tableModel = new InfTableModel(entity);
		table = new JTable(tableModel);
		//table.setPreferredScrollableViewportSize(new Dimension(900, 300));
		table.setFillsViewportHeight(true);
	}
	
	public JTable getTable() {
		return table;
	}

	public Entity getEntity() {
		return entity;
	}
}
