package com.lms.lms.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lms.lms.model.User;
import com.lms.lms.repositories.UserRepository;






@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

  

    @GetMapping("/home")
    public String Home(){
        return "This is the home page";
    }

    @GetMapping("/all")
    public List<User> getUsers() {
        return userRepository.findAll(); 
    }
    

    @PostMapping("/register")
    public User createUser(@RequestBody User user) {
       return userRepository.save(user);
    }

    
}
