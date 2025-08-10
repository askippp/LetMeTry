package com.example.application.dao;

import com.example.application.model.LatihanSoalModel;
import com.vaadin.flow.component.textfield.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.connection.Koneksi.getConnection;

public class LatihanSoalDAO {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    ArrayList<LatihanSoalModel> listLatsol;

    public LatihanSoalDAO() {
        conn = getConnection();
        listLatsol = new ArrayList<>();
    }

    public List<LatihanSoalModel> getSoalByIdMateri(int idMateri) throws SQLException {
        List<LatihanSoalModel> latihanSoalList = new ArrayList<>();

        String sql = """
                    SELECT s.id_soal_latsol, s.no_soal, s.id_mapel, s.id_materi, s.pertanyaan,
                           j.id_jawaban, j.opsi, j.text_jawaban, j.benar
                    FROM soal_latsol s
                    JOIN jawaban_latsol j ON s.id_soal_latsol = j.id_soal_latsol
                    WHERE s.id_materi = ?
                    ORDER BY s.no_soal ASC, j.opsi ASC
                """;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idMateri);
            rs = ps.executeQuery();

            while (rs.next()) {
                LatihanSoalModel model = new LatihanSoalModel();
                model.setIdSoalLatsol(rs.getInt("id_soal_latsol"));
                model.setNoSoal(rs.getInt("no_soal"));
                model.setIdMapel(rs.getInt("id_mapel"));
                model.setIdMateri(rs.getInt("id_materi"));
                model.setPertanyaan(rs.getString("pertanyaan"));
                model.setIdJawaban(rs.getInt("id_jawaban"));
                model.setOpsi(rs.getString("opsi"));
                model.setTextJawaban(rs.getString("text_jawaban"));
                model.setBenar(rs.getString("benar"));
                latihanSoalList.add(model);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return latihanSoalList;
    }

    public List<LatihanSoalModel> getSoalByIdMateriAndBenar(int idMateri, String benar) throws SQLException {
        List<LatihanSoalModel> latihanSoalList = new ArrayList<>();

        String sql = """
                    SELECT s.id_soal_latsol, s.no_soal, s.id_mapel, s.id_materi, s.pertanyaan,
                           j.id_jawaban, j.opsi, j.text_jawaban, j.benar
                    FROM soal_latsol s
                    JOIN jawaban_latsol j ON s.id_soal_latsol = j.id_soal_latsol
                    WHERE s.id_materi = ? AND j.benar = ?
                    ORDER BY s.no_soal ASC, j.opsi ASC
                """;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idMateri);
            ps.setString(2, benar);
            rs = ps.executeQuery();

            while (rs.next()) {
                LatihanSoalModel model = new LatihanSoalModel();
                model.setIdSoalLatsol(rs.getInt("id_soal_latsol"));
                model.setNoSoal(rs.getInt("no_soal"));
                model.setIdMapel(rs.getInt("id_mapel"));
                model.setIdMateri(rs.getInt("id_materi"));
                model.setPertanyaan(rs.getString("pertanyaan"));
                model.setIdJawaban(rs.getInt("id_jawaban"));
                model.setOpsi(rs.getString("opsi"));
                model.setTextJawaban(rs.getString("text_jawaban"));
                model.setBenar(rs.getString("benar"));
                latihanSoalList.add(model);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return latihanSoalList;
    }

