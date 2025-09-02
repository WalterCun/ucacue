package org.acme.dto;

import java.time.LocalDateTime;

public class UserDTO {
    
    public Long id;
    public String username;
    public String email;
    public String firstName;
    public String lastName;
    public boolean isActive;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    
    public UserDTO() {}
    
    public UserDTO(Long id, String username, String email, String firstName, 
                   String lastName, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
