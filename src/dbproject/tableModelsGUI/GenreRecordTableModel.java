package dbproject.tableModelsGUI;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import javax.swing.table.AbstractTableModel;

public class GenreRecordTableModel extends AbstractTableModel {

	
	private String [] columnNames = { "Sjanger", "Selskap", "Antall filmer"};
	private ArrayList<ArrayList<String>> result;
	
	public GenreRecordTableModel(ArrayList<ArrayList<String>> genreCompanyArray) {
		result = genreCompanyArray;
	}

	@Override
	public int getRowCount() {
		return result.size();
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
		return result.get(rowIndex).get(columnIndex);
	}
	
	

}