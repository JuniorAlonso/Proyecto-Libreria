package com.proyecto.Libreria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Campos de Mapeo Directo (Coincidencia con MySQL) ---
    // MySQL tiene: nombre (para nombre completo)
    @Column(name = "nombre") 
    private String nombreCompleto; 

    // MySQL tiene: apellido (para primer apellido)
    @Column(name = "apellido") 
    private String primerApellido; 
    
    // MySQL tiene: correo
    @Column(name = "correo")
    private String correo; // antes era email

    // MySQL tiene: contraseña (Aquí usamos la columna sin tilde para evitar problemas de nombres)
    @Column(name = "contrasena") 
    private String contrasena; // Usamos 'contrasena' en Java, más fácil de escribir

    // --- Campos Adicionales del Formulario que NO están en tu tabla MySQL simple ---
    // Si quieres guardar estos campos, DEBES crearlos en tu tabla 'usuarios'
    private String nombreUsuario;
    private String dui;
    private String telefono;
    private String segundoApellido;
    private String direccion;
    
    // --- Campos de Pago (Necesarios para el mapeo en registro-pago.html) ---
    private String numeroTarjeta;
    private String fechaExpiracion;
    private String cvv;

    // --- CONSTRUCTORES ---

    // Constructor vacío (Necesario para JPA)
    public Usuario() {}

    // Constructor parcial (Opcional, para inicialización rápida)
    public Usuario(String nombreCompleto, String primerApellido, String correo, String contrasena) {
        this.nombreCompleto = nombreCompleto;
        this.primerApellido = primerApellido;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // --- GETTERS Y SETTERS (Todos son obligatorios) ---
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // Nombre Completo (mapea a la columna 'nombre')
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    // Primer Apellido (mapea a la columna 'apellido')
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    // Email (mapea a la columna 'correo')
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    // Contrasena (mapea a la columna 'contraseña')
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    // Demás campos (Sin mapeo especial, asumen que la columna se llama igual que el atributo)
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    // Campos de Pago
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public String getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(String fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

}