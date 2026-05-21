package aulasenora.controller;

import aulasenora.service.RecursoAulaService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class RecursoAulaController {

    private final RecursoAulaService recursoService;

    public RecursoAulaController(RecursoAulaService recursoService) {
        this.recursoService = recursoService;
    }

    @PostMapping("/volunteer/aulas/{aulaId}/recursos/upload")
    public String uploadRecurso(@PathVariable Long aulaId,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam(value = "descripcion", required = false) String descripcion,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            recursoService.guardarRecurso(file, aulaId, authentication.getName(), descripcion);
            redirectAttributes.addFlashAttribute("successMessage", "Recurso subido correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al subir recurso: " + e.getMessage());
        }
        return "redirect:/volunteer/aulas/" + aulaId;
    }

    @PostMapping("/volunteer/aulas/{aulaId}/recursos/{recursoId}/delete")
    public String deleteRecurso(@PathVariable Long aulaId,
                                @PathVariable Long recursoId,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            recursoService.eliminarRecurso(recursoId, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Recurso eliminado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar recurso: " + e.getMessage());
        }
        return "redirect:/volunteer/aulas/" + aulaId;
    }

    @GetMapping("/recursos/{recursoId}/view")
    public ResponseEntity<Resource> viewRecurso(@PathVariable Long recursoId,
                                                Authentication authentication) throws IOException {
        return servirRecurso(recursoId, authentication, false);
    }

    @GetMapping("/recursos/{recursoId}/download")
    public ResponseEntity<Resource> downloadRecurso(@PathVariable Long recursoId,
                                                    Authentication authentication) throws IOException {
        return servirRecurso(recursoId, authentication, true);
    }

    private ResponseEntity<Resource> servirRecurso(Long recursoId, Authentication authentication, boolean forzarDescarga) throws IOException {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            RecursoAulaService.RecursoAcceso acceso = recursoService.cargarParaUsuario(recursoId, authentication.getName());
            String filename = acceso.nombreOriginal() != null ? acceso.nombreOriginal() : "recurso";
            String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
            String disposition = (forzarDescarga ? "attachment" : "inline") + "; filename=\"" + filename + "\"; filename*=UTF-8''" + encoded;
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if (acceso.mimeType() != null && !acceso.mimeType().isBlank()) {
                try {
                    mediaType = MediaType.parseMediaType(acceso.mimeType());
                } catch (Exception ignored) {}
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                    .contentType(mediaType)
                    .body(acceso.resource());
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build();
        }
    }
}
