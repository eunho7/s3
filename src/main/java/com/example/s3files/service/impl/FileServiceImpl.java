package com.example.s3files.service.impl;

import com.example.s3files.config.S3Properties;
import com.example.s3files.dto.request.FileListRequest;
import com.example.s3files.dto.request.FileMetadataUpdateRequest;
import com.example.s3files.dto.response.DownloadUrlResponse;
import com.example.s3files.dto.response.FileResponse;
import com.example.s3files.dto.response.FileUploadResponse;
import com.example.s3files.entity.FileAttachment;
import com.example.s3files.exception.BusinessException;
import com.example.s3files.mapper.FileAttachmentMapper;
import com.example.s3files.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3Properties s3Properties;
    private final FileAttachmentMapper fileAttachmentMapper;

    @Override
    @Transactional
    public FileUploadResponse uploadFile(MultipartFile file, String createUser) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "업로드할 파일이 없습니다.");
        }
        String originalName = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalName);
        String storedFileName = UUID.randomUUID() + (extension != null ? "." + extension : "");
        String s3Key = "uploads/" + storedFileName;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException ex) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드 중 오류가 발생했습니다.");
        }

        FileAttachment attachment = new FileAttachment();
        attachment.setOriginalFileName(originalName);
        attachment.setStoredFileName(storedFileName);
        attachment.setS3Key(s3Key);
        attachment.setContentType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setUseYn("Y");
        attachment.setCreateUser(createUser);
        attachment.setUpdateUser(createUser);
        attachment.setCreateDttm(LocalDateTime.now());
        attachment.setUpdateDttm(LocalDateTime.now());
        fileAttachmentMapper.insert(attachment);

        return FileUploadResponse.builder()
                .fileSeq(attachment.getFileSeq())
                .originalFileName(attachment.getOriginalFileName())
                .storedFileName(attachment.getStoredFileName())
                .s3Key(attachment.getS3Key())
                .build();
    }

    @Override
    public FileResponse getFile(Long fileSeq) {
        return FileResponse.from(getFileEntity(fileSeq));
    }

    @Override
    public List<FileResponse> getFiles(FileListRequest request) {
        return fileAttachmentMapper.findAll(request.getUseYn(), request.getKeyword()).stream()
                .map(FileResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public FileResponse updateMetadata(Long fileSeq, FileMetadataUpdateRequest request) {
        FileAttachment current = getFileEntity(fileSeq);
        current.setUpdateUser(request.getUpdateUser());
        if (StringUtils.hasText(request.getUseYn())) {
            current.setUseYn(request.getUseYn());
        }
        current.setUpdateDttm(LocalDateTime.now());

        int updated = fileAttachmentMapper.updateMetadata(current);
        if (updated == 0) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "수정할 파일이 없습니다.");
        }
        return FileResponse.from(getFileEntity(fileSeq));
    }

    @Override
    @Transactional
    public void deleteFile(Long fileSeq, String updateUser) {
        FileAttachment attachment = getFileEntity(fileSeq);

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(attachment.getS3Key())
                .build());

        int deleted = fileAttachmentMapper.deleteByFileSeq(fileSeq, updateUser);
        if (deleted == 0) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "삭제할 파일이 없습니다.");
        }
    }

    @Override
    public DownloadUrlResponse getDownloadUrl(Long fileSeq) {
        FileAttachment attachment = getFileEntity(fileSeq);

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(attachment.getS3Key())
                .build();

        long expiresInMinutes = s3Properties.getPresignedUrlDurationMinutes();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expiresInMinutes))
                .getObjectRequest(objectRequest)
                .build();

        String url = s3Presigner.presignGetObject(presignRequest).url().toExternalForm();
        return new DownloadUrlResponse(url, expiresInMinutes);
    }

    private FileAttachment getFileEntity(Long fileSeq) {
        FileAttachment attachment = fileAttachmentMapper.findByFileSeq(fileSeq);
        if (attachment == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다. fileSeq=" + fileSeq);
        }
        return attachment;
    }
}
