package com.aubotmationlab.be.service;

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

    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // 파일 확장자 검증
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }

        // 고유한 파일명 생성
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // 디렉토리 생성
        Path uploadPath = Paths.get(uploadDir, subDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 저장
        Path targetLocation = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 상대 경로 반환 (예: templates/glb/uuid.glb)
        return Paths.get(subDirectory, uniqueFilename).toString().replace("\\", "/");
    }

    public void deleteFile(String filePath) throws IOException {
        if (filePath != null && !filePath.isEmpty()) {
            Path fullPath = Paths.get(uploadDir, filePath);
            if (Files.exists(fullPath)) {
                Files.delete(fullPath);
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

    // 파일 타입별 디렉토리 반환
    public String getGlbDirectory() {
        return "templates/glb";
    }

    public String getThumbnailDirectory() {
        return "templates/thumbnails";
    }

    public String getLodDirectory() {
        return "templates/lod";
    }
}
