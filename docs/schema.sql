CREATE TABLE IF NOT EXISTS tb_file_attachment (
    file_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT '파일 일련번호',
    original_file_name VARCHAR(255) NOT NULL COMMENT '원본 파일명',
    stored_file_name VARCHAR(255) NOT NULL COMMENT '저장 파일명(UUID)',
    s3_key VARCHAR(500) NOT NULL COMMENT 'S3 Object Key',
    content_type VARCHAR(100) NULL COMMENT 'MIME 타입',
    file_size BIGINT NOT NULL COMMENT '파일 크기(bytes)',
    use_yn CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '사용 여부',
    create_user VARCHAR(100) NOT NULL COMMENT '등록자',
    create_dttm DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    update_user VARCHAR(100) NOT NULL COMMENT '수정자',
    update_dttm DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (file_seq),
    INDEX idx_tb_file_attachment_use_yn (use_yn),
    INDEX idx_tb_file_attachment_create_dttm (create_dttm)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='파일 첨부 메타데이터';
