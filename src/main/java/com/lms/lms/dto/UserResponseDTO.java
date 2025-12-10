package com.lms.lms.dto;


public class UserResponseDTO {
    private String id;
    private String username;
    private String name;
    private String email;
    private String role;

    public UserResponseDTO(String id, String username, String name, String email, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
