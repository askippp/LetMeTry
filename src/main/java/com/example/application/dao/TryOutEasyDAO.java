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

    public List<TryOutEasyModel> getSoalByMapel(int idMapel) throws SQLException {
        List<TryOutEasyModel> soalList = new ArrayList<>();
        String sql = """
                SELECT s.id_soal_to_easy, s.no_soal, s.id_mapel, s.id_materi, s.pertanyaan,
                       j.id_jawaban_to_easy, j.opsi, j.text_jawaban, j.benar
                FROM soal_tryout_easy s
                JOIN jawaban_tryout_easy j ON s.id_soal_to_easy = j.id_soal_to_easy
                WHERE s.id_mapel = ? AND j.benar = 'ya'
                ORDER BY s.no_soal ASC
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
                soalList.add(model);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return soalList;
    }

    public int getQuestionCountByMapel(int idMapel) throws SQLException {
        String sql = "SELECT COUNT(*) FROM soal_tryout_easy WHERE id_mapel = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idMapel);
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

    public List<String> getAllJawabanForEdit(int idSoal) throws SQLException {
        List<String> jawabanList = new ArrayList<>();
        String sql = "SELECT text_jawaban FROM jawaban_tryout_easy WHERE id_soal_to_easy = ? ORDER BY opsi ASC";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSoal);
            rs = ps.executeQuery();

            while (rs.next()) {
                jawabanList.add(rs.getString("text_jawaban"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return jawabanList;
    }

    public void insertMultipleSoal(List<TryOutEasyModel> soalList) throws SQLException {
        conn.setAutoCommit(false);
        try {
            // Insert questions
            String soalSql = "INSERT INTO soal_tryout_easy (no_soal, id_mapel, id_materi, pertanyaan) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(soalSql, Statement.RETURN_GENERATED_KEYS);

            for (TryOutEasyModel soal : soalList) {
                ps.setInt(1, soal.getNoSoal());
                ps.setInt(2, soal.getIdMapel());
                ps.setInt(3, soal.getIdMateri());
                ps.setString(4, soal.getPertanyaan());
                ps.addBatch();
            }

            ps.executeBatch();
            rs = ps.getGeneratedKeys();

            List<Integer> generatedIds = new ArrayList<>();
            while (rs.next()) {
                generatedIds.add(rs.getInt(1));
            }

            ps.close();
            rs.close();

            // Insert answers
            String jawabanSql = "INSERT INTO jawaban_tryout_easy (id_soal_to_easy, opsi, text_jawaban, benar) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(jawabanSql);

            for (int i = 0; i < soalList.size(); i++) {
                TryOutEasyModel soal = soalList.get(i);
                int idSoal = generatedIds.get(i);

                // Insert all options (A-E)
                for (char opsi = 'A'; opsi <= 'E'; opsi++) {
                    String textJawaban = "";
                    String benar = "tidak";

                    if (opsi == soal.getOpsi().charAt(0)) {
                        textJawaban = soal.getTextJawaban();
                        benar = "ya";
                    }

                    ps.setInt(1, idSoal);
                    ps.setString(2, String.valueOf(opsi));
                    ps.setString(3, textJawaban);
                    ps.setString(4, benar);
                    ps.addBatch();
                }
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

    public void updateSoalAndJawaban(TryOutEasyModel soal, List<String> jawabanUpdate) throws SQLException {
        conn.setAutoCommit(false);
        try {
            // Update question
            String soalSql = "UPDATE soal_tryout_easy SET no_soal = ?, id_materi = ?, pertanyaan = ? WHERE id_soal_to_easy = ?";
            ps = conn.prepareStatement(soalSql);
            ps.setInt(1, soal.getNoSoal());
            ps.setInt(2, soal.getIdMateri());
            ps.setString(3, soal.getPertanyaan());
            ps.setInt(4, soal.getIdSoalToEasy());
            ps.executeUpdate();
            ps.close();

            // Delete existing answers
            String deleteJawabanSql = "DELETE FROM jawaban_tryout_easy WHERE id_soal_to_easy = ?";
            ps = conn.prepareStatement(deleteJawabanSql);
            ps.setInt(1, soal.getIdSoalToEasy());
            ps.executeUpdate();
            ps.close();

            // Insert new answers
            String jawabanSql = "INSERT INTO jawaban_tryout_easy (id_soal_to_easy, opsi, text_jawaban, benar) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(jawabanSql);

            for (int i = 0; i < jawabanUpdate.size(); i++) {
                char opsi = (char) ('A' + i);
                String textJawaban = jawabanUpdate.get(i);
                String benar = (opsi == soal.getOpsi().charAt(0)) ? "ya" : "tidak";

                ps.setInt(1, soal.getIdSoalToEasy());
                ps.setString(2, String.valueOf(opsi));
                ps.setString(3, textJawaban);
                ps.setString(4, benar);
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

    public void deleteSoal(int idSoal) throws SQLException {
        conn.setAutoCommit(false);
        try {
            // Delete answers first
            String deleteJawabanSql = "DELETE FROM jawaban_tryout_easy WHERE id_soal_to_easy = ?";
            ps = conn.prepareStatement(deleteJawabanSql);
            ps.setInt(1, idSoal);
            ps.executeUpdate();
            ps.close();

            // Delete question
            String deleteSoalSql = "DELETE FROM soal_tryout_easy WHERE id_soal_to_easy = ?";
            ps = conn.prepareStatement(deleteSoalSql);
            ps.setInt(1, idSoal);
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
}