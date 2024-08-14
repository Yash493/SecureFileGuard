package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
   private static Connection connection = null;

   static {
      try {
         // Load the MySQL JDBC driver
         Class.forName("com.mysql.cj.jdbc.Driver");

         // Establish the connection
         connection = DriverManager.getConnection(
                 "jdbc:mysql://localhost:3306/project?useSSL=false",
                 "root",
                 "123456"
         );
      } catch (ClassNotFoundException | SQLException e) {
         e.printStackTrace();
         // Handle exceptions (e.g., log them or rethrow them as needed)
      }
   }

   public static Connection getConnection() {
      System.out.println("Connection Done!");
      return connection;
   }

  /* public static void closeConnection(){
      if (connection != null){
         try {
            connection.close();
            System.out.println("Connection Closed!");
         } catch (SQLException ex) {
            ex.printStackTrace();
         }
      }
   }*/

   public static void main(String[] args) {
      // Test the connection
      Connection conn = MyConnection.getConnection();

      // Check if the connection is not null
      if (conn != null) {
         System.out.println("Connection to the database is successful.");
      } else {
         System.out.println("Failed to connect to the database.");
      }

      // Close the connection
     // MyConnection.closeConnection();
   }
}
