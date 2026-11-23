package cl.mpazpro.contenidos.service;

import cl.mpazpro.contenidos.model.EstadoContenido;
import cl.mpazpro.contenidos.repository.ContenidoDidacticoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecursoService {

    private static final Set<String> EXTENSIONES = Set.of(
            "png", "jpg", "jpeg", "webp",
            "mp3", "m4a", "wav", "ogg"
    );

    private final ContenidoDidacticoRepository contenidoRepository;

    @Value("${mpaz.recursos.directorio:uploads}")
    private String directorio;

    public Map<String, String> subir(MultipartFile archivo) {

        if (archivo.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Archivo vacio"
            );
        }

        if (archivo.getSize() > 10 * 1024 * 1024) {
            throw new ResponseStatusException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    "El archivo supera los 10 MB"
            );
        }

        String original = archivo.getOriginalFilename();

        if (original == null || !original.contains(".")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nombre de archivo invalido"
            );
        }

        String extension = original.substring(
                original.lastIndexOf('.') + 1
        ).toLowerCase(java.util.Locale.ROOT);

        if (!EXTENSIONES.contains(extension)) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "Formato no permitido"
            );
        }

        try {
            byte[] cabecera;
            try (InputStream entrada = archivo.getInputStream()) {
                cabecera = entrada.readNBytes(12);
            }

            if (!firmaValida(extension, cabecera)) {
                throw new ResponseStatusException(
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                        "El contenido del archivo no coincide con su formato"
                );
            }

            Path carpeta = Path.of(directorio)
                    .toAbsolutePath().normalize();

            Files.createDirectories(carpeta);

            String nombre = UUID.randomUUID() + "." + extension;
            Path destino = carpeta.resolve(nombre);

            try (InputStream entrada = archivo.getInputStream()) {
                Files.copy(entrada, destino);
            }

            return Map.of(
                    "url", "/api/v1/recursos/" + nombre,
                    "tipo", esImagen(extension) ? "IMAGEN" : "AUDIO"
            );

        } catch (IOException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo guardar el archivo", ex
            );
        }
    }

    @Transactional(readOnly = true)
    public Resource obtener(String nombre, boolean esUtp) {

        if (!nombre.matches(
                "[a-f0-9-]{36}\\.(png|jpg|jpeg|webp|mp3|m4a|wav|ogg)"
        )) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Recurso no encontrado"
            );
        }

        String url = "/api/v1/recursos/" + nombre;

        if (!esUtp) {
            boolean publicado = contenidoRepository.findAll().stream()
                    .anyMatch(c ->
                            c.getEstado() == EstadoContenido.PUBLICADO
                            && c.getObjetivoAprendizaje().getEstado()
                                    == EstadoContenido.PUBLICADO
                            && c.getObjetivoAprendizaje().getUnidad()
                                    .getEstado() == EstadoContenido.PUBLICADO
                            && (url.equals(c.getArchivoAudio())
                                || c.getImagenes().contains(url))
                    );

            if (!publicado) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Recurso no disponible"
                );
            }
        }

        Path archivo = Path.of(directorio)
                .toAbsolutePath().normalize()
                .resolve(nombre);

        if (!Files.isRegularFile(archivo)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Recurso no encontrado"
            );
        }

        return new FileSystemResource(archivo);
    }

    public String tipoContenido(String nombre) {
        String extension = nombre.substring(nombre.lastIndexOf('.') + 1);

        return switch (extension) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "webp" -> "image/webp";
            case "mp3" -> "audio/mpeg";
            case "m4a" -> "audio/mp4";
            case "wav" -> "audio/wav";
            case "ogg" -> "audio/ogg";
            default -> "application/octet-stream";
        };
    }

    private boolean esImagen(String extension) {
        return Set.of("png", "jpg", "jpeg", "webp")
                .contains(extension);
    }

    private boolean firmaValida(String extension, byte[] b) {
        if (b.length < 12) {
            return false;
        }

        return switch (extension) {
            case "png" ->
                    (b[0] & 255) == 137
                    && b[1] == 80 && b[2] == 78 && b[3] == 71;

            case "jpg", "jpeg" ->
                    (b[0] & 255) == 255
                    && (b[1] & 255) == 216
                    && (b[2] & 255) == 255;

            case "webp" ->
                    texto(b, 0, 4).equals("RIFF")
                    && texto(b, 8, 4).equals("WEBP");

            case "mp3" ->
                    texto(b, 0, 3).equals("ID3")
                    || ((b[0] & 255) == 255
                        && ((b[1] & 224) == 224));

            case "m4a" -> texto(b, 4, 4).equals("ftyp");

            case "wav" ->
                    texto(b, 0, 4).equals("RIFF")
                    && texto(b, 8, 4).equals("WAVE");

            case "ogg" -> texto(b, 0, 4).equals("OggS");

            default -> false;
        };
    }

    private String texto(byte[] bytes, int inicio, int largo) {
        return new String(
                bytes, inicio, largo, StandardCharsets.US_ASCII
        );
    }
}
