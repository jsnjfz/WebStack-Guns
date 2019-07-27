/**
 * 网站管理的单例
 */
var Site = {
    id: "siteTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Site.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '分类', field: 'categoryTitle', align: 'center', valign: 'middle', sortable: true},
        {title: '标题', field: 'title', align: 'center', valign: 'middle', sortable: true},
        {title: '图标', field: 'thumb', align: 'center', valign: 'middle', sortable: true,
        formatter: function (value) {
                if (null == value || "" == value) {
                    return "";
                } else {
                    var str = '<img src=' + Feng.ctxPath + '/kaptcha/' + value + ' style=width:40px;height: 40px>';
                    return str;
                }
            }},
        {title: '描述', field: 'description', align: 'center', valign: 'middle', sortable: true},
        {title: '地址', field: 'url', align: 'center', valign: 'middle', sortable: true}]
    return columns;
};


/**
 * 检查是否选中
 */
Site.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Site.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加网站
 */
Site.openAddSite = function () {
    var index = layer.open({
        type: 2,
        title: '添加网站',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/site/site_add'
    });
    this.layerIndex = index;
    layer.full(index);
};

/**
 * 打开查看网站详情
 */
Site.openSiteDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '网站详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/site/site_update/' + Site.seItem.id
        });
        this.layerIndex = index;
        layer.full(index);
    }
};

/**
 * 删除网站
 */
Site.delete = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/site/delete", function () {
                Feng.success("删除成功!");
                Site.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id",Site.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除网站?", operation);
    }
};

/**
 * 搜索
 */
Site.search = function () {
    var queryData = {};

    queryData['title'] = $("#title").val();

    Site.table.refresh({query: queryData});
}

$(function () {
    var defaultColunms = Site.initColumn();
    var table = new BSTable(Site.id, "/site/list", defaultColunms);
    table.setPaginationType("server");
    table.init();
    Site.table = table;
});
