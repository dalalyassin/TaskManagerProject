package MainPackageForTaskManager.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="User")
@Data //setter and getter
@NoArgsConstructor
public class Users implements UserDetails {
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String username;
    private int age;
    @Column(unique = true)
    private String email;

    private String password;


    @OneToMany(mappedBy="user", cascade= CascadeType.REMOVE)
    @JsonIgnore //@JsonIgnore is used to ignore the logical property used in serialization and deserialization
    private List<Tasks> tasks ;

    @OneToMany(mappedBy = "user")
    private List<Tokens> Token = new ArrayList<>();


    public Users(Users userClass) {
        this.id = userClass.id;
        this.username= userClass.username;
        this.age =userClass.age;
        this.email = userClass.email;
        this.password = userClass.password; }

    public Users(int id, String username, int age, String email, String password) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {

        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addToken(Tokens tokens) {
      Token.add(tokens);
    }
}