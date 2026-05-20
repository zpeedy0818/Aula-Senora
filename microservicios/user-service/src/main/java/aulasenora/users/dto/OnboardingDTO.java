package aulasenora.users.dto;

import java.time.LocalDate;

public class OnboardingDTO {

    private String rol;               // "ESTUDIANTE" o "VOLUNTARIO"

    // Campos de Voluntario (obligatorios si rol=VOLUNTARIO)
    private String institution;
    private String skills;
    private String materiaEspecializada;

    // Campos opcionales comunes
    private String ciudad;
    private String bio;
    private String phoneNumber;
    private LocalDate birthDate;
    private String gradoAcademico;

    // Getters y Setters
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getMateriaEspecializada() { return materiaEspecializada; }
    public void setMateriaEspecializada(String materiaEspecializada) { this.materiaEspecializada = materiaEspecializada; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getGradoAcademico() { return gradoAcademico; }
    public void setGradoAcademico(String gradoAcademico) { this.gradoAcademico = gradoAcademico; }
}
