package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Entity;
import model.files.File;
import model.files.InvalidRecordException;
import view.SearchDialog;
import view.TabbedTables;

public class TabbedTablesController {

	TabbedTables tt;

	public TabbedTablesController(TabbedTables tt) {
		this.tt = tt;
		tt.getTabs().addChangeListener(new TabChangeListener());
		tt.getNextBlock().addActionListener(new NextBlockClickListener());
		tt.getDoSearch().addActionListener(new SearchClickListener());
		tt.getBlockFactor().addChangeListener(new BlockFactorChangeListener());
	}

	private class BlockFactorChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			Entity entity = tt.getSelectedEntity();
			if (entity != null && entity instanceof File) {
				((File) entity).setBlockFactor((Integer) tt.getBlockFactor().getValue());
			}
		}
	}

	private class SearchClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			SearchDialog searchDialog = new SearchDialog();
			searchDialog.setModal(true);
			searchDialog.setVisible(true); // block!
		}
	}

	private class NextBlockClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Entity entity = tt.getSelectedEntity();
			if (entity != null && entity instanceof File) {
				try {
					((File) entity).fetchNextBlock();
					((File) entity).fireUpdateBlockPerformed(); // ozvezavanje
																// tabele
					tt.getBlocksFetched().setText(String.valueOf(((File) entity).getBlocksFetched()));
				} catch (IOException | InvalidRecordException ex) {
					System.out.println("Invalid blockfetch");
					ex.printStackTrace();
				}
			}
		}

	}

	private class TabChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			Entity entity = tt.getSelectedEntity();
			if (entity != null) {
				tt.enableToolbar(entity);
			} else {
				tt.disableToolbar();
			}

		}

	}

}
