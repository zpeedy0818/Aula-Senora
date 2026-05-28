package aulasenora.files.controller;

import aulasenora.files.model.Archivo;
import aulasenora.files.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tipo") String tipo,
            Authentication auth) {

        try {
            String username = auth != null ? auth.getName() : "anonymous";
            Archivo archivo = fileService.guardarArchivo(file, tipo, username);
            return ResponseEntity.ok(Map.of(
                    "uuid", archivo.getUuid(),
                    "url", "/api/files/" + archivo.getUuid(),
                    "nombreOriginal", archivo.getNombreOriginal(),
                    "mimeType", archivo.getMimeType(),
                    "tamanoBytes", archivo.getTamanoBytes(),
                    "tipo", archivo.getTipo()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Resource> descargar(@PathVariable String uuid) {
        Archivo archivo = fileService.obtenerPorUuid(uuid)
                .orElse(null);
        if (archivo == null) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = fileService.cargarComoResource(archivo);

        String contentDisposition = "inline";
        String filename = archivo.getNombreOriginal();
        String encodedFilename = filename.replaceAll("[^a-zA-Z0-9._-]", "_");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(archivo.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        contentDisposition + "; filename=\"" + encodedFilename + "\"")
                .body(resource);
    }

    @GetMapping("/{uuid}/download")
    public ResponseEntity<Resource> descargarComoAdjunto(@PathVariable String uuid) {
        Archivo archivo = fileService.obtenerPorUuid(uuid)
                .orElse(null);
        if (archivo == null) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = fileService.cargarComoResource(archivo);

        String filename = archivo.getNombreOriginal();
        String encodedFilename = filename.replaceAll("[^a-zA-Z0-9._-]", "_");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(archivo.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodedFilename + "\"")
                .body(resource);
    }

    @GetMapping("/{uuid}/meta")
    public ResponseEntity<?> metadatos(@PathVariable String uuid) {
        Archivo archivo = fileService.obtenerPorUuid(uuid)
                .orElse(null);
        if (archivo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of(
                "uuid", archivo.getUuid(),
                "nombreOriginal", archivo.getNombreOriginal(),
                "mimeType", archivo.getMimeType(),
                "tamanoBytes", archivo.getTamanoBytes(),
                "tipo", archivo.getTipo(),
                "fechaSubida", archivo.getFechaSubida().toString()
        ));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> eliminar(@PathVariable String uuid) {
        try {
            fileService.eliminar(uuid);
            return ResponseEntity.ok(Map.of("mensaje", "Archivo eliminado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
