package controller;

import java.util.ArrayList;

import model.Record;

public interface UpdateBlockListener {
	void blockUpdated(ArrayList<Record> currentBlock);
}
