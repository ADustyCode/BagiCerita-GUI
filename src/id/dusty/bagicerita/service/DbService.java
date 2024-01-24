package id.dusty.bagicerita.service;

import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class DbService {
    private static final String connUrl = "jdbc:sqlite:bagicerita.db";
    private static ConnectionSource conn;

    public static ConnectionSource getConn(){
        if (conn == null){
            try {
                conn = new JdbcConnectionSource(connUrl);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
