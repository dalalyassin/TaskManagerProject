package com.example.taskmanager.security.JWTSecurity;
import MainPackageForTaskManager.Entity.Users;
import MainPackageForTaskManager.Repository.TokenRepository;
import MainPackageForTaskManager.Repository.UserRepository;
import MainPackageForTaskManager.security.JWTSecurity.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {
    @Mock
    TokenRepository tokenRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    JwtUtil jwtUtil;

    private final Users user = new Users(1, "Dalal", 22, "dalal@gmail.com ", "dalal");
    @Test
    public void testGenerateToken() {
        when(userRepository.findByUsername("Dalal")).thenReturn(Optional.of(this.user));
        String token = jwtUtil.generateToken(this.user);
        assertNotNull(token);

    }

    @Test
    void testIsTokenInDB() {
        when(tokenRepository.existsById(anyString())).thenReturn(true);
        assertEquals(true, jwtUtil.isTokenInDB(anyString(), this.user));
    }

    @Test
    void testValidateToken() {
        when(userRepository.findByUsername("Dalal")).thenReturn(Optional.of(this.user));
        String token = jwtUtil.generateToken(this.user);
        when(userRepository.findById(1)).thenReturn(Optional.of(this.user));
        when(tokenRepository.existsById(token)).thenReturn(true);
        assertEquals(true, jwtUtil.validateToken(token, this.user));
    }
}


