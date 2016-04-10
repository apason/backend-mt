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

    /*
     * All following methods are main API methods called immidiately
     * from UserResources methods of same name.
     */

    
    
    public boolean createUser(String email, String password){
	String sql
	    = "INSERT INTO user "
	    + "(email, password, enabled, create_time)"
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
     

    public int createSubUser(User u, String nick){
	
	String sql
	    = "INSERT INTO subuser "
	    + "(nick, user_id) "
	    + "VALUES "
	    + "(:nick, :puid)";

	try (Connection con = sql2o.open()) {
	    Integer newKey = con.createQuery(sql, true)
		.addParameter("nick", nick)
		.addParameter("puid", u.getId())
		.executeUpdate()
		.getKey(Integer.class);

	    return newKey;
	}	
    }
    

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
    
    public int getSubUserPrivacyLevel(int subuserId) {
        Optional<Subuser> subuser = getSubUserById(subuserId);
        
        if (!subuser.isPresent()) {
            return -1; //No subuser found.
        }
   
        Subuser subi = subuser.get();
        
        Optional<User> user = getUserById(subi.getUser_id()); //No need to check as user must exist.
        
        return user.get().getPrivacyLevel();
    }




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
	
    //rename?
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
                // Unset user password hash in object.
                users.get(0).setPassword("");
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
