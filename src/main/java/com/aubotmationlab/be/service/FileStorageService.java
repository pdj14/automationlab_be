package com.automationlab.be.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:objectTemplate}")
    public String uploadDir;

    public String storeFile(MultipartFile file, String templateName, String fileType) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // ?Œì¼ ?•ì¥??ê²€ì¦?
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }

        // ?œí”Œë¦??´ë¦„?¼ë¡œ ?´ë”ëª??ì„± (?¹ìˆ˜ë¬¸ì ?œê±°)
        String safeTemplateName = sanitizeFolderName(templateName);
        
        // ?Œì¼ ?•ì¥??ê°€?¸ì˜¤ê¸?
        String fileExtension = getFileExtension(originalFilename);
        
        // ?Œì¼ëª??ì„± (?œí”Œë¦¿ì´ë¦??Œì¼?€??+ ?•ì¥??
        String filename = safeTemplateName + "_" + fileType + fileExtension;

        // ?”ë ‰? ë¦¬ ?ì„±: objectTemplate/templates/{templateName}/
        Path templateDir = Paths.get(uploadDir, "templates", safeTemplateName);
        if (!Files.exists(templateDir)) {
            Files.createDirectories(templateDir);
        }

        // ?Œì¼ ?€??
        Path targetLocation = templateDir.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // ?ë? ê²½ë¡œ ë°˜í™˜ (?? templates/templateName/glb.glb)
        return Paths.get("templates", safeTemplateName, filename).toString().replace("\\", "/");
    }

    public void deleteFile(String filePath) throws IOException {
        if (filePath != null && !filePath.isEmpty()) {
            Path fullPath = Paths.get(uploadDir, filePath);
            if (Files.exists(fullPath)) {
                Files.delete(fullPath);
            }
        }
    }

    public void deleteTemplateFolder(String templateName) throws IOException {
        if (templateName != null && !templateName.isEmpty()) {
            String safeTemplateName = sanitizeFolderName(templateName);
            Path templateDir = Paths.get(uploadDir, "templates", safeTemplateName);
            if (Files.exists(templateDir)) {
                // ?´ë” ??ëª¨ë“  ?Œì¼ ?? œ
                Files.walk(templateDir)
                    .sorted((a, b) -> b.compareTo(a)) // ??ˆœ ?•ë ¬ (?Œì¼ ë¨¼ì?, ?´ë” ?˜ì¤‘??
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete file: " + path, e);
                        }
                    });
            }
        }
    }

    public boolean fileExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        Path fullPath = Paths.get(uploadDir, filePath);
        return Files.exists(fullPath);
    }

    public Path getFilePath(String filePath) {
        return Paths.get(uploadDir, filePath);
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex);
    }

    private String sanitizeFolderName(String templateName) {
        if (templateName == null || templateName.isEmpty()) {
            throw new IllegalArgumentException("Template name cannot be null or empty");
        }
        
        // ?¹ìˆ˜ë¬¸ì ?œê±° ë°??ˆì „???´ë”ëª??ì„±
        return templateName
                .replaceAll("[^a-zA-Z0-9ê°€-??-]", "_") // ?ë¬¸, ?«ì, ?œê?, ?¸ë”?¤ì½”?? ?˜ì´?ˆë§Œ ?ˆìš©
                .replaceAll("_{2,}", "_") // ?°ì†???¸ë”?¤ì½”?´ë? ?˜ë‚˜ë¡?
                .replaceAll("^_|_$", "") // ?ë’¤ ?¸ë”?¤ì½”???œê±°
                .toLowerCase(); // ?Œë¬¸?ë¡œ ë³€??
    }


}
