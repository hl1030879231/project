var COLOURS = '#ffffff';
var radius = 0;
var startDraw = 0;
var specialCharLabel = ['Ｔ', 'ｑ', '⊕', 'ｅ', 'ｋ', 'Ｈ', '５', '＋', 'ｂ', '７', '４', 'ｗ', 'Ｙ', 'ｓ', 'Ｍ', 'ｍ', 'Ｒ', '↓', '＜', '［', '８', '２', 'ｙ', '→', 'Ｏ', '６', 'ｉ', '＝', 'Ａ', '）', 'ｎ', 'ｘ', 'Ｎ', 'Ｉ', 'Ｌ', '←', 'Ｑ', 'ｐ', '＃', '９', 'a', 'Ｆ', 'ｄ', '＊', 'ｔ', '｝', '≤', 'ｕ', 'Ｊ', 'ｆ', 'Ｇ', '＞', 'ｇ', '（', '３', '１', '↑', 'Ｄ', '÷', '？', '－', 'Ｂ', 'ｊ', 'ｌ', 'Ｕ', 'Ｅ'];
var charLabel2char = {'５':'5', '７':'7', '４':'4', '８':'8', '２':'2','６':'6', '９':'9',  '３':'3', '１':'1', 'Ｈ':'H'}; // 编码转换
var pointCoodi=[];
var times=0;
var signal=0;
var result = [];
var hidetimes=0;
var height = document.documentElement.clientHeight ; 
var width = document.documentElement.clientWidth ; 
var ViewHeight = 4*height/5;
var ViewWidth= 3*width/10;
var tableNum=0;
var tableNumDraw=0;
var ViewStyle = document.getElementById("View");
var container = document.getElementById("areaDraw");
var element = document.getElementById("draw");
var DrawTableStyle = document.getElementById("tableView");
var chem;
var id;
var idChosen;
var isTable = false;
var isText = true;
var testRec = ["3", "ｘ", "2", "+", "+", "-", "-", "+", "+", "-", "-"];
var testend = -1;
var stroke = -1;
var strokelist = [];
var strokend = [];

var writeType = 1;
var writetype = true;


var isNextLine = false;
var canvasChosen;
var getTime = 400;
var mouseX,mouseY;
var ctx;
var handler;

var pointCoodi_copy = [];
var charlist = []; 
var cell_char_list = {};
var lineNum = 0;
//spf11-15
var isHide = true;
//11-15
document.write("<script src='balance.js'></script>");
document.oncontextmenu = function(e){
	return false;
};

//1122
var pointX = 0;
var isShow = false;
var tmpStr = "";

//1025
var isBlankLine = false;
//1030
var canvasNum = 0;
//画布创建与笔迹位置坐标存储
createDrawArea();
function createDrawArea(){
	
	canvasNum++;
	//1030
	var PreLineY;
	var doDraw = Sketch.create({
		container: document.getElementById("areaDraw"),	
	    autoclear: false,
		retina: 'auto',
	    setup: function() { 	
			PreLineY = 0;
	    },
	    update: function() {
	        radius = 1 + abs( sin( this.millis * 0.003 ) * 4 );
	    },

	    keydown: function() {
	        if ( this.keys.C ) this.clear();//清除页面
	    },
	    touchstart:function(){
	    	if (event.button == 2 && charlist.length>0){
				startDraw = 0;
				
				deleteMark(this);
				delContext(canvasChosen);
				pointCoodi=[];
			}
//	    	else if(event.button == 1)
//			{
//				writetype=!writetype; 
//				change();
//				pointCoodi=[];
//			}  
	    	
	    	//1025
	    	else if(event.button == 0){	    		
	    		startDraw=1;
	    		//console.log(stroke);
	    		//1025
	    		//console.log("this.mouse.oy"+this.mouse.oy);
	    		
	    		if(this.mouse.oy > (PreLineY+20)){  			
	    			isNextLine = true;
	    			//1130
		    		pointX = this.mouse.ox;
		    		
		    		//1130
	    			
					var line = findchem_equa();
	    			if(line != -1){
//	    				console.log("runshow" );
						var chem = getChem(line);
						var split_list = split_chem(chem); // 拆分化学方程式字符串变为数组
						var yuan_coeff = get_coeff(chem);
						var cut_chem = cut_coeff(chem);
						var balance_coeff = solve(cut_chem);
						var coeff_diff_list = coeff_diff(yuan_coeff, balance_coeff);
						var format_chem_list = format_chem(split_list);
						console.log(yuan_coeff);
						console.log(balance_coeff);
						console.log(coeff_diff_list);
						var red_list = turn_red(format_chem_list, coeff_diff_list);
						var left = red_list[0].join("+");
						var right = red_list[1].join("+");
						var res = left + "=" + right;
						console.log("标红且规范化： "+ res);
	    				showLatex(res);
	    			}
	    			if((this.mouse.oy-PreLineY)>height/12){
	    				isBlankLine =true;
	    			}	    			
	    		}
	    		//1130
    			if((this.mouse.ox-pointX)>width/10){   				
    				isShow = true; 				
    			}
    			//1130    			
	    		isTable = false;
	    		clearTimeout(handler);
	    	}
	    	
	    	
	    },
	    touchmove: function() {
			//console.log(this);
	        if(startDraw){
				// 12.31按照笔迹修改，先clear canvas内容，再画除最后一个字的所有的笔迹
	            for ( var i = this.touches.length - 1, touch; i >= 0; i-- ) {
				touch = this.touches[i];
				strokelist.push({x:touch.x, y:touch.y, ox:touch.ox, oy:touch.oy}); // 很诡异，直接添加touch对象发现数组里存的不是当前的touch，改存创建的对象解决
	            this.lineCap = 'round';
	            this.lineJoin = 'round';
	            this.fillStyle = this.strokeStyle = COLOURS;
	            this.lineWidth = radius;
	            this.beginPath();
	            this.moveTo( touch.ox, touch.oy );
	            this.lineTo( touch.x, touch.y );
//	            this.arc(touch.x,touch.y,10,0,2*Math.PI);
	            this.stroke();
				pointCoodi.push({x:touch.ox,y:touch.oy});
				pointCoodi_copy.push({x:touch.ox,y:touch.oy});
				}
	            if(this.mouse.oy>PreLineY){
	            	//console.log(PreLineY);
	            	PreLineY = this.mouse.oy;
	            }
	            
	            if(this.mouse.ox>pointX){
	            	//console.log(PreLineY);
	            	pointX = this.mouse.ox;
	            }
	        }	        
	    },
	    touchend:function(){
			startDraw=0;
			mouseX = this.mouse.ox;
			mouseY = this.mouse.oy;
			pointCoodi.push({x:this.mouse.ox,y:this.mouse.oy})
			//1030
			console.log("PreLineY"+PreLineY);
			
			pointCoodi_copy.push({x:this.mouse.ox,y:this.mouse.oy});
			//strokeList.push(pointCoodi_copy);
			//strokelist[stroke].push({x:this.mouse.ox,y:this.mouse.oy});
			//console.log(strokelist[stroke]);
			//console.log(strokelist);
			if( event.button != 1&&event.button !=2 ){
				postCoodi(); 				
					handler=setTimeout(function(){
						if(writetype){
							getRecogResult();
						}else{
							getRecogResultElse();
						}			
						clearTimeout(handler);
					},getTime);
		}		
	    }

	});

}


