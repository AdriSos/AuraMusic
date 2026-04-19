package com.auramusic.backend.repository;

import com.auramusic.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Aquí podemos añadir búsquedas personalizadas, por ejemplo, buscar por email:
    User findByEmail(String email);
}