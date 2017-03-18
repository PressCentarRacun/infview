package model;

import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;

public class Attribute extends InfResource {
	Class<?> valueClass;
	
	public Attribute(String name, Class<?> valueClass){
		this.name = name;
		this.valueClass = valueClass;
	}
	

	public Class<?> getValueClass() {
		return valueClass;
	}

	public void setValueClass(Class<?> valueClass) {
		this.valueClass = valueClass;
	}
	
	@Override
	public String toIndentedString(int indentSpaces) {
		return indentStringRepresentation(String.format(
			"Attribute \"%s\" {\n" +
			"    valueClass = \"%s\"\n" +
			"}", name, valueClass.toString()), indentSpaces);
	}
}