//滑动显示或隐藏缩略图
function move(){
	if(hidetimes!=0){
		Hide.reverse();
		Hide2.reverse();
	}	
	Hide.play();
	Hide2.play();
	hidetimes++;
//	console.log("hidetimes01: "+hidetimes);
}


//1025add
var Hide = anime({
	targets: '#ViewAll',
	translateX: function(){
		return ViewWidth/4;		
	},
	translateY: function(){
		return -ViewHeight/4;
	},
	scale:0.5,
	autoplay: false,	
	});
//1025
var Hide2 = anime({
	targets: '#img02',
//	rotate: function(){
//		return 60;
//	  },
	autoplay: false,
});

//1020add

//页面数据获得模拟//
var text02,text03;
var confirm02 = document.getElementById("confirm02");

//向识别缩略图内添加内容
//添加文字内容//1025
function addContext(str,canvasChosen){
	if(!isTable){							
		if(isNextLine){
			tmpStr = "";
			lineNum++;
			if(lineNum == 2||isBlankLine){
				ViewStyle.innerHTML+="<br>";
				isBlankLine = false;
			}	
			ViewStyle.innerHTML+="<div id=lineNum"+lineNum+"></div>";
			isNextLine = false;
		}		
		var thisline = document.getElementById("lineNum"+lineNum);
		//1130
		
		tmpStr += str;
		// 12.31注释
		// if (tmpStr.includes(' ')){			
		// 	thisline.innerHTML="";
		// 	var linestr = tmpStr.split(" ");	
		// 	console.log(linestr);
		// 	for(var n = 0;n < linestr.length;n++){	
		// 		console.log("linestr[n]"+linestr[n]);
		// 		linestr[n]=changeChar(linestr[n]);
		// 		console.log("changeChar: "+linestr[n]);
				
		// 		linestr[n]="<button id = 'model' onclick = 'getViewCoodi(this)'>"+linestr[n]+"</button>";
		// 		thisline.innerHTML+=linestr[n];
		// 	}				
		// 	console.log(linestr);
		// 	}
		// else{
		// 	thisline.innerHTML+=str;
		// }
		//1130
		thisline.innerHTML += str;
		midOfView();
	}
	else{	
//		console.log(canvasChosen.id);
		var canvasId = canvasChosen.id;
		var CellId = "Cell"+canvasId;
//		console.log(CellId);
		var chosenCell = document.getElementById(CellId);
		chosenCell.innerHTML+=str;
	}	
}


//添加预览表格
function createTable(row, cols) {
	
    tableNode = document.createElement("table"); //获得对象   
    tableNode.setAttribute("id", "table"+tableNum);
    tableNode.setAttribute("class", "table");
    	
	//alert(row);  
    if (row <= 0 || isNaN(row)) {
        alert("输入的行号错误，不能创建表格，请重新输入：");
        return;
    }
    if (isNaN(cols) || cols <= 0) {
        alert("输入的列号错误，不能创建表格，请重新输入：");
        return;
	}
    //上面确定了 现在开始创建  
    for (var x = 0; x < row; x++) {
        var trNode = tableNode.insertRow();
        for (var y = 0; y < cols; y++) {
            var tdNode = trNode.insertCell();
            tdNode.setAttribute("id","CellToCanvas"+(x + 1) + " " + (y+1))
            if(x==0){
            	tdNode.style.fontSize = "25px";
            	tdNode.style.fontWeight = "bold";
            }
            tdNode.style.borderColor="#363433";
            // tdNode.innerHTML = "单元格" + (x + 1) + "-" + (y + 1);
        }
    }
    ViewStyle.appendChild(tableNode);  
    ViewStyle.innerHTML+="<p>";
    midOfView();
    
}

