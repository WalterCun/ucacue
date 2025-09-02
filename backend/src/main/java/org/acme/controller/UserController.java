package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CreateUserDTO;
import org.acme.dto.UserDTO;
import org.acme.model.User;
import org.acme.service.UserService;
import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// @Path removed to disable User endpoints per new requirements
// @Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private static final Logger LOG = Logger.getLogger(UserController.class);

    @Inject
    UserService userService;

    /**
     * Crea un nuevo usuario
     */
    @POST
    public Response createUser(@Valid CreateUserDTO createUserDTO) {
        try {
            // Validación adicional de nulos
            if (createUserDTO == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Los datos del usuario son requeridos\"}")
                        .build();
            }

            User user = new User();
            user.username = createUserDTO.username;
            user.email = createUserDTO.email;
            user.password = createUserDTO.password;
            user.firstName = createUserDTO.firstName;
            user.lastName = createUserDTO.lastName;

            User createdUser = userService.createUser(user);

            // Verificar que el usuario se creó correctamente
            if (createdUser == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\": \"Error interno al crear el usuario\"}")
                        .build();
            }

            UserDTO userDTO = convertToDTO(createdUser);

            return Response.status(Response.Status.CREATED)
                    .entity(userDTO)
                    .build();
        } catch (RuntimeException e) {
            LOG.error("Error al crear usuario", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Obtiene todos los usuarios activos
     */
    @GET
    public Response getAllUsers() {
        try {
            // Verificar que el servicio no sea null
            if (userService == null) {
                LOG.error("UserService no está inyectado correctamente");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\": \"Error interno del servidor\"}")
                        .build();
            }

            List<User> users = userService.getActiveUsers();

            // Manejar lista null o vacía
            if (users == null) {
                LOG.warn("getActiveUsers() retornó null");
                users = Collections.emptyList();
            }

            if (users.isEmpty()) {
                return Response.ok()
                        .entity("{\"users\": [], \"count\": 0, \"message\": \"No hay usuarios activos\"}")
                        .build();
            }

            // Filtrar nulos y convertir a DTO con manejo seguro
            List<UserDTO> userDTOs = users.stream()
                    .peek(user -> {
                        if (user == null) {
                            LOG.warn("Usuario null encontrado en la lista");
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(this::convertToDTO)
                    .filter(Objects::nonNull) // Filtrar DTOs nulos también
                    .collect(Collectors.toList());

            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("users", userDTOs);
            payload.put("count", userDTOs.size());
            return Response.ok()
                    .entity(payload)
                    .build();

        } catch (Exception e) {
            LOG.error("Error al obtener usuarios activos", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno al obtener usuarios\"}")
                    .build();
        }
    }

    /**
     * Obtiene un usuario por ID
     */
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") @NotNull Long id) {
        try {
            // Validar ID
            if (id == null || id <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"ID de usuario inválido\"}")
                        .build();
            }

            return userService.getUserById(id)
                    .map(user -> {
                        UserDTO userDTO = convertToDTO(user);
                        if (userDTO == null) {
                            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                    .entity("{\"error\": \"Error al procesar datos del usuario\"}")
                                    .build();
                        }
                        return Response.ok(userDTO).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Usuario no encontrado\"}")
                            .build());
        } catch (Exception e) {
            LOG.error("Error al obtener usuario por ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno al obtener usuario\"}")
                    .build();
        }
    }

    /**
     * Obtiene un usuario por username
     */
    @GET
    @Path("/username/{username}")
    public Response getUserByUsername(@PathParam("username") String username) {
        try {
            // Validar username
            if (username == null || username.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Username es requerido\"}")
                        .build();
            }

            return userService.getUserByUsername(username.trim())
                    .map(user -> {
                        UserDTO userDTO = convertToDTO(user);
                        if (userDTO == null) {
                            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                    .entity("{\"error\": \"Error al procesar datos del usuario\"}")
                                    .build();
                        }
                        return Response.ok(userDTO).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\": \"Usuario no encontrado\"}")
                            .build());
        } catch (Exception e) {
            LOG.error("Error al obtener usuario por username: " + username, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno al obtener usuario\"}")
                    .build();
        }
    }

    /**
     * Busca usuarios por nombre
     */
    @GET
    @Path("/search")
    public Response searchUsers(@QueryParam("name") String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"El parámetro 'name' es requerido\"}")
                        .build();
            }

            List<User> users = userService.searchUsersByName(name.trim());

            // Manejar lista null
            if (users == null) {
                LOG.warn("searchUsersByName() retornó null para: " + name);
                users = Collections.emptyList();
            }

            if (users.isEmpty()) {
                return Response.ok()
                        .entity("{\"users\": [], \"count\": 0, \"message\": \"No se encontraron usuarios con el nombre: " + name + "\"}")
                        .build();
            }

            List<UserDTO> userDTOs = users.stream()
                    .filter(Objects::nonNull)
                    .map(this::convertToDTO)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("users", userDTOs);
            payload.put("count", userDTOs.size());
            return Response.ok()
                    .entity(payload)
                    .build();

        } catch (Exception e) {
            LOG.error("Error al buscar usuarios por nombre: " + name, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno al buscar usuarios\"}")
                    .build();
        }
    }

    /**
     * Actualiza un usuario
     */
    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") @NotNull Long id, @Valid CreateUserDTO updateUserDTO) {
        try {
            // Validaciones
            if (id == null || id <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"ID de usuario inválido\"}")
                        .build();
            }

            if (updateUserDTO == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Los datos de actualización son requeridos\"}")
                        .build();
            }

            User userDetails = new User();
            userDetails.username = updateUserDTO.username;
            userDetails.email = updateUserDTO.email;
            userDetails.password = updateUserDTO.password;
            userDetails.firstName = updateUserDTO.firstName;
            userDetails.lastName = updateUserDTO.lastName;

            User updatedUser = userService.updateUser(id, userDetails);

            if (updatedUser == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\": \"Error al actualizar el usuario\"}")
                        .build();
            }

            UserDTO userDTO = convertToDTO(updatedUser);

            return Response.ok(userDTO).build();
        } catch (RuntimeException e) {
            LOG.error("Error al actualizar usuario ID: " + id, e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Desactiva un usuario
     */
    @PATCH
    @Path("/{id}/deactivate")
    public Response deactivateUser(@PathParam("id") @NotNull Long id) {
        try {
            if (id == null || id <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"ID de usuario inválido\"}")
                        .build();
            }

            userService.deactivateUser(id);
            return Response.ok("{\"message\": \"Usuario desactivado correctamente\"}").build();
        } catch (RuntimeException e) {
            LOG.error("Error al desactivar usuario ID: " + id, e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Activa un usuario
     */
    @PATCH
    @Path("/{id}/activate")
    public Response activateUser(@PathParam("id") @NotNull Long id) {
        try {
            if (id == null || id <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"ID de usuario inválido\"}")
                        .build();
            }

            userService.activateUser(id);
            return Response.ok("{\"message\": \"Usuario activado correctamente\"}").build();
        } catch (RuntimeException e) {
            LOG.error("Error al activar usuario ID: " + id, e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Elimina un usuario
     */
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") @NotNull Long id) {
        try {
            if (id == null || id <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"ID de usuario inválido\"}")
                        .build();
            }

            userService.deleteUser(id);
            return Response.ok("{\"message\": \"Usuario eliminado correctamente\"}").build();
        } catch (RuntimeException e) {
            LOG.error("Error al eliminar usuario ID: " + id, e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Obtiene estadísticas de usuarios
     */
    @GET
    @Path("/stats")
    public Response getUserStats() {
        try {
            long activeUsers = userService.countActiveUsers();
            long totalUsers = userService.countTotalUsers(); // Corregido: método separado para total

            String stats = String.format(
                    "{\"activeUsers\": %d, \"totalUsers\": %d}",
                    activeUsers, totalUsers
            );

            return Response.ok(stats).build();
        } catch (Exception e) {
            LOG.error("Error al obtener estadísticas de usuarios", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno al obtener estadísticas\"}")
                    .build();
        }
    }

    /**
     * Convierte un User a UserDTO con manejo seguro de nulos
     */
    private UserDTO convertToDTO(User user) {
        if (user == null) {
            LOG.warn("Intentando convertir User null a DTO");
            return null;
        }

        try {
            return new UserDTO(
                    user.id,
                    user.username,
                    user.email,
                    user.firstName,
                    user.lastName,
                    user.isActive,
                    user.createdAt,
                    user.updatedAt
            );
        } catch (Exception e) {
            LOG.error("Error al convertir User a DTO: " + user.id, e);
            return null;
        }
    }

    /**
     * Método auxiliar para serializar lista a JSON (simplificado)
     * En un entorno real, usar Jackson o similar
     */
    private String toJson(List<UserDTO> userDTOs) {
        // Implementación básica - en producción usar ObjectMapper
        return "[" + userDTOs.stream()
                .map(dto -> "\"" + dto.toString() + "\"")
                .collect(Collectors.joining(",")) + "]";
    }
}