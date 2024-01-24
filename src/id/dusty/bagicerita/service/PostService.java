package id.dusty.bagicerita.service;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;

import id.dusty.bagicerita.entity.Post;

public class PostService {
    private static Dao<Post, Integer> postDao;

    static {
        try {
            TableUtils.createTableIfNotExists(
                DbService.getConn(),
                Post.class
            );
            postDao = DaoManager.createDao(
                DbService.getConn(),
                Post.class
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Post> getAll(){
        try {
            return postDao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public static Post get(int id){
        try {
            return postDao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean create(Post post) {
        try {
            if (postDao.create(post) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean update(Post post) {
        try {
            if (postDao.update(post) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(Post post){
        try {
            if (postDao.delete(post) > 0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