//可获得点击单元格的id/数据
//function getColumnDetail(object){ 
//	isTable = true;
//	isText = false;
//	object.style.background="grey";
//	if (id != null)
//	{
//		idBefore=id;
//		var cellBefore=document.getElementById(idBefore);//清楚前一个选择的cell的样式
//		cellBefore.style.background="white";
//	}
//	id = object.id;  //弹出被点单元格里的内容
//	
//	
//}

// 表格添加列
function addColumn()
{
	var table1 = document.getElementById("tableDraw"+ tableNum);
	var table2 = document.getElementById("table" + tableNum);
	
	addColumntable(table1);
	addColumntable(table2);
}
function addColumntable(table){
	var r = table.rows.length;
	var c = table.rows[0].cells.length;
	for(i=0; i<r; i++)
	{
		var trNode = table.rows[i];
		var tdNode = trNode.insertCell();
		tdNode.setAttribute("id","CellToCanvas"+(i + 1) + " " + (c+1));
		 if(table.id == "tableDraw"+ tableNum){
			tdNode.style.border = "5px solid";
			tdNode.style.borderColor = "#ffffff"; 
			 var canvas = document.createElement("canvas");
			  	canvas.setAttribute("class", "canvasOfTable");
				canvas.setAttribute("id", "ToCanvas"+(i + 1) + " " + (c+1));
				cellMonitor(canvas,tdNode);
		  }else {
				//1026
				tdNode.style.border = "2px solid";
				tdNode.style.borderColor = "#363433";
		  }	
		
	}
  }

// 表格删除列
function delColumn()
{
	var table1 = document.getElementById("tableDraw"+ tableNum);
	var table2 = document.getElementById("table" + tableNum);
	deleteColumntable(table1);
	deleteColumntable(table2);
}
function deleteColumntable(table)
    {
      var r = table.rows.length;
      var c = table.rows[0].cells.length;
      for(i=0; i<r; i++)
      {
        table.rows[i].deleteCell(c-1);
      }
    }

// 表格添加行
function addRow()
{
	var table1 = document.getElementById("tableDraw"+ tableNum);
	var table2 = document.getElementById("table" + tableNum);
	addRowtable(table1);
	addRowtable(table2)
}
function addRowtable(table){
	var c = table.rows[0].cells.length;
	var r = table.rows.length;
	table.insertRow(r);
	for(j=0; j<c; j++)
	{
	  var tdNode = table.rows[r].insertCell(j);
	  tdNode.setAttribute("id","CellToCanvas"+(r + 1) + " " + (j + 1));
	  
	  if(table.id == "tableDraw"+ tableNum){
		 tdNode.style.border = "5px solid";
		 tdNode.style.borderColor = "#ffffff";
			
		  var canvas = document.createElement("canvas");
		  canvas.setAttribute("class", "canvasOfTable");
			canvas.setAttribute("id", "ToCanvas"+(r+1)+" "+(j+1));
			cellMonitor(canvas,tdNode);
	  }else {
			//1026
			tdNode.style.border = "2px solid";
			tdNode.style.borderColor = "#363433";
	  }	
	 
		
	}
  }

  // 表格删除行
function delRow()
{
	var table1 = document.getElementById("tableDraw"+ tableNum);
	var table2 = document.getElementById("table" + tableNum);
	delRowtable(table1);
	delRowtable(table2)
}
function delRowtable(table)
  {
	var r = table.rows.length;
	table.deleteRow(r-1);
  }

//View控制，区域长度View.height，始终滑动条保持当前输入内容显示
function midOfView(){
	ViewStyle.scrollTop = ViewStyle.scrollHeight;
}
//页面控制，页面滚轮移到表格位置
function midOfDraw(positionX, positionY){
	window.scrollTo(0,positionY);
}

//发送点坐标函数
postCoodi = function(){
	var json = JSON.stringify(pointCoodi);
	times++;
	pointCoodi_copy=[];
	$.ajax({//向服务器发出请求的方法
		type:"post",
		url:"/project/index",//向服务器请求的url
		data:{coodi:json,times:times},//注意，就算不需要发送数据给后端也要有data
		dataType:'text',//服务器响应的数据类型
		success:function(){//请求服务器成功后返回页面时页面可以进行处理，data就是后端返回的数据
			//console.log(pointCoodi);		
			pointCoodi=[];
		},
		error:function(e){
			alert("postCoodi异常"+e.responseText);
		}
		});
}
function cellContext(id, str){
	if(id!=null){
		var cell = document.getElementById(id);
		console.log(cell);
		cell.innerHTML +="<div class ='inputText'>"+str+"</div><br>";
		midOfView();
	}
	
	
}


getRecogResult = function(){
	charlist.push(strokelist);
	strokelist = [];
	$.ajax({
		type : 'get',
		url : '/project/index',
		data: {recog:"CHN"},
		dataType : "text",
		success : function(str){
			chem=str;
			result.push(chem);
			strokend = []
			//console.log("get success: "+str);
			console.log("result: " + result)
			if(str == ('w')){
				del();
			}else{
				addContext(str,canvasChosen);
			}
		},
		error : function(e) {
			alert("getRecogResult异常！"+e.responseText);
		}
	});
}



//spf1115
//ch2019-11-13  添回show.js(绘制有机物分子结构),three.js
getViewCoodi = function(button){//获取有机物绘制分子信息
	$.ajax({
		type : 'get',
		url : '/project/index',
		data : {name:button.innerHTML},
		dataType : "text",
		success : function(str_modle){				
			var data=JSON.parse(str_modle);
			var moles=eval('data.moles');
			var bonds=eval("data.bonds");
			if(isHide){
				Show(moles,bonds);
				isHide=!isHide;
			}else{
				removeMod();
				isHide=!isHide;
			}
		},
		error : function(e) {
			alert("异常！"+e.responseText);
		}
	});
}
//ch2019-11-13



