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
<link rel="stylesheet" href="${contextPath}/framework/bootstrap/css/datepicker3.css">
<link rel="stylesheet" href="${contextPath}/style/css/basic.css">
<script src="${contextPath}/framework/jquery/jquery-1.11.1.js"></script>
<script src="${contextPath}/framework/bootstrap/js/bootstrap.min.js"></script>
<script src="${contextPath}/framework/bootstrap/js/bootstrap-datepicker.js"></script>
<script src="${contextPath}/framework/bootstrap/js/bootstrap-datepicker.zh-CN.js"></script>
<script src="${contextPath}/framework/json/json2.js"></script>
<script src="${contextPath}/script/station_name.js"></script>
<script src="${contextPath}/script/FengUtils.js"></script>
<script type="text/javascript">
	$(function(){
	 	$(".date").datepicker({
		 	language:  'zh-CN',
	        weekStart: 1,
			autoclose: 1,
			format: 'yyyy-mm-dd',
			todayHighlight:true,
			forceParse:false,
			startDate:new Date(),
			clearBtn:true
		});
		
		
		$(".station").each(function(){
			new InitStation($(this));
		});
		
		$(".ticketType").click(function(){
			$(".ticketType").css({"background-color":"#FFF"});
			$("input[name='purposeCodes']").val($(this).attr("typeVal"));
			$(this).css({"background-color":"#e6e6e6"});
		});
		
		$(".ticketType[typeVal='ADULT']").click();
		
		var columns = [{property:"station_train_code"},
					  {property:"start_time"},
					  {property:"arrive_time"},
					  {property:"lishi"},
					  {property:"btns"}
					 ];
		
		var sitList = [{property:"wz_num",name:"无座"},
					   {property:"yz_num",name:"硬座"},
					   {property:"rz_num",name:"软座"},
					   {property:"yw_num",name:"硬卧"},
					   {property:"rw_num",name:"软卧"},
					   {property:"gr_num",name:"高级软卧"},
					   {property:"ze_num",name:"二等座"},
					   {property:"zy_num",name:"一等座"},
					   {property:"tz_num",name:"特等座"},
					   {property:"qt_num",name:"其它"},
					   {property:"swz_num",name:"商务座"}];
		$("#queryTicketBtn").click(function(){
			$("#queryForm").find("input").each(function(){
				if(this.value == ""){
					return;
				}
			});
			var params = $("#queryForm").serialize();
			$.post("${ctx}/ticket/queryTicket",params,function(data){
				var trainArr = JSON.parse(data).result;
				var trs = "";
				for(var i = 0,len = trainArr.length;i < len; i++){
					var train = trainArr[i];
					var tr = "<tr>";
					var flag = false;
					for(var j = 0,jLen = columns.length;j< jLen; j++){
						var column = columns[j];
						if(column.property != "btns"){
							tr += "<td>"+train.queryLeftNewDTO[column.property]+"</td>";
						}else{
							tr += "<td>";
							for(var k = 0 ,kLen =sitList.length;k < kLen;k++ ){
								var sit = sitList[k];
								var num = train.queryLeftNewDTO[sit.property];
								if(num != '' && num != '无' && num != '--' && num != '*'){
									flag = true;
									tr += '<button type="button" class="btn btn-success sit">'+sit.name+'('+num+')'+'</button>';
								}
							}
							tr += "</td>";
						}
					}
					tr += "</tr>";
					if(flag){
						trs += tr;
					}
				}
				$("#sits").find("tbody").html(trs);
			});
		});
	});
</script>
</head>

<body>
	<div style="margin: 20px auto;width: 80%;text-align:center;">
		${httpTicket.userName }登陆成功！
	</div>
	<div style="margin: 20px auto;width: 80%;">
		<form class="form-horizontal" role="form" id="queryForm" action="${ctx}/ticket/login">
			<div class="form-group">
				<label for="fromStation" class="col-sm-1 control-label">出发地</label>
				<div class="col-sm-2">
					<div id="fromStation" class="station"></div>
				</div>
				<label for="toStation" class="col-sm-1 control-label">目的地</label>
				<div class="col-sm-2">
					<div id="toStation" class="station"></div>
				</div>
				<label for="trainDate" class="col-sm-1 control-label">出发日期</label>
				<div class="col-sm-2">
					<input type="text" class="form-control date" readonly="readonly" id="trainDate" name="trainDate"
						placeholder="请输入出发日期">
				</div>
				<label for="toStation" class="col-sm-1 control-label">票种</label>
				<div class="col-sm-2">
				<input type="hidden" name="purposeCodes" />
					<div class="btn-group" role="group" aria-label="...">
					  <button type="button" class="btn btn-default ticketType" typeVal="ADULT">普通</button>
					  <button type="button" class="btn btn-default ticketType" typeVal="0X00">学生</button>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-10 col-sm-2">
					<button type="button" id="queryTicketBtn" class="btn btn-default">开始刷票</button>
				</div>
			</div>
		</form>
	</div>
	<div style="margin: 20px auto;width: 80%;">
		<table id="sits" class="table table-hover table-bordered">
			<thead>
				<tr>
					<th>车次</th>
					<th>出发时间</th>
					<th>到达时间</th>
					<th>历时</th>
					<th>座位</th>
				</tr>
			</thead>
			<tbody>
			
			</tbody>
		</table>
	</div>
</body>
</html>
