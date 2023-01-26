package MainPackageForTaskManager.security;

import MainPackageForTaskManager.Entity.Users;
import MainPackageForTaskManager.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
//@ComponentScan //no need for this annotation but i was trying to scan
//something while coding and i forgot it here
//This class is likely used to load user details from the data store when a user attempts to authenticate.
public class IUserDetailsService implements UserDetailsService { // load user details by username
    private final UserRepository userRepository;


    public IUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    // The implementation of this method first checks if the user with the specified username exists@Override
    public UserDetails loadUserByUserId(String userID) throws UsernameNotFoundException {
        int parsedId = Integer.parseInt(userID);
        if (!userRepository.findById(parsedId).isPresent()) {

            throw new UsernameNotFoundException(userID);
        }
        return userRepository.findById(parsedId).get();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!userRepository.findByUsername(username).isPresent()) {

            throw new UsernameNotFoundException("user exist");
        }
        Users user = new Users(userRepository.findByUsername(username).get());

        return user;
    }
}
