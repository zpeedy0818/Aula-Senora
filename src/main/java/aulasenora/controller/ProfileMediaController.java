package aulasenora.controller;

import aulasenora.model.Usuario;
import aulasenora.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.UUID;

@Controller
public class ProfileMediaController {

    private final UsuarioRepository usuarioRepository;
    private final String uploadBaseDir = "uploads/profiles";

    public ProfileMediaController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/profile/avatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "redirectUrl", defaultValue = "/dashboard") String redirectUrl,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "El archivo de imagen de perfil está vacío.");
            return "redirect:" + redirectUrl;
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            redirectAttributes.addFlashAttribute("errorMessage", "El archivo debe ser una imagen válida.");
            return "redirect:" + redirectUrl;
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "jpg";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            }

            Path avatarsDir = Paths.get(uploadBaseDir, "avatars").toAbsolutePath().normalize();
            Files.createDirectories(avatarsDir);

            // Limpieza de archivos antiguos de avatar para este usuario
            String prefix = "avatar_" + username + "_";
            File[] existingFiles = avatarsDir.toFile().listFiles((d, name) -> name.startsWith(prefix));
            if (existingFiles != null) {
                for (File f : existingFiles) {
                    f.delete();
                }
            }

            String filename = prefix + UUID.randomUUID().toString() + "." + extension;
            Path targetPath = avatarsDir.resolve(filename);

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            usuario.setProfileImageUrl("/uploads/profiles/avatars/" + filename);
            usuarioRepository.save(usuario);

            redirectAttributes.addFlashAttribute("successMessage", "Foto de perfil actualizada correctamente.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la foto de perfil: " + e.getMessage());
        }

        return "redirect:" + redirectUrl;
    }

    @PostMapping("/profile/banner")
    public String uploadBanner(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "redirectUrl", defaultValue = "/dashboard") String redirectUrl,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "El archivo de portada está vacío.");
            return "redirect:" + redirectUrl;
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            redirectAttributes.addFlashAttribute("errorMessage", "El archivo debe ser una imagen válida.");
            return "redirect:" + redirectUrl;
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "jpg";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            }

            Path bannersDir = Paths.get(uploadBaseDir, "banners").toAbsolutePath().normalize();
            Files.createDirectories(bannersDir);

            // Limpieza de archivos antiguos de portada para este usuario
            String prefix = "banner_" + username + "_";
            File[] existingFiles = bannersDir.toFile().listFiles((d, name) -> name.startsWith(prefix));
            if (existingFiles != null) {
                for (File f : existingFiles) {
                    f.delete();
                }
            }

            String filename = prefix + UUID.randomUUID().toString() + "." + extension;
            Path targetPath = bannersDir.resolve(filename);

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            usuario.setBannerImageUrl("/uploads/profiles/banners/" + filename);
            usuarioRepository.save(usuario);

            redirectAttributes.addFlashAttribute("successMessage", "Portada de perfil actualizada correctamente.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la portada: " + e.getMessage());
        }

        return "redirect:" + redirectUrl;
    }
}
