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
<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){

		//调用查询的函数
		selectActivityAll(1,10);

		//给查询按钮绑定单击事件
		$("#selectActivityBtn").click(function (){

			selectActivityAll(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));

		})

		//给全选按钮绑定事件
		$("#checkedAll").click(function (){

			//$("#checkedAll").prop("checked");
			/*if (this.checked == true){
				$("#tBody input[type=checkbox]").prop("checked",true);
			}else {
				$("#tBody input[type=checkbox]").prop("checked",false);
			}*/
			$("#tBody input[type=checkbox]").prop("checked",this.checked);
		})

		//如果全部选框选中，就给全选选上
		/*$("#tBody input[type=checkbox]").click(function (){
			//每选中一个就先判断一下,
			if ($("#tBody input[type=checkbox]").size() == $("#tBody input[type=checkbox]:checked").size()){
				$("#checkedAll").prop("checked",true);
			}else {
				$("#checkedAll").prop("checked",false);
			}
		})*/
		$("#tBody").on("click","input[type='checkbox']",function(){
			//每选中一个就先判断一下,
			if ($("#tBody input[type=checkbox]").size() == $("#tBody input[type=checkbox]:checked").size()){
				$("#checkedAll").prop("checked",true);
			}else {
				$("#checkedAll").prop("checked",false);
			}
		})


		//为删除绑定单击事件
		$("#deleteActivityBtn").click(function (){

			if(window.confirm("你确定要删除吗？")){
				//有参数先收集参数
				var checkedIds = $("#tBody input[type='checkbox']:checked");
				//之后再判断有没有数据，没有就要提醒勾选数据
				if (checkedIds.size()==0){
					alert("请选择需要删除的数据！");
					return;
				}
				//能走到这里的就是正常的
				//循环遍历出来选中的id
				var ids = "";
				$.each(checkedIds,function (){
					ids = "id="+this.value+"&";
				})
				//拼接好之后就是这个样子id=xxx&id=xxxx&id=xxx&
				//这样就要把后面的&去掉
				ids = ids.substr(0,ids.length-1);

				//这样就拿到ids的数据，就能发送ajax请求删除数据
				$.ajax({
					url:'workbench/activity/deleteActivityById.do',
					data:ids,
					type:'post',
					dataType:'json',
					success:function (data){
						//先判断返回的数据，如果是正常删除就刷新市场活动列表
						if(data.code =="1"){
							//如果是的话就要刷新列表
							selectActivityAll(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
						}else {
							//其他情况显示原因
							alert(data.message);
						}
					}
				})
			}
		})

        //给修改绑定单击事件
        $("#editActivityModalBtn").click(function (){

        	//先获取数据
			var ids = $("#tBody input[type='checkbox']:checked");
			if (ids.size() == 0){
				//没有选择，提醒
				alert("请选择需要修改的活动！");
				return;
			}else if (ids.size() >1){
				//选多了。提醒
				alert("修改信息只能修改一条！");
				return;
			}
			var id = ids[0].value;

			//获取好数据，就发送ajxa请求
			$.ajax({
				url:'workbench/activity/selectActivityById.do',
				data:{
					id:id
				},
				type:'post',
				dataType:'json',
				success:function (data){
					//将数据展示到页面上
					$("#edit-id").val(data.id);
					$("#edit-owner").val(data.owner);
					$("#edit-name").val(data.name);
					$("#edit-startDate").val(data.startDate);
					$("#edit-endDate").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-description").val(data.description);

					$("#editActivityModal").modal("show");
				}
			})

        });

		//给保存按钮绑定单击事件
		$("#updateActivityBtn").click(function (){

			//先收集数据
			var id = $("#edit-id").val();
			var owner = $("#edit-owner").val();
			var name = $.trim($("#edit-name").val());
			var startDate = $("#edit-startDate").val();
			var endDate = $("#edit-endDate").val();
			var cost = $.trim($("#edit-cost").val());
			var description = $.trim($("#edit-description").val());

			if (owner == ''){
				alert("所有者不能为空！");
				return;;
			}
			if (name == ''){
				alert("市场活动名称不能为空！");
				return;
			}
			if (startDate !='' && endDate !=''){
				if (startDate >endDate ){
					alert("开始日期不能大于结束日期");
					return;
				}
			}

			/**
			 * 使用正则表达式判断成本的数值，成本的数值必须为正整数，然后不是就要跳出提示
			 */

			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本只能为正整数!");
				return;
			}



			//
			/*alert(id);
			alert(owner);
			alert(name);
			alert(startDate);
			alert(endDate);
			alert(cost);
			alert(description);*/
			//发送请求
			$.ajax({
				url:'workbench/activity/updateActivityById.do',
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data){

					//判断返回的数据
					if(data.code == "1"){
						//操作成功，刷新列表
						selectActivityAll(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
						$("#editActivityModal").modal("hide");
					}else {
						//操作失败，提醒一下，不做反应
						alert(data.message);
					}
				}
			})
		});


		//给创建绑定单击事件
		$("#createActivityModalBtn").click(function (){

			//打开模态窗口之前，需要把之前的信息删除掉,通过获取表单的id，使用reset方法删除表单中的信息
			//$("#createForm").get(0).reset(),这个也是可以删除表单中的信息
			$("#createForm")[0].reset();

            $("#createActivityModal").modal("show");
			/*$.ajax({

				url:"",
				type:"get",
				dataType:"json",
				success:function (data){

					var html = "<option></option>";

					$.each(data ,function (i,n){

						html += "<option value='"+n.id+"'>"+n.name+"</option>";

					})

					$("#create-marketActivityOwner").html(html);


				}
			})*/

		});

		//给保存按钮绑定单击事件
		$("#saveCreateActivityBtn").click(function (){

			//传参数，发请求，处理业务
			//获取数据
			var owner = $.trim($("#create-marketActivityOwner").val());
			var name = $.trim($("#create-marketActivityName").val());
			var startDate = $("#create-startDate").val();
			var endDate = $("#create-endDate").val();
			var cost = $.trim($("#create-cost").val());
			var description = $.trim($("#create-description").val());

			//获取之后就要判断需求有没有正确，表单验证
			if (owner == ""){

				alert("所有者不能为空!");
				return;

			}
			if (name == ""){
				alert("活动名称不能为空！");
				return;
			}
			//判断时间不能为空
			if (startDate != "" && endDate !=""){
				//在jsp中，这是一个弱类型的语言，可以直接使用字符串的大小代替日期的大小
				if(startDate > endDate){
					alert("开始日期不能比结束日期大！ ");
					return;
				}
			}
			/**
			 * 使用正则表达式判断成本的数值，成本的数值必须为正整数，然后不是就要跳出提示
			 */

			var regExp = /^(([1-9]\d*)|0)$/;//^(([1-9]\d*])|0)$/;

			if(!regExp.test(cost)){
				alert("成本只能是非负整数");
				return;
			}

			//判断完了之后就使用ajax发送请求，操作数据库，存储数据
			$.ajax({
				url:"workbench/activity/insertActivity.do",
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:"post",
				dataType:"json",
				success:function (data){
					if (data.code == "1"){
						//如果是等于1就是成功了
						//成功之后就要关闭模态窗口
						$("#createActivityModal").modal("hide");
						selectActivityAll(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));

					}else {
						//如果是等于0就是失败了
						//失败之后就要弹窗口，不要关闭模态窗口
						alert(data.message);
						$("#createActivityModal").modal("show");
					}
				}

			})
		});

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

		//为批量导出按钮绑定函数
		$("#exportActivityAllBtn").click(function (){
			//这里直接发送同步请求
			window.location.href ="workbench/activity/selectAllActivityDow.do";
		})

		//为导入按钮绑定单击事件
		$("#importActivityBtn").click(function (){
			//第一步收集数据
			var activityFileName = $("#activityFile").val();
			//截取后面三位字符，判断是不是xls文件
			var suffix = activityFileName.substr(activityFileName.indexOf(".")+1).toLocaleLowerCase();
			if (suffix != "xls"){
				alert("只支持xls文件");
				return;
			}
			//通过dow对象获取文件
			var activityFile = $("#activityFile")[0].files;
			//获取号文件之后就要对文件的大小进行判断
			if (activityFile.size > 5*1024*1024){
				alert("文件大小不能超过5MB！")
				return;
			}

			//FormData是ajax提供的接口,可以模拟键值对向后台提交参数;
			//FormData最大的优势是不但能提交文本数据，还能提交二进制数据
			var formData = new FormData();
			formData.append("activityFile",activityFile);
			formData.append("userName","张三");

			//发送ajax请求
			$.ajax({
				url:'workbench/activity/insertActivityByList.do',
				data:formData,
				processData:false,//设置ajax向后台提交参数之前，是否把参数统- 转换成字符串: true--是, false--不是，默认是true
				contentType:false,//设置ajax向后台提交参数之前，是否把所有的参数统一"按urlencoded编码: true--是，false--不是，默认是true
				type:'post',
				dataType:'json',
				success:function (data){
					if (data.code == "1"){
						//成功之后提示消息，导入多少条
						alert("成功导入"+data.reData+"条数据");
						//关闭模态窗口
						$("#importActivityModal").modal("hide");
						//刷新列表
						selectActivityAll(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
					}else {
						alert(data.message);
					}
				}
			})
		})
		
	});

	//封装成一个函数，根据条件查询到所有符合条件的市场活动列表
	function selectActivityAll(pageNo,pageSize){
		//先获取参数
		var name = $("#query-name").val();
		var owner = $("#query-owner").val();
		var startDate = $("#query-startDate").val();
		var endDate = $("#query-endDate").val();
		/*var pageNo=1;
		var pageSize=10;*/
		/*alert(name);
        alert(owner);
        alert(startDate);
        alert(endDate);*/

		//这个是查询，不需要进行验证，直接发送ajax请求查询
		$.ajax({
			url:'workbench/activity/selectActivityAll.do',
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type: 'post',
			dataType:'json',
			success:function (data){
				//显示查到多少条
				//$("#totalRowsB").html(data.totalRows);

				//直接展示市场活动列表
				//先将查询到的市场活动列表遍历出来，拼接好
				var html = "";
				$.each(data.activityList,function (index,obj){
					html += "<tr class=\"active\">";
					html += "<td><input type=\"checkbox\" value='"+obj.id+"' /></td>";
					html += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='detail.html';\">"+obj.name+"</a></td>";
					html += "<td>"+obj.owner+"</td>";
					html += "<td>"+obj.startDate+"</td>";
					html += "<td>"+obj.endDate+"</td>";
				})

				$("#tBody").html(html);

				//计算总页数
				var totalpages = 1;

				if (data.totalRows%pageSize==0){
					totalpages = data.totalRows/pageSize;
				}else {
					totalpages = parseInt(data.totalRows/pageSize)+1;
				}

				//调用下方的展示数据卡片时，对全选框进行取消
				$("#checkedAll").prop("checked",false);

				//demo_pag1
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
						selectActivityAll(pageObj.currentPage,pageObj.rowsPerPage);
					}
				})

			}
		})
	}
	
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" id="createForm" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label" >开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control_Date"  id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control_Date" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
									  <c:forEach items="${userList}" var="u">
										  <option value="${u.id}">${u.name}</option>
									  </c:forEach>
								</select>
							</div>
                            <label for="edit-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control_Date" id="edit-startDate" value="2020-10-10" readonly>
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control_Date" id="edit-endDate" value="2020-10-20" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner" >
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control_Date" type="text" id="query-startDate" readonly/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control_Date" type="text" id="query-endDate" readonly>
				    </div>
				  </div>
				  
				  <button type="button" id="selectActivityBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityModalBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityModalBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input id="checkedAll" type="checkbox" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>

				<div id="demo_pag1"></div>

			</div>
			
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
		
	</div>
</body>
</html>