package MainPackageForTaskManager.security;
import MainPackageForTaskManager.security.Filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private IUserDetailsService userDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    public SecurityConfig(IUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;

    }
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
     http.csrf().disable()
             .authorizeRequests().antMatchers("/login").permitAll()
             .and()
             .authorizeRequests().antMatchers("/register").permitAll()
             .anyRequest().authenticated()
             .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
     http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

// will be used by the authentication manager to load user details when a user attempts to authenticate.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

                auth.userDetailsService(userDetailsService);

    }
    // authenticating a user given a set of credentials
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
