package com.example.s3files.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileMetadataUpdateRequest {

    @NotBlank
    @Size(max = 100)
    private String updateUser;

    @Size(max = 1)
    private String useYn;
}
