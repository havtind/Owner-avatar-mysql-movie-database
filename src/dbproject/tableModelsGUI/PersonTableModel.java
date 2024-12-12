package dbproject.tableModelsGUI;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import javax.swing.table.AbstractTableModel;

public class PersonTableModel extends AbstractTableModel {
	//private static final int NAME_COL = 0;
	//private static final int BIRTH_COL = 1;
	
	private String [] columnNames = { "Funksjon", "Navn", "Rolle"};
	private ArrayList<ArrayList<String>> persons;
	
	public PersonTableModel(ArrayList<ArrayList<String>> personArray) {
		persons = personArray;
	}

	@Override
	public int getRowCount() {
		return persons.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return persons.get(rowIndex).get(columnIndex);
	}
	
	

}