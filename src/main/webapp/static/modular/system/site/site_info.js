/**
 * 初始化网站详情对话框
 */
var SiteDlg = {
    siteData : {},
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
        description: {
            validators: {
                notEmpty: {
                    message: '描述不能为空'
                }
            }
        },
        url: {
            validators: {
                notEmpty: {
                    message: '地址不能为空'
                }
            }
        },
        categoryTitle: {
            validators: {
                notEmpty: {
                    message: '分类不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
SiteDlg.clearData = function() {
    this.siteData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SiteDlg.set = function(key, val) {
    this.siteData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SiteDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SiteDlg.close = function() {
    parent.layer.close(window.parent.Site.layerIndex);
}

/**
 * 点击分类ztree列表的选项时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
SiteDlg.onClickSite = function(e, treeId, treeNode) {
    $("#categoryTitle").attr("value", SiteDlg.zTreeInstance.getSelectedVal());
    $("#categoryId").attr("value", treeNode.id);
}

/**
 * 显示分类选择的树
 *
 * @returns
 */
SiteDlg.showCategorySelectTree = function() {
    var categoryTitle = $("#categoryTitle");
    var categoryTitleOffset = $("#categoryTitle").offset();
    $("#parentCategoryMenu").css({
        left : categoryTitleOffset.left + "px",
        top : categoryTitleOffset.top + categoryTitle.outerHeight() + "px"
    }).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
}

/**
 * 隐藏分类选择的树
 */
SiteDlg.hideCategorySelectTree = function() {
    $("#parentCategoryMenu").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}

/**
 * 收集数据
 */
SiteDlg.collectData = function() {
    this.set('id').set('thumb').set('title').set('description').set('categoryId').set('url');
}

/**
 * 验证数据是否为空
 */
SiteDlg.validate = function () {
    $('#siteForm').data("bootstrapValidator").resetForm();
    $('#siteForm').bootstrapValidator('validate');
    return $("#siteForm").data('bootstrapValidator').isValid();
}

/**
 * 提交新增
 */
SiteDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    var reg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
    if(!reg.test(this.siteData.url)){
        Feng.error("请输入正确的网址!");
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/site/add", function(data){
        Feng.success("添加成功!");
        window.parent.Site.table.refresh();
        SiteDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.siteData);
    ajax.start();
}

/**
 * 提交修改
 */
SiteDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    var reg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
    if(!reg.test(this.siteData.url)){
        Feng.error("请输入正确的网址!");
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/site/update", function(data){
        Feng.success("修改成功!");
        window.parent.Site.table.refresh();
        SiteDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.siteData);
    ajax.start();
}

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "parentCategoryMenu" || $(
            event.target).parents("#parentCategoryMenu").length > 0)) {
        SiteDlg.hideCategorySelectTree();
    }
}

$(function() {
    Feng.initValidator("siteForm", SiteDlg.validateFields);

    var ztree = new $ZTree("parentCategoryMenuTree", "/site/tree");
    ztree.bindOnClick(SiteDlg.onClickSite);
    ztree.init();
    SiteDlg.zTreeInstance = ztree;

    // 初始化头像上传
    var avatarUp = new $WebUpload("thumb");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init();
});
