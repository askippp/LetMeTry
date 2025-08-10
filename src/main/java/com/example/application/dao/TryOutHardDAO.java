package com.example.application.dao;


import com.example.application.model.TryOutEasyModel;
import com.example.application.model.TryOutHardModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
}
