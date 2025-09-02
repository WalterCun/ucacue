package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.User;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    
    /**
     * Busca un usuario por su nombre de usuario
     */
    public Optional<User> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }
    
    /**
     * Busca un usuario por su email
     */
    public Optional<User> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
    
    /**
     * Busca usuarios activos
     */
    public List<User> findActiveUsers() {
        return find("isActive", true).list();
    }
    
    /**
     * Busca usuarios por nombre o apellido (búsqueda parcial)
     */
    public List<User> findByNameContaining(String name) {
        return find("firstName like ?1 or lastName like ?1", "%" + name + "%").list();
    }
    
    /**
     * Verifica si un nombre de usuario está disponible
     */
    public boolean isUsernameAvailable(String username) {
        return count("username", username) == 0;
    }
    
    /**
     * Verifica si un email está disponible
     */
    public boolean isEmailAvailable(String email) {
        return count("email", email) == 0;
    }
    
    /**
     * Busca usuarios creados después de una fecha específica
     */
    public List<User> findUsersCreatedAfter(java.time.LocalDateTime date) {
        return find("createdAt > ?1", date).list();
    }
    
    /**
     * Cuenta usuarios activos
     */
    public long countActiveUsers() {
        return count("isActive", true);
    }
}
