package com.lms.lms.model;
import org.springframework.data.annotation.*;
import lombok.Data;

@Data
public class User {

    @Id
    private String id;
    private String username;
    private String name;
    private String email;
    private String password;


    
}
