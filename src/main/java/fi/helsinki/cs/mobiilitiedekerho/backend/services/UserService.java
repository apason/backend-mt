package fi.helsinki.cs.mobiilitiedekerho.backend.services;


import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Subuser;
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

    /*User-database methods start*/

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
                // Unset user password hash in object.
                users.get(0).setPassword("");
                return Optional.of(users.get(0));
            }
        }
    }  

    // Returns a list of all users in the database.
    // TODO: This in not needed, right?
    public List<User> getAllUsers() {
        String sql
                = "SELECT *"
                + "FROM user";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(User.class);
        }
    }


    // Creates a new user to the database.
    // NOTE: User enabled by default, may be wanted to be changed if email-verification, etc.
    public boolean createUser(String email, String password){
        String sql
            = "INSERT INTO user "
            + "(email, password, enabled, created)"
            + "VALUES"
            + "(:email, :password, true, NOW())";

        try (Connection con = sql2o.open()){
            Integer newKey = con.createQuery(sql)
                .addParameter("email", email)
                .addParameter("password", password)
                .executeUpdate()
                .getKey(Integer.class);
            
            if(newKey != null)
                return true;
        }
        return false;
    }

    // Checks if a user exists with the given email.
    public boolean userExists(String email){
        String sql
            = "SELECT * FROM user "
            + "WHERE email = :email";

        try (Connection con = sql2o.open()){
            List<User> user = con.createQuery(sql)
                .addParameter("email", email)
                .executeAndFetch(User.class);

            return !user.isEmpty();
        }
    }


    // Sets the user's privacy-level.
    public String setPrivacyLevel(User user, Integer privacyLevel) {
            String sql =
                "UPDATE user SET " +
                "privacy_level = :pl, " +
                "WHERE id = :id";
                
            try(Connection con = sql2o.open()){
                con.createQuery(sql)
                .addParameter("pl", privacyLevel)
                .addParameter("id", user.getId())
                .executeUpdate();

                return "Success";
            }
            catch(Exception e){
                return "DatabaseError";
            }
    }

    // Sets the user's pin.
    public String setPin(User user, String pin) {
            String sql =
                "UPDATE user SET " +
                "pin = :pin, " +
                "WHERE id = :id";
                
            try(Connection con = sql2o.open()){
                con.createQuery(sql)
                .addParameter("pin", pin)
                .addParameter("id", user.getId())
                .executeUpdate();

                return "Success";
            }
            catch(Exception e){
                return "DatabaseError";
            }
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
                if (users.get(0).isEnabled()) {
                    return Optional.of(users.get(0));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    /*User-database methods end*/


    /*SubUser-database methods start*/

    // Gets all the SubUsers of a user.
    public List<Subuser> getSubUsers(User u){
        String sql 
            = "SELECT * "
            + "FROM subuser "
            + "WHERE user_id = :uid";
        try (Connection con = sql2o.open()) {
            List<Subuser> subusers = con.createQuery(sql)
                .addParameter("uid", u.getId())
                .executeAndFetch(Subuser.class);

            return subusers;
        }
    }

    // TODO: Duplicate!
    // Describe all SubUsers of the given user.
    public List<Subuser> describeSubUsers(User user){
        List<Subuser> users;
        String sql
            = "SELECT * FROM subuser "
            + "WHERE user_id = :uid";

        try(Connection con = sql2o.open()){
            users = con.createQuery(sql)
                .addParameter("uid", user.getId())
                .executeAndFetch(Subuser.class);

            return users;
        }
    }

    // Gets the SubUser pointed by the given id.
    public Optional<Subuser> getSubUserById(int suid){
        String sql =
            "Select * " +
            "FROM subuser " +
            "WHERE id = :id";

        try(Connection con = sql2o.open()){
            List<Subuser> subusers = con.createQuery(sql)
                .throwOnMappingFailure(false)
                .addParameter("id", suid)
                .executeAndFetch(Subuser.class);
            
        if (subusers.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(subusers.get(0));
        }
        }
    }


    // Creates a new SubUser to the given user with the given nick and returns it.
    public int createSubUser(User user, String nick){
        
        String sql
            = "INSERT INTO subuser "
            + "(nick, user_id, created) "
            + "VALUES "
            + "(:nick, :puid, NOW())";

        try (Connection con = sql2o.open()) {
            Integer newKey = con.createQuery(sql, true)
                .addParameter("nick", nick)
                .addParameter("puid", user.getId())
                .executeUpdate()
                .getKey(Integer.class);

            return newKey;
        }
    }

    // Deletes the pointed SubUser by the given id.
    public void deleteSubUser(String subuserId){
        String sql
            = "DELETE FROM answer "
            + "WHERE subuser_id = :suid";

        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                .addParameter("suid", Integer.parseInt(subuserId))
                .executeUpdate();

            sql
                = "DELETE FROM subuser "
                + "WHERE id = :suid";

            con.createQuery(sql)
                .addParameter("suid", Integer.parseInt(subuserId))
                .executeUpdate();
        }
    }

    // Gets the SUbUser's privacy-level (that is, its user-master's). If the SubUser is not found it returns -1. 
    public int getSubUserPrivacyLevel(int subuserId) {
        Optional<Subuser> subuser = getSubUserById(subuserId);
        
        if (!subuser.isPresent()) {
            return -1; //No subuser found.
        }

        Subuser subi = subuser.get();
        
        Optional<User> user = getUserById(subi.getUser_id()); //No need to check as user must exist.
        
        return user.get().getPrivacyLevel();
    }


    // I think that this checks if the SubUser belongs to the given user.
    // TODO: Change methods name.
    public Subuser requireSubUser(User u, Integer subuserId){
        
        if(subuserId == null) return null;
        
        String sql
            = "SELECT * "
            + "FROM subuser "
            + "WHERE id = :id "
            + "AND user_id = :uid";
        
        try (Connection con = sql2o.open()) {
            List<Subuser> user = con.createQuery(sql)
                .addParameter("id", subuserId)
                .addParameter("uid", u.getId())
                .executeAndFetch(Subuser.class);

            if(user.isEmpty())
                return null;
            else
                return user.get(0);
            
            //catch?
        }
    }

    /*SubUser-database methods end*/



    /* All the methods below are all about token-stuff: */

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
    // If token is valid returns true.
    // Otherwise it returns false.
    // This method does not check the payload data.
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
