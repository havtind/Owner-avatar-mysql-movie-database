package dbproject.tableModelsGUI;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import javax.swing.table.AbstractTableModel;

public class FilmsOfActorTableModel extends AbstractTableModel {

	
	private String [] columnNames = { "Filmer"};
	private ArrayList<String> filmer;
	
	public FilmsOfActorTableModel(ArrayList<String> filmArray) {
		filmer = filmArray;
	}

	@Override
	public int getRowCount() {
		return filmer.size();
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
		return filmer.get(rowIndex);
	}
	
	

}






