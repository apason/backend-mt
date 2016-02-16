package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.JwtException;
import java.security.Key;

import org.sql2o.*;

import java.util.List;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Optional;

public class UserService {

    private final Sql2o sql2o;
    private final Key secretKey;

    public UserService(Sql2o sql2o, Key key) {
        this.sql2o = sql2o;
        this.secretKey = key;
    }    

    public Key getSecretKey() {
        return secretKey;
    }

    // Authenticates the user with email and password.
    // If authentication is successful, returns an Optional<User> with the user object.
    // Otherwise returns an empty Optional<User>.
    
    public Optional<User> authenticateUser(String email, String password) {
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
                return Optional.empty();
            } else {
                return Optional.of(users.get(0));
            }
        }
    }
    
    // Generates a JSON Web Token for an anonymous user.
    // Returns the token.
    public String generateAnonymousToken(String client_ip) {

        String token = Jwts.builder()
                .setIssuedAt(new Date())
                .claim("user_type", "anonymous")
                .claim("client_ip", client_ip)
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();

        return token;
    }

    // Generates a JSON Web Token for an authenticated user.
    // Stores information about the user in the data section of the token.
    // Returns the generated token.
    public String generateAuthenticatedToken(User u, String client_ip) {
        String token = Jwts.builder()
                .setIssuedAt(new Date())
                .claim("user_type", "authenticated")
                .claim("user_id", u.getId())
                .claim("client_ip", client_ip)
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();

        return token;
    }

    // Validates a JSON Web Token.
    // Checks the signature.
    // If token validates, returns true.
    // Otherwise, returns false.
    // This method does not check the payload data.
    boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Returns an user from the database.
    // If the user is found, returns Optional<User> with the user object.
    // Otherwise returns an empty Optional<User>.
    public Optional<User> getUserById(int userId) {
        String sql
                = "SELECT * "
                + "FROM user "
                + "WHERE id = :id";

        try (Connection con = sql2o.open()) {
            List<User> users = con.createQuery(sql)
                    .addParameter("id", userId)
                    .executeAndFetch(User.class);

            if (users.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(users.get(0));
            }
        }
    }  
    
    // Returns a list of all users in the database.
    public List<User> getAllUsers() {
        String sql
                = "SELECT *"
                + "FROM user";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(User.class);
        }
    }    
}
