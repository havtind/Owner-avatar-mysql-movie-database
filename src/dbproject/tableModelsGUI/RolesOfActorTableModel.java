package dbproject.tableModelsGUI;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import javax.swing.table.AbstractTableModel;

public class RolesOfActorTableModel extends AbstractTableModel {

	
	private String [] columnNames = { "Rolle i filmer/serier"};
	private ArrayList<String> roles;
	
	public RolesOfActorTableModel(ArrayList<String> roleArray) {
		roles = roleArray;
	}

	@Override
	public int getRowCount() {
		return roles.size();
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
		return roles.get(rowIndex);
	}
	
	

}