//原10-25版本
/*getRecogResultElse = function(){
	charlist.push(strokeList);
	strokeList = [];
	console.log("“ª∏ˆ◊÷∑˚–¥ÕÍ¡À");
	$.ajax({
		type : 'get',
		url : '/project/index',
		data: {recog:"1"},
		dataType : "text",
		success : function(str){
			chem=str;
			result.push(chem);
			strokend = []
			console.log("get success: "+str);
			addContext(str,canvasChosen);
			if(!isTable){
				var res = result.length;
				console.log("当前长度：" + res);
				console.log("当前识别结果：" + result[res-1]);
				if (result[res-2] == "ｘ"){
					console.log('前一位是乘号');
					console.log("行： " + result[res-3]);
					console.log("列： " + result[res-1]);
					var r = Number(charLabel2char[result[res-3]]);
					var c = Number(charLabel2char[result[res-1]]);
					createDoubleTable(r,c,mouseX,mouseY);
				} // 创建表格
				
				
			}
			if (tableNum > 0){
				console.log("table 数"+ tableNum);
				console.log("this "+result[res-1]);
				if (result[res-1] == '＋' || result[res-1] == '－')
				{
				var tableDraw = document.getElementById("tableDraw"+ tableNum);
				 //console.log(tableDraw);
				var xright = tableDraw.offsetWidth + tableDraw.offsetLeft;
				var ybottom = tableDraw.offsetHeight + tableDraw.offsetTop;
				var xleft = tableDraw.offsetLeft;
				var ytop = tableDraw.offsetTop;
				var n = strokelist[stroke].length;
				var n1 = strokelist[stroke-1].length;
				if (mouseX > xright && mouseY <ybottom && mouseY > ytop ){  // 还需要加判断识别字符是加号
					if (result[res-1] == '－')
					{
						var x1 = strokelist[stroke][0].x;
						var x2 = strokelist[stroke][n-1].x;
						var y1 = strokelist[stroke][0].y;
						var y2 = strokelist[stroke][n-1].y;
						var ymin1 = Math.min(y1, y2);
						var xmin1 = Math.min(x1, x2);
						ctx.clearRect(xmin1-5, ymin1-5, Math.abs(x2 - x1) + 15, Math.abs(y2 - y1) + 15);
						delColumn();
					}
					if (result[res-1] == '＋')
					{
						var x1 = strokelist[stroke-1][0].x;
						var x2 = strokelist[stroke-1][n1-1].x;
						var y1 = strokelist[stroke][0].y;
						var y2 = strokelist[stroke][n-1].y;
						ctx.clearRect(x1-5, y1-5, Math.abs(x2 - x1) + 15, Math.abs(y2 - y1) + 15);
						addColumn();
					}
				}

				if (mouseX > xleft && mouseX <xright && mouseY > ybottom ){  // 还需要加判断识别字符是加号
					if (result[res-1] == '－')
					{
						var x1 = strokelist[stroke][0].x;
						var x2 = strokelist[stroke][n-1].x;
						var y1 = strokelist[stroke][0].y;
						var y2 = strokelist[stroke][n-1].y;
						var ymin1 = Math.min(y1, y2);
						var xmin1 = Math.min(x1, x2);
						ctx.clearRect(xmin1-5, ymin1-5, Math.abs(x2 - x1) + 15, Math.abs(y2 - y1) + 15);
						delRow();
					}
					if (result[res-1] == '＋')
					{
						var x1 = strokelist[stroke-1][0].x;
						var x2 = strokelist[stroke-1][n1-1].x;
						var y1 = strokelist[stroke][0].y;
						var y2 = strokelist[stroke][n-1].y;
						ctx.clearRect(x1-5, y1-5, Math.abs(x2 - x1) + 15, Math.abs(y2 - y1) + 15);
						addRow();
					}
				}
				}
				}
			
			console.log("result: " + result);
			if(str == ("x"||"X")&&isTable)
				clearCellInner(canvasChosen);
		},
		error : function(e) {
			alert("getRecogResult异常！"+e.responseText);
		}
	});
}*/

