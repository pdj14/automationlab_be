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

        // File name validation
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }

        // Create folder using template name (remove special characters)
        String safeTemplateName = sanitizeFolderName(templateName);
        
        // Get file extension
        String fileExtension = getFileExtension(originalFilename);
        
        // Create filename (template name + file type + extension)
        String filename = safeTemplateName + "_" + fileType + fileExtension;

        // Create directory: objectTemplate/templates/{templateName}/
        Path templateDir = Paths.get(uploadDir, "templates", safeTemplateName);
        if (!Files.exists(templateDir)) {
            Files.createDirectories(templateDir);
        }

        // Save file
        Path targetLocation = templateDir.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Return relative path (e.g., templates/templateName/glb.glb)
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
                // Delete all files in folder recursively
                Files.walk(templateDir)
                    .sorted((a, b) -> b.compareTo(a)) // Reverse order (files first, folders last)
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
        
        // Remove special characters and create safe folder name
        return templateName
                .replaceAll("[^a-zA-Z0-9가-힣-]", "_") // Only allow letters, numbers, Korean, hyphens
                .replaceAll("_{2,}", "_") // Replace multiple underscores with single
                .replaceAll("^_|_$", "") // Remove leading/trailing underscores
                .toLowerCase(); // Convert to lowercase
    }

}