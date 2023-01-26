package com.example.taskmanager.security;
import MainPackageForTaskManager.Entity.Users;
import MainPackageForTaskManager.Repository.UserRepository;
import MainPackageForTaskManager.security.IUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IUserDetailsServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    IUserDetailsService userDetailsService;

    private final Users user = new Users(1, "Dalal", 22, "dalal@gmail.com ", "dalal123");

    @Test
    void testLoadUserByUsername() {
        when(userRepository.findByUsername(this.user.getUsername())).thenReturn(Optional.of(this.user));
        assertEquals(userDetailsService.loadUserByUsername(user.getUsername()), this.user);
    }


    @Test
    public void testLoadUserByUserId() {
        when(userRepository.findById(1)).thenReturn(Optional.of(this.user));
        UserDetails actualUser = userDetailsService.loadUserByUserId("1");
        assertEquals(this.user, actualUser);
    }
}