//wx10-26改
getRecogResultElse = function(){
	charlist.push(strokelist);
	strokelist = [];
	$.ajax({
		type : 'get',
		url : '/project/index',
		data: {recog:"1"},
		dataType : "text",
		success : function(str){
			chem=str;
			result.push(chem);
			strokend = [];
			console.log("get success: "+str);
			//console.log("执行else不是table");	
			if(str == ('ｗ')){
				del();
			}else{
				if(str=='（') str = 'C';
				if(isShow){
					addContext(" ",canvasChosen);
					isShow = false;
				}
				addContext(str,canvasChosen); // 当不是表格正常添加进右边canvas
				if(!isTable){
					var res = result.length;
					console.log("当前长度：" + res);
					console.log("当前识别结果：" + result[res-1]);
					if (result[res-2] == "ｘ"){
						console.log('前一位是乘号');
						console.log("行： " + result[res-3]);
						console.log("列： " + result[res-1]);
						var r = Number(charLabel2char[result[res-3]]);
						var c = Number(charLabel2char[result[res-1]]);
						// 删除右边的3*2
						delContext(canvasChosen);
						delContext(canvasChosen);
						delContext(canvasChosen);
						createDoubleTable(r,c,mouseX,mouseY);
					} // 创建表格
				}
				if (tableNum > 0){
					console.log("table 数"+ tableNum);
					console.log("this "+result[res-1]);
					if (result[res-1] == '＋' || result[res-1] == '－')
					{
						delContext(canvasChosen);	
					var tableDraw = document.getElementById("tableDraw"+ tableNum);
					 //console.log(tableDraw);
					var xright = tableDraw.offsetWidth + tableDraw.offsetLeft;
					var ybottom = tableDraw.offsetHeight + tableDraw.offsetTop;
					var xleft = tableDraw.offsetLeft;
					var ytop = tableDraw.offsetTop;
					var n = strokelist[stroke].length;
					var n1 = strokelist[stroke-1].length;
					if (mouseX > xright && mouseY <ybottom && mouseY > ytop ){  // 还需要加判断识别字符是加号
						if (result[res-1] == '－')
						{
							var x1 = strokelist[stroke][0].x;
							var x2 = strokelist[stroke][n-1].x;
							var y1 = strokelist[stroke][0].y;
							var y2 = strokelist[stroke][n-1].y;
							var ymin1 = Math.min(y1, y2);
							var xmin1 = Math.min(x1, x2);
							ctx.clearRect(xmin1-5, ymin1-5, Math.abs(x2 - x1) + 15, Math.abs(y2 - y1) + 15);
							delColumn();
						}
						if (result[res-1] == '＋')
						{
							var x1 = strokelist[stroke-1][0].x;
							var x2 = strokelist[stroke-1][n1-1].x;
							var y1 = strokelist[stroke][0].y;
							var y2 = strokelist[stroke][n-1].y;
							ctx.clearRect(x1-5, y1-5, Math.abs(x2 - x1) + 15, Math.abs(y2 - y1) + 15);
							addColumn();
						}
					}

					if (mouseX > xleft && mouseX <xright && mouseY > ybottom ){  // 还需要加判断识别字符是加号
						if (result[res-1] == '－')
						{
							var x1 = strokelist[stroke][0].x;
							var x2 = strokelist[stroke][n-1].x;
							var y1 = strokelist[stroke][0].y;
							var y2 = strokelist[stroke][n-1].y;
							var ymin1 = Math.min(y1, y2);
							var xmin1 = Math.min(x1, x2);
							ctx.clearRect(xmin1-5, ymin1-5, Math.abs(x2 - x1) + 15, Math.abs(y2 - y1) + 15);
							delRow();
						}
						if (result[res-1] == '＋')
						{
							var x1 = strokelist[stroke-1][0].x;
							var x2 = strokelist[stroke-1][n1-1].x;
							var y1 = strokelist[stroke][0].y;
							var y2 = strokelist[stroke][n-1].y;
							ctx.clearRect(x1-5, y1-5, Math.abs(x2 - x1) + 15, Math.abs(y2 - y1) + 15);
							addRow();
						}
					}
					} // 当是表格且出现加号或减号时，不加进去
					/*else{
						console.log("执行else");
						addContext(str,canvasChosen);
					}*/
					}
			}			
			
			
		},
		error : function(e) {
			alert("getRecogResult异常！"+e.responseText);
		}
	});
}


function createDoubleTable(row, col ,positionX ,positionY){
	tableNum = tableNum + 1;
	isTable = true;
	createTableCanvas(row,col,positionX,positionY);
	createTable(row, col);
	midOfDraw(positionX,positionY);
}


function special(){
	for (i of specialCharLabel.values()){
    	document.body.appendChild(i);
}
}

function createTableCanvas(row, cols,positionX,positionY) {
	//doDraw.stop();
	// tableNum++;
    var tableNode = document.createElement("table"); //获得对象   
    tableNode.setAttribute("id", "tableDraw"+tableNum);
    tableNode.setAttribute("class", "table");
    	
	//alert(row);   
    if (row <= 0 || isNaN(row)) {
        alert("输入的行号错误，不能创建表格，请重新输入：");
        return;
    }
    if (isNaN(cols) || cols <= 0) {
        alert("输入的列号错误，不能创建表格，请重新输入：");
        return;
	}
    //上面确定了 现在开始创建  
    for (var x = 0; x < row; x++) {
        var trNode = tableNode.insertRow();
        for (var y = 0; y < cols; y++) {
			var tdNode = trNode.insertCell();
			tdNode.setAttribute("id", (x+1)+" "+(y+1)+"tablecanvas"+tableNum);
			var canvas = document.createElement("canvas");
			canvas.setAttribute("class", "canvasOfTable");
			canvas.setAttribute("id", "ToCanvas"+(x+1)+" "+(y+1));
			cellMonitor(canvas,tdNode);
			tdNode.style.border = "5px solid";
			tdNode.style.borderColor = "#ffffff";
			// tdNode.innerHTML = "单元格" + (x + 1) + "-" + (y + 1);
        }
    }
    //tableNode.style.background = "grey";
    
    document.body.appendChild(tableNode);  
    tableNode.style.position="absolute";
    tableNode.style.left= "10px";
    tableNode.style.top = 70+positionY+"px";
    tableNode.style.zIndex = 10;
    //tableNode.style.background = "grey"
    ViewStyle.innerHTML+="<p>";
    midOfView();
}


