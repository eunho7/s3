package com.example.s3files.controller;

import com.example.s3files.dto.request.FileListRequest;
import com.example.s3files.dto.request.FileMetadataUpdateRequest;
import com.example.s3files.dto.response.DownloadUrlResponse;
import com.example.s3files.dto.response.FileResponse;
import com.example.s3files.dto.response.FileUploadResponse;
import com.example.s3files.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "File API", description = "AWS S3 파일 CRUD API")
public class FileController {

    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "파일 업로드")
    public FileUploadResponse uploadFile(
            @RequestPart("file") MultipartFile file,
            @Parameter(description = "등록 사용자") @RequestPart("createUser") String createUser) {
        return fileService.uploadFile(file, createUser);
    }

    @GetMapping("/{fileSeq}")
    @Operation(summary = "파일 상세 조회")
    public FileResponse getFile(@PathVariable Long fileSeq) {
        return fileService.getFile(fileSeq);
    }

    @GetMapping
    @Operation(summary = "파일 목록 조회")
    public List<FileResponse> getFiles(@ModelAttribute FileListRequest request) {
        return fileService.getFiles(request);
    }

    @PutMapping("/{fileSeq}")
    @Operation(summary = "파일 메타데이터 수정")
    public FileResponse updateMetadata(@PathVariable Long fileSeq,
                                       @Valid @RequestBody FileMetadataUpdateRequest request) {
        return fileService.updateMetadata(fileSeq, request);
    }

    @DeleteMapping("/{fileSeq}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "파일 삭제")
    public void deleteFile(@PathVariable Long fileSeq,
                           @RequestParam(defaultValue = "system") String updateUser) {
        fileService.deleteFile(fileSeq, updateUser);
    }

    @GetMapping("/{fileSeq}/download-url")
    @Operation(summary = "다운로드용 Presigned URL 조회")
    public DownloadUrlResponse getDownloadUrl(@PathVariable Long fileSeq) {
        return fileService.getDownloadUrl(fileSeq);
    }
}
