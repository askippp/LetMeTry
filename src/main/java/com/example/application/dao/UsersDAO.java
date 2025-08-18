package com.example.application.dao;

import com.example.application.model.UsersModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.connection.Koneksi.getConnection;

public class UsersDAO {
    Connection conn;
    ArrayList<UsersModel> listUsers;
    UsersModel users;

    public UsersDAO() {
        conn = getConnection();
        listUsers = new ArrayList<>();
    }

    public boolean checkEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        boolean available = !rs.next();
        rs.close();
        ps.close();
        return available;
    }

    // FIXED: Tambahkan level dan point pada method login
    public UsersModel login(String email, String password) throws SQLException {
        Connection conn = getConnection();
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            UsersModel user = new UsersModel();
            user.setIdUsers(rs.getInt("id_users"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setUsername(rs.getString("username"));
            user.setRole(rs.getString("role"));
            user.setStatus(rs.getString("status"));
            user.setLevel(rs.getInt("level"));  // TAMBAHKAN INI
            user.setPoint(rs.getInt("point"));  // TAMBAHKAN INI
            user.setFoto(rs.getString("foto"));

            return user;
        }

        return null;
    }

    public void addUsers(UsersModel users) throws SQLException {
        String sqlMaxId = "SELECT MAX(id_users) FROM users";
        PreparedStatement psMaxId = conn.prepareStatement(sqlMaxId);
        ResultSet rsMaxId = psMaxId.executeQuery();

        int id = 1;
        if (rsMaxId.next()) {
            int maxId = rsMaxId.getInt(1);
            if (!rsMaxId.wasNull()) {
                id = maxId + 1;
            }
        }

        rsMaxId.close();
        psMaxId.close();

        String sql = "INSERT INTO users (id_users, email, password, username, role, status, level, point) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement psInsert = conn.prepareStatement(sql);
        psInsert.setInt(1, id);
        psInsert.setString(2, users.getEmail());
        psInsert.setString(3, users.getPassword());
        psInsert.setString(4, users.getUsername());
        psInsert.setString(5, users.getRole() != null ? users.getRole() : "user");
        psInsert.setString(6, users.getStatus() != null ? users.getStatus() : "Tidak Diterima");
        psInsert.setInt(7, users.getLevel() > 0 ? users.getLevel() : 1);
        psInsert.setInt(8, Math.max(users.getPoint(), 0));

        psInsert.executeUpdate();
        psInsert.close();

        System.out.println("User berhasil ditambahkan dengan id: " + id);
    }

    public void updateUser(int id, String email, String username, String foto) throws SQLException {
        Connection conn = getConnection();
        String sql = "UPDATE users SET email = ?, username = ?, foto = ? WHERE id_users = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, username);
            ps.setString(3, foto);
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public UsersModel getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id_users = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        UsersModel user = null;
        if (rs.next()) {
            user = new UsersModel();
            user.setIdUsers(rs.getInt("id_users"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setUsername(rs.getString("username"));
            user.setRole(rs.getString("role"));
            user.setStatus(rs.getString("status"));
            user.setLevel(rs.getInt("level"));
            user.setPoint(rs.getInt("point"));
            user.setFoto(rs.getString("foto"));
        }

        rs.close();
        ps.close();
        return user;
    }

    // New methods for user management
    public List<UsersModel> getUsersByRoleAndStatus(String role, String status) throws SQLException {
        List<UsersModel> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ? AND status = ? ORDER BY username";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setString(2, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UsersModel user = new UsersModel();
                    user.setIdUsers(rs.getInt("id_users"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setLevel(rs.getInt("level"));  // TAMBAHKAN INI
                    user.setPoint(rs.getInt("point"));  // TAMBAHKAN INI
                    user.setFoto(rs.getString("foto"));
                    users.add(user);
                }
            }
        }

        return users;
    }

    public void updateUserStatus(int userId, String status) throws SQLException {
        String sql = "UPDATE users SET status = ? WHERE id_users = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No user found with ID: " + userId);
            }

            System.out.println("User status updated successfully for ID: " + userId + " to status: " + status);
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id_users = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No user found with ID: " + userId);
            }

            System.out.println("User deleted successfully with ID: " + userId);
        }
    }
}