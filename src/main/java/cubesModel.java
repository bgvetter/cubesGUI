import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sylentbv on 4/12/2017.
 */
public class cubesModel extends AbstractTableModel {

    private int rowCount = 0;
    private int colCount = 0;
    ResultSet resultSet;


    public cubesModel(ResultSet rs) {
        this.resultSet = rs;
        try{
            colCount = resultSet.getMetaData().getColumnCount();

            countRows();

        } catch (SQLException se) {
            System.out.println("Error counting columns" + se);
        }
    }

    public void updateResultSet(ResultSet newRS){
        resultSet = newRS;
        try{
            colCount = resultSet.getMetaData().getColumnCount();

            countRows();

        } catch (SQLException se) {
            System.out.println("Error counting columns" + se);
        }
    }

    //Create or recreate a ResultSet containing the whole database
    public boolean loadAll(){

        try{

            if (resultSet!=null) {
                resultSet.close();
            }

            String getAllData = "SELECT * FROM " + DB.TABLE_NAME;
            resultSet = DB.statement.executeQuery(getAllData);

            if (cubes.cubeDataModel == null) {
                //If no current movieDataModel, then make one
                cubes.cubeDataModel = new cubesModel(resultSet);
            } else {
                //Or, if one already exists, update its ResultSet
                cubes.cubeDataModel.updateResultSet(resultSet);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading movies");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }

    private void countRows() {
        rowCount = 0;
        try {
            //Move cursor to the start...
            resultSet.beforeFirst();
            // next() method moves the cursor forward one row and returns true if there is another row ahead
            while (resultSet.next()) {
                rowCount++;

            }
            resultSet.beforeFirst();

        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }

    }

    @Override
    public String getColumnName(int column) {
        return super.getColumnName(column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return super.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
    }

    @Override
    public int getRowCount() {
        countRows();
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return colCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try{
            resultSet.absolute(rowIndex+1);
            Object o = resultSet.getObject(columnIndex+1);
            return o.toString();
        }catch (SQLException se) {
            System.out.println(se);
            return se.toString();

        }
    }

    //Delete row, return true if successful, false otherwise
    public boolean deleteRow(int row){
        try {
            resultSet.absolute(row + 1);
            resultSet.deleteRow();
            //Tell table to redraw itself
            fireTableDataChanged();
            return true;
        }catch (SQLException se) {
            System.out.println("Delete row error " + se);
            return false;
        }
    }

    //returns true if successful, false if error occurs
    public boolean insertRow(String name, double time) {

        try {
            //Move to insert row, insert the appropriate data in each column, insert the row, move cursor back to where it was before we started
            resultSet.moveToInsertRow();
            resultSet.updateString(DB.NAME_COL, name);
            resultSet.updateDouble(DB.TIME_COL, time);
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
            fireTableDataChanged();
            return true;

        } catch (SQLException e) {
            System.out.println("Error adding row");
            System.out.println(e);
            return false;
        }

    }
}