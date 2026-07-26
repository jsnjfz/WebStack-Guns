package com.jsnjfz.manage.core.common.exception;

/**
 * 上传内容不是受支持的安全图片。
 */
public class InvalidUploadException extends RuntimeException {

    public InvalidUploadException() {
        super("上传图片格式或内容不合法");
    }
}
