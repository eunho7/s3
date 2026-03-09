package com.example.s3files.mapper;

import com.example.s3files.entity.FileAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileAttachmentMapper {

    int insert(FileAttachment fileAttachment);

    FileAttachment findByFileSeq(@Param("fileSeq") Long fileSeq);

    List<FileAttachment> findAll(@Param("useYn") String useYn, @Param("keyword") String keyword);

    int updateMetadata(FileAttachment fileAttachment);

    int deleteByFileSeq(@Param("fileSeq") Long fileSeq, @Param("updateUser") String updateUser);
}
