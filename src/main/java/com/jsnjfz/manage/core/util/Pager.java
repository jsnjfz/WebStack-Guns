package com.jsnjfz.manage.core.util;

import java.util.List;

/**
 * 用于分页的工具类
 */
public class Pager<T> {

    /** 对象记录结果集 */
    private List<T> list;

    /** 总记录数 */
    private Integer total = 0;

    /** 每页显示记录数 */
    private Integer limit = 10;

    /**
     * 总页数
     */
    private Integer pages = 1;

    /**
     * 当前页
     */
    private Integer pageNumber = 1;

    /** 是否为第一页 */
    private boolean isFirstPage = false;

    /** 是否为最后一页 */
    private boolean isLastPage = false;

    /** 是否有前一页 */
    private boolean hasPreviousPage = false;

    /** 是否有下一页 */
    private boolean hasNextPage = false;

    /**
     * 导航页码数
     */
    private Integer navigatePages = 8;

    /**
     * 所有导航页号
     */
    private Integer[] navigatePageNumbers;

    /**
     * 起始值
     */
    private Integer offset = 0;

    /**
     * 分页处理
     * 
     * @param total 总记录数
     * @param pageNumber 当前页
     */
    public Pager(Integer total, Integer pageNumber) {
        init(total, pageNumber, limit);
    }

    /**
     * 分页处理
     * 
     * @param total 总记录数
     * @param pageNumber 当前页
     * @param limit 每页显示记录数
     */
    public Pager(Integer total, Integer pageNumber, Integer limit) {
        init(total, pageNumber, limit);
    }

    private void init(Integer total, Integer pageNumber, Integer limit) {
        limit = (null == limit) ? this.limit : limit;
        // 设置基本参数
        this.total = total;
        this.limit = limit;
        this.pages = (this.total - 1) / this.limit + 1;

        // 计算偏移量
        calcOffSet(pageNumber, limit);

        // 基本参数设定之后进行导航页面的计算
        calcNavigatePageNumbers();

        // 以及页面边界的判定
        judgePageBoudary();
    }

    /**
     * 计算导航页
     */
    private void calcNavigatePageNumbers() {
        // 当总页数小于或等于导航页码数时
        if (pages <= navigatePages) {
            navigatePageNumbers = new Integer[pages];
            for (int i = 0; i < pages; i++) {
                navigatePageNumbers[i] = i + 1;
            }
        } else { // 当总页数大于导航页码数时
            navigatePageNumbers = new Integer[navigatePages];
            int startNum = pageNumber - navigatePages / 2;
            int endNum = pageNumber + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                // (最前navPageCount页
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNumbers[i] = startNum++;
                }
            } else if (endNum > pages) {
                endNum = pages;
                // 最后navPageCount页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatePageNumbers[i] = endNum--;
                }
            } else {
                // 所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNumbers[i] = startNum++;
                }
            }
        }
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        isFirstPage = pageNumber == 1;
        isLastPage = pageNumber == pages && pageNumber != 1;
        hasPreviousPage = pageNumber != 1;
        hasNextPage = pageNumber != pages;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    /**
     * 得到当前页的内容
     * 
     * @return {List}
     */
    public List<T> getList() {
        return list;
    }

    /**
     * 计算偏移量，每页起始值
     * 
     * @return {List}
     */
    public void calcOffSet(Integer pageNumber, Integer limit) {
        // 根据输入可能错误的当前号码进行自动纠正
        if (null == pageNumber || pageNumber < 1) {
            this.pageNumber = 1;
        } else if (pageNumber > this.pages) {
            this.pageNumber = this.pages;
        } else {
            this.pageNumber = pageNumber;
        }
        if(this.pageNumber == 0){
            this.pageNumber = 1;
        }
        this.offset = (this.pageNumber - 1) * limit;
    }

    /**
     * 得到记录总数
     * 
     * @return {Integer}
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * 得到每页显示多少条记录
     * 
     * @return {Integer}
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * 得到页面总数
     * 
     * @return {Integer}
     */
    public Integer getPages() {
        return pages;
    }

    /**
     * 得到当前页号
     * 
     * @return {Integer}
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * 得到所有导航页号
     * 
     * @return {Integer[]}
     */
    public Integer[] getNavigatePageNumbers() {
        return navigatePageNumbers;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public boolean hasPreviousPage() {
        return hasPreviousPage;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public Integer getOffset() {
        return offset;
    }

    public String toString() {
        String str = new String();
        str = "[" + "total=" + total + ",pages=" + pages + ",pageNumber=" + pageNumber + ",limit="
                + limit
                +
                // ",navigatePages="+navigatePages+
                ",isFirstPage=" + isFirstPage + ",isLastPage=" + isLastPage + ",hasPreviousPage="
                + hasPreviousPage + ",hasNextPage=" + hasNextPage + ",navigatePageNumbers=";
        int len = navigatePageNumbers.length;
        if (len > 0)
            str += (navigatePageNumbers[0]);
        for (int i = 1; i < len; i++) {
            str += (" " + navigatePageNumbers[i]);
        }
        // sb+=",list="+list;
        str += "]";
        return str;
    }
}
