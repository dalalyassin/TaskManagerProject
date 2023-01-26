package MainPackageForTaskManager.security.JWTSecurity;

import MainPackageForTaskManager.Entity.Tokens;
import MainPackageForTaskManager.Entity.Users;
import MainPackageForTaskManager.Repository.TokenRepository;
import MainPackageForTaskManager.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtUtil {

    public final String SECRET_KEY = "mysecretkey"; //same as password but its more secure.. SECRET_KEY, which is used to sign and verify JWTs.

    private TokenRepository tokenRepository;
    private UserRepository userRepo;


    //used to extract the user ID from a JWT and return it as an integer. It is typically used to authenticate users and associate requests with a specific use
    public int getUserIdFromToken(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        Claims claims = Jwts.parser().setSigningKey("mysecretkey")
                .parseClaimsJws(jwt)
                .getBody();
        //getSubject method returns the value of the subject claim, which should contain the user ID. This value is stored in a string variable called userId.
        String userId = claims.getSubject();
        int userIdInt = Integer.parseInt(userId);
        return userIdInt;
    }

    public JwtUtil(TokenRepository tokenRepository, UserRepository userRepository)
    {

        this.tokenRepository = tokenRepository;
        this.userRepo = userRepository;
    }
    public void addToken(Tokens tokens)
    {
        tokenRepository.save(tokens);
    }
    public boolean isTokenInDB(String token, UserDetails userDetails){

        return tokenRepository.existsById(token);
    }
//The extractUsername method extracts the subject claim from the JWT, which is usually the username of the user.
    public String extractUserId(String token) {

        return extractClaim(token, Claims::getSubject);
    }


    //The extractExpiration method extracts the expiration claim from the JWT
    // which is the time at which the JWT will no longer be considered valid.
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //extracting a claim from the JWT. It takes in
    // a Function object that specifies how to extract the desired claim.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //method uses the Jwts parser to parse the JWT and extract all of its claims.
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    //The generateToken method creates a new JWT with a subject claim equal to the username of the user
    // It signs the JWT using the SECRET_KEY and returns it.
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Optional<Users> userDB = userRepo.findByUsername(userDetails.getUsername());
        return createToken(claims, userDB.get().getId() + "");
    }
//create jwt, The claims in a JWT are encoded as a JSON object that is digitally signed using a secret or a public/private key pair.
private String createToken(Map<String, Object> claims, String subject) {

    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
}

    //The validateToken method verifies that the JWT is not expired,
    //has a subject claim equal to the username of the user, and is present in the database.
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userID = extractUserId(token);
        Optional<Users> dbUser = userRepo.findById(Integer.parseInt(userID));
        return (dbUser.get().getUsername().equals(userDetails.getUsername()) && !isTokenExpired(token) && isTokenInDB(token,userDetails));

    }

    public static void main(String[] args) {
        System.out.println(Jwts.parser().setSigningKey("mysecretkey")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5YXphbiIsImV4cCI6MTY3MzM3NzE1MCwiaWF0IjoxNjczMzQxMTUwfQ.J_tRIWefZBPduC3LoinjblbKf_-UKaoHTwyOV_IaOQs")
                .getBody());
    }
}





