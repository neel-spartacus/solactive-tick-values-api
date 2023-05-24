package com.solactive.challenge.solactivetickvalues.assignment.task;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.lang.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
//import javax.validation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.commons.lang3.EnumUtils;


@RestController
public class TaskController {

    private final TaskRepository repository;

    @Autowired
    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }


    @RequestMapping(path = "/tasks", method = RequestMethod.POST)
    public ResponseEntity postTask(@RequestBody TaskDto taskDto){

        Task task= new Task(taskDto.getTitle());
        System.out.println();
        //task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        Task savedTask=repository.save(task);
        return new ResponseEntity(savedTask,HttpStatus.OK);
    }

    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.GET)
    public ResponseEntity getTask(@PathVariable Long id) {

        if(Objects.nonNull(id)){
            Optional<Task> task=repository.findById(id);
            if(task.isPresent()){
                Task savedTask=task.get();
                TaskDto taskDto=savedTask.toDto();
                return new ResponseEntity(taskDto,HttpStatus.OK);
            }
            else{
                return new ResponseEntity("Task not found",HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity("Id cannot be null",HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateTask(@RequestBody TaskDto taskDto, @PathVariable Long id ) {

        if (Objects.nonNull(id)) {
            Optional<Task> task = repository.findById(id);
            if (task.isPresent()) {
                String taskStatus = taskDto.getStatus();
                if (EnumUtils.isValidEnum(TaskStatus.class, taskStatus)) {
                    Task savedTask = task.get();
                    if(Objects.nonNull(taskDto.getTitle())){
                        savedTask.setTitle(taskDto.getTitle());
                    }
                    if(Objects.nonNull(taskDto.getDescription())){
                        savedTask.setDescription(taskDto.getDescription());
                    }
                    if(Objects.nonNull(taskDto.getStatus())){
                        savedTask.setStatus(TaskStatus.valueOf(taskDto.getStatus()));
                    }
                    Task updatedTask=repository.save(savedTask);
                    return new ResponseEntity(updatedTask.toDto(), HttpStatus.OK);
                } else {
                    return new ResponseEntity("Available statuses are: CREATED, APPROVED, REJECTED, BLOCKED, DONE.", HttpStatus.BAD_REQUEST);
                }
            }
        else {
            return new ResponseEntity("Task not found", HttpStatus.NO_CONTENT);
           }
        }
        return new ResponseEntity("Id cannot be null",HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(path = "/tasks/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTask(@PathVariable Long id ){

        if (Objects.nonNull(id)) {
            Optional<Task> task = repository.findById(id);
            if (task.isPresent()) {
                repository.delete(task.get());
                return new ResponseEntity("Task deleted successfully",HttpStatus.OK);
            }
            else{
                return new ResponseEntity("Task not found",HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity(" Task id cannot be null",HttpStatus.BAD_REQUEST);

    }

    @RequestMapping(path = "/tasks", method = RequestMethod.GET)
    public ResponseEntity getAllTasks() {

            List<Task> tasks= new ArrayList<>();
            tasks= (List<Task>) repository.findAll();
            if(Objects.nonNull(tasks) && !tasks.isEmpty()){

                List<TaskDto> taskDtos=tasks.stream().map(task->task.toDto()).collect(Collectors.toList());
                return new ResponseEntity(taskDtos,HttpStatus.OK);
            }
            else{
                return new ResponseEntity(tasks,HttpStatus.OK);
            }
        }
    }
    
