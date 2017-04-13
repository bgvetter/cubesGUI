import com.mysql.cj.api.jdbc.*;

import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by sylentbv on 4/12/2017.
 */
public class DB {


    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";        //Configure the driver needed
    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";     //Connection string – where's the database?
    private static final String USER = "bradv";
    private static final String DB_NAME = "car";
    private static final String PASSWORD = System.getenv("MYSQLPW");
    public static final String TABLE_NAME = "cubes";
    public static final String PK_COLUMN = "id";
    public static final String NAME_COL = "itemName";
    public static final String TIME_COL = "solveTime";
    static Statement statement = null;
    static Connection conn = null;
    ResultSet resultSet;



    DB() {

        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check you have drives and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);  //No driver? Need to fix before anything else will work. So quit the program
        }

        try{
            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASSWORD);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);}
        catch (SQLException se){

        }
    }

    void createTable() {
        try{
            String createTableSQLTemplate = "CREATE TABLE IF NOT EXISTS %s (%s int NOT NULL AUTO_INCREMENT, %s VARCHAR (100), %s DOUBLE, PRIMARY KEY(%s))";
            String createTableSQL = String.format(createTableSQLTemplate, TABLE_NAME,PK_COLUMN, NAME_COL, TIME_COL,PK_COLUMN);

            statement.executeUpdate(createTableSQL);
            System.out.println("Created cubes table");


        } catch (SQLException se) {
            se.printStackTrace();
        }
    }




    void addRecord(cubeTime cube)  {

        try {

            String insertSQL = "INSERT INTO " + TABLE_NAME + " VALUES ( ? , ? ) " ;
            PreparedStatement insertSQLPS = conn.prepareStatement(insertSQL);
            insertSQLPS.setString(1, cube.itemName);
            insertSQLPS.setDouble(2, cube.solveTime);

            insertSQLPS.execute();

            System.out.println("Added record for " + cube.itemName);

            insertSQLPS.close();


        } catch (SQLException se) {
            se.printStackTrace();
        }

    }


    ResultSet fetchAllRecords() {

        try  {

            String selectAllSQL = "SELECT * FROM " + TABLE_NAME;
            ResultSet rs = statement.executeQuery(selectAllSQL);


            return rs;

        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
    }


    public void delete(cubeTime cube) {
        try{
            String deleteSQLTemplate = "DELETE FROM %s where %s = ?";
            String deleteSQL = String.format(deleteSQLTemplate,TABLE_NAME,PK_COLUMN);
            System.out.println("The SQL for the prepared statement is :" + deleteSQL);
            PreparedStatement deletePreparedStatement = conn.prepareStatement(deleteSQL);
            deletePreparedStatement.setInt(1,cube.ID);

            System.out.println(deletePreparedStatement.toString());

            deletePreparedStatement.execute();

            deletePreparedStatement.close();


        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addTestData() {

        try{

            statement.execute("INSERT INTO cubes("+NAME_COL+","+TIME_COL+") VALUES ('Cubestormer II robot', 5.270)  ");
            statement.execute("INSERT INTO cubes("+NAME_COL+","+TIME_COL+") VALUES ('Fakhri Raihaan (using his feet)', 27.93)  ");
            statement.execute("INSERT INTO cubes("+NAME_COL+","+TIME_COL+") VALUES ('Ruxin Liu (age 3)', 99.33)  ");
            statement.execute("INSERT INTO cubes("+NAME_COL+","+TIME_COL+") VALUES ('Mats Valk (human record holder)', 6.27)  ");


        }
        catch(Exception e)
        {
            System.out.println("An exception was encountered:\n"
                    + e.getMessage());
        }
    }

    //Close the ResultSet, statement and connection, in that order.
    public static void shutdown(){

        try {
            if (statement != null) {
                statement.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException se){
            //Closing the connection could throw an exception too
            se.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed");
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }


}
