/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jsnjfz.manage.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import com.jsnjfz.manage.core.common.node.MenuNode;
import com.jsnjfz.manage.modular.system.model.Category;
import com.jsnjfz.manage.modular.system.service.IOperationLogService;
import com.jsnjfz.manage.modular.system.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;


/**
 * @Author jsnjfz
 * @Date 2019/7/25 21:23
 * 首页控制器
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private CategoryServiceImpl categoryService;

    /**
     * 跳转到首页
     */
    @RequestMapping("/")
    public String index(Model model) {
        List<MenuNode> menus = categoryService.getCatogryNode(new HashMap<>());
        List<MenuNode> titles = MenuNode.buildTitle(menus);
        List<Category> categorySiteList = categoryService.getCatogrySite(null);
        model.addAttribute("categorySiteList", categorySiteList);
        model.addAttribute("titles", titles);
        System.out.println(titles);
        return "/index.html";
    }

    /**
     * 跳转到关于页面
     */
    @RequestMapping("/about")
    public String about(Model model) {
        return "/about.html";
    }

}
