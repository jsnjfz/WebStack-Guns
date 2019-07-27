package com.jsnjfz.manage.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsnjfz.manage.core.common.constant.factory.PageFactory;
import com.jsnjfz.manage.core.common.exception.BizExceptionEnum;
import com.jsnjfz.manage.core.common.node.ZTreeNode;
import com.jsnjfz.manage.core.common.page.PageInfoBT;
import com.jsnjfz.manage.core.util.Pager;
import com.jsnjfz.manage.modular.system.model.Category;
import com.jsnjfz.manage.modular.system.model.Site;
import com.jsnjfz.manage.modular.system.service.impl.CategoryServiceImpl;
import com.jsnjfz.manage.modular.system.service.impl.SiteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author fz
 * @Date 2019/7/23 14:29
 * 网站控制器
 */
@Controller
@RequestMapping("site")
public class SiteController extends BaseController {

    private static String PREFIX = "/system/site/";

    @Autowired
    private SiteServiceImpl siteService;

    @Autowired
    private CategoryServiceImpl categoryService;

    /**
     * 跳转到菜单列表列表页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "site.html";
    }


    /**
     * 获取网站列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String title) {
        Map map = new HashMap();
        map.put("title",title);
        Page<Site> page = new PageFactory<Site>().defaultPage();
        Pager<Site> pages = siteService.getPage(map, page.getOffset(), page.getLimit());
        page.setTotal(pages.getTotal());
        page.setRecords(pages.getList());
        return new PageInfoBT<>(page);
    }

    /**
     * 跳转到添加网站
     */
    @RequestMapping("/site_add")
    public String siteAdd() {
        return PREFIX + "site_add.html";
    }

    /**
     * 跳转到修改网站
     */
    @RequestMapping("/site_update/{id}")
    public String siteUpdate(@PathVariable Integer id, Model model) {
        Site site = siteService.get(id);
        model.addAttribute(site);
        return PREFIX + "site_edit.html";
    }

    /**
     * 获取分类的tree列表
     */
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = categoryService.tree();
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 新增网站
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Site site) {
        siteService.saveOrUpdate(site,"");
        return SUCCESS_TIP;
    }


    /**
     * 修改网站
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Site site) {
        if (ToolUtil.isEmpty(site) || site.getId() == null) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        siteService.saveOrUpdate(site,site.getId());
        return SUCCESS_TIP;
    }

    /**
     * 删除网站
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer id) {
        siteService.delete(id);
        return SUCCESS_TIP;
    }


}
