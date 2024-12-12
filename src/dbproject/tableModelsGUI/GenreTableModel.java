
package dbproject.tableModelsGUI;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import javax.swing.table.AbstractTableModel;

public class GenreTableModel extends AbstractTableModel {

	
	private String [] columnNames = { "Sjanger"};
	private ArrayList<String> genres;
	
	public GenreTableModel(ArrayList<String> genreArray) {
		genres = genreArray;
	}
	
	public void genreTableUpdate(ArrayList<String> genreArray) {
		genres = genreArray;
	}

	@Override
	public int getRowCount() {
		return genres.size();
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
		return genres.get(rowIndex);
	}
	
	

}