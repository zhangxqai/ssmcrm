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
<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){


		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});

		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});

		/*$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});*/

		$("#remarkDivList").on("mouseover",".remarkDiv",function (){
			$(this).children("div").children("div").show();
		})

		/*$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});*/

		$("#remarkDivList").on("mouseout",".remarkDiv",function (){
			$(this).children("div").children("div").hide();
		})

		/*$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});*/
		$("#remarkDivList").on("mouseover",".myHref",function (){
			$(this).children("span").css("color","red");
		})

		/*
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});*/

		$("#remarkDivList").on("mouseout",".myHref",function (){
			$(this).children("span").css("color","#E6E6E6");
		})


		//为线索备注的保存添加点击事件
		$("#saveRemarkBtn").click(function (){

			//收集数据
			var clueId = "${clue.id}"
			var noteContent = $.trim($("#remark").val());

			if (noteContent==""){
				return;
			}

			//收集好数据就发送请求
			$.ajax({
				url:'workbench/clue/insertClueRemark.do',
				data:{
					clueId:clueId,
					noteContent:noteContent
				},
				type:'post',
				dataType:'json',
				success:function (data){
					//判断有没有成功
					if (data.code){
						//成功，在备注添加一条
						//先拼接字符串
						var html = "";
						html +="<div id=\"div_"+data.retData.id+"\" class=\"remarkDiv\" style=\"height: 60px;\">";
						html +="<img title=\""+data.retData.createBy+"\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
						html +="<div style=\"position: relative; top: -40px; left: 40px;\" >";
						html +="<h5 id=\"h5"+data.retData.id+"\">"+data.retData.noteContent+"</h5>";
						html +="<font color=\"gray\">线索</font> <font color=\"gray\">-</font> <b>${clue.fullname}${clue.appellation}-${clue.company}</b> <small style=\"color: gray;\"> "+data.retData.createTime+" 由${sessionScope.sessionUser.name}创建</small>";
						html +="<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
						html +="<a class=\"myHref\" name='editA' remarkId=\""+data.retData.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						html +="&nbsp;&nbsp;&nbsp;&nbsp;";
						html +="<a class=\"myHref\" name='deleteA' remarkId=\""+data.retData.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						html +="</div>";
						html +="</div>";
						html +="</div>";

						//拼接，在那个地方拼接呢？
						$("#remarkDiv").append(html);

						//保存好了之后将内容清空
						$("#remark").val("");
					}else {
						//失败
						alert("备注添加失败")
					}
				}
			})

		})

		//为删除备注按钮绑定事件
		$("#remarkDivList").on("click","a[name='deleteA']",function (){

			//收集数据
			var id = $(this).attr("remarkId");

			//发送请求
			$.ajax({
				url:'workbench/clue/deleteClueRemarkById.do',
				data: {
					id:id
				},
				type:"post",
				dataType: 'json',
				success:function (data){
					//判断有没有删除成功
					if(data.code == "1"){
						//成功,将信息删除
						$("#div_"+id).remove();
					}else {
						//失败
						alert(data.message);
					}
				}
			})

		})

		//为修改按钮绑定事件
		//第一步，先找到父的div
		$("#remarkDivList").on("click","a[name='editA']",function (){

			//在打开窗口之前还要将未修改的信息获取一下
			//通过attr获取
			var id = $(this).attr("remarkId");

			//还有一个内容的通过父子选择器获取
			var noteContent = $("#div_"+id+" h5").text();

			//收集好数据就要展示到模态窗口上
			$("#edit-id").val(id);
			$("#edit-noteContent").val(noteContent);
			//打开修改模态窗口
			$("#editRemarkModal").modal("show");

		})

		//为修改备注修改保存按钮绑定事件
		$("#updateRemarkBtn").click(function (){

			//收集数据
			var id = $("#edit-id").val();
			var noteContent = $("#edit-noteContent").val();

			if (noteContent ==""){
				alert("备注内容不能为空！");
				return;
			}

			$.ajax({
				url:'workbench/clue/updateClueRemarkById.do',
				data:{
					id:id,
					noteContent:noteContent
				},
				type:'post',
				dataType:'json',
				success:function (data){
					//判断有没有修改成功
					if (data.code == "1"){
						//成功，将之前的备注修改一下
						$("#div_"+id+" h5").text(noteContent);
						$("#div_"+id+" small").text(" "+data.retData.editTime+"由${sessionScope.sessionUser.name}修改");

						//关闭模态窗口
						$("#editRemarkModal").modal("hide");

					}else {
						//失败
						alert(data.message);
					}
				}
			})
		})

		//为关联市场活动绑定点击事件
		$("#relationActivityBtn").click(function (){

			//打开模态窗口前将查询条件清空
			$("#activityName").val("");
			//在打开关联市场活动模态窗口的时候
			selectRelationActivity(1,5);
			//打开模态窗口
			$("#bundModal").modal("show");



		})


		//为键盘按下绑定事件
		/*$("#activityName").keydown(function (){

			selectRelationActivity(1,$("#demo_pag1").bs_pagination(1,"rowsPerPage"));
		})*/
		/**/

		$("#activityName").keyup(function (){

			selectRelationActivity(1,5);

		})

		//为关联市场活动模态窗口的全选框绑定事件
		$("#checkedAll").click(function (){
			//那么下面的列表都要选上，通过父子选择器选择
			if (this.checked == true){

				$("#activityTBody input[type='checkbox']").prop("checked",true);

			}else {

				$("#activityTBody input[type='checkbox']").prop("checked",false);
			}

		})

		//为选择框绑定全选
		$("#activityTBody").on("click","input[type='checkbox']",function (){

			//判断这些选中的框有没有相等这些框的数量
			if ($("#activityTBody input[type='checkbox']").size() == $("#activityTBody input[type='checkbox']:checked").size()){
				$("#checkedAll").prop("checked",true);
			}else {
				$("#checkedAll").prop("checked",false);
			}

		})

		//为保存按钮绑定事件
		$("#relationClueActivityBtn").click(function (){
			//收集数据,通过父子选择器获取选中的id
			var activityIds = $("#activityTBody input[type='checkbox']:checked");
			//判断获取的id数有多少个，
			if(activityIds.size() == 0){
				alert("关联的市场活动条数不能为0!");
				return;
			}

			//能到这里就是正常的
			//循环获得到的id
			var ids = "";
			$.each(activityIds,function (){
				ids += "activityId="+this.value+"&";
			})

			//后面还有一个&符号要去掉
			/*var ids = ids.substr(0,ids.length-1);*/

			//还要再将clieId拼接上
			ids += "clueId=${clue.id}";

			//发送请求
			$.ajax({
				url:'workbench/clue/insertClueActivityRelation.do',
				data:ids,
				type:'post',
				dataType:'json',
				success:function (data){
					//判断有没有修改成功
					if (data.code == "1"){
						//成功,刷新列表
						var html = "";

						//回来有很多数据，要循环
						$.each(data.retData,function (index,obj){
							html += "<tr id=\"tr_"+obj.id+"\">";
							html += "<td>"+obj.name+"</td>";
							html += "<td>"+obj.startDate+"</td>";
							html += "<td>"+obj.endDate+"</td>";
							html += "<td>"+obj.owner+"</td>";
							html += "<td><a href=\"javascript:void(0);\" name=\"relieveA\" activityId=\""+obj.id+"\" style=\"text-decoration: none;\"><span class=\"glyphicon glyphicon-remove\"></span>解除关联</a></td>";
							html += "</tr>";
						})

						$("#aTBody").append(html);

						//关闭模态窗口
						$("#bundModal").modal("hide");


					}else {
						//失败
						alert(data.message);
					}
				}
			})
		})

		//为解除按钮绑定事件，使用父子选择器选中
		$("#aTBody").on("click","a[name='relieveA']",function (){
			//获取数据
			var activityId = $(this).attr("activityId");
			var clueId = "${clue.id}"
			//发送请求
			$.ajax({
				url:'workbench/clue/deleteClueActivityRelationById.do',
				data:{
					activityId:activityId,
					clueId:clueId
				},
				type:'post',
				dataType:'json',
				success:function (data){
					//判断有没有删除成功
					if (data.code == "1"){
						//成功，删除需要删除的市场活动关联信息
						$("#tr_"+activityId).remove();

						//关闭模态窗口
						$("#bundModal").modal("hide");

					}else {
						//失败
						alert(data.message);
					}
				}
			})
		})

	});


	//查询市场活动的封装函数
	function selectRelationActivity(pageNo,pageSize){
		//收集参数
		var name = $("#activityName").val();
		var clueId = "${clue.id}";

		//判断有没有输入名称
		/*if (name == ""){
			alert("请输入要查询的名称");
			return;
		}*/

		//发送请求
		$.ajax({
			url:'workbench/clue/selectForClueRelationActivityAll.do',
			data:{
				name:name,
				clueId:clueId,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			dataType:'json',
			success:function (data){
				//不用判断直接展示
				var html = "";
				$.each(data.activityList,function (index,obj){
					html += "<tr>";
					html += "<td><input value=\""+obj.id+"\" type=\"checkbox\"/></td>";
					html += "<td>"+obj.name+"</td>";
					html += "<td>"+obj.startDate+"</td>";
					html += "<td>"+obj.endDate+"</td>";
					html += "<td>"+obj.owner+"</td>";
					html += "</tr>";
				})
				//拼接好了之后就要放到表单中
				$("#activityTBody").html(html);

				//在引用翻页创建的时候要计算页数
				var totalpages = 1;

				if (data.totalRows%pageSize==0){

					totalpages = data.totalRows/pageSize;

				}else {
					totalpages = parseInt(data.totalRows/pageSize)+1;
				}

				$("#demo_pag1").bs_pagination({

					//里面有很多参数
					currentPage:pageNo,//当前页号
					rowsPerPage:pageSize,//每页显示条数
					totalRows:data.totalRows,//总条数
					totalPages:totalpages,//总页数，必填框

					visiblePageLinks: 5,//最多显示多少个卡片数

					showGoToPage: true,//显示跳转页数的按钮
					showRowsPerPage: true,//每页显示条数
					showRowsInfo: true,//显示记录条数

					onChangePage:function (event,pageObj){
						/*alert(pageObj.currentPage);
						alert(pageObj.totalRows);*/
						selectRelationActivity(pageObj.currentPage,pageObj.rowsPerPage);
					}
				})
			}
		})
	}
	
</script>

</head>
<body>

	<!-- 关联市场活动的模态窗口 -->
	<div class="modal fade" id="bundModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" >关联市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" id="activityName" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td><input id="checkedAll"  type="checkbox"/></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activityTBody">
							<%--<tr>
								<td><input type="checkbox"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="checkbox"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>

				</div>

				<div id="demo_pag1"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="relationClueActivityBtn">关联</button>
				</div>
			</div>
		</div>
	</div>


	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
		<div class="modal-dialog" role="document" style="width: 40%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改备注</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-noteContent" class="col-sm-2 control-label">内容</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-noteContent"></textarea>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
				</div>
			</div>
		</div>
	</div>


	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${clue.fullname}${clue.appellation} <small>${clue.company}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" onclick="window.location.href='workbench/clue/selectForConVersionById.do?id=${clue.id}';"><span class="glyphicon glyphicon-retweet"></span> 转换</button>
			
		</div>
	</div>
	
	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.fullname}${clue.appellation}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.owner}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">公司</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.company}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">职位</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.job}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">邮箱</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.email}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.phone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">公司网站</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.website}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">手机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.mphone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">线索状态</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.state}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">线索来源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.source}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${clue.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${clue.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${clue.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${clue.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${clue.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${clue.contactSummary}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.nextContactTime}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 100px;">
            <div style="width: 300px; color: gray;">详细地址</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
					${clue.address}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
        </div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkDivList" style="position: relative; top: 40px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>

		<c:forEach items="${clueRemarkList}" var="crl">
			<div id="div_${crl.id}" class="remarkDiv" style="height: 60px;">
				<img title="${crl.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
				<div style="position: relative; top: -40px; left: 40px;" >
					<h5>${crl.noteContent}</h5>
					<font color="gray">线索</font> <font color="gray">-</font> <b>${clue.fullname}${clue.appellation}-${clue.company}</b> <small style="color: gray;"> ${crl.editFlag == "0"?crl.createTime:crl.editTime}由${crl.editFlag == "0"?crl.createBy:crl.createTime}${crl.editFlag == "0"?"创建":"修改"}</small>
					<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
						<a class="myHref" name='editA' remarkId="${crl.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a class="myHref" name='deleteA' remarkId="${crl.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
					</div>
				</div>
			</div>
		</c:forEach>
		<!-- 备注1 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">线索</font> <font color="gray">-</font> <b>李四先生-动力节点</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		
		<!-- 备注2 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">线索</font> <font color="gray">-</font> <b>李四先生-动力节点</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		--%>
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" id="saveRemarkBtn" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 市场活动 -->
	<div>
		<div  style="position: relative; top: 60px; left: 40px;">
			<div class="page-header">
				<h4>市场活动</h4>
			</div>
			<div  style="position: relative;top: 0px;">
				<table  class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>开始日期</td>
							<td>结束日期</td>
							<td>所有者</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="aTBody">
						<c:forEach items="${activityList}" var="a">
							<tr id="tr_${a.id}">
								<td>${a.name}</td>
								<td>${a.startDate}</td>
								<td>${a.endDate}</td>
								<td>${a.owner}</td>
								<td><a href="javascript:void(0);" name="relieveA" activityId="${a.id}" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
							</tr>
						</c:forEach>
						<%--<tr>
							<td>发传单</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
							<td>zhangsan</td>
							<td><a href="javascript:void(0);"  style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
						</tr>
						<tr>
							<td>发传单</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
							<td>zhangsan</td>
							<td><a href="javascript:void(0);"  style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
						</tr>--%>
					</tbody>
				</table>
			</div>
			
			<div >
				<%--data-toggle="modal" data-target="#bundModal"--%>
				<a href="javascript:void(0);" id="relationActivityBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联市场活动</a>
			</div>
		</div>
	</div>
	
	
	<div style="height: 200px;"></div>
</body>
</html>