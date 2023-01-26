package MainPackageForTaskManager.security.Filters;
import MainPackageForTaskManager.security.IUserDetailsService;
import MainPackageForTaskManager.security.JWTSecurity.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter { //filter that ensures a single execution per request dispatch.
    @Autowired
    private IUserDetailsService userDetails;
    @Autowired
    private JwtUtil jwtUtil;
    private String authorizationHeader;

    @Override
    //The method first extracts the Authorization header from the request.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
//        String jwt = null;

// If the header exists and starts with the string "Bearer ", it extracts the JWT from the header.
        if (authorizationHeader != null /*&& authorizationHeader.startsWith("Bearer ")*/) {
//            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUserId(authorizationHeader);

        }
//if its exists it recieves the user details from user details service
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetails.loadUserByUserId(username);

            if (jwtUtil.validateToken(authorizationHeader, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response); //This filter is responsible for intercepting incoming
        // requests and checking for a valid JWT in the Authorization header
    }


}
