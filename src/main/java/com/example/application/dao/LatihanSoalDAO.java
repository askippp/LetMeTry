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

        public int startLatihan(int idUser, int idMateri) throws SQLException {
            String sql = "INSERT INTO latihan_soal (id_user, id_materi, nilai) VALUES (?, ?, 0)";
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idUser);
                ps.setInt(2, idMateri);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            throw new SQLException("Gagal membuat latihan baru");
        }

        public void saveJawaban(int idLatsol, int idSoalLatsol, int idJawaban) throws SQLException {
            String sql = "INSERT INTO detail_latsol (id_latsol, id_soal_latsol, id_jawaban, started_at, end_at, nilai) " +
                    "VALUES (?, ?, ?, NOW(), NOW(), (SELECT CASE WHEN benar = 'Ya' THEN 100 ELSE 0 END " +
                    "FROM jawaban_latsol WHERE id_jawaban = ?))";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idLatsol);
                ps.setInt(2, idSoalLatsol);
                ps.setInt(3, idJawaban);
                ps.setInt(4, idJawaban);
                ps.executeUpdate();
            }
        }

        public void updateNilaiLatihan(int idLatsol) throws SQLException {
            String sql = "UPDATE latihan_soal ls " +
                    "SET nilai = (SELECT AVG(nilai) FROM detail_latsol WHERE id_latsol = ?) " +
                    "WHERE id_latsol = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idLatsol);
                ps.setInt(2, idLatsol);
                ps.executeUpdate();
            }
        }

        public boolean isJawabanBenar(int idJawaban) throws SQLException {
            String sql = "SELECT benar FROM jawaban_latsol WHERE id_jawaban = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idJawaban);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return "Ya".equalsIgnoreCase(rs.getString("benar"));
                    }
                }
            }
            return false;
        }
    }