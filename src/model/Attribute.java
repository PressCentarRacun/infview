package model;

import java.util.Scanner;

public class Attribute extends InfResource {
	Class<?> valueClass;
	
	public Attribute(String name, Class<?> valueClass){
		this.name = name;
		this.valueClass = valueClass;
	}
}