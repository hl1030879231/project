var scene,camera,renderer,pile;
renderer = new THREE.WebGLRenderer();

function InitScene(Canvas){	
	renderer.setSize(500, 400);
//	document.body.appendChild(renderer.domElement);
	renderer.setClearColor(0xeeeeee);
	Canvas.appendChild(renderer.domElement);
	// 场景
	scene = new THREE.Scene();            
	// 相机
	camera = new THREE.OrthographicCamera(-5, 5, 4, -4, 0.1, 100);
	camera.position.set(25, 15, 25);
	camera.lookAt(new THREE.Vector3(0, 0, 0));
	scene.add(camera);           
	// 添加光照
	var ambientLight = new THREE.AmbientLight(0xa5a5a5);
	scene.add(ambientLight);
	
	dirLight = new THREE.DirectionalLight( 0xffffff, 0.8 );
	    dirLight.color.setHSL( 0.1, 1, 0.95 );
	    dirLight.position.set( -1, 1.75, 1 );
	    dirLight.position.multiplyScalar( 30 );
	    scene.add( dirLight );
	dirLight.castShadow = true;
	pile = new THREE.Object3D();
	
}
//绘制化学键
function DrawBonds(x0,y0,z0,x1,y1,z1,material,Num){
	var dir_x = x1 - x0;
	var dir_y = y1 - y0;
	var dir_z = z1 - z0;
	var mid_x = (x1 + x0)/2;
	var mid_y = (y1 + y0)/2;
	var mid_z = (z1 + z0)/2;
	var length = Math.sqrt( dir_x*dir_x + dir_y*dir_y + dir_z*dir_z );					
	//向量夹角计算
	var Vec3_0 = new THREE.Vector3(0,1,0);		
	var Vec3_1 = new THREE.Vector3(dir_x,dir_y,dir_z);
	
	var _angle = Vec3_1.angleTo(Vec3_0);  
	
	//旋转轴计算
	var Vec3 = new THREE.Vector3(dir_z,0,-dir_x);
	
	//材质设定
	 var material = new THREE.MeshPhongMaterial({
        color: 0xffffff,
        emissive: 0x303030,
        shininess: 20
	}); 
	//多键绘制
	for(var i = 0;i< Num;i++){
		var cylinder = new THREE.Mesh(new THREE.CylinderGeometry(0.08, 0.08 ,length ,40 ,40),material); 		
		cylinder.position.set(mid_x,mid_y,mid_z);		
		cylinder.translateOnAxis(Vec3.normalize(),i/10-(Num-1)/20);
	    cylinder.rotateOnAxis(Vec3.normalize(),_angle); 		
		pile.add(cylinder);  
		
	}
		
}

function Draw(jsonMoles,jsonBonds) {	
			//不同原子材质设定
	var moles=JSON.parse(jsonMoles);
	var bonds=JSON.parse(jsonBonds);
	for(var i in moles){
		var mole=moles[i];
		var color;
		switch(mole.type){
		case "C"||"c":
			color = 0xde8100;
			break;
		case "H"||"h":
			color = 0x1f6fb5;				  
			break;
    	case "O"||"o":
    		color = 0xabc327;      		 
    		break;
    	}			
		var material = new THREE.MeshPhongMaterial({
			color: color,
			emissive: 0x303030,
			shininess: 20
			});     			
    	//画原子
    	var sphere = new THREE.Mesh(new THREE.SphereGeometry(0.4, 40, 16), material);   
    	var x = mole.x/100;var y = mole.y/100;var z = mole.z/100;       			
    	sphere.position.set(x, y, z); 
    	pile.add(sphere);        			
      
	}
	
    for(var i in bonds) {
    	var bond=bonds[i];
    	var temp = bond.split(" ");
    	var mole1=[];
   		var mole2=[];
		for(var j in moles) {
			var mole=moles[j];
			if(mole.name===temp[0]) {
				mole1[0] = mole.x;mole1[1] = mole.y;mole1[2] = mole.z;
    		}
			if(mole.name===temp[1]) {
				mole2[0] = mole.x;mole2[1] = mole.y;mole2[2] = mole.z;
			}
		}      			      
		var x0 = mole1[0]/100;var y0 = mole1[1]/100;var z0 = mole1[2]/100;      			
		var x1 = mole2[0]/100;var y1 = mole2[1]/100;var z1 = mole2[2]/100;      	
       
        var BondsNum = 1;
        if(temp[2].indexOf("Double") != -1){  
        	BondsNum = 2;
        }else if(temp[2].indexOf("Triple") != -1){			
        	BondsNum = 3;
        }else{ 
        	BondsNum = 1;      				
        }
           				
    	DrawBonds(x0,y0,z0,x1,y1,z1,material,BondsNum); 
    	    				
	}       		
        
}

//旋转动画   
function Animation(){
	pile.rotation.y+=0.01;       	
	renderer.render(scene, camera);
	requestAnimationFrame(Render);
	}
	
function Render(){
	scene.add(pile);    
	Animation();
    renderer.render(scene, camera);  
}
//spf1115
 function Show(moles,bonds){
	 var Canvas = document.createElement("CanvasMod"); 
	 Canvas.setAttribute("id","CanvasMod");
	 document.body.appendChild(Canvas);
	 InitScene(Canvas);
	 Draw(moles,bonds);
	 Render();
 }  
 //spf1115
// function HideMod(){
//	 var Canvas =document.getElementById("CanvasMod");
//	 Canvas.style.visibility="hidden";
// }
 function removeMod(){
	 Canvas =document.getElementById("CanvasMod");
	 Canvas.parentNode.removeChild(Canvas);
 }