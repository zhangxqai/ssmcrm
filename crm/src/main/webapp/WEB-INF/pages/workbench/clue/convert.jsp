<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath =request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});

		//为市场活动查询绑定事件
		$("#selectActivityBtn").click(function (){
			//data-toggle="modal" data-target="#searchActivityModal"
			$("#searchActivityModal").modal("show");
			//打开模态窗口前将之前的文字删除
			$("#activityName").val("");

			//打开前要将文本清空
			$("#activityTBody").html("");

		})

		//为搜索框的键盘按下绑定事件
		$("#activityName").keyup(function (){

			var name = $("#activityName").val();
			var clueId = "${clue.id}";

			//收集好了数据，发送请求
			$.ajax({
				url:'workbench/clue/selectActivityForConversion.do',
				data:{
					name:name,
					clueId:clueId
				},
				type:'post',
				dataType:'json',
				success:function (data){
					//对数据进行拼接
					var html = "";

					//对查询出来的数据进行遍历
					$.each(data,function (index,obj){

						html += "<tr>";
						html += "<td><input type=\"radio\" value=\""+obj.id+"\" activityName=\""+obj.name+"\" name=\"activity\"/></td>";
						html += "<td>"+obj.name+"</td>";
						html += "<td>"+obj.startDate+"</td>";
						html += "<td>"+obj.endDate+"</td>";
						html += "<td>"+obj.owner+"</td>";
						html += "</tr>";
					})
					//拼接好的在拼接表单中
					$("#activityTBody").html(html);

				}
			})
		})

		//选着选框中的数据
		$("#activityTBody").on("click","input[type='radio']",function (){
			//获取id和name
			var id = this.value;
			var activityName = $(this).attr("activityName");

			$("#activityId").val(id);
			$("#activity").val(activityName);

			//将市场活动模态窗口关闭
			$("#searchActivityModal").modal("hide");

		})

		//为转换按钮绑定事件
		$("#conversionBtn").click(function (){

			//收集数据，还需要对数据进行判断
			var clueId = "${clue.id}";
			var money = $("#money").val();
			var TransactionName = $.trim($("#TransactionName").val());
			var expectedDate = $("#expectedDate").val();
			var stage = $("#stage").val();
			var activityId = $("#activityId").val();
			var isCreateTran =  $("#isCreateTransaction").prop("checked");
			//对数据进行判断
			if(isCreateTran == true) {
				if (money == "") {
					alert("金额不能为空...");
					return;
				}
				var regExp = /^(([1-9]\d*)|0)$/;
				if (!regExp.test(money)) {
					alert("金额只能是正整数...");
					return;
				}

				if (TransactionName == "") {
					alert("交易名称不能为空...");
					return;
				}

				if (stage == "") {
					alert("阶段不能为空...");
					return;
				}

			}


			//判断好了之后就发生请求
			$.ajax({
				url:'workbench/clue/insertConvert.do',
				data:{
					clueId:clueId,
					money:money,
					TransactionName:TransactionName,
					expectedDate:expectedDate,
					stage:stage,
					activityId:activityId,
					isCreateTran:isCreateTran
				},
				type:'post',
				dataType: 'json',
				success:function (data){
					if (data.code == "1"){
						//成功
						//跳转页面
						window.location.href = "workbench/clue/jumpForIndex.do";
					}else {
						//失败
						alert(data.message);
					}
				}
			})

		})

		//为时间做一个选项功能
		$(".form-control_Date").datetimepicker({

			language:'zh-CN',//这个是显示语言的
			format:'yyyy-mm-dd',//这个是显示到日期框中的日期格式
			minView:'month',//可以选择的最小视图
			initialDate:new Date(),//初始化当前时间
			autoclose:true,//当选择完之后就会关闭日期窗口，默认是false,不关闭日历
			todayBtn:true,//设置今天日期的按钮，默认为false
			clearBtn:true//设置是否显示清空按钮，默认是false

		})

	});
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="activityName" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activityTBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${clue.fullname}${clue.appellation}-${clue.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${clue.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${clue.fullname}${clue.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
	
		<form>
		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">


		    <label for="money">金额</label>
		    <input type="text" class="form-control" id="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="TransactionName">交易名称</label>
		    <input type="text" class="form-control" id="TransactionName" value="">
		  </div>

		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select  class="form-control" id="stage" >
		    	<option></option>
				<c:forEach items="${stageList}" var="s">
					<option value="${s.id}">${s.value}</option>
				</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activityName">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="selectActivityBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
			  <input type="hidden" id="activityId">
			  <input type="text" class="form-control" id="activity" placeholder="点击上面搜索" readonly>
		  </div>
		</form>

		<div class="form-group" style="width: 400px;position: relative; left: 20px;">
			<label for="expectedDate">预计成交日期</label><br/>
			<input type="text" class="form-control_Date" id="expectedDate" readonly>
		</div>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${clue.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="conversionBtn" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>