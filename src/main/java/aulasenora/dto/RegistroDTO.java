package aulasenora.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistroDTO {
    // Datos de Usuario
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo debe contener letras")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo debe contener letras")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del correo es inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
    
    private String rol;

    // Datos Adicionales (Voluntario)
    private String institution;
    private String skills;

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
}
