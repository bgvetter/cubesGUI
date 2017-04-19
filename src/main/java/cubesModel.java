
import javax.swing.table.AbstractTableModel;
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
            //get column count
            colCount = resultSet.getMetaData().getColumnCount();
            //get row count
            countRows();

        } catch (SQLException se) {
            System.out.println("Error counting columns" + se);
        }
    }

    //update the model recordset with a new recordset
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
            resultSet = DB.fetchAllRecords();

            if (cubes.cubeDataModel == null) {
                //If no current DataModel, then make one
                cubes.cubeDataModel = new cubesModel(resultSet);
            } else {
                //Or, if one already exists, update its ResultSet
                updateResultSet(resultSet);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading data");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }

    //get row count
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

    //insert a new record into the recordset
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

    //updates an existing record in the recordset
    public boolean updateRow(int rowID, String name, double time) {

        try {
            resultSet.absolute(rowID+1);
            resultSet.updateString(DB.NAME_COL, name);
            resultSet.updateDouble(DB.TIME_COL, time);
            resultSet.updateRow();
            fireTableDataChanged();
            return true;

        } catch (Exception e) {
            System.out.println("Error updating row");
            System.out.println(e);
            return false;
        }

    }
}