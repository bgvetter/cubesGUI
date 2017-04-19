import java.sql.*;
import java.util.Scanner;

/**
 * Created by sylentbv on 4/5/2017.
 */
public class cubes {

    public static cubesModel cubeDataModel;
    private static DB db;

    public static void main(String[] args) {
        setupDatabase();
    }

    public static void setupDatabase() {

        //Create database
        db = new DB();

        //Create table, and add test data
        db.createTable();

        ResultSet cubeResultSet = null;
        cubesModel cubeModel = null;

        //check if there are any records, if not then add some default ones
        boolean bRecords = false;
        while(!bRecords) {
            try {
                //fetch all records into new recordset
                cubeResultSet = db.fetchAllRecords();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }

            //put new recordset in to jtable model object
            cubeModel = new cubesModel(cubeResultSet);

            if (cubeModel.getRowCount() == 0) {
                db.addTestData();}
            else{
                bRecords=true;
            }
        }
        //Create and show the GUI
        GUI tableGUI = new GUI(cubeModel);


    }


}
