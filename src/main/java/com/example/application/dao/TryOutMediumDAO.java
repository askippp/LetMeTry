package com.example.application.dao;


import com.example.application.model.TryOutMediumModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.connection.Koneksi.getConnection;

public class TryOutMediumDAO {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ArrayList<TryOutMediumModel> listTryOutMedium;

    public TryOutMediumDAO() {
        conn = getConnection();
        listTryOutMedium = new ArrayList<>();
    }

    public List<TryOutMediumModel> getTryOutMediumByIdMapel(int idMapel) throws SQLException {
        List<TryOutMediumModel> tryOutMediumModelList = new ArrayList<>();

        String sql = """
                SELECT s.*, j.* FROM soal_tryout_medium s 
                JOIN jawaban_tryout_medium j ON S.id_soal_to_medium = j.id_soal_to_medium 
                WHERE s.id_mapel = ? ORDER BY s.no_soal ASC, j.opsi ASC
                """;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idMapel);
            rs = ps.executeQuery();

            while (rs.next()) {
                TryOutMediumModel model = new TryOutMediumModel();
                model.setIdSoalToMedium(rs.getInt("id_soal_to_medium"));
                model.setNoSoal(rs.getInt("no_soal"));
                model.setIdMapel(rs.getInt("id_mapel"));
                model.setIdMateri(rs.getInt("id_materi"));
                model.setPertanyaan(rs.getString("pertanyaan"));
                model.setIdJawabanToMedium(rs.getInt("id_jawaban_to_medium"));
                model.setOpsi(rs.getString("opsi"));
                model.setTextJawaban(rs.getString("text_jawaban"));
                model.setBenar(rs.getString("benar"));
                tryOutMediumModelList.add(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tryOutMediumModelList;
    }
}
