package com.jsnjfz.manage.core.util;

import com.jsnjfz.manage.modular.system.VO.ResultVO;

/**
 * @Author fz
 * @Date 2018-04-18 10:57
 * 
 */

public class ResultVOUtil {

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }


    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }



}
