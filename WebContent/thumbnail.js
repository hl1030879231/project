////页面大小参数
//var height = document.documentElement.clientHeight ; 
//var width = document.documentElement.clientWidth ; 
//var ViewHeight = height/4;
//var isHiden = false;
//var tableNum=0;
//var ViewStyle = document.getElementById("View");
//var chem;
//
//
////显示或隐藏缩略图
//function move(){
//	Hide.reverse();
//	Hide.play();
//}
//var Hide = anime({
//	targets: '#ViewAll',
//	translateY: -ViewHeight,
//	//easing: 'easeInOutSine',
//	autoplay: false
//	});
//
//////////////////页面数据获得模拟////////////////
//var text01,text02,text03;
//
//
//var confirm01 = document.getElementById("confirm01");
//var confirm02 = document.getElementById("confirm02");
////向识别缩略图内添加内容
////添加文字内容
//function addContext(str){
//	//text01 = document.getElementById("text01").value+" \n ";
//	ViewStyle.innerHTML+="<div class ='inputText'>"+str+"</div><br>";	
//}
//
////添加表格绘制
//function createTable() {
//	tableNum++;
//    tableNode = document.createElement("table"); //获得对象   
//    tableNode.setAttribute("id", "table"+tableNum);
//    tableNode.setAttribute("class", "table");
//    
//    text02 = document.getElementById("text02").value;
//	text03 = document.getElementById("text03").value;	
//	
//    var row = parseInt(text02); //获得行号 
//    //alert(row);   
//    if (row <= 0 || isNaN(row)) {
//        alert("输入的行号错误，不能创建表格，请重新输入：");
//        return;
//    }
//    var cols = parseInt(text03);
//    if (isNaN(cols) || cols <= 0) {
//        alert("输入的列号错误，不能创建表格，请重新输入：");
//        return;
//    }
//    //上面确定了 现在开始创建  
//    for (var x = 0; x < row; x++) {
//        var trNode = tableNode.insertRow();
//        for (var y = 0; y < cols; y++) {
//            var tdNode = trNode.insertCell();
//            tdNode.innerHTML = "单元格" + (x + 1) + "-" + (y + 1);
//        }
//    }
//    ViewStyle.appendChild(tableNode);  
//    ViewStyle.innerHTML+="<p>";
//    midOfView();
//}
//
//////表格删除行
////function delRow() {
////    //要删除行，必须得到table对象才能删除，所以在创建的时候必须要设置table对象的 id 方便操作 
////    var tab = document.getElementById("table"); //获得table对象  
////    if (tab == null) {
////        alert("删除的表不存在！")
////        return;
////    }
////    var rows = parseInt(document.getElementsByName("delrow1")[0].value); //获得要删除的对象  
////    if (isNaN(rows)) {
////        alert("输入的行不正确。请输入要删除的行。。。");
////        return;
////    }
////    if (rows >= 1 && rows <= tab.rows.length) {
////        tab.deleteRow(rows - 1);
////    } else {
////        alert("删除的行不存在！！");
////        return;
////    } 
////}
////
//////表格删除列
////function delCols() {
////    //获得table对象  
////    var tab = document.getElementById("table");
////    if (tab == null) {
////        alert("删除的表不存在！！");
////        return;
////    }
////    //获得文本框里面的内容    
////    var cols = parseInt(document.getElementsByName("delcols1")[0].value);
////    //检查是否可靠   
////    if (isNaN(cols)) {
////        alert("输入不正确。请输入要输出的列。。");
////        return;
////    }
////    if (!(cols >= 1 && cols < tab.rows[0].cells.length)) {
////        alert("您要删除的行不存在！！");
////        return;
////    }
////    for (var x = 0; x < tab.rows.length; x++) {//所有的行  
////        tab.rows[x].deleteCell(cols - 1);
////    } 
////}
//
////显示界面控制，始终保持当前输入内容显示
//function midOfView(){
//	ViewStyle.scrollTop = ViewStyle.scrollHeight;
//}
//
//
//getRecogResult = function(){//获取识别结果
//	$.ajax({
//		type : 'get',
//		url : '/chemical01/ServletTest',
//		data: {recog:"1"},
//		dataType : "text",
//		success : function(str){
//			//alert(data);
//			$("#View").empty();
//			chem=str;	
//			//addContext(str);
//		},
//		error : function(e) {
//			alert("异常！");
//		}
//	});
//}
//
//
