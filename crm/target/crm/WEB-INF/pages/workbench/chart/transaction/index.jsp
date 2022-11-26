<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath =request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">

	<%--引入jquery插件--%>
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<%--引入echars插件--%>
	<script type="text/javascript" src="jquery/echarts/echarts.min.js"></script>

	<script type="text/javascript">

		$(function (){

			//加载完毕之后就直接发送请求
			$.ajax({
				url:'workbench/chart/transaction/selectCountOfTranGroupByStage.do',
				type:'post',
				dataType:'json',
				success:function (data){
					//查询到了之后
					//就调用插件显示图标
					//第一步调用容器
					var myChart = echarts.init(document.getElementById("main"));

					var option = {
						title: {
							text: '交易统计图表',
							subtext: '交易表中个阶段的数量'
						},
						tooltip: {
							trigger:'item',
							formatter:"{a} <br/>{b} : {c}"
						},
						toolbox:{
							feature:{
								dateView:{readOnly :false},
								restore:{},
								saveAsImage:{}
							}
						},
						series: [
							{
								name: '数量',
								type: 'funnel',
								left:'10%',
								width:'80%',
								label:{
									formatter: '{a}'
								},
								labelLine:{
									show:true
								},
								itemStyle:{
									opacity:0.7
								},
								emphasis:{
									label: {
										position:'inside',
										formatter:'{b}: {c}'
									}
								},
								data: data
							}
						]
					};

					// 使用刚指定的配置项和数据显示图表。
					myChart.setOption(option);
				}
			})
		})

	</script>

</head>
<body>
	<div id="main" style="width: 600px;height: 400px;"></div>
</body>
</html>