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

        db.addTestData();

        ResultSet cubeResultSet = null;
        cubesModel cubeModel;

        try {
            cubeResultSet = db.fetchAllRecords();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        cubeModel = new cubesModel(cubeResultSet);

        //Create and show the GUI
        GUI tableGUI = new GUI(cubeModel);


    }


}
