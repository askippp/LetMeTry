package com.example.application.dao;

import com.example.application.model.MateriModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.connection.Koneksi.getConnection;

public class MateriDAO {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ArrayList<MateriModel> listMateri;

    public MateriDAO() {
        conn = getConnection();
        listMateri = new ArrayList<>();
    }

    public List<MateriModel> getAllMateriMtkSepuluh(int idMapel, String kelas) {
        List<MateriModel> list = new ArrayList<>();
        String sql = "SELECT * FROM materi WHERE id_mapel = ? AND kelas = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idMapel);
            ps.setString(2, kelas);
            rs = ps.executeQuery();
            while (rs.next()) {
                MateriModel m = new MateriModel();
                m.setIdMateri(rs.getInt("id_materi"));
                m.setNamaMateri(rs.getString("nama_materi"));
                m.setIdMapel(rs.getInt("id_mapel"));
                m.setKelas(rs.getString("kelas"));
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MateriModel> getAllMateriByMapel(int idMapel) {
        List<MateriModel> list = new ArrayList<>();
        String sql = "SELECT * FROM materi WHERE id_mapel = ? ORDER BY kelas, nama_materi";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idMapel);
            rs = ps.executeQuery();
            while (rs.next()) {
                MateriModel m = new MateriModel();
                m.setIdMateri(rs.getInt("id_materi"));
                m.setNamaMateri(rs.getString("nama_materi"));
                m.setIdMapel(rs.getInt("id_mapel"));
                m.setKelas(rs.getString("kelas"));
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public MateriModel getMateriById(int id) throws SQLException {
        String sql = "SELECT * FROM materi WHERE id_mapel = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                MateriModel m = new MateriModel();
                m.setIdMateri(rs.getInt("id_materi"));
                m.setNamaMateri(rs.getString("nama_materi"));
                m.setIdMapel(rs.getInt("id_mapel"));
                m.setKelas(rs.getString("kelas"));
                return m;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
