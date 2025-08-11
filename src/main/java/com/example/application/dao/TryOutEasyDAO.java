package com.example.application.dao;

import com.example.application.model.TryOutEasyModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.connection.Koneksi.getConnection;

public class TryOutEasyDAO {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ArrayList<TryOutEasyModel> listTryOutEasy;

    public TryOutEasyDAO() {
        conn = getConnection();
        listTryOutEasy = new ArrayList<>();
    }

    public List<TryOutEasyModel> getTryOutEasyByIdMapel(int idMapel) throws SQLException {
        List<TryOutEasyModel> tryOutEasyModelList = new ArrayList<>();

        String sql = """
                SELECT s.*, j.* FROM soal_tryout_easy s 
                JOIN jawaban_tryout_easy j ON S.id_soal_to_easy = j.id_soal_to_easy 
                WHERE s.id_mapel = ? ORDER BY s.no_soal ASC, j.opsi ASC
                """;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idMapel);
            rs = ps.executeQuery();

            while (rs.next()) {
                TryOutEasyModel model = new TryOutEasyModel();
                model.setIdSoalToEasy(rs.getInt("id_soal_to_easy"));
                model.setNoSoal(rs.getInt("no_soal"));
                model.setIdMapel(rs.getInt("id_mapel"));
                model.setIdMateri(rs.getInt("id_materi"));
                model.setPertanyaan(rs.getString("pertanyaan"));
                model.setIdJawabanToEasy(rs.getInt("id_jawaban_to_easy"));
                model.setOpsi(rs.getString("opsi"));
                model.setTextJawaban(rs.getString("text_jawaban"));
                model.setBenar(rs.getString("benar"));
                tryOutEasyModelList.add(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tryOutEasyModelList;
    }
}