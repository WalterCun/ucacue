package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.acme.model.User;
import org.acme.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {
    
    @Inject
    UserRepository userRepository;
    
    /**
     * Crea un nuevo usuario
     */
    @Transactional
    public User createUser(@Valid User user) {
        // Verificar que el username y email no existan
        if (!userRepository.isUsernameAvailable(user.username)) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        
        if (!userRepository.isEmailAvailable(user.email)) {
            throw new RuntimeException("El email ya está en uso");
        }
        
        // Encriptar la contraseña (en un caso real usarías BCrypt o similar)
        user.password = hashPassword(user.password);
        
        userRepository.persist(user);
        return user;
    }
    
    /**
     * Actualiza un usuario existente
     */
    @Transactional
    public User updateUser(Long id, @Valid User userDetails) {
        User user = userRepository.findByIdOptional(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Verificar que el nuevo username no esté en uso por otro usuario
        if (!user.username.equals(userDetails.username) && 
            !userRepository.isUsernameAvailable(userDetails.username)) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        
        // Verificar que el nuevo email no esté en uso por otro usuario
        if (!user.email.equals(userDetails.email) && 
            !userRepository.isEmailAvailable(userDetails.email)) {
            throw new RuntimeException("El email ya está en uso");
        }
        
        // Actualizar campos
        user.username = userDetails.username;
        user.email = userDetails.email;
        user.firstName = userDetails.firstName;
        user.lastName = userDetails.lastName;
        user.isActive = userDetails.isActive;
        
        // Solo actualizar contraseña si se proporciona una nueva
        if (userDetails.password != null && !userDetails.password.trim().isEmpty()) {
            user.password = hashPassword(userDetails.password);
        }
        
        userRepository.persist(user);
        return user;
    }
    
    /**
     * Obtiene un usuario por ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findByIdOptional(id);
    }
    
    /**
     * Obtiene un usuario por username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Obtiene un usuario por email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Obtiene todos los usuarios activos
     */
    public List<User> getActiveUsers() {
        List<User> users = userRepository.findActiveUsers();
        if (users == null) {
            return java.util.Collections.emptyList();
        }
        return users.stream().filter(java.util.Objects::nonNull).toList();
    }
    
    /**
     * Busca usuarios por nombre
     */
    public List<User> searchUsersByName(String name) {
        List<User> users = userRepository.findByNameContaining(name);
        if (users == null) {
            return java.util.Collections.emptyList();
        }
        return users.stream().filter(java.util.Objects::nonNull).toList();
    }
    
    /**
     * Desactiva un usuario
     */
    @Transactional
    public void deactivateUser(Long id) {
        User user = userRepository.findByIdOptional(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.isActive = false;
        userRepository.persist(user);
    }
    
    /**
     * Activa un usuario
     */
    @Transactional
    public void activateUser(Long id) {
        User user = userRepository.findByIdOptional(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.isActive = true;
        userRepository.persist(user);
    }

    @Transactional
    public long countTotalUsers(){
        return userRepository.count();
    }
    
    /**
     * Elimina un usuario
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findByIdOptional(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        userRepository.delete(user);
    }
    
    /**
     * Cuenta usuarios activos
     */
    public long countActiveUsers() {
        return userRepository.countActiveUsers();
    }
    
    /**
     * Método simple para hash de contraseña (en producción usar BCrypt)
     */
    private String hashPassword(String password) {
        // En un caso real, usarías BCrypt o similar
        // Por ahora, solo retornamos la contraseña como está
        return password;
    }
}
