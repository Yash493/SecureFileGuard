package dao;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.MyConnection;
import model.Data;

public class DataDAO {
    public static List<Data> getAllFiles(String email) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from data where email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        List<Data> files = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String path = rs.getString(3);

            files.add(new Data(id, name, path));
        }
        rs.close();
        ps.close();
        connection.close();
        return files;
    }

    public static int hideFile(Data file) throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = null;
        FileReader fr = null;

        try {
            ps = connection.prepareStatement(
                    "insert into data (name, path, email, bin_data) values (?, ?, ?, ?)");

            ps.setString(1, file.getFileName());
            ps.setString(2, file.getPath());
            ps.setString(3, file.getEmail());

            File f = new File(file.getPath()); // Use the full path
            if (!f.exists()) {
                throw new FileNotFoundException("File not found: " + file.getPath());
            }

            fr = new FileReader(f);
            ps.setCharacterStream(4, fr, (int) f.length());
            int ans = ps.executeUpdate();

            fr.close(); // Close FileReader before deleting the file
            if (f.delete()) {
                System.out.println("File hidden (deleted) successfully.");
            } else {
                System.out.println("Failed to delete the file after hiding.");
            }

            return ans;
        } finally {
            if (fr != null) fr.close();
            if (ps != null) ps.close();
            connection.close();
        }
    }

    public static void unhide(int id) throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        FileWriter fw = null;

        try {
            ps = connection.prepareStatement("select path, bin_data from data where id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                String path = rs.getString("path");
                Clob c = rs.getClob("bin_data");

                Reader r = c.getCharacterStream();
                fw = new FileWriter(path);
                int i;
                while ((i = r.read()) != -1) {
                    fw.write((char) i);
                }
                fw.close();

                ps = connection.prepareStatement("delete from data where id = ?");
                ps.setInt(1, id);
                ps.executeUpdate();
                System.out.println("Successfully unhid file!");
            } else {
                System.out.println("No file found with the provided ID.");
            }
        } finally {
            if (fw != null) fw.close();
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            connection.close();
        }
    }
}
