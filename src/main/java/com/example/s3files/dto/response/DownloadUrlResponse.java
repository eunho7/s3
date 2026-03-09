package com.example.s3files.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DownloadUrlResponse {

    private String downloadUrl;
    private Long expiresInMinutes;
}
