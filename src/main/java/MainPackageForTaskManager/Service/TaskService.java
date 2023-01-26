package MainPackageForTaskManager.Service;
import MainPackageForTaskManager.Entity.Tasks;
import MainPackageForTaskManager.Entity.Users;
import MainPackageForTaskManager.Exception.ExceptionNotFound;
import MainPackageForTaskManager.Exception.NotAllowedException;
import MainPackageForTaskManager.Repository.TaskRepository;
import MainPackageForTaskManager.Repository.UserRepository;
import MainPackageForTaskManager.security.JWTSecurity.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {
    public final Logger LOGGER = LoggerFactory.getLogger(TaskService.class.getName());

    @Autowired
    private TaskRepository TaskRepo;
    @Autowired
    private UserRepository UserRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public List<Tasks> getTasks() {
        LOGGER.debug("getting task");
        return (List<Tasks>) TaskRepo.findAll();
    }

    @Override
    @Transactional
    public Tasks createTask(@RequestBody Tasks task){
        LOGGER.trace("creating task");
        Users user = UserRepo.findById(task.getUser().getId()).orElse(null);
        if (user == null) {
            throw new ExceptionNotFound("No user id found so add user first");
        } else {
            task.setUser(user);
        }
        CheckIfValidate(task, false); //This check will ensure that the task object contains a user object and the user object contains an ID before calling the checkTimeValidation method.
        return TaskRepo.save(task);
    }


    @Override
    @Transactional
    public Tasks getTaskById(int id) {
        if (TaskRepo.findById(id).isEmpty()) {
            throw new ExceptionNotFound("This id doesn't exist");
        }
        return TaskRepo.findById(id).get();
    }

    @Override
    @Transactional
    public void deleteTask(int id) {
        LOGGER.trace("deleting task");
        Optional<Tasks> task = TaskRepo.findById(id);
        if (task.isEmpty()) {
            throw new ExceptionNotFound("Task with id: " + id + " does not exist.");
        }
        TaskRepo.deleteById(id);
    }

    public Tasks updateTask(Tasks task, int taskId) {
        CheckIfValidate(task, true);
        Tasks existingTask = TaskRepo.findById(taskId).orElse(null);
        if (existingTask != null) {
            existingTask.setDescription(task.getDescription());
            existingTask.setStartDate(task.getStartDate());
            existingTask.setEndDate(task.getEndDate());
            existingTask.setDescription(task.getDescription());
            TaskRepo.save(existingTask);
            return existingTask;
        } else {
            throw new ExceptionNotFound("Task not found");
        }
    }

    public void CheckIfValidate(Tasks task, boolean updateTask) {
        {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Integer userId = jwtUtil.getUserIdFromToken(httpServletRequest);
            Date startDate = task.getStartDate();
            Date endDate = task.getEndDate();
            // Retrieve only the tasks that have overlapping start and end times for the current user
            List<Tasks> existingTasks = TaskRepo.findOverlappingTasks(userId, startDate, endDate);
            for (Tasks existingTask : existingTasks) {
                if (updateTask && (existingTask.getId() == task.getId())) {
                    continue; // Allow updating the same task
                }
                throw new NotAllowedException("Conflict with existing task: " + existingTask.getId());
            }
        }}}
