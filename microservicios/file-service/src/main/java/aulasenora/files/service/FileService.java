package aulasenora.files.service;

import aulasenora.files.model.Archivo;
import aulasenora.files.repository.ArchivoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    private final ArchivoRepository archivoRepository;
    private final Path uploadDir;

    public FileService(ArchivoRepository archivoRepository,
                       @Value("${app.upload.dir:uploads}") String uploadDir) {
        this.archivoRepository = archivoRepository;
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de subida: " + this.uploadDir, e);
        }
    }

    @Transactional
    public Archivo guardarArchivo(MultipartFile file, String tipo, String uploadedBy) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo esta vacio");
        }

        validarTipo(tipo);

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            originalFilename = "sin_nombre";
        }

        String uuid = UUID.randomUUID().toString();
        String extension = "";
        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String storageName = uuid + extension;

        Path targetPath = uploadDir.resolve(storageName);
        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo en disco", e);
        }

        Archivo archivo = new Archivo();
        archivo.setUuid(uuid);
        archivo.setNombreOriginal(originalFilename);
        archivo.setMimeType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");
        archivo.setTamanoBytes(file.getSize());
        archivo.setRutaRelativa(storageName);
        archivo.setTipo(tipo);
        archivo.setUploadedBy(uploadedBy);

        return archivoRepository.save(archivo);
    }

    public Optional<Archivo> obtenerPorUuid(String uuid) {
        return archivoRepository.findByUuid(uuid);
    }

    public Resource cargarComoResource(Archivo archivo) {
        Path filePath = uploadDir.resolve(archivo.getRutaRelativa()).normalize();
        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Archivo no encontrado en disco: " + archivo.getRutaRelativa());
        }
        return resource;
    }

    @Transactional
    public void eliminar(String uuid) {
        Archivo archivo = archivoRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Archivo no encontrado: " + uuid));

        try {
            Path filePath = uploadDir.resolve(archivo.getRutaRelativa()).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo del disco", e);
        }

        archivoRepository.delete(archivo);
    }

    private void validarTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de archivo es requerido");
        }
        String t = tipo.toUpperCase();
        if (!t.equals("RECURSO") && !t.equals("AVATAR") && !t.equals("BANNER") && !t.equals("DIPLOMA")) {
            throw new IllegalArgumentException("Tipo de archivo invalido: " + tipo);
        }
    }
}
