/**
 * 初始化分类详情对话框
 */
var CategoryDlg = {
    categoryData : {},
    zTreeInstance : null,
    validateFields: {
        title: {
            validators: {
                notEmpty: {
                    message: '标题不能为空'
                }
            }
        },
        icon: {
            validators: {
                notEmpty: {
                    message: '图标不能为空'
                }
            }
        },
        sort: {
            validators: {
                notEmpty: {
                    message: '排序不能为空'
                }
            }
        },
        pName: {
            validators: {
                notEmpty: {
                    message: '上级名称不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
CategoryDlg.clearData = function() {
    this.categoryData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CategoryDlg.set = function(key, val) {
    this.categoryData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CategoryDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CategoryDlg.close = function() {
    parent.layer.close(window.parent.Category.layerIndex);
}

/**
 * 点击分类ztree列表的选项时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
CategoryDlg.onClickCategory = function(e, treeId, treeNode) {
    $("#pName").attr("value", CategoryDlg.zTreeInstance.getSelectedVal());
    $("#parentId").attr("value", treeNode.id);
}

/**
 * 显示分类选择的树
 *
 * @returns
 */
CategoryDlg.showCategorySelectTree = function() {
    var pName = $("#pName");
    var pNameOffset = $("#pName").offset();
    $("#parentCategoryMenu").css({
        left : pNameOffset.left + "px",
        top : pNameOffset.top + pName.outerHeight() + "px"
    }).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
}

/**
 * 隐藏分类选择的树
 */
CategoryDlg.hideCategorySelectTree = function() {
    $("#parentCategoryMenu").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}

/**
 * 收集数据
 */
CategoryDlg.collectData = function() {
    this.set('id').set('sort').set('title').set('icon').set('parentId');
}

/**
 * 验证数据是否为空
 */
CategoryDlg.validate = function () {
    $('#categoryForm').data("bootstrapValidator").resetForm();
    $('#categoryForm').bootstrapValidator('validate');
    return $("#categoryForm").data('bootstrapValidator').isValid();
}

/**
 * 提交添加分类
 */
CategoryDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/category/add", function(data){
        Feng.success("添加成功!");
        window.parent.Category.table.refresh();
        CategoryDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.categoryData);
    ajax.start();
}

/**
 * 提交修改
 */
CategoryDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/category/update", function(data){
        Feng.success("修改成功!");
        window.parent.Category.table.refresh();
        CategoryDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.categoryData);
    ajax.start();
}

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "parentCategoryMenu" || $(
            event.target).parents("#parentCategoryMenu").length > 0)) {
        CategoryDlg.hideCategorySelectTree();
    }
}

$(function() {
    Feng.initValidator("categoryForm", CategoryDlg.validateFields);

    var ztree = new $ZTree("parentCategoryMenuTree", "/category/tree");
    ztree.bindOnClick(CategoryDlg.onClickCategory);
    ztree.init();
    CategoryDlg.zTreeInstance = ztree;
});
