package com.example.application.dao;


import com.example.application.model.TryOutMediumModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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


}
