package model.files;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import model.DuplicateKeyException;
import model.InfResource;
import model.Record;
import model.indextree.Tree;

import java.util.Map.Entry;
import model.Attribute;
import model.indextree.Tree;
import model.indextree.Node;
import model.indextree.KeyElement;
import model.indextree.NodeElement;
import model.indextree.InvariantViolationException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedReader;

import java.util.TreeMap;

public class SequentialFile extends File {
	public SequentialFile(String name, String path, InfResource parent) {
		super(name, path, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean addRecord(Record record) throws IOException {
		FileOutputStream stream = new FileOutputStream(getChangesFilePath(), true);
		PrintWriter pw = new PrintWriter(stream);
		pw.println(record.toString() + "A");
		pw.close();
		stream.close();
		return false;
	}

	@Override
	public boolean updateRecord(Record record) throws IOException {
		FileOutputStream stream = new FileOutputStream(getChangesFilePath(), true);
		PrintWriter pw = new PrintWriter(stream);
		pw.println(record.toString() + "U");
		pw.close();
		stream.close();
		return true;
	}

	private String getChangesFilePath() {
		return path.replace(".stxt", ".changes");
	}
	
	private void rewind() {
		filePointer = 0;
	}
	
	@Override
	public List<Record> findRecord(String[] terms, boolean all) {

		ArrayList<Record> result = new ArrayList<>();

		ArrayList<Record> currentBlock;
		try {
			currentBlock = fetchNextBlock();

			while (currentBlock != null) {
				for (Record record : currentBlock) {
					if (record.matches(terms)) {
						result.add(record);

						if (!all)
							return result;
					}

					if (record.greaterThan(terms)) {
						return result;
					}
				}
				currentBlock = fetchNextBlock();
				System.out.println(this.filePointer);
			}
		} catch (Exception e) {
			e.printStackTrace(); // lol
		}
		return result;
	}
	
	public ArrayList<Record> findRecord(String[] terms, boolean all, boolean toFile, boolean fromStart) {
		System.out.println("Pocinjem da trazim gari...");
		if (fromStart)
			this.filePointer = 0;

		ArrayList<Record> result = (ArrayList<Record>) this.findRecord(terms, all);

		if (!toFile) {
			ArrayList<Record> currentBlock = (ArrayList<Record>) result;
			System.out.println("Naso sam " + currentBlock.size() + " gari...");
		} else {
			try {
				java.io.File f = new java.io.File("search-results.txt");

				PrintWriter writer = new PrintWriter(f, "UTF-8");
				for (Record record : result) {
					writer.println(record);
				}
				writer.close();

				Desktop.getDesktop().open(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private TreeMap<Record, ChangeType> readChanges(FileInputStream changesStream) throws IOException, InvalidRecordException {
		TreeMap<Record, ChangeType> changes = new TreeMap<>();

		InputStreamReader changesReader = new InputStreamReader(changesStream);
		BufferedReader br = new BufferedReader(changesReader);
		
		String line;
		while ((line = br.readLine()) != null) {
			Record r = parseRecordLine(line);
			
			ChangeType changeType;
			char changeTypeChar = line.charAt(line.length() - 1);
			switch (changeTypeChar) {
			case 'A': changeType = ChangeType.ADD; break;
			case 'U': changeType = ChangeType.UPDATE; break;
			case 'D': changeType = ChangeType.DELETE; break;
			default: throw new InvalidRecordException("changeType", String.valueOf(changeTypeChar));
			}
			
			changes.put(r, changeType); // Overwrites the previous change
		}
		br.close();
		
		return changes;
	}
	
	private ArrayList<Record> readRecordsWithChanges(TreeMap<Record, ChangeType> changes) throws IOException, InvalidRecordException, DuplicateKeyException {
		// Brace yourselves
		ArrayList<Record> allRecords = new ArrayList<>();
		ArrayList<Record> currentBlock;
		while ((currentBlock = fetchNextBlock()) != null) {
			for (Record r : currentBlock) {
				Entry<Record, ChangeType> entry = changes.ceilingEntry(r);
				if (entry.getKey().compareTo(r) == 0) { // We have it
					switch (entry.getValue()) {
					case ADD: throw new DuplicateKeyException("Record " + entry.getKey().toString() + " is being added, but has the same primary key as " + r.toString());
					case UPDATE: allRecords.add(entry.getKey()); break;
					case DELETE: // Do nothing
					}
				} else {
					allRecords.add(r);
				}
			}
		}

		// Add the additional records
		for (Entry<Record, ChangeType> entry : changes.entrySet()) {
			if (entry.getValue() == ChangeType.ADD) {
				allRecords.add(entry.getKey());
			}
		}

		Collections.sort(allRecords);
		return allRecords;
	}
	
	private void writeToFile(FileOutputStream stream, ArrayList<Record> records) {
		PrintWriter pw = new PrintWriter(stream);
		for (Record r : records) {
			pw.println(r.toString());
		}
		
		pw.close();
	}
	
	public void merge() throws IOException, InvalidRecordException, DuplicateKeyException {
		rewind();

		FileInputStream changesStream = new FileInputStream(getChangesFilePath());
		TreeMap<Record, ChangeType> changes = readChanges(changesStream);
		changesStream.close();
		
		ArrayList<Record> allRecords = readRecordsWithChanges(changes);
		
		closeFile();
		rewind();

		FileOutputStream stream = new FileOutputStream(path);
		writeToFile(stream, allRecords);
		stream.close();
	}
	
	@Override
	public boolean deleteRecord(Record record) throws IOException {
		FileOutputStream stream = new FileOutputStream(getChangesFilePath(), true);
		PrintWriter pw = new PrintWriter(stream);
		pw.println(record.toString() + "D");
		pw.close();
		stream.close();
		return true;
	}

}
