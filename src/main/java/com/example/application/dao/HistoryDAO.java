package com.example.application.dao;

import com.example.application.model.HistoryModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.connection.Koneksi.getConnection;

public class HistoryDAO {
    Connection conn;

    public HistoryDAO() {
        conn = getConnection();
    }

    public List<HistoryModel> getHistoryByUserAndMapel(int idUsers, int idMapel) throws SQLException {
        List<HistoryModel> historyList = new ArrayList<>();

        String sql = """
            SELECT 
                'Easy' as level,
                te.id_to_easy as id_tryout,
                te.tanggal,
                te.waktu_pengerjaan,
                te.nilai,
                te.berhasil,
                m.nama_mapel,
                (SELECT COUNT(DISTINCT dte.id_detail_to_easy) FROM detail_tryout_easy dte WHERE dte.id_to_easy = te.id_to_easy) as total_terjawab,
                (SELECT SUM(CASE WHEN dte.nilai > 0 THEN 1 ELSE 0 END) FROM detail_tryout_easy dte WHERE dte.id_to_easy = te.id_to_easy) as jumlah_benar,
                (SELECT SUM(CASE WHEN dte.nilai = 0 THEN 1 ELSE 0 END) FROM detail_tryout_easy dte WHERE dte.id_to_easy = te.id_to_easy) as jumlah_salah,
                (SELECT COUNT(DISTINCT no_soal) FROM soal_tryout_easy WHERE id_mapel = te.id_mapel) as total_soal
            FROM tryout_easy te
            JOIN mapel m ON te.id_mapel = m.id_mapel
            WHERE te.id_users = ? AND te.id_mapel = ?
            
            UNION ALL
            
            SELECT 
                'Medium' as level,
                tm.id_to_medium as id_tryout,
                tm.tanggal,
                tm.waktu_pengerjaan,
                tm.nilai,
                tm.berhasil,
                m.nama_mapel,
                (SELECT COUNT(DISTINCT dtm.id_detail_to_medium) FROM detail_tryout_medium dtm WHERE dtm.id_to_medium = tm.id_to_medium) as total_terjawab,
                (SELECT SUM(CASE WHEN dtm.nilai > 0 THEN 1 ELSE 0 END) FROM detail_tryout_medium dtm WHERE dtm.id_to_medium = tm.id_to_medium) as jumlah_benar,
                (SELECT SUM(CASE WHEN dtm.nilai = 0 THEN 1 ELSE 0 END) FROM detail_tryout_medium dtm WHERE dtm.id_to_medium = tm.id_to_medium) as jumlah_salah,
                (SELECT COUNT(DISTINCT no_soal) FROM soal_tryout_medium WHERE id_mapel = tm.id_mapel) as total_soal
            FROM tryout_medium tm
            JOIN mapel m ON tm.id_mapel = m.id_mapel
            WHERE tm.id_users = ? AND tm.id_mapel = ?
            
            UNION ALL
            
            SELECT 
                'Hard' as level,
                th.id_to_hard as id_tryout,
                th.tanggal,
                th.waktu_pengerjaan,
                th.nilai,
                th.berhasil,
                m.nama_mapel,
                (SELECT COUNT(DISTINCT dth.id_detail_to_hard) FROM detail_tryout_hard dth WHERE dth.id_to_hard = th.id_to_hard) as total_terjawab,
                (SELECT SUM(CASE WHEN dth.nilai > 0 THEN 1 ELSE 0 END) FROM detail_tryout_hard dth WHERE dth.id_to_hard = th.id_to_hard) as jumlah_benar,
                (SELECT SUM(CASE WHEN dth.nilai = 0 THEN 1 ELSE 0 END) FROM detail_tryout_hard dth WHERE dth.id_to_hard = th.id_to_hard) as jumlah_salah,
                (SELECT COUNT(DISTINCT no_soal) FROM soal_tryout_hard WHERE id_mapel = th.id_mapel) as total_soal
            FROM tryout_hard th
            JOIN mapel m ON th.id_mapel = m.id_mapel
            WHERE th.id_users = ? AND th.id_mapel = ?
            
            ORDER BY tanggal DESC
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsers);
            ps.setInt(2, idMapel);
            ps.setInt(3, idUsers);
            ps.setInt(4, idMapel);
            ps.setInt(5, idUsers);
            ps.setInt(6, idMapel);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HistoryModel history = new HistoryModel();
                    history.setLevel(rs.getString("level"));
                    history.setIdTryout(rs.getInt("id_tryout"));
                    history.setTanggal(rs.getString("tanggal"));
                    history.setWaktuPengerjaan(rs.getString("waktu_pengerjaan"));
                    history.setNilai(rs.getInt("nilai"));
                    history.setBerhasil(rs.getString("berhasil"));
                    history.setNamaMapel(rs.getString("nama_mapel"));
                    history.setTotalTerjawab(rs.getInt("total_terjawab"));
                    history.setJumlahBenar(rs.getInt("jumlah_benar"));
                    history.setJumlahSalah(rs.getInt("jumlah_salah"));
                    history.setTotalSoal(rs.getInt("total_soal"));

                    // Calculate percentage
                    int totalSoal = rs.getInt("total_soal");
                    int jumlahBenar = rs.getInt("jumlah_benar");
                    int percentage = totalSoal > 0 ? (jumlahBenar * 100) / totalSoal : 0;
                    history.setPersentase(percentage);

                    historyList.add(history);
                }
            }
        }

        return historyList;
    }

    public HistoryModel getDetailHistory(String level, int idTryout) throws SQLException {
        String sql = "";

        switch (level.toLowerCase()) {
            case "easy":
                sql = """
                    SELECT 
                        'Easy' as level,
                        te.id_to_easy as id_tryout,
                        te.tanggal,
                        te.waktu_pengerjaan,
                        te.nilai,
                        te.berhasil,
                        m.nama_mapel,
                        (SELECT COUNT(DISTINCT dte.id_detail_to_easy) FROM detail_tryout_easy dte WHERE dte.id_to_easy = te.id_to_easy) as total_terjawab,
                        (SELECT SUM(CASE WHEN dte.nilai > 0 THEN 1 ELSE 0 END) FROM detail_tryout_easy dte WHERE dte.id_to_easy = te.id_to_easy) as jumlah_benar,
                        (SELECT SUM(CASE WHEN dte.nilai = 0 THEN 1 ELSE 0 END) FROM detail_tryout_easy dte WHERE dte.id_to_easy = te.id_to_easy) as jumlah_salah,
                        (SELECT COUNT(DISTINCT no_soal) FROM soal_tryout_easy WHERE id_mapel = te.id_mapel) as total_soal
                    FROM tryout_easy te
                    JOIN mapel m ON te.id_mapel = m.id_mapel
                    WHERE te.id_to_easy = ?
                    """;
                break;
            case "medium":
                sql = """
                    SELECT 
                        'Medium' as level,
                        tm.id_to_medium as id_tryout,
                        tm.tanggal,
                        tm.waktu_pengerjaan,
                        tm.nilai,
                        tm.berhasil,
                        m.nama_mapel,
                        (SELECT COUNT(DISTINCT dtm.id_detail_to_medium) FROM detail_tryout_medium dtm WHERE dtm.id_to_medium = tm.id_to_medium) as total_terjawab,
                        (SELECT SUM(CASE WHEN dtm.nilai > 0 THEN 1 ELSE 0 END) FROM detail_tryout_medium dtm WHERE dtm.id_to_medium = tm.id_to_medium) as jumlah_benar,
                        (SELECT SUM(CASE WHEN dtm.nilai = 0 THEN 1 ELSE 0 END) FROM detail_tryout_medium dtm WHERE dtm.id_to_medium = tm.id_to_medium) as jumlah_salah,
                        (SELECT COUNT(DISTINCT no_soal) FROM soal_tryout_medium WHERE id_mapel = tm.id_mapel) as total_soal
                    FROM tryout_medium tm
                    JOIN mapel m ON tm.id_mapel = m.id_mapel
                    WHERE tm.id_to_medium = ?
                    """;
                break;
            case "hard":
                sql = """
                    SELECT 
                        'Hard' as level,
                        th.id_to_hard as id_tryout,
                        th.tanggal,
                        th.waktu_pengerjaan,
                        th.nilai,
                        th.berhasil,
                        m.nama_mapel,
                        (SELECT COUNT(DISTINCT dth.id_detail_to_hard) FROM detail_tryout_hard dth WHERE dth.id_to_hard = th.id_to_hard) as total_terjawab,
                        (SELECT SUM(CASE WHEN dth.nilai > 0 THEN 1 ELSE 0 END) FROM detail_tryout_hard dth WHERE dth.id_to_hard = th.id_to_hard) as jumlah_benar,
                        (SELECT SUM(CASE WHEN dth.nilai = 0 THEN 1 ELSE 0 END) FROM detail_tryout_hard dth WHERE dth.id_to_hard = th.id_to_hard) as jumlah_salah,
                        (SELECT COUNT(DISTINCT no_soal) FROM soal_tryout_hard WHERE id_mapel = th.id_mapel) as total_soal
                    FROM tryout_hard th
                    JOIN mapel m ON th.id_mapel = m.id_mapel
                    WHERE th.id_to_hard = ?
                    """;
                break;
            default:
                throw new SQLException("Invalid level: " + level);
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTryout);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HistoryModel history = new HistoryModel();
                    history.setLevel(rs.getString("level"));
                    history.setIdTryout(rs.getInt("id_tryout"));
                    history.setTanggal(rs.getString("tanggal"));
                    history.setWaktuPengerjaan(rs.getString("waktu_pengerjaan"));
                    history.setNilai(rs.getInt("nilai"));
                    history.setBerhasil(rs.getString("berhasil"));
                    history.setNamaMapel(rs.getString("nama_mapel"));
                    history.setTotalTerjawab(rs.getInt("total_terjawab"));
                    history.setJumlahBenar(rs.getInt("jumlah_benar"));
                    history.setJumlahSalah(rs.getInt("jumlah_salah"));
                    history.setTotalSoal(rs.getInt("total_soal"));

                    // Calculate percentage
                    int totalSoal = rs.getInt("total_soal");
                    int jumlahBenar = rs.getInt("jumlah_benar");
                    int percentage = totalSoal > 0 ? (jumlahBenar * 100) / totalSoal : 0;
                    history.setPersentase(percentage);

                    return history;
                }
            }
        }

        return null;
    }
}