package MainPackageForTaskManager.Service;

import MainPackageForTaskManager.Entity.Tasks;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ITaskService {

    public List<Tasks> getTasks();
    public Tasks getTaskById(int id);
    public Tasks createTask(Tasks task);
    public void deleteTask(int id);


    @Transactional
    Tasks updateTask(Tasks task, int id) ;
}