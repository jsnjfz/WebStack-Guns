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
package com.jsnjfz.manage.core.common.constant.factory;

import cn.stylefeng.roses.core.util.HttpContext;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsnjfz.manage.core.common.constant.state.Order;

import javax.servlet.http.HttpServletRequest;

/**
 * BootStrap Table默认的分页参数创建
 *
 * @author fengshuonan
 * @date 2017-04-05 22:25
 */
public class PageFactory<T> {

    static final int DEFAULT_LIMIT = 20;
    static final int MAX_LIMIT = 200;
    static final int MAX_OFFSET = 1_000_000;

    /**
     * 默认禁止客户端指定排序字段。确需排序的调用方必须显式传入白名单。
     */
    public Page<T> defaultPage(String... allowedSortFields) {
        return createPage(HttpContext.getRequest(), allowedSortFields);
    }

    Page<T> createPage(HttpServletRequest request, String... allowedSortFields) {
        int limit = parseBoundedInteger(request.getParameter("limit"), DEFAULT_LIMIT, 1, MAX_LIMIT);
        int offset = parseBoundedInteger(request.getParameter("offset"), 0, 0, MAX_OFFSET);
        String sort = request.getParameter("sort");

        if (!isAllowedSortField(sort, allowedSortFields)) {
            Page<T> page = new Page<>((offset / limit) + 1, limit);
            page.setOpenSort(false);
            return page;
        }

        Page<T> page = new Page<>((offset / limit) + 1, limit, sort);
        page.setAsc(Order.ASC.getDes().equalsIgnoreCase(request.getParameter("order")));
        return page;
    }

    private boolean isAllowedSortField(String sort, String... allowedSortFields) {
        if (sort == null || sort.isEmpty() || allowedSortFields == null) {
            return false;
        }
        for (String allowedSortField : allowedSortFields) {
            if (sort.equals(allowedSortField)) {
                return true;
            }
        }
        return false;
    }

    private int parseBoundedInteger(String value, int defaultValue, int minimum, int maximum) {
        if (value == null) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(value);
            return Math.max(minimum, Math.min(parsed, maximum));
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }
}
