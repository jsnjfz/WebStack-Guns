package com.jsnjfz.manage.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.jsnjfz.manage.core.common.exception.BizExceptionEnum;
import com.jsnjfz.manage.core.common.node.ZTreeNode;
import com.jsnjfz.manage.modular.system.model.Category;
import com.jsnjfz.manage.modular.system.service.impl.CategoryServiceImpl;
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
 * @Date 2019/7/21 14:29
 * 分类控制器
 */
@Controller
@RequestMapping("category")
public class CategoryController extends BaseController {

    private static String PREFIX = "/system/site/";

    @Autowired
    private CategoryServiceImpl categoryService;

    /**
     * 跳转到菜单列表列表页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "category.html";
    }


    /**
     * 获取分类列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String title) {
        Map map = new HashMap();
        map.put("title", title);
        List<Map<String, Object>> mapList = categoryService.getCatogry(map);
        return super.warpObject(new BaseControllerWrapper(mapList) {
            @Override
            protected void wrapTheMap(Map<String, Object> map) {

            }
        });
    }

    /**
     * 跳转到添加分类
     */
    @RequestMapping("/category_add")
    public String categoryAdd() {
        return PREFIX + "category_add.html";
    }

    /**
     * 跳转到修改分类
     */
    @RequestMapping("/category_update/{id}")
    public String categoryUpdate(@PathVariable Integer id, Model model) {
        Category category = categoryService.get(id);
        model.addAttribute(category);
        int pId = category.getParentId();
        if (pId == 0) {
            model.addAttribute("pName", "顶级");
            return PREFIX + "category_edit.html";
        }
        model.addAttribute("pName", categoryService.get(pId).getTitle());
        return PREFIX + "category_edit.html";
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
     * 新增分类
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Category category) {
        int level = category.getParentId() == 0 ? 0 : categoryService.get(category.getParentId()).getLevels();
        category.setLevels(level + 1);
        categoryService.saveOrUpdate(category, "");
        return SUCCESS_TIP;
    }


    /**
     * 修改分类
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Category category) {
        if (ToolUtil.isEmpty(category) || category.getId() == null) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        int level = category.getParentId() == 0 ? 0 : categoryService.get(category.getParentId()).getLevels();
        category.setLevels(level + 1);
        categoryService.saveOrUpdate(category, category.getId());
        return SUCCESS_TIP;
    }

    /**
     * 删除分类
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer id) {
        categoryService.delete(id);

        return SUCCESS_TIP;
    }


}
