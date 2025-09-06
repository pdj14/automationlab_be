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

        // ?�일 ?�장??검�?
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }

        // ?�플�??�름?�로 ?�더�??�성 (?�수문자 ?�거)
        String safeTemplateName = sanitizeFolderName(templateName);
        
        // ?�일 ?�장??가?�오�?
        String fileExtension = getFileExtension(originalFilename);
        
        // ?�일�??�성 (?�플릿이�??�일?�??+ ?�장??
        String filename = safeTemplateName + "_" + fileType + fileExtension;

        // ?�렉?�리 ?�성: objectTemplate/templates/{templateName}/
        Path templateDir = Paths.get(uploadDir, "templates", safeTemplateName);
        if (!Files.exists(templateDir)) {
            Files.createDirectories(templateDir);
        }

        // ?�일 ?�??
        Path targetLocation = templateDir.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // ?��? 경로 반환 (?? templates/templateName/glb.glb)
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
                // ?�더 ??모든 ?�일 ??��
                Files.walk(templateDir)
                    .sorted((a, b) -> b.compareTo(a)) // ??�� ?�렬 (?�일 먼�?, ?�더 ?�중??
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
        
        // ?�수문자 ?�거 �??�전???�더�??�성
        return templateName
                .replaceAll("[^a-zA-Z0-9가-??-]", "_") // ?�문, ?�자, ?��?, ?�더?�코?? ?�이?�만 ?�용
                .replaceAll("_{2,}", "_") // ?�속???�더?�코?��? ?�나�?
                .replaceAll("^_|_$", "") // ?�뒤 ?�더?�코???�거
                .toLowerCase(); // ?�문?�로 변??
    }


}
