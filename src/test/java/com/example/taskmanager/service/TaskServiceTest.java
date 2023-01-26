package com.example.taskmanager.service;
import MainPackageForTaskManager.Entity.Tasks;
import MainPackageForTaskManager.Entity.Users;
import MainPackageForTaskManager.Exception.ExceptionNotFound;
import MainPackageForTaskManager.Exception.NotAllowedException;
import MainPackageForTaskManager.Repository.TaskRepository;
import MainPackageForTaskManager.Repository.UserRepository;
import MainPackageForTaskManager.Service.TaskService;
import MainPackageForTaskManager.security.JWTSecurity.JwtUtil;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepo;
    @Mock
    private UserRepository userRepo;
    @InjectMocks
    private TaskService taskService;
    @Mock
    private JwtUtil jwtUtil;
    private final Users user = new Users(1, "Dalal", 22, "dalal@gmail.com ", "dalal");
    private final Tasks task = new Tasks(1, "Task 1");
    private final Tasks task2 = new Tasks(2, "Task 2");

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTasks_Pass() {
        List<Tasks> tasks = Arrays.asList(this.task, this.task2);
        when(taskRepo.findAll()).thenReturn(tasks);
        List<Tasks> result = taskService.getTasks();
        assertEquals(tasks, result);
    }

    @Test
    public void testGetTasks_emptyList() throws ParseException {
        when(taskRepo.findAll()).thenReturn(Collections.emptyList());
        List<Tasks> result = taskService.getTasks();
        assertEquals(Collections.emptyList(), result);
    }


    @Test
    public void testCreateTask_Pass() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        this.task.setUser(this.user);
        when(userRepo.findById(1)).thenReturn(Optional.of(this.user));
        when(taskRepo.save(task)).thenReturn(this.task);
        Tasks result = taskService.createTask(this.task);
        assertEquals(this.task, result);
        Mockito.verify(userRepo, Mockito.times(1)).findById(1);
        Mockito.verify(taskRepo, Mockito.times(1)).save(task);
    }


    @Test
    public void testCreateTask_userNotFound_throwsException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Tasks task = new Tasks(1, "Task 1");
        task.setUser(this.user);
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(ExceptionNotFound.class, () -> taskService.createTask(task));
    }

    @Test
    public void testGetTaskById_idExists() {
        when(taskRepo.findById(1)).thenReturn(Optional.of(this.task));
        Tasks actualTask = taskService.getTaskById(1);
        assertEquals(this.task, actualTask);
    }

    @Test
    public void testGetTaskById_idDoesNotExist() {
        int id = 1;
        when(taskRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(ExceptionNotFound.class, () -> taskService.getTaskById(id));
    }

    @Test
    public void testUpdateTask() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        this.task.setUser(this.user);
        when(taskRepo.findById(1)).thenReturn(Optional.of(this.task));
        when(taskRepo.save(task)).thenReturn(this.task);
        Tasks result = taskService.updateTask(this.task, 1);
        assertEquals(this.task, result);
        Mockito.verify(taskRepo, Mockito.times(1)).findById(1);
        Mockito.verify(taskRepo, Mockito.times(1)).save(task);
    }

    @Test
    public void testUpdateTask_taskNotFound_throwsException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Tasks task = new Tasks(1, "Task 1");
        task.setUser(this.user);
        when(taskRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(ExceptionNotFound.class, () -> taskService.updateTask(task, 1));
    }

    @Test
    public void deleteTask_validId_deletesTask() throws ExceptionNotFound {
        when(taskRepo.findById(1)).thenReturn(Optional.of(this.task));
        taskService.deleteTask(1);
        verify(taskRepo).findById(1);
        verify(taskRepo).deleteById(1);
    }
    @Test
    public void testDeleteTask_throwsExceptionNotFound() {
        int nonExistentId = 999;
        when(taskRepo.findById(nonExistentId)).thenReturn(Optional.empty());
        assertThrows(ExceptionNotFound.class, () -> taskService.deleteTask(nonExistentId));
    }

    @Test
    public void testCheckIfValidate_doesNotThrowException() throws ParseException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        List<Tasks> existingTasks = new ArrayList<>();
        existingTasks.add(this.task2);
        when(taskRepo.findOverlappingTasks(anyInt(), any(), any())).thenReturn(existingTasks);
        // Update task to have the same ID as conflicting task
        this.task.setId(this.task2.getId());
        taskService.CheckIfValidate(this.task, true);
    }
    @Test
    public void testCheckIfValidate_throwsNotAllowedException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        List<Tasks> existingTasks = new ArrayList<>();
        existingTasks.add(this.task2);
        when(taskRepo.findOverlappingTasks(anyInt(), any(), any())).thenReturn(existingTasks);
        assertThrows(NotAllowedException.class, () -> taskService.CheckIfValidate(this.task, false));
    }}