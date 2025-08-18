package com.example.application.dao;

import com.example.application.model.TryOutEasyModel;
import com.vaadin.flow.component.textfield.TextField;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                JOIN jawaban_tryout_easy j ON s.id_soal_to_easy = j.id_soal_to_easy 
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
            throw new RuntimeException(e);
        }

        return tryOutEasyModelList;
    }

    public int startTryOut(int idUser, int idMapel) throws SQLException {
        String sql = "INSERT INTO tryout_easy (id_users, id_mapel, waktu_pengerjaan, nilai, berhasil) " +
                "VALUES (?, ?, 3600, 0, 'Tidak')";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idUser);
            ps.setInt(2, idMapel);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Gagal memulai tryout");
    }

    public void saveJawaban(int idToEasy, int idSoalToEasy, int idJawabanToEasy) throws SQLException {
        String sql = "INSERT INTO detail_tryout_easy " +
                "(id_to_easy, id_soal_to_easy, id_jawaban_to_easy, nilai) " +
                "VALUES (?, ?, ?, (SELECT CASE WHEN LOWER(benar) = 'ya' THEN 100 ELSE 0 END " +
                "FROM jawaban_tryout_easy WHERE id_jawaban_to_easy = ?))";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idToEasy);
            ps.setInt(2, idSoalToEasy);
            ps.setInt(3, idJawabanToEasy);
            ps.setInt(4, idJawabanToEasy);
            ps.executeUpdate();
        }
    }

    public boolean isJawabanBenar(int idJawabanToEasy) throws SQLException {
        String sql = "SELECT benar FROM jawaban_tryout_easy WHERE id_jawaban_to_easy = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJawabanToEasy);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("benar") != null && rs.getString("benar").trim().equalsIgnoreCase("ya");
                }
            }
        }
        return false;
    }

    public TryOutEasyModel getHasilTryOut(int idToEasy) throws SQLException {
        String sql = """
            SELECT te.*, u.username, m.nama_mapel,
                   COUNT(DISTINCT dte.id_detail_to_easy) as total_terjawab,
                   SUM(CASE WHEN dte.nilai > 0 THEN 1 ELSE 0 END) as jumlah_benar,
                   SUM(CASE WHEN dte.nilai = 0 THEN 1 ELSE 0 END) as jumlah_salah,
                   (SELECT COUNT(DISTINCT no_soal) FROM soal_tryout_easy WHERE id_mapel = te.id_mapel) as total_soal
            FROM tryout_easy te
            JOIN users u ON te.id_users = u.id_users
            JOIN mapel m ON te.id_mapel = m.id_mapel
            LEFT JOIN detail_tryout_easy dte ON te.id_to_easy = dte.id_to_easy
            WHERE te.id_to_easy = ?
            GROUP BY te.id_to_easy, te.id_users, te.id_mapel, te.tanggal, te.waktu_pengerjaan, te.nilai, te.berhasil, u.username, m.nama_mapel
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idToEasy);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    TryOutEasyModel result = new TryOutEasyModel();
                    result.setIdToEasy(rs.getInt("id_to_easy"));
                    result.setIdUsers(rs.getInt("id_users"));
                    result.setIdMapel(rs.getInt("id_mapel"));
                    result.setTanggal(rs.getString("tanggal"));
                    result.setWaktuPengerjaan(rs.getString("waktu_pengerjaan"));
                    result.setNilai(rs.getInt("nilai"));
                    result.setBerhasil(rs.getString("berhasil"));
                    result.setNamaLengkap(rs.getString("username"));
                    result.setNamaMapel(rs.getString("nama_mapel"));

                    int totalSoal = rs.getInt("total_soal");
                    int totalTerjawab = rs.getInt("total_terjawab");
                    int benar = rs.getInt("jumlah_benar");
                    int salah = rs.getInt("jumlah_salah");
                    int kosong = totalSoal - totalTerjawab;

                    result.setTotalSoal(totalSoal);
                    result.setTotalTerjawab(totalTerjawab);
                    result.setJumlahBenar(benar);
                    result.setJumlahSalah(salah);
                    result.setJumlahKosong(kosong);

                    return result;
                }
            }
        }
        return null;
    }

    // Method untuk menyelesaikan tryout (submit)
    public void finishTryOut(int idToEasy, int waktuPengerjaan) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            // Update waktu pengerjaan
            String sqlUpdateWaktu = "UPDATE tryout_easy SET waktu_pengerjaan = ? WHERE id_to_easy = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlUpdateWaktu)) {
                ps.setInt(1, waktuPengerjaan);
                ps.setInt(2, idToEasy);
                ps.executeUpdate();
            }

            // Update nilai
            String sqlUpdateNilai = "UPDATE tryout_easy te " +
                    "SET nilai = (SELECT COALESCE(AVG(nilai), 0) FROM detail_tryout_easy WHERE id_to_easy = ?) " +
                    "WHERE id_to_easy = ?";

            String sqlUpdateStatus = "UPDATE tryout_easy SET berhasil = CASE WHEN nilai >= 70 THEN 'ya' ELSE 'tidak' END " +
                    "WHERE id_to_easy = ?";

            String sqlUpdatePoint = "UPDATE users u " +
                    "JOIN tryout_easy te ON u.id_users = te.id_users " +
                    "SET u.point = u.point + CASE " +
                    "   WHEN te.nilai >= 70 THEN 10 " +
                    "   ELSE 5 " +
                    "END " +
                    "WHERE te.id_to_easy = ?";

            try (PreparedStatement ps = connection.prepareStatement(sqlUpdateNilai)) {
                ps.setInt(1, idToEasy);
                ps.setInt(2, idToEasy);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = connection.prepareStatement(sqlUpdateStatus)) {
                ps.setInt(1, idToEasy);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = connection.prepareStatement(sqlUpdatePoint)) {
                ps.setInt(1, idToEasy);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addSoalWithAnswers(int noSoal, int idMapel, int idMateri, String pertanyaan,
                                   List<TextField> optionFields, String correctOption) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            // Insert question
            String sqlSoal = "INSERT INTO soal_tryout_easy (no_soal, id_mapel, id_materi, pertanyaan) VALUES (?, ?, ?, ?)";
            int idSoal;

            try (PreparedStatement ps = connection.prepareStatement(sqlSoal, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, noSoal);
                ps.setInt(2, idMapel);
                ps.setInt(3, idMateri);
                ps.setString(4, pertanyaan);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idSoal = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to get generated ID for question");
                    }
                }
            }

            // Insert answers
            String sqlJawaban = "INSERT INTO jawaban_tryout_easy (id_soal_to_easy, opsi, text_jawaban, benar) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sqlJawaban)) {
                for (int i = 0; i < optionFields.size(); i++) {
                    String option = String.valueOf((char) ('A' + i));
                    String textJawaban = optionFields.get(i).getValue();
                    String benar = option.equals(correctOption) ? "ya" : "tidak";

                    ps.setInt(1, idSoal);
                    ps.setString(2, option);
                    ps.setString(3, textJawaban);
                    ps.setString(4, benar);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSoalWithAnswers(int idSoalToEasy, int noSoal, String pertanyaan,
                                      List<TextField> optionFields, String correctOption) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            // Update question
            String sqlUpdateSoal = "UPDATE soal_tryout_easy SET no_soal = ?, pertanyaan = ? WHERE id_soal_to_easy = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlUpdateSoal)) {
                ps.setInt(1, noSoal);
                ps.setString(2, pertanyaan);
                ps.setInt(3, idSoalToEasy);
                ps.executeUpdate();
            }

            // Delete existing answers
            String sqlDeleteJawaban = "DELETE FROM jawaban_tryout_easy WHERE id_soal_to_easy = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlDeleteJawaban)) {
                ps.setInt(1, idSoalToEasy);
                ps.executeUpdate();
            }

            // Insert new answers
            String sqlInsertJawaban = "INSERT INTO jawaban_tryout_easy (id_soal_to_easy, opsi, text_jawaban, benar) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sqlInsertJawaban)) {
                for (int i = 0; i < optionFields.size(); i++) {
                    String option = String.valueOf((char) ('A' + i));
                    String textJawaban = optionFields.get(i).getValue();
                    String benar = option.equals(correctOption) ? "ya" : "tidak";

                    ps.setInt(1, idSoalToEasy);
                    ps.setString(2, option);
                    ps.setString(3, textJawaban);
                    ps.setString(4, benar);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteSoalWithAnswers(int idSoalToEasy) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            // Check if question is used in any tryout attempts
            String sqlCheckUsage = "SELECT COUNT(*) FROM detail_tryout_easy WHERE id_soal_to_easy = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlCheckUsage)) {
                ps.setInt(1, idSoalToEasy);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Cannot delete question that has been used in tryout attempts");
                    }
                }
            }

            // Delete answers first (foreign key constraint)
            String sqlDeleteJawaban = "DELETE FROM jawaban_tryout_easy WHERE id_soal_to_easy = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlDeleteJawaban)) {
                ps.setInt(1, idSoalToEasy);
                ps.executeUpdate();
            }

            // Delete question
            String sqlDeleteSoal = "DELETE FROM soal_tryout_easy WHERE id_soal_to_easy = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlDeleteSoal)) {
                ps.setInt(1, idSoalToEasy);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isQuestionNumberUsed(int noSoal, int idMapel, Integer excludeIdSoal) throws SQLException {
        String sql = "SELECT COUNT(*) FROM soal_tryout_easy WHERE no_soal = ? AND id_mapel = ?";
        if (excludeIdSoal != null) {
            sql += " AND id_soal_to_easy != ?";
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, noSoal);
            ps.setInt(2, idMapel);
            if (excludeIdSoal != null) {
                ps.setInt(3, excludeIdSoal);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}