package com.example.application.dao;


import com.example.application.model.TryOutHardModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.connection.Koneksi.getConnection;

public class TryOutHardDAO {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ArrayList<TryOutHardModel> listTryOutHard;

    public TryOutHardDAO() {
        conn = getConnection();
        listTryOutHard = new ArrayList<>();
    }

    public List<TryOutHardModel> getTryOutHardByIdMapel(int idMapel) throws SQLException {
        List<TryOutHardModel> tryOutHardModelList = new ArrayList<>();

        String sql = """
                SELECT s.*, j.* FROM soal_tryout_hard s 
                JOIN jawaban_to_hard j ON S.id_soal_to_hard = j.id_soal_to_hard 
                WHERE s.id_mapel = ? ORDER BY s.no_soal ASC, j.opsi ASC
                """;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idMapel);
            rs = ps.executeQuery();

            while (rs.next()) {
                TryOutHardModel model = new TryOutHardModel();
                model.setIdSoalToHard(rs.getInt("id_soal_to_hard"));
                model.setNoSoal(rs.getInt("no_soal"));
                model.setIdMapel(rs.getInt("id_mapel"));
                model.setIdMateri(rs.getInt("id_materi"));
                model.setPertanyaan(rs.getString("pertanyaan"));
                model.setIdJawabanToHard(rs.getInt("id_jawaban_to_hard"));
                model.setOpsi(rs.getString("opsi"));
                model.setTextJawaban(rs.getString("text_jawaban"));
                model.setBenar(rs.getString("benar"));
                tryOutHardModelList.add(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tryOutHardModelList;
    }
}
