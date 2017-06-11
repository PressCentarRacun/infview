package view.search;

import java.util.HashMap;

import javax.swing.JComboBox;

public class OrderComboBox extends JComboBox<String> implements ExpressionComponent {

	HashMap<String, String> mapa = new HashMap<>();

	public OrderComboBox() {
		super();
		mapa.put("rastuce", "ASC ");
		mapa.put("opadajuce", "DESC ");
		
		for(String s : mapa.keySet()) {
			this.addItem(s);
		}
	}

	@Override
	public String getExpression() {
		return mapa.get(this.getSelectedItem());
	}
}
