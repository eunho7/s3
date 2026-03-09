package com.example.s3files.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileListRequest {

    private String useYn;
    private String keyword;
}
