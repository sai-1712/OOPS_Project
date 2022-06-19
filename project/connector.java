package project;

import java.sql.*;

// This interface is implemented by connector and Admin classes
interface connection_details {

    String url = "jdbc:mysql://localhost:3306/project";
    String uname = "root";
    String password = "saikiran123";
}

// This class will connect to sql and execute queries.
public class connector implements connection_details {

    // This will print result of queries in table format
    public static void print_result(ResultSet rs)
    {
        try{
            ResultSetMetaData rsmd = rs.getMetaData();

            if(!rs.isBeforeFirst()){
                System.out.println("Empty set\n");
                return;
            }

            int numberOfColumns = rsmd.getColumnCount();

            // Loop to compute Dashes
            int dashes = 21*numberOfColumns + 2;
            int na = 0;
            for (int i = 1; i <= numberOfColumns; i++) {
                if(rsmd.getColumnName(i).contains("Name"))
                    na++;
            }
            dashes = dashes + na*22;

            // Loop to print dashes
            for (int i = 0; i < dashes; i++) {
                System.out.print("-");
            }
            System.out.println();

            // Loops to print Result
            for (int i = 1; i <= numberOfColumns; i++) {
                System.out.print("|  ");
                String columnName = rsmd.getColumnName(i);
                if(columnName.contains("Name"))
                    System.out.format("%40s",columnName);
                else
                System.out.format("%18s",columnName);
            }
            System.out.println(" |");

            for (int i = 0; i < dashes; i++) {
                System.out.print("-");
            }
            System.out.println();

            while (rs.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    System.out.print("|  ");
                    String columnValue = rs.getString(i);
                    String columnName = rsmd.getColumnName(i);
                    if(columnName.contains("Name"))
                        System.out.format("%40s",columnValue);
                    else
                        System.out.format("%18s",columnValue);
                }
                System.out.println(" |");
            }

            for (int i = 0; i < dashes; i++) {
                System.out.print("-");
            }
            System.out.println();
        }
        catch(Exception e)
        {
            System.err.print("SQLException: ");
            System.err.println(e.getMessage());
        }

    }

    // This will execute the ddl queries (select, search)
    public static void query_execute(String query) {

        try (Connection con = DriverManager.getConnection(url, uname, password);
             Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)){

            System.out.println("Connected to MySQL\n");
            System.out.println("Result: \n");
            ResultSet rs = st.executeQuery(query);
            print_result(rs);
        } catch (SQLException ex) {
            System.err.print("SQLException: ");
            System.err.println(ex.getMessage());
        }

    }

    // This will assign values to prepared statement and execute it.
    // This will execute DML queries (Insert, Delete, Update)
    public static int prepared_query (String s, String[] arg)
    {
        int r = 0;
        try (Connection con = DriverManager.getConnection(url, uname, password);
            PreparedStatement ps = con.prepareStatement(s)){

            System.out.println("Connected to MySQL\n");

            for (int i = 0; i < arg.length; i++) {
                ps.setString(i+1,arg[i]);
            }
            if(s.contains("UPDATE") || s.contains("DELETE") || s.contains("INSERT"))
                r = ps.executeUpdate();
            else
                ps.executeQuery();
        }
        catch (SQLException ex) {
            System.err.print("SQLException: ");
            System.err.println(ex.getMessage());
        }
        return r;
    }

    public static void query_call(String query) {

        try(Connection con = DriverManager.getConnection(url, uname, password);
            CallableStatement st = con.prepareCall(query)) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Connected to MySQL\n");
            System.out.println("Result: \n");
            ResultSet rs = st.executeQuery();
            print_result(rs);

        } catch (SQLException ex) {
            System.err.print("SQLException: ");
            System.err.println(ex.getMessage());
        }catch(ClassNotFoundException ex){
            System.err.print("ClassNotFoundException: ");
            System.err.println(ex.getMessage());
        }

    }

}