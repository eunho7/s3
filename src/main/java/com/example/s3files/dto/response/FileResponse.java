package com.example.s3files.dto.response;

import com.example.s3files.entity.FileAttachment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FileResponse {

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

    public static FileResponse from(FileAttachment attachment) {
        return FileResponse.builder()
                .fileSeq(attachment.getFileSeq())
                .originalFileName(attachment.getOriginalFileName())
                .storedFileName(attachment.getStoredFileName())
                .s3Key(attachment.getS3Key())
                .contentType(attachment.getContentType())
                .fileSize(attachment.getFileSize())
                .useYn(attachment.getUseYn())
                .createUser(attachment.getCreateUser())
                .createDttm(attachment.getCreateDttm())
                .updateUser(attachment.getUpdateUser())
                .updateDttm(attachment.getUpdateDttm())
                .build();
    }
}
