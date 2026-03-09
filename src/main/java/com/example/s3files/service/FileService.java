package com.example.s3files.service;

import com.example.s3files.dto.request.FileListRequest;
import com.example.s3files.dto.request.FileMetadataUpdateRequest;
import com.example.s3files.dto.response.DownloadUrlResponse;
import com.example.s3files.dto.response.FileResponse;
import com.example.s3files.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    FileUploadResponse uploadFile(MultipartFile file, String createUser);

    FileResponse getFile(Long fileSeq);

    List<FileResponse> getFiles(FileListRequest request);

    FileResponse updateMetadata(Long fileSeq, FileMetadataUpdateRequest request);

    void deleteFile(Long fileSeq, String updateUser);

    DownloadUrlResponse getDownloadUrl(Long fileSeq);
}
