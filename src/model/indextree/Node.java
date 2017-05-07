package model.indextree;
import java.util.ArrayList;

public class Node {
	private ArrayList<Node> children;
	private ArrayList<NodeElement> elements;
	
	public Node(ArrayList<NodeElement> elements) {
		this.elements = elements;
	}
	
	public void addChild(Node child) throws InvariantViolationException {
		if (children.size() > 1) {
			throw new InvariantViolationException("Cannot have more than 2 children");
		}
		children.add(child);
	}
	
	public void removeChild(Node child) {
		children.remove(child);
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public ArrayList<NodeElement> getElements() {
		return elements;
	}
}
