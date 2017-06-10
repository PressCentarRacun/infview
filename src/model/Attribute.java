package model;

import model.datatypes.CharType;
import model.datatypes.DateType;
import model.datatypes.VarCharType;

public class Attribute extends InfResource {
	Class<?> valueClass;
	int length;
	boolean primaryKey;
	boolean mandatory;
	
	public Attribute(String name, InfResource parent, Class<?> valueClass, int length, boolean primaryKey, boolean mandatory) {
		super(name, parent);
		this.name = name;
		this.valueClass = valueClass;
		this.length = length;
		this.primaryKey = primaryKey;
		this.mandatory = mandatory;
	}

	public String getFullyQualifiedName() {
		return parent.getName() + "/" + name;
	}

	public Class<?> getValueClass() {
		return valueClass;
	}

	public void setValueClass(Class<?> valueClass) {
		this.valueClass = valueClass;
	}

	@Override
	public String toIndentedString(int indentSpaces) {
		return indentStringRepresentation(
				String.format("Attribute \"%s\" {\n" + "    valueClass = \"%s\"\n" + "    length = %d\n" + "    primaryKey = %s\n" + "    mandatory = %s\n" +  "}", name, valueClass.toString(), length, primaryKey ? "yes" : "no", mandatory ? "yes" : "no"),
				indentSpaces);
	}

	public int getLength() {
		return length;
	}

	public boolean isPrimaryKey() {
		return this.primaryKey;
	}
		
	public boolean isMandatory() {
		return mandatory;
	}
	
	public static Object fromValue(Attribute a, Object value) throws InvalidLengthException {
		if (a.valueClass == VarCharType.class || a.valueClass == CharType.class) {
			String s = (String)value;
			if (a.valueClass == VarCharType.class) {
				VarCharType varchar = new VarCharType(s.length());
				varchar.set(s);
				
				return varchar;
			} else {
				CharType ch = new CharType(s.length());
				ch.set(s);
				
				return ch;
			}
		} else if (a.valueClass == Integer.class) {
			Integer i = (Integer)value;
			return i;
		} else if (a.valueClass == DateType.class) {
			java.sql.Date d;
			if (value instanceof java.sql.Timestamp) {
				java.sql.Timestamp ts = (java.sql.Timestamp)value;
				d = new java.sql.Date(ts.getYear(), ts.getMonth(), ts.getDay());
			} else {
				d = (java.sql.Date)value;
			}
			
			return new DateType(d);
		} else if (a.valueClass == Boolean.class) {
			Boolean b = (Boolean)value;		
			return b;
		} else {
			throw new IllegalArgumentException("Invalid data type");
		}
	}
	
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
}