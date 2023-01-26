package MainPackageForTaskManager.Controller;
import MainPackageForTaskManager.Entity.Tasks;
import MainPackageForTaskManager.Entity.Users;
import MainPackageForTaskManager.Exception.NotAllowedException;
import MainPackageForTaskManager.Repository.TaskRepository;
import MainPackageForTaskManager.Service.IUserService;
import MainPackageForTaskManager.Service.TaskService;
import MainPackageForTaskManager.security.JWTSecurity.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/task")

public class TaskController {

    private TaskService taskservice;
    private IUserService userService;
    private JwtUtil jwtUtil;
    private TaskRepository taskRepository;

    public TaskController(TaskService taskservice, IUserService userservice, JwtUtil jwtUtil, TaskRepository taskRepository) {
        this.taskservice = taskservice;
        this.userService = userservice;
        this.jwtUtil = jwtUtil;
        this.taskRepository = taskRepository;
    }
    @GetMapping()
    public Page<Tasks> returnAllTasks(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam Optional<String> sortBy ,
                                      @RequestParam Optional<String> sortDirection, HttpServletRequest request) {

        int userId = jwtUtil.getUserIdFromToken(request);
        String sortByValue = sortBy.orElse("id");
        String sortDirectionValue = sortDirection.orElse("asc");
        PageRequest pageRequest = PageRequest.of(page, 4, Sort.Direction.fromString(sortDirectionValue), sortByValue);
        return taskRepository.findAllByUserId(userId, pageRequest);
    }

    @GetMapping("/tasks")
    public List<Tasks> getTask(HttpServletRequest request) {
        // Get the user ID of the currently logged-in user
        int currentUserId =  jwtUtil.getUserIdFromToken(request);
        // Retrieve the list of tasks belonging to the current user from the repository
        List<Tasks> tasks = taskRepository.findAllByUserId(currentUserId);
        return tasks;
    }


     @PostMapping
    public Tasks createTask(@RequestBody Tasks task,HttpServletRequest request) throws AccessDeniedException {
             // Get the user ID from the token
             int userId = jwtUtil.getUserIdFromToken(request);
             // Retrieve the user from the service
             Users user = userService.getById(userId);
             // Set the user ID of the task to the user's ID
             task.setUser(user);
             // Save the task
             return taskservice.createTask(task);
         }



    @PutMapping("/{id}")
    public Tasks updateTask(@RequestBody Tasks task, @PathVariable int id, HttpServletRequest request)  {
        checkPermission(id, request);
        return taskservice.updateTask(task, id);
    }
        // Update the task

    void checkPermission(int taskId, HttpServletRequest request) {
        // Get the user ID of the currently logged-in user
        int currentUserId = jwtUtil.getUserIdFromToken(request);
        // Retrieve the task from the service
        Tasks task = taskservice.getTaskById(taskId);
        // Check that the task belongs to the current user
        if (task.getUser().getId() != currentUserId) {
            throw new NotAllowedException("You do not have permission to access this task.");
        }
    }
    @DeleteMapping("/{id}")
    public String deleteTask ( @PathVariable int id, HttpServletRequest request) {
            // Check that the user has permission to delete the task
            checkPermission(id, request);
            // Delete the task
            taskservice.deleteTask(id);
        return "Task deleted successfully";
    }
    @GetMapping ("/{id}")
    public Tasks getById(@PathVariable int id, HttpServletRequest request) {
        // Check that the user has permission to view the task
        checkPermission(id, request);
        // Retrieve the task from the service
        return taskservice.getTaskById(id);
    }
}
