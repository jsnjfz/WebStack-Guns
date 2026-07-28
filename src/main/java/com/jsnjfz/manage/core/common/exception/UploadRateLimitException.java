package com.jsnjfz.manage.core.common.exception;

/**
 * 当前账号或来源超过图片上传额度。
 */
public class UploadRateLimitException extends RuntimeException {

    public UploadRateLimitException() {
        super("图片上传过于频繁，请稍后再试");
    }
}