/*function cellMonitor(elementCanvas,containerCanvas)*/
{
/*	var doDraw1 = Sketch.create({
		container: containerCanvas,
		element:elementCanvas,
		autoclear: false,
		retina: 'auto',
		setup: function() { 
			//console.log("表格中container " + containerCanvas.id)
		},
		update: function() {
			radius = 2 + abs( sin( this.millis * 0.003 ) * 2 );
			//console.log(container.clientHeight||container.offsetHeight);
		},
	
		keydown: function() {
			if ( this.keys.C ) this.clear();//清除页面
		},
		touchstart:function(){
			console.log(elementCanvas.id);
			startDraw=1;
			isTable = true;
			canvasChosen = elementCanvas;
			//pointCoodi.push({x:this.mouse.ox,y:this.mouse.oy});
		},
		touchmove: function() {
			if(startDraw && event.button != 2){
				for ( var i = this.touches.length - 1, touch; i >= 0; i-- ) {
				touch = this.touches[i];
				//console.log(touch);
				this.lineCap = 'round';
				this.lineJoin = 'round';
				this.fillStyle = this.strokeStyle = COLOURS;
				this.lineWidth = radius;
				this.beginPath();
				this.moveTo( touch.ox, touch.oy );
				this.lineTo( touch.x, touch.y );
				this.stroke();
				pointCoodi.push({x:touch.ox,y:touch.oy});
				}
			}   
		},
		touchend:function(){
			startDraw=0;
			var mouseX = this.mouse.ox;
			var mouseY = this.mouse.oy;
			pointCoodi.push({x:this.mouse.ox,y:this.mouse.oy})
			if(pointCoodi.length!=0)
				postCoodi();
			
			if(signal==0){
				signal=1;//互斥信号量保证同时只有一个函数在等待执行
				handler=setTimeout(function(){
					signal=0;				
					cellContext(id, specialCharLabel[10]);
					clearTimeout(handler);
					if(writetype){
						getRecogResult();
					}else{
						getRecogResultElse();
					}
				},getTime);//test，延迟1.5秒进行笔迹路径post和识别执行
			}
		}
	
	});
*/}

function cellMonitor(elementCanvas,containerCanvas)
{
	var id = elementCanvas.id;
	console.log("this id is : "+id);
	var cell_point = [];
	var cell_stroke = [];
	cell_char_list[id] = [];
	//1030
//	containerCanvas.ondblclick = function() {
//		clearTimeout(handler);
//		pointCoodi=[];
//		writetype=!writetype; 
//		change();
//	};
	var doDraw1 = Sketch.create({
		container: containerCanvas,
		element:elementCanvas,
		autoclear: false,
		retina: 'auto',
		setup: function() { 
			//console.log("表格中container " + containerCanvas.id)
		},
		update: function() {
			radius = 2 + abs( sin( this.millis * 0.003 ) * 2 );
			//console.log(container.clientHeight||container.offsetHeight);
		},
	
		keydown: function() {
			if ( this.keys.C ) this.clear();//清除页面
		},
		touchstart:function(){	
			canvasChosen = elementCanvas;
//			if (event.button == 2 && cell_char_list[id].length > 0){
//				console.log("点击右键事件");
//				deleteCellMark(id);
//				delContext(canvasChosen);
//			}
//			else if(event.button == 1)
//			{
//				writetype=!writetype; 
//				change();
//				console.log("writetype:"+writetype);
//				pointCoodi=[];
//				console.log(pointCoodi.length);
//			}  
//			else if(event.button == 0){
				clearTimeout(handler);
				console.log(elementCanvas.id);
				startDraw=1;
				isTable = true;	
				idChosen = id;
//			}
			
			//pointCoodi.push({x:this.mouse.ox,y:this.mouse.oy});
		},
		touchmove: function() {
			if(startDraw){
				for ( var i = this.touches.length - 1, touch; i >= 0; i-- ) {
				touch = this.touches[i];
				//console.log(touch);
				this.lineCap = 'round';
				this.lineJoin = 'round';
				this.fillStyle = this.strokeStyle = COLOURS;
				this.lineWidth = radius;
				this.beginPath();
				this.moveTo( touch.ox, touch.oy );
				this.lineTo( touch.x, touch.y );
				this.stroke();
				pointCoodi.push({x:touch.ox,y:touch.oy});
				cell_point.push({x:touch.ox,y:touch.oy});
				}
			}   
		},
		touchend:function(){
			startDraw=0;
			var mouseX = this.mouse.ox;
			var mouseY = this.mouse.oy;
			pointCoodi.push({x:this.mouse.ox,y:this.mouse.oy});
			cell_point.push({x:this.mouse.ox,y:this.mouse.oy});
			cell_stroke.push(cell_point);
						
//			if(event.button != 1&&event.button !=2)
//			{
				postCoodi();
				cell_point = [];
				handler=setTimeout(function(){	
					if(writetype){
						getRecogResult();
						cell_char_list[id].push(cell_stroke);
						cell_stroke = [];
					}else{
						getRecogResultElse();
						cell_char_list[id].push(cell_stroke);
						cell_stroke = [];
					}	
					
					
					if(writeType == 1){
						
					}else{
						
					}
					clearTimeout(handler);
				},getTime);
//			}
			
		}
	});
}
//改变识别状态图
function change(){
	var changeimg = document.getElementById("change");
	if(writetype) changeimg.setAttribute("src","img/img04.png");
	else changeimg.setAttribute("src","img/img05.png");
}

function clearCellInner(canvasChosen){
    var cxt=canvasChosen.getContext("2d");  
    cxt.clearRect(0,0,canvasChosen.width,canvasChosen.height);  
    
    var canvasId = canvasChosen.id;
	var CellId = "Cell"+canvasId;
	var chosenCell = document.getElementById(CellId);
	chosenCell.innerHTML = "";	
	
//	var cxt=canvasChosen.getContext("2d");  
//	var imgData = cxt.getImageData(0,0,canvasChosen.width,canvasChosen.height);
//	console.log(canvasChosen.width);
//	canvasChosen.width = canvasChosen.width+50;
//	cxt.width = canvasChosen.width+50;
//	canvasChosen.height = canvasChosen.height;
//    cxt.putImageData(imgData,0,0);	
}


