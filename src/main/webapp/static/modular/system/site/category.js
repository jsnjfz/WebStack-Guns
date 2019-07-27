/**
 * 分类管理的单例
 */
var Category = {
    id: "categoryTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Category.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '标题', field: 'title', align: 'center', valign: 'middle', sortable: true},
        {title: '图标', field: 'icon', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value) {
                if (null == value || "" == value) {
                    return "";
                } else {
                    var str = '<i class= "fa '  + value + '">';
                    return str;
                }
            }
            },
        {title: '排序', field: 'sort', align: 'center', valign: 'middle', sortable: true}]
    return columns;
};


/**
 * 检查是否选中
 */
Category.check = function () {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Category.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加分类
 */
Category.openAddCategory = function () {
    var index = layer.open({
        type: 2,
        title: '添加分类',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/category/category_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看分类详情
 */
Category.openCategoryDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '分类详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/category/category_update/' + Category.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除分类
 */
Category.delete = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/category/delete", function () {
                Feng.success("删除成功!");
                Category.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id",Category.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除分类?", operation);
    }
};

/**
 * 搜索
 */
Category.search = function () {
    var queryData = {};

    queryData['title'] = $("#title").val();

    Category.table.refresh({query: queryData});
}

$(function () {
    var defaultColunms = Category.initColumn();
    var table = new BSTreeTable(Category.id, "/category/list", defaultColunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("parentId");
    table.setExpandAll(true);
    table.init();
    Category.table = table;
});
