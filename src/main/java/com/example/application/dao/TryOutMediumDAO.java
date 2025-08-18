package com.example.application.dao;

import com.example.application.model.TryOutMediumModel;
import com.vaadin.flow.component.textfield.TextField;

import java.sql.*;
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
                JOIN jawaban_tryout_medium j ON s.id_soal_to_medium = j.id_soal_to_medium 
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

    public int startTryOut(int idUser, int idMapel) throws SQLException {
        String sql = "INSERT INTO tryout_medium (id_users, id_mapel, waktu_pengerjaan, nilai, berhasil) " +
                "VALUES (?, ?, 3000, 0, 'Tidak')";

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
        throw new SQLException("Gagal memulai tryout medium");
    }

    public void saveJawaban(int idToMedium, int idSoalToMedium, int idJawabanToMedium) throws SQLException {
        String sql = "INSERT INTO detail_tryout_medium (id_to_medium, id_soal_to_medium, id_jawaban_to_medium, nilai) " +
                "VALUES (?, ?, ?, (SELECT CASE WHEN benar = 'ya' THEN 100 ELSE 0 END " +
                "FROM jawaban_tryout_medium WHERE id_jawaban_to_medium = ?))";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idToMedium);
            ps.setInt(2, idSoalToMedium);
            ps.setInt(3, idJawabanToMedium);
            ps.setInt(4, idJawabanToMedium);
            ps.executeUpdate();
        }
    }

    public boolean isJawabanBenar(int idJawabanToMedium) throws SQLException {
        String sql = "SELECT benar FROM jawaban_tryout_medium WHERE id_jawaban_to_medium = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJawabanToMedium);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "ya".equalsIgnoreCase(rs.getString("benar"));
                }
            }
        }
        return false;
    }

    public TryOutMediumModel getHasilTryOut(int idToMedium) throws SQLException {
        String sql = """
            SELECT tm.*, u.username, m.nama_mapel,
                   COUNT(DISTINCT dtm.id_detail_to_medium) as total_terjawab,
                   SUM(CASE WHEN dtm.nilai > 0 THEN 1 ELSE 0 END) as jumlah_benar,
                   SUM(CASE WHEN dtm.nilai = 0 THEN 1 ELSE 0 END) as jumlah_salah,
                   (SELECT COUNT(DISTINCT no_soal) FROM soal_tryout_medium WHERE id_mapel = tm.id_mapel) as total_soal
            FROM tryout_medium tm
            JOIN users u ON tm.id_users = u.id_users
            JOIN mapel m ON tm.id_mapel = m.id_mapel
            LEFT JOIN detail_tryout_medium dtm ON tm.id_to_medium = dtm.id_to_medium
            WHERE tm.id_to_medium = ?
            GROUP BY tm.id_to_medium, tm.id_users, tm.id_mapel, tm.tanggal, tm.waktu_pengerjaan, tm.nilai, tm.berhasil, u.username, m.nama_mapel
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idToMedium);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    TryOutMediumModel result = new TryOutMediumModel();
                    result.setIdToMedium(rs.getInt("id_to_medium"));
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

    public void finishTryOut(int idToMedium, int waktuPengerjaan) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            String sqlUpdateWaktu = "UPDATE tryout_medium SET waktu_pengerjaan = ? WHERE id_to_medium = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlUpdateWaktu)) {
                ps.setInt(1, waktuPengerjaan);
                ps.setInt(2, idToMedium);
                ps.executeUpdate();
            }

            String sqlUpdateNilai = "UPDATE tryout_medium tm " +
                    "SET nilai = (SELECT COALESCE(AVG(nilai), 0) FROM detail_tryout_medium WHERE id_to_medium = ?) " +
                    "WHERE id_to_medium = ?";

            String sqlUpdateStatus = "UPDATE tryout_medium SET berhasil = CASE WHEN nilai >= 70 THEN 'ya' ELSE 'tidak' END " +
                    "WHERE id_to_medium = ?";

            String sqlUpdatePoint = "UPDATE users u " +
                    "JOIN tryout_medium tm ON u.id_users = tm.id_users " +
                    "SET u.point = u.point + CASE " +
                    "   WHEN tm.nilai >= 70 THEN 20 " + // bisa beda point dari easy
                    "   ELSE 10 " +
                    "END " +
                    "WHERE tm.id_to_medium = ?";

            try (PreparedStatement ps = connection.prepareStatement(sqlUpdateNilai)) {
                ps.setInt(1, idToMedium);
                ps.setInt(2, idToMedium);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = connection.prepareStatement(sqlUpdateStatus)) {
                ps.setInt(1, idToMedium);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = connection.prepareStatement(sqlUpdatePoint)) {
                ps.setInt(1, idToMedium);
                ps.executeUpdate();
            }

            connection.commit();
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
            String sqlSoal = "INSERT INTO soal_tryout_medium (no_soal, id_mapel, id_materi, pertanyaan) VALUES (?, ?, ?, ?)";
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
            String sqlJawaban = "INSERT INTO jawaban_tryout_medium (id_soal_to_medium, opsi, text_jawaban, benar) VALUES (?, ?, ?, ?)";
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
            String sqlUpdateSoal = "UPDATE soal_tryout_medium SET no_soal = ?, pertanyaan = ? WHERE id_soal_to_medium = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlUpdateSoal)) {
                ps.setInt(1, noSoal);
                ps.setString(2, pertanyaan);
                ps.setInt(3, idSoalToEasy);
                ps.executeUpdate();
            }

            // Delete existing answers
            String sqlDeleteJawaban = "DELETE FROM jawaban_tryout_medium WHERE id_soal_to_medium = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlDeleteJawaban)) {
                ps.setInt(1, idSoalToEasy);
                ps.executeUpdate();
            }

            // Insert new answers
            String sqlInsertJawaban = "INSERT INTO jawaban_tryout_medium (id_soal_to_medium, opsi, text_jawaban, benar) VALUES (?, ?, ?, ?)";
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
            String sqlCheckUsage = "SELECT COUNT(*) FROM detail_tryout_medium WHERE id_soal_to_medium = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlCheckUsage)) {
                ps.setInt(1, idSoalToEasy);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Cannot delete question that has been used in tryout attempts");
                    }
                }
            }

            // Delete answers first (foreign key constraint)
            String sqlDeleteJawaban = "DELETE FROM jawaban_tryout_medium WHERE id_soal_to_medium = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlDeleteJawaban)) {
                ps.setInt(1, idSoalToEasy);
                ps.executeUpdate();
            }

            // Delete question
            String sqlDeleteSoal = "DELETE FROM soal_tryout_medium WHERE id_soal_to_medium = ?";
            try (PreparedStatement ps = connection.prepareStatement(sqlDeleteSoal)) {
                ps.setInt(1, idSoalToEasy);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isQuestionNumberUsed(int noSoal, int idMapel, Integer excludeIdSoal) throws SQLException {
        String sql = "SELECT COUNT(*) FROM soal_tryout_medium WHERE no_soal = ? AND id_mapel = ?";
        if (excludeIdSoal != null) {
            sql += " AND id_soal_to_medium != ?";
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