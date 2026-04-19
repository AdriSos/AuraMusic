package com.auramusic.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity // Le dice a Spring que esta clase es una tabla de base de datos
@Table(name = "genres") // El nombre exacto de la tabla en tu base de datos
@Data // Magia de Lombok: crea automáticamente los getters, setters y más
@NoArgsConstructor // Lombok: crea un constructor vacío
@AllArgsConstructor // Lombok: crea un constructor con todos los datos
public class Genre {

    @Id // Indica que este es el identificador único (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Hace que el ID sea auto-incrementable
    private Integer id;

    @Column(nullable = false, unique = true) // No puede estar vacío y no se puede repetir
    private String nombre;
}