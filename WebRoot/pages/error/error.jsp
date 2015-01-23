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
</script>
</head>

<body>
	<div style="margin: 20px auto;width: 40%;">
		${error}
	</div>
</body>
</html>
