package aulasenora.service;

import aulasenora.model.Aula;
import aulasenora.model.MiembroAula;
import aulasenora.model.RecursoAula;
import aulasenora.model.Usuario;
import aulasenora.repository.AulaRepository;
import aulasenora.repository.MiembroAulaRepository;
import aulasenora.repository.RecursoAulaRepository;
import aulasenora.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class RecursoAulaService {

    private static final Map<String, String> MIME_TO_TIPO = Map.ofEntries(
            Map.entry("application/pdf", "PDF"),
            Map.entry("image/png", "IMAGEN"),
            Map.entry("image/jpeg", "IMAGEN"),
            Map.entry("image/jpg", "IMAGEN"),
            Map.entry("image/webp", "IMAGEN"),
            Map.entry("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "OFFICE"),
            Map.entry("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "OFFICE"),
            Map.entry("application/vnd.openxmlformats-officedocument.presentationml.presentation", "OFFICE"),
            Map.entry("application/msword", "OFFICE"),
            Map.entry("application/vnd.ms-excel", "OFFICE"),
            Map.entry("application/vnd.ms-powerpoint", "OFFICE")
    );

    private static final Set<String> EXTENSIONES_PERMITIDAS = Set.of(
            "pdf", "png", "jpg", "jpeg", "webp", "docx", "xlsx", "pptx", "doc", "xls", "ppt"
    );

    private final RecursoAulaRepository recursoRepository;
    private final AulaRepository aulaRepository;
    private final MiembroAulaRepository miembroAulaRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${app.upload.dir:uploads/recursos}")
    private String uploadDir;

    public RecursoAulaService(RecursoAulaRepository recursoRepository,
                              AulaRepository aulaRepository,
                              MiembroAulaRepository miembroAulaRepository,
                              UsuarioRepository usuarioRepository) {
        this.recursoRepository = recursoRepository;
        this.aulaRepository = aulaRepository;
        this.miembroAulaRepository = miembroAulaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<RecursoAula> listarPorAula(Long aulaId) {
        return recursoRepository.findByAula_IdOrderByFechaSubidaDesc(aulaId);
    }

    @Transactional
    public RecursoAula guardarRecurso(MultipartFile file, Long aulaId, String username, String descripcion) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío.");
        }

        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new RuntimeException("Aula no encontrada"));
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!aula.getVoluntario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Solo el voluntario del aula puede subir recursos.");
        }

        String nombreOriginal = file.getOriginalFilename();
        if (nombreOriginal == null || nombreOriginal.isBlank()) {
            throw new RuntimeException("Nombre de archivo inválido.");
        }
        nombreOriginal = Paths.get(nombreOriginal).getFileName().toString();

        String extension = extraerExtension(nombreOriginal).toLowerCase(Locale.ROOT);
        if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
            throw new RuntimeException("Tipo de archivo no permitido: ." + extension);
        }

        String mimeType = file.getContentType();
        String tipo = MIME_TO_TIPO.getOrDefault(mimeType, deducirTipoPorExtension(extension));

        Path baseDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path aulaDir = baseDir.resolve("aula-" + aulaId).normalize();
        if (!aulaDir.startsWith(baseDir)) {
            throw new RuntimeException("Ruta de almacenamiento inválida.");
        }
        Files.createDirectories(aulaDir);

        String nombreSeguro = UUID.randomUUID().toString() + "." + extension;
        Path destino = aulaDir.resolve(nombreSeguro).normalize();
        if (!destino.startsWith(aulaDir)) {
            throw new RuntimeException("Ruta de destino inválida.");
        }

        try (var in = file.getInputStream()) {
            Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
        }

        RecursoAula recurso = new RecursoAula();
        recurso.setAula(aula);
        recurso.setVoluntarioUsername(username);
        recurso.setNombreOriginal(nombreOriginal);
        recurso.setDescripcion(descripcion != null && !descripcion.isBlank() ? descripcion.trim() : null);
        recurso.setTipo(tipo);
        recurso.setMimeType(mimeType);
        recurso.setRutaRelativa("aula-" + aulaId + "/" + nombreSeguro);
        recurso.setTamanoBytes(file.getSize());
        return recursoRepository.save(recurso);
    }

    @Transactional
    public void eliminarRecurso(Long recursoId, String username) throws IOException {
        RecursoAula recurso = recursoRepository.findById(recursoId)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!recurso.getAula().getVoluntario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Solo el voluntario del aula puede eliminar este recurso.");
        }

        Path baseDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path archivo = baseDir.resolve(recurso.getRutaRelativa()).normalize();
        if (archivo.startsWith(baseDir)) {
            Files.deleteIfExists(archivo);
        }

        recursoRepository.delete(recurso);
    }

    public RecursoAcceso cargarParaUsuario(Long recursoId, String username) throws IOException {
        RecursoAula recurso = recursoRepository.findById(recursoId)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));

        if (!tieneAcceso(recurso, username)) {
            throw new RuntimeException("Sin acceso a este recurso.");
        }

        Path baseDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path archivo = baseDir.resolve(recurso.getRutaRelativa()).normalize();
        if (!archivo.startsWith(baseDir) || !Files.exists(archivo)) {
            throw new RuntimeException("Archivo no encontrado en disco.");
        }

        Resource resource = new UrlResource(archivo.toUri());
        return new RecursoAcceso(resource, recurso.getMimeType(), recurso.getNombreOriginal());
    }

    private boolean tieneAcceso(RecursoAula recurso, String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isEmpty()) return false;
        Usuario usuario = usuarioOpt.get();

        if (recurso.getAula().getVoluntario().getId().equals(usuario.getId())) {
            return true;
        }
        Optional<MiembroAula> miembro = miembroAulaRepository.findByAulaIdAndUsuarioId(
                recurso.getAula().getId(), usuario.getId());
        return miembro.isPresent();
    }

    private String extraerExtension(String nombre) {
        int i = nombre.lastIndexOf('.');
        return (i >= 0 && i < nombre.length() - 1) ? nombre.substring(i + 1) : "";
    }

    private String deducirTipoPorExtension(String ext) {
        return switch (ext) {
            case "pdf" -> "PDF";
            case "png", "jpg", "jpeg", "webp" -> "IMAGEN";
            case "doc", "docx", "xls", "xlsx", "ppt", "pptx" -> "OFFICE";
            default -> "OTRO";
        };
    }

    public record RecursoAcceso(Resource resource, String mimeType, String nombreOriginal) {}
}
