package model;

import java.util.ArrayList;
import java.util.List;

import controller.UpdateBlockListener;

public class Entity extends InfResource {

	protected ArrayList<Attribute> attributes;
	protected ArrayList<Relation> relations;
	protected ArrayList<Relation> inverseRelations;
	private ArrayList<UpdateBlockListener> updateBlockListeners;


	public Entity(String name, InfResource parent) {
		super(name, parent);

		this.name = name;
		attributes = new ArrayList<Attribute>();
		relations = new ArrayList<Relation>();
		inverseRelations = new ArrayList<Relation>();
		updateBlockListeners = new ArrayList<UpdateBlockListener>();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Entity)) {
			return false;
		}
		Entity e = (Entity) o;
		if (name.equals(e.name) && attributes.equals(e.attributes) && relations.equals(e.relations)) {
			return true;
		}
		return false;
	}

	/*
	 * // lista sluÅ¡aÄ�a koja se koristi da se osveÅ¾i prikaz tabele u klasi
	 * FileView // prilikom uÄ�itavanja novog bloka iz datoteke
	 * 
	 * EventListenerList listenerBlockList = new EventListenerList();
	 * UpdateBlockEvent updateBlockEvent = null;
	 * 
	 */
	public void addUpdateBlockListener(UpdateBlockListener l) {
		updateBlockListeners.add(l);
	}

	public void removeUpdateBlockListener(UpdateBlockListener l) {
		updateBlockListeners.remove(l);
	}

	public void fireUpdateBlockPerformed(ArrayList<Record> currentBlock) {
		for (UpdateBlockListener listener : updateBlockListeners) {
			listener.blockUpdated(currentBlock);
		}
	}
	
	@Override
	public List<? extends InfResource> getChildren() {
		// return Stream.concat(this.attributes.stream(),
		// this.relations.stream()).collect(Collectors.toList());
		return this.inverseRelations;
	}

	public ArrayList<Attribute> getAttributes() {
		if (attributes == null)
			attributes = new ArrayList<Attribute>();
		return attributes;
	}

	public void addAttribute(Attribute newAttribute) {
		if (newAttribute == null)
			return;
		if (this.attributes == null)
			this.attributes = new ArrayList<Attribute>();
		if (!this.attributes.contains(newAttribute))
			this.attributes.add(newAttribute);
	}

	public void removeAttribute(Attribute oldAttribute) {
		if (oldAttribute == null)
			return;
		if (this.attributes != null)
			if (this.attributes.contains(oldAttribute))
				this.attributes.remove(oldAttribute);
	}

	public void removeAllAttributes() {
		if (attributes != null)
			attributes.clear();
	}

	public ArrayList<Relation> getRelations() {
		if (relations == null)
			relations = new ArrayList<Relation>();
		return relations;
	}

	public void addRelation(Relation newRelation) {
		if (newRelation == null)
			return;
		if (this.relations == null)
			this.relations = new ArrayList<Relation>();
		if (!this.relations.contains(newRelation))
			this.relations.add(newRelation);
	}

	public void removeRelation(Relation oldRelation) {
		if (oldRelation == null)
			return;
		if (this.relations != null)
			if (this.relations.contains(oldRelation))
				this.relations.remove(oldRelation);
	}

	public void removeAllRelations() {
		if (relations != null)
			relations.clear();
	}

	public ArrayList<Relation> getInverseRelations() {
		if (inverseRelations == null)
			inverseRelations = new ArrayList<Relation>();
		return inverseRelations;
	}

	public void addInverseRelation(Relation newRelation) {
		if (newRelation == null)
			return;
		if (this.inverseRelations == null)
			this.inverseRelations = new ArrayList<Relation>();
		if (!this.inverseRelations.contains(newRelation))
			this.inverseRelations.add(newRelation);
	}

	public void removeInverseRelation(Relation oldRelation) {
		if (oldRelation == null)
			return;
		if (this.inverseRelations != null)
			if (this.inverseRelations.contains(oldRelation))
				this.inverseRelations.remove(oldRelation);
	}

	public void removeAllInverseRelations() {
		if (inverseRelations != null)
			inverseRelations.clear();
	}

	public Attribute findAttributeByName(String name) {
		for (Attribute a : attributes) {
			if (a.getName().equals(name)) {
				return a;
			}
		}

		return null;
	}

	public String toIndentedString(int indentSpaces) {
		ArrayList<String> relationStrings = new ArrayList<>();
		for (Relation r : relations) {
			relationStrings.add(r.toIndentedString(8));
		}
		String relationsStr = String.join("\n", relationStrings);

		ArrayList<String> attributeStrings = new ArrayList<>();
		for (Attribute a : attributes) {
			attributeStrings.add(a.toIndentedString(8));
		}
		String attributesStr = String.join("\n", attributeStrings);

		return indentStringRepresentation(String.format("Entity \"%s\" {\n" + "    attributes = [\n" + "%s\n"
				+ "    ]\n" + "    relations = [\n" + "%s\n" + "    ]\n" + "}", name, attributesStr, relationsStr),
				indentSpaces);
	}
}