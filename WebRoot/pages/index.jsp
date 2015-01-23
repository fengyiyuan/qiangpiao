<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="contextPath" value="<%=basePath%>" />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="${contextPath}/framework/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/style/css/basic.css">
<script src="${contextPath}/framework/jquery/jquery-1.11.1.js"></script>
<script src="${contextPath}/framework/bootstrap/js/bootstrap.min.js"></script>
<script src="${contextPath}/framework/json/json2.js"></script>
<script src="${contextPath}/script/FengUtils.js"></script>
<script type="text/javascript">
	$(function(){
		$("#codeImg").click(refreshImg);
		refreshImg();
		$("input").blur(function(){
			var ret = true;
			if(this.value.trim()){
				ret = false;
			}
			if(!ret && this.name == "code"){
				var dom = this;
				if(this.value.length == 4){
					$.post("${ctx}/login/validCode",{code:this.value},function(data){
						if(JSON.parse(data).data.result == 1){
							ret = false;
						}else{
							ret = true;
						}
						addStyle(dom,ret);
					});
				}else{
					ret = true;
				}
			}
			addStyle(this,ret);
		});
		
		$("#loginBtn").click(function(){
			$("input").trigger("blur");
			if($(".has-error").length == 0){
				var params = $("#loginForm").serialize();
				$.post("${ctx}/login/login",params,function(data){
					var ret = JSON.parse(data);
					if(ret.type == 0){
						window.location = "${ctx}/ticket/index";
					}else{
						alert(ret.msg);
					}
				});
			}
		});
	});
	
	function addStyle(dom,ret){
		var oldClass = "has-error";
		var newClass = "has-success";
		if(ret){
			oldClass = "has-success";
			newClass = "has-error";
		}
		$(dom).closest(".form-group").removeClass(oldClass).addClass(newClass);
	}
	
	function refreshImg(){
		$.get("${ctx}//login/getCodeImage",function(data){
			$("#codeImg").attr("src","${ctx}/"+data);
		});
	}
</script>
</head>

<body>
	<div style="margin: 20px auto;width: 40%;">
		<form class="form-horizontal" role="form" id="loginForm" action="${ctx}/ticket/login">
			<div class="form-group">
				<label for="username" class="col-sm-2 control-label">登录名</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="username" name="username"
						placeholder="请输入登录名">
				</div>
			</div>
			<div class="form-group">
				<label for="password" class="col-sm-2 control-label">密码</label>
				<div class="col-sm-10">
					<input type="password" class="form-control" id="password" name="password"
						placeholder="请输入密码">
				</div>
			</div>
			<div class="form-group">
				<label for="code" class="col-sm-2 control-label">验证码</label>
				<div class="col-sm-5">
					<input type="text" class="form-control" id="code" name="code"
						placeholder="请输入验证码">
				</div>
				<div class="col-sm-5">
					<img src="" class="img-responsive" id="codeImg" alt="验证码">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="button" id="loginBtn" class="btn btn-default">登陆</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
