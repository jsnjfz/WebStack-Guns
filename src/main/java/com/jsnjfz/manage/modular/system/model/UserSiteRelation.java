package com.jsnjfz.manage.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 
 * @TableName user_site_relation
 */
@TableName(value ="user_site_relation")
@Data
public class UserSiteRelation implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 网站id
     */
    private Integer siteId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}