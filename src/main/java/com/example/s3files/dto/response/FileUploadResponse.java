package com.example.s3files.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileUploadResponse {

    private Long fileSeq;
    private String originalFileName;
    private String storedFileName;
    private String s3Key;
}
