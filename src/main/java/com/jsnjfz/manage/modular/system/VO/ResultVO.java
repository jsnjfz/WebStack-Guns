package com.jsnjfz.manage.modular.system.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author jsnjfz
 * @Date 2019-07-22 14:17
 * 
 */
@Data
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 3236329195874147801L;
    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;
}
