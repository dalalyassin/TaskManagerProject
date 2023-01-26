package MainPackageForTaskManager.Service;

import MainPackageForTaskManager.Entity.Users;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IUserService {
    public List<Users> getUser();
    public void deleteUser(int id);
    public Users getById(int id);
    public Users createUser(Users User);
    int getCurrentUserId(HttpServletRequest request);
    public Users updateUser(Users User);


    }