function deleteMark(context)
{	
	context.clear();
	charlist.pop();
	for (var char=0; char<charlist.length; char++)
	{
		for (var i=0; i<charlist[char].length; i++)
		{
			touch = charlist[char][i];
			context.lineCap = 'round';
			context.lineJoin = 'round';
			context.fillStyle = context.strokeStyle = COLOURS;
			context.lineWidth = radius;
			context.beginPath();
			context.moveTo( touch.ox, touch.oy );
			context.lineTo( touch.x, touch.y );
			context.stroke();
		}
	}
	result.pop();
}

function deleteCellMark(cellid)
{
	console.log(cell_char_list[cellid]);
	// 清空字迹
	var charLength = cell_char_list[cellid].length;
	// console.log("charLength: " + charLength);
	// console.log(charlist[charLength - 1]);
	var xlist = [];
	var ylist = [];
	for (var i=0; i< cell_char_list[cellid][charLength - 1].length; i++) // 笔画数组
	{
		// console.log("笔画数组 " + charlist[charLength - 1][i]);
		for (j=0; j< cell_char_list[cellid][charLength - 1][i].length; j++) // 点数组
		{
			// console.log("点数组 " + charlist[charLength - 1][i][j]);
			xlist.push(cell_char_list[cellid][charLength - 1][i][j].x);
			ylist.push(cell_char_list[cellid][charLength - 1][i][j].y);
		}
	}
	console.log("xlist ", xlist);
	console.log("ylist ", ylist);
	var xleft, xright, ytop, ybottom;
	xleft = Math.min(...xlist);
	xright = Math.max(...xlist);
	ytop = Math.min(...ylist);
	ybottom = Math.max(...ylist);
	console.log("xleft ", xleft);
	console.log("xright ", xright);
	console.log("ytop ", ytop);
	console.log("ybottom", ybottom);
	var cellCanvas = document.getElementById(cellid);
	var cellCtx = cellCanvas.getContext("2d");
	cellCtx.clearRect(xleft-3, ytop-3, xright - xleft  + 10, ybottom - ytop + 10);

	// 弹出字符笔迹数组
	cell_char_list[cellid].pop();
	result.pop();
}

//1025
function delContext(canvasChosen){
	var strlist, dellist;
	if(!isTable){
//		strlist = ViewStyle.innerHTML;
		var thisline = document.getElementById("lineNum"+lineNum);
		if (tmpStr.includes(' ')){
			strlist = tmpStr;
			dellist = strlist.slice(0, strlist.length-1);
			tmpStr = dellist;
			console.log("dellist "+dellist);
			thisline.innerHTML="";
			var linestr = dellist.split(" ");	
			console.log(linestr);
			for(var n = 0;n < linestr.length;n++){
				linestr[n]=changeChar(linestr[n]);
				linestr[n]="<button id = 'model' onclick = 'getViewCoodi(this)'>"+linestr[n]+"</button>";
				thisline.innerHTML+=linestr[n];
			}				
			
			}
		else{
			strlist = tmpStr;
			dellist = strlist.slice(0, strlist.length-1);
			tmpStr = dellist;
			thisline.innerHTML = dellist;	
		}
		
		
		
//		strlist = thisline.innerHTML;
//		dellist = strlist.slice(0, strlist.length-1);
//		thisline.innerHTML = dellist;	
		midOfView();
	}
	else{	
		console.log(canvasChosen.id);
		var canvasId = canvasChosen.id;
		var CellId = "Cell"+canvasId;
		console.log(CellId);
		chosenCell = document.getElementById(CellId);
		strlist = chosenCell.innerHTML;
		dellist = strlist.slice(0, strlist.length-1);
		chosenCell.innerHTML = dellist;
	}
	
}


var Today = new Date();
document.getElementById("date").innerHTML += Today.getFullYear()+" / ";
document.getElementById("date").innerHTML += Today.getMonth() + 1;
document.getElementById("date").innerHTML += " / "+Today.getDate();
console.log("hidetimes"+hidetimes);

$("#img02").hover(
	    function () {
//	    	if(hidetimes%2 == 0){
	    		$(this).css({ "bottom": "-130px" });
	    		$(this).css({ "left": "0px" });
//	    	}else if(hidetimes%2 == 1){
//	    		$(this).css({ "left": "-35px" });
//	    		$(this).css({ "bottom": "-150px" });
//	    	}	        
	    })
	    ;
	$("#img02").mouseout(
	    function () {	    		
//	    		if(hidetimes%2 == 0){
	    			$(this).css({ "bottom": "-100px" });
		    		$(this).css({ "left": "0px" });
//		    	}else if(hidetimes%2 == 1){
//		    		$(this).css({ "bottom": "-130px" });
//		    		$(this).css({ "left": "0px" });
//		    	}
	    });
	
//wx2019-10-26+17:24化学方程式
	var charLabel2char = {'a':'a', 'Ｏ':'O', 'Ｎ':'N', 'Ｈ':'H', '５':'5', '７':'7', '４':'4', '８':'8', '２':'2','６':'6', '９':'9',  '３':'3', '１':'1', '＋':'+', '＝':'=','↑':'↑','（':'C','C':'C'}; // 编码转换
	var chemStr = "２Ｎa＋Ｈ２Ｏ＝２ＮaＯＨ＋２Ｈ２↑";
	function findchem_equa(){
	    for (var i =0; i<lineNum; i++){
//			console.log("lineHtml"+document.getElementById("lineNum"+(i+1)));
	        var thisline = document.getElementById("lineNum"+(i+1)).innerHTML;
//			console.log("thisline"+thisline);
	        if (thisline.includes('＝')){
	            return i+1;
	        }
	    }
	return -1;
	}
	function changeChar(str){
	    var newStr = "";
	    for (var i =0; i<str.length;i++)
	    {
	        newStr += charLabel2char[str[i]];
	    }
	    return newStr;
	}
	function str2latex(str){
	    var newStr = str[0];
	    for (var i = 1; i<str.length;i++)
	    {
	        if (str[i]<='9'&&str[i]>='0'&&(str[i-1]<='z'&&str[i-1]>='a'||str[i-1]<='Z'&&str[i-1]>='A'))
	        {
				/*
	            newStr += '_{';
	            newStr += str[i];
	            newStr += '}';
				 */
				newStr += "<sub>";
				newStr += str[i];
				newStr += "</sub>";
	        }
	        else{
	            newStr += str[i];
	        }
	    }
	    return newStr;
	}

	function showLatex(res){
		var tmp = document.getElementById("lineNum"+lineNum);
	    tmp.innerHTML = res;
	}


