package com.jsnjfz.manage.modular.system.model;

import lombok.Data;

import java.util.List;

/**
 * @Author jsnjfz
 * @Date 2019/7/21 14:44
 * 网站分类表
 */
@Data
public class Category {
    private Integer id;

    private Integer parentId;

    private Integer sort;

    private String title;

    private String icon;

    private Integer levels;

    private List<Site> sites;

    private String createTime;

    private String updateTime;


}