    public void addSoalWithAnswers(
            int noSoal, int idMapel, int idMateri, String pertanyaan,
            List<TextField> optionFields, String correctOption
    ) throws SQLException {
        conn.setAutoCommit(false);

        try {
            // Insert question
            String soalSql = "INSERT INTO soal_latsol (no_soal, id_mapel, id_materi, pertanyaan) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(soalSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, noSoal);
            ps.setInt(2, idMapel);
            ps.setInt(3, idMateri);
            ps.setString(4, pertanyaan);
            ps.executeUpdate();

            // Get generated soal ID
            rs = ps.getGeneratedKeys();
            int idSoalLatsol = -1;
            if (rs.next()) {
                idSoalLatsol = rs.getInt(1);
            }
            ps.close();
            rs.close();

            if (idSoalLatsol == -1) {
                throw new SQLException("Failed to get generated soal ID");
            }

            // Insert answers
            String jawabanSql = "INSERT INTO jawaban_latsol (id_soal_latsol, opsi, text_jawaban, benar) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(jawabanSql);

            for (int i = 0; i < optionFields.size(); i++) {
                String option = String.valueOf((char) ('A' + i));
                String text = optionFields.get(i).getValue();
                String isCorrect = option.equals(correctOption) ? "Ya" : "Tidak";

                ps.setInt(1, idSoalLatsol);
                ps.setString(2, option);
                ps.setString(3, text);
                ps.setString(4, isCorrect);
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        }
    }

    public void updateSoalWithAnswers(
            int idSoalLatsol, int noSoal, String pertanyaan,
            List<TextField> optionFields, String correctOption
    ) throws SQLException {
        conn.setAutoCommit(false);

        try {
            // Update question
            String soalSql = "UPDATE soal_latsol SET no_soal = ?, pertanyaan = ? WHERE id_soal_latsol = ?";
            ps = conn.prepareStatement(soalSql);
            ps.setInt(1, noSoal);
            ps.setString(2, pertanyaan);
            ps.setInt(3, idSoalLatsol);
            ps.executeUpdate();
            ps.close();

            // Delete existing answers
            String deleteJawabanSql = "DELETE FROM jawaban_latsol WHERE id_soal_latsol = ?";
            ps = conn.prepareStatement(deleteJawabanSql);
            ps.setInt(1, idSoalLatsol);
            ps.executeUpdate();
            ps.close();

            // Insert new answers
            String jawabanSql = "INSERT INTO jawaban_latsol (id_soal_latsol, opsi, text_jawaban, benar) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(jawabanSql);

            for (int i = 0; i < optionFields.size(); i++) {
                String option = String.valueOf((char) ('A' + i));
                String text = optionFields.get(i).getValue();
                String isCorrect = option.equals(correctOption) ? "Ya" : "Tidak";

                ps.setInt(1, idSoalLatsol);
                ps.setString(2, option);
                ps.setString(3, text);
                ps.setString(4, isCorrect);
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            if (ps != null) ps.close();
        }
    }

    public void deleteSoalWithAnswers(int idSoalLatsol) throws SQLException {
        conn.setAutoCommit(false);

        try {
            // Delete answers first (foreign key constraint)
            String deleteJawabanSql = "DELETE FROM jawaban_latsol WHERE id_soal_latsol = ?";
            ps = conn.prepareStatement(deleteJawabanSql);
            ps.setInt(1, idSoalLatsol);
            ps.executeUpdate();
            ps.close();

            // Delete question
            String deleteSoalSql = "DELETE FROM soal_latsol WHERE id_soal_latsol = ?";
            ps = conn.prepareStatement(deleteSoalSql);
            ps.setInt(1, idSoalLatsol);
            ps.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            if (ps != null) ps.close();
        }
    }

    public boolean isDuplicateNoSoal(int noSoal, int idMateri, int excludeIdSoal) throws SQLException {
        String sql = "SELECT COUNT(*) FROM soal_latsol WHERE no_soal = ? AND id_materi = ? AND id_soal_latsol != ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, noSoal);
            ps.setInt(2, idMateri);
            ps.setInt(3, excludeIdSoal);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return false;
    }

    public int getMaxNoSoalForMateri(int idMateri) throws SQLException {
        String sql = "SELECT COALESCE(MAX(no_soal), 0) FROM soal_latsol WHERE id_materi = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idMateri);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return 0;
    }

    public List<LatihanSoalModel> getSoalByNoSoal(int noSoal, int idMateri) throws SQLException {
        List<LatihanSoalModel> latihanSoalList = new ArrayList<>();

        String sql = """
                    SELECT s.id_soal_latsol, s.no_soal, s.id_mapel, s.id_materi, s.pertanyaan,
                           j.id_jawaban, j.opsi, j.text_jawaban, j.benar
                    FROM soal_latsol s
                    JOIN jawaban_latsol j ON s.id_soal_latsol = j.id_soal_latsol
                    WHERE s.no_soal = ? AND s.id_materi = ?
                    ORDER BY j.opsi ASC
                """;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, noSoal);
            ps.setInt(2, idMateri);
            rs = ps.executeQuery();

            while (rs.next()) {
                LatihanSoalModel model = new LatihanSoalModel();
                model.setIdSoalLatsol(rs.getInt("id_soal_latsol"));
                model.setNoSoal(rs.getInt("no_soal"));
                model.setIdMapel(rs.getInt("id_mapel"));
                model.setIdMateri(rs.getInt("id_materi"));
                model.setPertanyaan(rs.getString("pertanyaan"));
                model.setIdJawaban(rs.getInt("id_jawaban"));
                model.setOpsi(rs.getString("opsi"));
                model.setTextJawaban(rs.getString("text_jawaban"));
                model.setBenar(rs.getString("benar"));
                latihanSoalList.add(model);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return latihanSoalList;
    }

    public int getTotalSoalCount(int idMateri) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT id_soal_latsol) FROM soal_latsol WHERE id_materi = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idMateri);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return 0;
    }

    // Utility method to close resources
    public void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}