// wx-hl 11-15 化学方程式配平
	function getChem(lineNum){
		var line = document.getElementById("lineNum"+lineNum).innerHTML;
		console.log("line++"+line);
	    var newStr = changeChar(line);
	    console.log(newStr);
		return newStr;
	}
	function get_coeff(str)
	{
		str = str.replace(/\s*/g,""); // delete all blank
		str = str.replace('↑', "");
		var sides = str.split("=");	// split into left / right
		sides[0] = sides[0].split("+");	// split left side
		sides[1] = sides[1].split("+");	// split right side
		var coeff_left = getNum(sides[0]);
		var coeff_right = getNum(sides[1]);
		var res = [];
		res.push(coeff_left);
		res.push(coeff_right);
		return res;
	} 

	function getNum(a){
		var res = [];
		for(var i=0; i<a.length; i++){
			var s = a[i];
			if (s[0]<'=9' && s[0]>='1'){
				res.push(parseInt(s[0]));
			}
			else{
				res.push(1);
			}
		}
		return res;
	}

	function cut_coeff(str)
	{
		str = str.replace(/\s*/g,""); // delete all blank
		str = str.replace('↑', "");
		var sides = str.split("=");	// split into left / right
		sides[0] = sides[0].split("+");	// split left side
		sides[1] = sides[1].split("+");	// split right side
		var cut_left = cut_coeff_detail(sides[0]);
		var cut_left_str = cut_left.join("+");
		var cut_right = cut_coeff_detail(sides[1]);
		var cut_right_str = cut_right.join("+");
		var cut_str = cut_left_str + "=" + cut_right_str;
		return cut_str;
	}
	function cut_coeff_detail(a)
	{
		res = [];
		for(var i=0; i<a.length; i++){
			var s = a[i];
			var cut_str = "";
			if (s[0]<'=9' && s[0]>='1'){
				cut_str+=s.slice(1, s.length);
			}
			else{
				cut_str+=s;
			}
			res.push(cut_str);
		}
		return res;
	}

	function split_chem(str)
	{
		str = str.replace(/\s*/g,""); // delete all blank
		var sides = str.split("=");	// split into left / right
		sides[0] = sides[0].split("+");	// split left side
		sides[1] = sides[1].split("+");	// split right side
		return sides;
	}

	function coeff_diff(l1, l2){
		var res = [];
		res.push([]);
		res.push([]);
		for (var i=0; i<l1[0].length; i++)
		{
			if (l1[0][i]!=l2[0][i]){
				res[0].push(i);
			}
		}
		for (var i=0; i<l1[1].length; i++)
		{
			if (l1[1][i]!=l2[1][i]){
				res[1].push(i);
			}
		}
		return res;
	}

	function turn_red(split_list, coeff_diff_list){
		var index;
		for(var i=0; i<coeff_diff_list.length; i++)
		{
			for (var j=0; j<coeff_diff_list[i].length; j++)
			{
				index = coeff_diff_list[i][j];
				var tmp = split_list[i][index];
				var new_str = "<font color='red'>";
				new_str += tmp;
				new_str += "</font>";
				split_list[i][index] = new_str;
			}
		}
		return split_list;
	}

	function format_chem(split_list){
		    for (var i=0; i<split_list.length; i++)
			{
				var tmp = split_list[i];
				for(var j=0; j<tmp.length; j++)
				{
					var str = tmp[j];
					tmp[j] = str2latex(str);
				}
			}
			return split_list;
	}

	function str2latex(str)
{
    var newStr = str[0];
     for (var j = 1; j<str.length; j++)
    {
        if (str[j]<='9'&&str[j]>='0'&&(str[j-1]<='z'&&str[j-1]>='a'||str[j-1]<='Z'&&str[j-1]>='A'))
        {
            /*
            newStr += '_{';
            newStr += str[i];
            newStr += '}';
                */
            newStr += "<sub>";
            newStr += str[j];
            newStr += "</sub>";
        }
        else{
            newStr += str[j];
        }
    }
    return newStr;
}
//1030无限增加画布长度
	window.addEventListener("scroll", function(event) {
        var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
        if(document.documentElement.scrollHeight == document.documentElement.clientHeight + scrollTop ) {
            createDrawArea();
        }        
    });
//	document.getElementById("areaDraw").ondblclick = function() {
//		clearTimeout(handler);
//		pointCoodi=[];
//		writetype=!writetype; 
//		change();
//	};
	function changeLan(){
		clearTimeout(handler);
		pointCoodi=[];
		writetype=!writetype; 
		change();
	}
	function del(){
		if(isTable){
			deleteCellMark(idChosen);
			delContext(canvasChosen);
			pointCoodi=[];
		}else{
			deleteMark();
			delContext(canvasChosen);
			pointCoodi=[];
		}
		
	}
	
	
	
	
// wx 12/31
