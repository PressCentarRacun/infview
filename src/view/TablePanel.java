package view;

import model.files.UpdateBlockListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import model.Entity;
import model.InfTableModel;
import model.files.File;
import model.files.InvalidRecordException;
import net.miginfocom.swing.MigLayout;

public class TablePanel extends JPanel implements UpdateBlockListener {

	// private BottomPanelController bottomPanelController;
	private JTable table;
	private InfTableModel tableModel;
	private Entity entity;

	public TablePanel(Entity entity) {
		this.entity = entity;
		this.setLayout(new MigLayout("fill", "0[]0", "0[]0"));
		initTable();
		
		if (entity instanceof File) {
			File f = (File)entity;
			f.addUpdateBlockListener(this);
			try {
				((File) entity).fetchNextBlock();	
			} catch(Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
		} else {
			System.out.println("Not a file");
		}
		add(new JScrollPane(table), "grow");
	}

	private void initTable() {
		tableModel = new InfTableModel(entity);
		table = new JTable(tableModel);
		//table.setPreferredScrollableViewportSize(new Dimension(900, 300));
		table.setFillsViewportHeight(true);
		((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer())
	    .setHorizontalAlignment(JLabel.CENTER);
	}
	
	public void blockUpdated() {
		tableModel.fireTableDataChanged();
	}
	
	public JTable getTable() {
		return table;
	}

	public Entity getEntity() {
		return entity;
	}
}
