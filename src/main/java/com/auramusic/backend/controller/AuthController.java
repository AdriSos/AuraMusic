package com.auramusic.backend.controller;

import com.auramusic.backend.model.User;
import com.auramusic.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return ResponseEntity.ok(user.getRol());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> userData) {
        String nombre = userData.get("nombre");
        String email = userData.get("email");


        if (userRepository.findByEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo ya está registrado.");
        }


        String generatedPassword = UUID.randomUUID().toString().substring(0, 8);


        User newUser = new User();
        newUser.setNombre(nombre);
        newUser.setEmail(email);
        newUser.setPassword(generatedPassword);
        newUser.setRol("cliente");
        userRepository.save(newUser);


        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Bienvenido a AuraMusic - Tu Contraseña de Acceso");
            message.setText("Hola " + nombre + ",\n\n" +
                    "Bienvenido a AuraMusic. Tu cuenta ha sido creada exitosamente.\n" +
                    "Tu contraseña temporal para iniciar sesión es: " + generatedPassword + "\n\n" +
                    "¡Disfruta de tu música!");

            mailSender.send(message);
            return ResponseEntity.ok("Usuario registrado. Contraseña enviada al correo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo.");
        }
    }
}