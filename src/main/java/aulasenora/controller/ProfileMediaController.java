package aulasenora.controller;

import aulasenora.client.FileServiceClient;
import aulasenora.client.JwtTokenProvider;
import aulasenora.model.Usuario;
import aulasenora.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class ProfileMediaController {

    private static final Logger log = LoggerFactory.getLogger(ProfileMediaController.class);
    private final UsuarioRepository usuarioRepository;
    private final FileServiceClient fileServiceClient;
    private final JwtTokenProvider jwtTokenProvider;

    public ProfileMediaController(UsuarioRepository usuarioRepository,
                                  FileServiceClient fileServiceClient,
                                  JwtTokenProvider jwtTokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.fileServiceClient = fileServiceClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/profile/avatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "redirectUrl", defaultValue = "/dashboard") String redirectUrl,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";
        try {
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
            String token = jwtTokenProvider.generateToken(username);
            String fileUuid = fileServiceClient.upload(file, "AVATAR", token).orElse(null);
            if (fileUuid != null) {
                Usuario usuario = usuarioRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                usuario.setProfileImageUrl("fs:" + fileUuid);
                usuarioRepository.save(usuario);
                redirectAttributes.addFlashAttribute("successMessage", "Foto de perfil actualizada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error al subir la foto al servidor.");
            }
        } catch (Exception e) {
            log.error("Error al subir avatar", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error al subir la foto: " + e.getMessage());
        }
        return "redirect:" + redirectUrl;
    }

    @PostMapping("/profile/banner")
    public String uploadBanner(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "redirectUrl", defaultValue = "/dashboard") String redirectUrl,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";
        try {
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
            String token = jwtTokenProvider.generateToken(username);
            String fileUuid = fileServiceClient.upload(file, "BANNER", token).orElse(null);
            if (fileUuid != null) {
                Usuario usuario = usuarioRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                usuario.setBannerImageUrl("fs:" + fileUuid);
                usuarioRepository.save(usuario);
                redirectAttributes.addFlashAttribute("successMessage", "Portada de perfil actualizada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error al subir la portada al servidor.");
            }
        } catch (Exception e) {
            log.error("Error al subir banner", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error al subir la portada: " + e.getMessage());
        }
        return "redirect:" + redirectUrl;
    }
}
