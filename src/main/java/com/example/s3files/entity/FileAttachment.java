package com.example.s3files.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FileAttachment {

    private Long fileSeq;
    private String originalFileName;
    private String storedFileName;
    private String s3Key;
    private String contentType;
    private Long fileSize;
    private String useYn;
    private String createUser;
    private LocalDateTime createDttm;
    private String updateUser;
    private LocalDateTime updateDttm;
}
