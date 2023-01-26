package com.example.taskmanager.service;
import MainPackageForTaskManager.Authentication.Request;
import MainPackageForTaskManager.Authentication.Response;
import MainPackageForTaskManager.Entity.Tokens;
import MainPackageForTaskManager.Entity.Users;
import MainPackageForTaskManager.Exception.ExceptionNotFound;
import MainPackageForTaskManager.Exception.NotAllowedException;
import MainPackageForTaskManager.Repository.TokenRepository;
import MainPackageForTaskManager.Repository.UserRepository;
import MainPackageForTaskManager.Service.UserService;
import MainPackageForTaskManager.security.IUserDetailsService;
import MainPackageForTaskManager.security.JWTSecurity.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private IUserDetailsService userDetailsService;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
     private UserService userService;
     @Mock
     private PasswordEncoder passwordEncoder;


    private final Users user = new Users(1, "Dalal", 22, "dalal@gmail.com ", "dalal");
    private final Users user2 = new Users(2, "Dalal2", 22, "dalal2@gmail.com ", "dalal2");



    @Test
    public void testCreateUser_Pass() {
        when(userRepository.save(user)).thenReturn(this.user);
        Users result = userService.createUser(this.user);
        assertEquals(user, result);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }
    @Test
    public void testCreateUser_Fail() {
        when(userRepository.save(user)).thenThrow(new ExceptionNotFound("Error creating user"));
        assertThrows(ExceptionNotFound.class, () -> userService.createUser(user));
    }
    @Test
    public void testGetCurrentUserId_Exist() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(1);
        int userId = userService.getCurrentUserId(request);
        assertEquals(1, userId);
    }
    @Test
    public void testGetCurrentUserId_NotExist() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(null);
        assertThrows(ExceptionNotFound.class, () -> userService.getCurrentUserId(request));
    }
    @Test
    public void testGetUser_Pass() {
        List<Users> expectedUsers = Arrays.asList(this.user, this.user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);
        List<Users> actualUser = userService.getUser();
        assertEquals(expectedUsers, actualUser);
    }
    @Test
    public void testGetUser_Fail() {
        when(userRepository.findAll()).thenThrow(new NotAllowedException("Error retrieving users"));
        assertThrows(NotAllowedException.class, () -> userService.getUser());
    }

    @Test
    public void testGetById_Exist() {
        when(userRepository.findById(1)).thenReturn(Optional.of(this.user));
        Users actualUser = userService.getById(1);
        assertEquals(this.user, actualUser);
    }

    @Test
    public void testGetById_NotExist() {
        int id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ExceptionNotFound.class, () -> {
            userService.getById(1);});
    }

    @Test
    public void testUpdateUser() {
        // mock the passwordEncoder.encode() method to return an encoded password
        when(passwordEncoder.encode("dalal")).thenReturn("encodedPassword");
        when(userRepository.save(this.user)).thenReturn(this.user);
        Users updatedUser = userService.updateUser(this.user);
        assertEquals(1, updatedUser.getId());
        assertEquals("Dalal", updatedUser.getUsername());
        assertEquals("encodedPassword", updatedUser.getPassword());
        verify(userRepository, times(1)).save(this.user);
        verify(passwordEncoder, times(1)).encode("dalal");

    }
        @Test
    public void testDeleteUser_Pass() {
        when(userRepository.findById(1)).thenReturn(Optional.of(this.user));
        userService.deleteUser(1);
        verify(userRepository).deleteById(1);
    }
    @Test
    public void testDeleteUser_Fail() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(ExceptionNotFound.class, () -> userService.deleteUser(1));
    }


    @Test
    void createAuthenticationToken_Pass()  {
        Request authenticationRequest =
                new Request("dalal", "123");
        when(userDetailsService.loadUserByUsername
                (authenticationRequest.getUsername())).thenReturn(this.user);
        String token = jwtUtil.generateToken(this.user);
        when(jwtUtil.generateToken(any())).thenReturn(token);
        Tokens tokens = new Tokens();
        tokens.setUser(this.user);
        tokens.setJwtToken(token);
        when(tokenRepository.save(tokens)).thenReturn(tokens);
        this.user.addToken(tokens);
        when(userRepository.save((this.user))).thenReturn(this.user);
        Response authenticationResponse = new Response(token);
        assertEquals(authenticationResponse.getJwt(),
                userService.createAuthenticationToken(authenticationRequest).getJwt());
    }
    @Test
    void createAuthenticationToken_Fail() throws Exception {
        Request authenticationRequest =
                new Request("dalal", "123");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (authenticationRequest.getUsername(), authenticationRequest.getPassword())))
                .thenThrow(BadCredentialsException.class);
        assertThrows(BadCredentialsException.class, () -> userService.createAuthenticationToken(authenticationRequest));

    }
}


