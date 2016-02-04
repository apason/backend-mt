package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;

import org.sql2o.*;

import java.util.List;
import java.security.MessageDigest;

public class UserService {

    private final Sql2o sql2o;

    public UserService(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public User getUserById(int userId) {
        String sql
                = "SELECT * "
                + "FROM user "
                + "WHERE id = :id";

        try (Connection con = sql2o.open()) {
            List<User> users = con.createQuery(sql)
                    .addParameter("id", userId)
                    .executeAndFetch(User.class);

            if (users.isEmpty()) {
                return null;
            } else {
                return users.get(0);
            }
        }
    }

    public User authenticateUser(String email, String password) {
        String sql
                = "SELECT * "
                + "FROM user "
                + "WHERE email = :email AND password = :password";

        try (Connection con = sql2o.open()) {
            List<User> users = con.createQuery(sql)
                    .addParameter("email", email)
                    .addParameter("password", password)
                    .executeAndFetch(User.class);

            if (users.isEmpty()) {
                return null;
            } else {
                return users.get(0);
            }
        }
    }

    public User authenticateUserByHash(String userHash) {
        String sql
                = "SELECT * "
                + "FROM user "
                + "WHERE hash = :hash";

        try (Connection con = sql2o.open()) {
            List<User> users = con.createQuery(sql)
                    .addParameter("hash", userHash)
                    .executeAndFetch(User.class);

            if (users.isEmpty()) {
                return null;
            } else {
                return users.get(0);
            }
        }
    }

    public boolean createAuthHashForUser(int userId) {

        String newHash = generateRandomSHA256Hash();

        String sql
                = "UPDATE user "
                + "SET hash = :hash "
                + "WHERE id = :id";

        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("hash", newHash)
                    .addParameter("id", userId)
                    .executeUpdate();
        }

        return true;
    }

    private String generateRandomSHA256Hash() {
        // Generate a new random sha-256 hash to be stored
        // in the user database.
        java.util.Random random = new java.util.Random();
        int randomInteger = random.nextInt();

        String base = Integer.toString(randomInteger);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<User> getAllUsers(int userId) {
        return java.util.Collections.emptyList();
    }
}
