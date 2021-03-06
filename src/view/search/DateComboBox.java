package view.search;

import java.util.HashMap;

import javax.swing.JComboBox;

class DateComboBox extends JComboBox<String> implements ExpressionComponent{
	
	HashMap<String, String> mapa = new HashMap<>();
	
	public DateComboBox() {
		super();
		mapa.put("na", "= ? ");
		mapa.put("pre", "< ? ");
		mapa.put("posle", "> ? ");
		
		for(String s : mapa.keySet()) {
			this.addItem(s);
		}
	}

	@Override
	public String getExpression() {
		return this.mapa.get(this.getSelectedItem());
	}
}