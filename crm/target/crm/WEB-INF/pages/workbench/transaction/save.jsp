<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath =request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

	<script type="text/javascript">

		$(function (){

			//为模糊查客户名称绑定事件，这个是需要插件操作的，第一步先引入包，第二部插件div，这个已经有了，直接调用就可以了， 第三步
			//使用插件提供的方法
			$("#create-accountName").typeahead({

				source:function (jquery,process){
					//process是将返回来的数据要展示到浏览器上的
					//jquery是传入的数据

					//直接发送请求就行了
					$.ajax({
						url:'workbench/transaction/selectCustomerById.do',
						data:{
							customerName:jquery
						},
						type:'post',
						dataType: 'json',
						success:function (data){
							process(data);
						}
					})
				}

			})

			//给阶段加一个change事件
		 	$("#create-transactionStage").change(function (){
		 		//获取选中的数据
				//$(this).find("option:selected").text();
				var stageValue = $("#create-transactionStage option:selected").text();


				//对表单进行验证
				if (stageValue == ""){
					//如果等于空，就清空可能性
					$("#create-possibility").val();
					return;
				}
				//到这里就说明没有问题，就能发送请求
				$.ajax({
					url:'workbench/transaction/getPossibilityByStage.do',
					data:{
						stageValue:stageValue
					},
					type:'post',
					dataType:'json',
					success:function (data){

						//返回来的可能性就显示到可能性的框中
						$("#create-possibility").val(data);
					}
				})
			})



			//为创建保存添加绑定事件
			$("#saveTransactionBtn").click(function (){

				//收集数据
				var owner = $("#create-transactionOwner").val();
				var money = $.trim($("#create-amountOfMoney").val());
				var name = $.trim($("#create-transactionName").val());
				var expectedDate = $("#create-expectedClosingDate").val();
				var customerName = $.trim($("#create-accountName").val());
				var stage = $("#create-transactionStage").val();
				var type = $("#create-transactionType").val();
				var source = $("#create-clueSource").val();
				var activityId = $("#activityId").val();
				var contactsId = $("#contactsId").val();
				var description = $.trim($("#create-describe").val());
				var contactSummary = $.trim($("#create-contactSummary").val());
				var nextContactTime = $("#create-nextContactTime").val();


				//对数据进行验证
				if (owner == ""){
					alert("所有者不能为空...");
					return;
				}
				if (money == ""){
					alert("所有者不能为空...");
					return;
				}


				var retMoney = /^(([1-9]\d*)|0)$/;
				if (!retMoney.test(money)){
					alert("金额只能是正整数...")
					return;
				}
				if (name == ""){
					alert("名称不能为空...");
					return;
				}
				if (expectedDate == ""){
					alert("预计成交日期不能为空...");
					return;
				}
				if (customerName == ""){
					alert("客户名称不能为空...");
					return;
				}
				if (stage == ""){
					alert("阶段不能为空...");
					return;
				}



				//发生请求
				$.ajax({
					url:'workbench/transaction/insertTransactionOne.do',
					data:{
						owner:owner,
						money:money,
						name:name,
						expectedDate:expectedDate,
						customerName:customerName,
						stage:stage,
						type:type,
						source:source,
						activityId:activityId,
						contactsId:contactsId,
						description:description,
						contactSummary:contactSummary,
						nextContactTime:nextContactTime
					},
					type:'post',
					dataType:'json',
					success:function (data){
						if (data.code == "1"){
							//成功
							window.location.href = "workbench/transaction/selectDicValueForTransaction.do";
						}else {
							//失败
							alert(data.message)
						}
					}
				})

			})

			//为市场活动元绑定事件
			$("#findMarketActivityBtn").click(function (){
				//打开模态窗口
				$("#findMarketActivity").modal("show");
				//收集数据,查询不需要验证

			})

			//为联系人绑定事件
			$("#findContactsBtn").click(function (){
				//打开模态窗口
				$("#findContacts").modal("show");
				//收集数据查询不需要验证
			})

			$("#activityName").keyup(function (){
				//收集参数
				var activityName = $("#activityName").val();

				//不需要验证，查询的直接发生请求
				$.ajax({

					url:'workbench/transaction/selectFroActivitySavaTranByName.do',
					data:{
						name:activityName
					},
					type:'post',
					dataType:'json',
					success:function (data){
						//收集的参数，就循环拼接
						var html = "";

						$.each(data,function (index,obj){
							html += "<tr>";
							html += "<td><input type=\"radio\" value=\""+obj.id+"\" activityName=\""+obj.name+"\" name=\"activity\"/></td>";
							html += "<td>"+obj.name+"</td>";
							html += "<td>"+obj.startDate+"</td>";
							html += "<td>"+obj.endDate+"</td>";
							html += "<td>"+obj.owner+"</td>";
							html += "</tr>";
						})

						$("#ABody").html(html);
					}

				})
			})
			//为键盘绑定一个谈起事件
			$("#contactsName").keyup(function (){
				var contactsName = $("#contactsName").val();


				//不需要验证，查询的直接发生请求
				$.ajax({

					url:'workbench/transaction/selectForContactsSaveTranByName.do',
					data:{
						fullname:contactsName
					},
					type:'post',
					dataType:'json',
					success:function (data){

						//收集的参数，就循环拼接
						var html = "";

						$.each(data,function (index,obj){
							html += "<tr>";
							html += "<td><input type=\"radio\" value=\""+obj.id+"\" contactsName=\""+obj.fullname+"\" name=\"contacts\"/></td>";
							html += "<td>"+obj.fullname+"</td>";
							html += "<td>"+obj.email+"</td>";
							html += "<td>"+obj.mphone+"</td>";
							html += "</tr>";
						})

						$("#CBody").html(html);

					}

				})

			})

			//为了选中市场活动源绑定点击事件
			$("#ABody").on("click","input[name='activity']",function (){
				//可以的
				//之后就是收集这个数据，将这个数据绑定一下
				var id = this.value;
				var name = $(this).attr("activityName");

				alert(id)
				alert(name)
				//获取到了之后在将这个送到那里
				$("#activityId").val(id);
				$("#create-activitySrc").val(name);

				//关闭模态窗口
				$("#findMarketActivity").modal("hide");
			})

			//为了选中联系人绑定点击事件
			$("#CBody").on("click","input[name='contacts']",function (){

				//获取参数
				var id =this.value;
				var contactsName = $(this).attr("contactsName");

				alert(id)
				alert(contactsName)
				//获取到了之后在将这个送到那里
				$("#contactsId").val(id);
				$("#create-contactsName").val(contactsName);

				//关闭模态窗口
				$("#findContacts").modal("hide");
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

		})

	</script>
</head>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
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
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="ABody">
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

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="contactsName" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback" ></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="CBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" id="saveTransactionBtn" class="btn btn-primary">保存</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionOwner">
				  <c:forEach items="${userList}" var="u">
					  <option value="${u.id}">${u.name}</option>
				  </c:forEach>
				</select>
			</div>
			<label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-amountOfMoney">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-transactionName">
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control_Date" id="create-expectedClosingDate" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-accountName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-transactionStage">
				  <option></option>
				  <c:forEach items="${stageList}" var="s">
					  <option value="${s.id}">${s.value}</option>
				  </c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionType">
				  <option></option>
					<c:forEach items="${transactionTypeList}" var="t">
						<option value="${t.id}">${t.value}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource">
				  <option></option>
					<c:forEach items="${sourceList}" var="su">
						<option value="${su.id}">${su.value}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="findMarketActivityBtn"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" value="activityId">
				<input type="text" class="form-control" id="create-activitySrc" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" id="findContactsBtn"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" value="contactsId">
				<input type="text" class="form-control" id="create-contactsName" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control_Date" id="create-nextContactTime" readonly>
			</div>
		</div>
		
	</form>
</body>
</html>