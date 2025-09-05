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

        // 파일 확장자 검증
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }

        // 템플릿 이름으로 폴더명 생성 (특수문자 제거)
        String safeTemplateName = sanitizeFolderName(templateName);
        
        // 파일 확장자 가져오기
        String fileExtension = getFileExtension(originalFilename);
        
        // 파일명 생성 (템플릿이름_파일타입 + 확장자)
        String filename = safeTemplateName + "_" + fileType + fileExtension;

        // 디렉토리 생성: objectTemplate/templates/{templateName}/
        Path templateDir = Paths.get(uploadDir, "templates", safeTemplateName);
        if (!Files.exists(templateDir)) {
            Files.createDirectories(templateDir);
        }

        // 파일 저장
        Path targetLocation = templateDir.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 상대 경로 반환 (예: templates/templateName/glb.glb)
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
                // 폴더 내 모든 파일 삭제
                Files.walk(templateDir)
                    .sorted((a, b) -> b.compareTo(a)) // 역순 정렬 (파일 먼저, 폴더 나중에)
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
        
        // 특수문자 제거 및 안전한 폴더명 생성
        return templateName
                .replaceAll("[^a-zA-Z0-9가-힣_-]", "_") // 영문, 숫자, 한글, 언더스코어, 하이픈만 허용
                .replaceAll("_{2,}", "_") // 연속된 언더스코어를 하나로
                .replaceAll("^_|_$", "") // 앞뒤 언더스코어 제거
                .toLowerCase(); // 소문자로 변환
    }


}
