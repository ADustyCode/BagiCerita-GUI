package id.dusty.bagicerita.service;
import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;

import id.dusty.bagicerita.entity.User;

public class UserService {
    private static Dao<User, Integer> userDao;

    static {
        try {
            TableUtils.createTableIfNotExists(DbService.getConn(), User.class);
            userDao = DaoManager.createDao(DbService.getConn(), User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getAll() {
        try {
            return userDao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public static User get(int id) {
        try {
            return userDao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int create(User user) {
        try {
            // Cek apakah username sudah ada sebelumnya
            if (isUsernameExists(user.getUsername())) {
                // System.out.println("Username sudah digunakan.");
                return 2;
            }

            // Jika username belum ada, lakukan proses pendaftaran
            if (userDao.create(user) > 0) {
                // System.out.println("Pendaftaran berhasil.");
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean update(User user) {
        try {
            if (userDao.update(user) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(User user) {
        try {
            if (userDao.delete(user) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metode untuk mengecek apakah username sudah digunakan sebelumnya
    public static boolean isUsernameExists(String username) {
        try {
            // Cek apakah terdapat user dengan username yang sama
            List<User> usersWithSameUsername = userDao.queryBuilder()
                    .where()
                    .eq(User.USERNAME_FIELD, username)
                    .query();
            
            return !usersWithSameUsername.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int login(User loginUser) {
        try {
            // Cek apakah username ada dalam database
            User user = userDao.queryBuilder()
                    .where()
                    .eq(User.USERNAME_FIELD, loginUser.getUsername())
                    .queryForFirst();

            if (user == null) {
                // Username tidak ditemukan
                return 0;
            }
            // Validasi password
            
            if (user.getPassword().equals(loginUser.getPassword())) {
                // Password benar
                return 1;
            } else {
                // Password salah
                return 2;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 3; // Error
    }
}
