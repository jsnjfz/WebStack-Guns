package com.jsnjfz.manage.modular.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class BaseController {

    public HttpServletRequest request;

    public HttpServletResponse response;


    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @ModelAttribute("url")
    public String baseURL(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + request.getContextPath() + "/";
    }

    public static final String PRODUCES = "application/json; charset=utf-8";


    /**
     * 向客户端返回指定json字符串
     *
     * @param json     json字符串
     * @param response
     */
    protected void printJson(String json, HttpServletResponse response) {
        if (response == null) {
            return;
        }
        try {
            response.setContentType("text/html;charset=UTF-8");
            if (json == null) {
                response.getWriter().write("{}");
            } else {
                response.getWriter().write(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
