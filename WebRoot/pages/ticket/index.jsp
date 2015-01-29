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
	var currTrain = null;
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
		
		$.get("${ctx}/ticket/initLeftTicket",function(data){
			
		});
		
		$.get("${ctx}/ticket/queryPassengers",function(data){
			var passArr = JSON.parse(data).result;
			for(var i = 0,len = passArr.length;i < len; i++){
				var pass = passArr[i];
				var $li = $('<li><input type="checkbox" name="pass" code="'+pass.code+'"/>'+pass.passenger_name+'</li>');
				$("#passengers").append($li);
			}
		});
				
		var columns = [{property:"station_train_code"},
					  {property:"start_time"},
					  {property:"arrive_time"},
					  {property:"lishi"},
					  {property:"btns"}
					 ];
		//商务座(9),特等座(P),一等座(M),二等座(O),高级软卧(6),软卧(4),硬卧(3),软座(2),硬座(1),无座(1)
		var sitList = [{property:"wz_num",name:"无座",val:"1"},
					   {property:"yz_num",name:"硬座",val:"1"},
					   {property:"rz_num",name:"软座",val:"2"},
					   {property:"yw_num",name:"硬卧",val:"3"},
					   {property:"rw_num",name:"软卧",val:"4"},
					   {property:"gr_num",name:"高级软卧",val:"6"},
					   {property:"ze_num",name:"二等座",val:"O"},
					   {property:"zy_num",name:"一等座",val:"M"},
					   {property:"tz_num",name:"特等座",val:"P"},
					   {property:"qt_num",name:"其它",val:"1"},
					   {property:"swz_num",name:"商务座",val:"9"}];
		$("#queryTicketBtn").click(function(){
			$("#queryForm").find("input").each(function(){
				if(this.value == ""){
					return;
				}
			});
			$("#sits").find("tbody").html('');
			var params = $("#queryForm").serialize();
			$.post("${ctx}/ticket/queryTicket",params,function(data){
				var trainArr = JSON.parse(data).result;
				for(var i = 0,len = trainArr.length;i < len; i++){
					var train = trainArr[i];
					var tr = '<tr secretStr="'+train.secretStr+'">';
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
									tr += '<button type="button" typeVal="'+sit.val+'" class="btn btn-success sit">'+sit.name+'('+num+')'+'</button>';
								}
							}
							tr += "</td>";
						}
					}
					tr += "</tr>";
					if(flag){
						var $tr = $(tr);
						$tr.data("train",train);
						$("#sits").find("tbody").append($tr);
					}
				}
				$("#sits").find(".sit").click(function(){
					var codeArr = new Array;
					$("input[name='pass']:checked").each(function(){
						codeArr.push($(this).attr("code"));
					});
					if(codeArr.length == 0){
						alert("请先选择乘客");
						return;
					}
					var params = "codes="+codeArr.join(",")+"&type="+$(this).attr("typeVal")+"&secretStr=" + $(this).closest("tr").attr("secretStr");
					var currDom = this;
					$.post("${ctx}/ticket/submitOrder?random=" + new Date().getTime(),params,function(data){
						var retObj = JSON.parse(data);
				 		if(retObj.type != 0){
				 			alert(retObj.msg);
				 			return;
				 		}
						 var ret = retObj.result;
						if(ret.status){
							currTrain = $(currDom).closest("tr").data("train");
							$('#myModal').modal('show');
						}else{
							alert(ret.messages);
						}
					}); 
				});
			}); 
		});
		
		$('#myModal').on('show.bs.modal', function (e) {
			reloadImg();
		});
		
		$('#orderCode').bind('keyup',function(){
			var currDom = this;
			if(this.value.length == 4){
				var sit = currTrain.queryLeftNewDTO;
				 $.post("${ctx}/ticket/validCode",{code:this.value},function(data){
						if(JSON.parse(data).data.result == 1){
							var params = 'code=' + currDom.value + '&leftTicketStr=' + sit.yp_info + '&trainLocation=' +sit.location_code;
							$.post("${ctx}/ticket/checkOrderInfo",params,function(data){
								alert(data);
							});
						}else{
							reloadImg();
						}
					});
			}
		});
	});
	
	function reloadImg(){
		$.get("${ctx}/ticket/getCodeImage",function(data){
			$("#orderCodeImg").attr("src","${ctx}/"+data);
			$('#orderCode').val('');
		});
	}
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
				<label for="fromStation" class="col-sm-1 control-label">选择乘客</label>
				<div class="col-sm-9" style="padding-top: 7px;">
					<ul id="passengers" class="passengers">
						
					</ul>
				</div>
				<div class="col-sm-2">
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
	
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">请输入验证码</h4>
      </div>
      <div class="modal-body">
      <form class="form-horizontal" role="form">
     	 <div class="form-group">
				<label for="orderCode" class="col-sm-2 control-label">验证码</label>
				<div class="col-sm-5">
					<input type="text" class="form-control" id="orderCode" name="orderCode"
						placeholder="请输入验证码">
				</div>
				<div class="col-sm-5">
					<img src="" class="img-responsive" id="orderCodeImg" alt="验证码">
				</div>
			</div>
		</form>	
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary">确定</button>
      </div>
    </div>
  </div>
</div>
</body>
</html>
