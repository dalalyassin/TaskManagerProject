package MainPackageForTaskManager.Entity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletRequest;

@Entity
@RequiredArgsConstructor
@EqualsAndHashCode

     //should be persisted to a database
    public class Tokens {

    //jwtToken: This field is used to store the JWT. It
    //is annotated with @Id, which indicates that it is the primary key of the entity.
    @Id
    private String jwtToken;

    @ManyToOne
    private Users user;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Tokens{" +"username"+user.getUsername()+'\''+
                "jwtToken='" + jwtToken + '\'' +
                '}';
    }
}
/*storing the JWTs of
authenticated users, with a reference to the
user that the JWT belongs to. This allows you to retrieve the user
associated with a JWT and perform actions based on the user's identity.*/