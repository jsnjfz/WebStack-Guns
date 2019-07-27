/**
 * 管理初始化
 */
var User = {
    id: "UserTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
User.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {
            field: 'Number',
            title: '序号',
            align: 'center',
            width: 50,
            formatter: function (value, row, index) {
                //return index + 1;
                var pageSize = $('#UserTable').bootstrapTable('getOptions').pageSize;//通过表的#id 可以得到每页多少条
                var pageNumber = $('#UserTable').bootstrapTable('getOptions').pageNumber;//通过表的#id 可以得到当前第几页
                return pageSize * (pageNumber - 1) + index + 1;//返回每条的序号： 每页条数 * （当前页 - 1 ）+ 序号
            }
        },
        {title: '用户ID', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {
            title: '用户头像',
            field: 'headimgurl',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                if (null == value || "" == value) {
                    return "";
                } else {
                    var str = '<img src=' + value + ' style=width:40px;height: 40px>';
                    return str;
                }
            }
        },
        {title: '手机号', field: 'phone', visible: true, align: 'center', valign: 'middle'},
        {title: '用户昵称', field: 'nickname', visible: true, align: 'center', valign: 'middle'},
        {
            title: '用户级别',
            field: 'language',
            visible: true,
            align: 'center',
            formatter: function (value, row, index) {
                if ("1" == value) {
                    return "普通用户";
                } else if ("2" == value) {
                    return "VIP用户";
                } else if ("3" == value) {
                    return "付费用户";
                } else {
                    return "其他用户";
                }
            }
        },
        {
            title: '是否购买',
            field: 'payStatus',
            visible: true,
            align: 'center',
            formatter: function (value) {
                if (value > 0) {
                    return "已购买";
                }
                return "未购买";
            }
        },
        {
            title: '用户排行(/百分比)',
            field: 'ranking',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                return parseFloat(value).toFixed(2);
            }
        },
        {title: '用户学习总时长(/分钟)', field: 'totaltime', visible: true, align: 'center', valign: 'middle'},
        {title: '累计学习次数(/次)', field: 'seriestime', visible: true, align: 'center', valign: 'middle'},
        {title: '用户今日学习时长(/分钟)', field: 'leantime', visible: true, align: 'center', valign: 'middle'},
        {title: '最近登录时间', field: 'logintime', visible: true, align: 'center', valign: 'middle'},
        {title: '指定vip时间', field: 'vipTime', visible: true,  align: 'center', valign: 'middle'},
        {title: '操作', field: 'oper', visible: true, align: 'center', valign: 'middle', formatter: formatOper},

    ];
};


function formatOper(val, row, index) {
    var oper = '';
    if ("2" == row.language) {
        oper = "<a style=\"text-decoration: none;\" class=\"linkbutton-edit\" onClick=\"setLanguage(" + row.id + ",1)\" href=\"javascript:;\" title=\"设为普通用户\">设为普通用户</a>";
    } else if ("2" != row.language) {
        oper = "<a style=\"text-decoration: none;\" class=\"linkbutton-edit\" onClick=\"setLanguage(" + row.id + ",2)\" href=\"javascript:;\" title=\"标记VIP\">标记VIP用户</a>";
    }
    return oper;
}

function setLanguage(userId, language) {
    $.ajax({
        type: "POST",
        url: Feng.ctxPath + '/user/setLanguage',
        data: {
            id: userId,
            language: language
        },
        success: function (data) {
            if (data == 1) {
                Feng.success("操作成功!");
            } else {
                Feng.error('操作失败');
            }
            User.table.refresh({query: User.formParams()});
        }
    });
}


/**
 * 检查是否选中
 */
User.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        User.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
User.openAddOrder = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/order/order_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
User.openOrderDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/order/order_update/' + User.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
User.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/order/delete", function (data) {
            Feng.success("删除成功!");
            User.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("orderId", this.seItem.id);
        ajax.start();
    }
};

// /**
//  * 查看购买详情
//  */
// User.detail = function () {
//     if (this.check()) {
//         var ajax = new $ax(Feng.ctxPath + "/user/payDetail/" + this.seItem.id, function (data) {
//             Feng.buyDetail("购买详情", data);
//         }, function (data) {
//             Feng.error("获取详情失败!");
//         });
//         ajax.start();
//     }
// };

/**
 * 查看购买详情
 */
User.detail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '购买详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/sysorder/show/' + this.seItem.id
        });
        this.layerIndex = index;
        layer.full(index);
    }
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
User.formParams = function () {
    var queryData = {};

    queryData['nickname'] = $("#nickname").val();
    queryData['phone'] = $("#phone").val();
    queryData['language'] = $("#language").val();
    queryData['payStatus'] = $("#payStatus").val();
    queryData['userId'] = $("#userId").val();
    return queryData;
}

/**
 * 查询列表
 */
User.search = function () {
    User.table.refresh({query: User.formParams()});
};

$(function () {
    var defaultColunms = User.initColumn();
    var table = new BSTable(User.id, "/user/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(User.formParams());
    User.table = table.init();
});
