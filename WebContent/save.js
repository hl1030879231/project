function saveToImg() {
	html2canvas(document.querySelector('#ViewAll')).then(function(canvas) {
		var image = new Image();
		// image.className = "newImg"
		image.src = canvas.toDataURL("image/png");
		// document.body.appendChild(image);
		downloadImg(image.src);
	});
}

function downloadImg(url) {
	var a = document.createElement('a'); // 创建一个a节点插入的document
	var event = new MouseEvent('click') // 模拟鼠标click点击事件
	a.download = 'baogao' // 设置a节点的download属性值
	a.href = url; // 将图片的src赋值给a节点的href
	a.dispatchEvent(event) // 触发鼠标点击事件
}
