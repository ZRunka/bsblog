/*!
 * Bolg main JS.
 *
 */
"use strict";
//# sourceURL=main.js
 
//DOM 加载完再执行
$(function() {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getUersByName(pageIndex, pageSize) {
         $.ajax({ 
             url: "/users", 
             contentType : 'application/json',
             data:{
                 "async":true, 
                 "pageIndex":pageIndex,
                 "pageSize":pageSize,
                 "name":$("#searchName").val()
             },
             success: function(data){
                 $("#mainContainer").html(data);
             },
             error : function() {
                 toastr.error("error!");
             }
         });
    }

    // 搜索
    $("#searchNameBtn").click(function() {
        getUersByName(0, _pageSize);
    });

    // 获取添加用户的界面
    $("#addUser").click(function() {
        $.ajax({
            url: "/users/add",
            success: function(data){
                $("#userFormContainer").html(data);
            },
            error : function(data) {
                toastr.error("error!");
            }
        });
    });

    // 获取编辑用户的界面
    $("#rightContainer").on("click",".blog-edit-user", function () {
        $.ajax({
             url: "/users/edit/" + $(this).attr("userId"),
             success: function(data){
                 $("#userFormContainer").html(data);
             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });

    // 提交变更后，清空表单
    $("#submitEdit").click(function() {
        var username = $("#username").val();
        var username = $("#username").val();
        var username = $("#username").val();
        var jsonUser = {};

        $.ajax({
            url: "/users",
            type: 'POST',
            data:$('#userForm').serialize(),
            success: function(data){
                $('#userForm')[0].reset();

                if (data.success) {
                    // 从新刷新主界面
                    getUersByName(0, _pageSize);
                } else {
                    toastr.error(data.message);
                }

            },
            error : function() {
                toastr.error("error!");
            }
        });
    });

    // 删除用户
    $("#rightContainer").on("click",".blog-delete-user", function () { 

        $.ajax({ 
             url: "/users/delete/" + $(this).attr("userId") ,
             success: function(data){
                 if (data.success) {
                     // 从新刷新主界面
                     getUersByName(0, _pageSize);
                 } else {
                     toastr.error(data.message);
                 }
             },
             error : function() {
                 toastr.error("error!");
             }
         });
    });
});