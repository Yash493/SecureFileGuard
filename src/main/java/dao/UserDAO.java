package dao;

import db.MyConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    static Connection connection = MyConnection.getConnection();
    public static boolean isExists(String email) throws SQLException {
        String query = "SELECT email FROM users WHERE email = ?";
        try (
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // returns true if a record exists
            }
        }
    }

    public static int saveUser(User user) throws SQLException {
        // Correcting the SQL syntax by completing the query
        String query = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (
             PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // return the generated ID
                    }
                }
            }
            return -1; // indicates that the insertion failed
        }
    }
}
