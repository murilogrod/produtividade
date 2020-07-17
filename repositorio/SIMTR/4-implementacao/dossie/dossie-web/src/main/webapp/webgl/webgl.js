log = function(message) {
	if (console) console.log(message);
}

mobileAndTabletcheck = function() {
  var check = false;
  (function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))) check = true;})(navigator.userAgent||navigator.vendor||window.opera);
  return check;
};

var isWebGL = Detector.webgl;
var isMobile = mobileAndTabletcheck();
var antialias = !isMobile;
var bumpMap = !isMobile;
var shadowMap = !isMobile;

log("isWebGL: " + isWebGL);
log("isMobile: " + isMobile);

var camera, scene, renderer, controls, dirLight, hemiLight;
var logotipo;

var fontLoader;

var clock;

var titleObject, subTitleObject;

var container;

var defaultObject = "webgl/logotipo.obj";
var defaultMaterial = "webgl/logotipo.mtl";

var lightHelper;
var mtlLoader;
var objLoader;
var elementHeigth = 400;
var primaryColor = "#2470a8";
var secundaryColor = "#fff"
var animation;

var containerId = null;

function alternative() {
		var splash = document.getElementById( 'splash' + containerId );
		if (splash) splash.setAttribute("style","display:inline!important");
}


function init(id,title,subtitle,object,material,color) {
	
	containerId = id;

	fontLoader = new THREE.FontLoader();
	clock = new THREE.Clock();
	mtlLoader = new THREE.MTLLoader();
	objLoader = new THREE.OBJLoader();

	if (isMobile) {
		elementHeigth = 300;
	}

	if (!isWebGL) {
		alternative();
		return;
	}

	if (color) primaryColor = color;

	if (material && material.indexOf(".mtl") > 0) {
		defaultMaterial = material;
		material = null;
	}
	if (object && object.indexOf(".obj") > 0) {
		defaultObject = object;
		object = null;
	}

	if (!material && !object) {
		mtlLoader.load( defaultMaterial, function( content ) {
			material = content;
			material.preload();

			objLoader.setMaterials( material );
			objLoader.load( defaultObject, function( content ) {
				object = content;
				load(id,title,subtitle,object,material);
			});

		});
	} else if (material) {


		mtlLoader.loadContent( material, function( content ) {
			material = content;
			material.preload();

			objLoader.setMaterials( material );
			objLoader.loadContent( object, function( content ) {
				object = content;
				load(id,title,subtitle,object,material);
			});

		});

	} else {
		objLoader.loadContent( object, function( content ) {
			object = content;
			load(id,title,subtitle,object,material);
		});
	}

}

function load(id,title,subtitle,object,material) {

	container = document.getElementById( id );

	//camera = new THREE.PerspectiveCamera( 30, window.innerWidth / window.innerHeight, 1, 1000 );
	if (!container || !container.offsetWidth) return;
	camera = new THREE.PerspectiveCamera( 30, container.offsetWidth / elementHeigth, 1, 1000 );
	//camera.position.set( -10, 10, 250 ); //CAMERA AT CENTER
	camera.position.set( -10, 10, 150 ); //CAMERA AT CENTER
	camera.lookAt( new THREE.Vector3( 0, 0, 0 ) );

	scene = new THREE.Scene();

	controls = new THREE.OrbitControls( camera ) ;
	controls.tiltEnabled = true ;  // default is false.  You need to turn this on to control with the gyro sensor.

	controls.minPolarAngle = Math.PI * 0.4; // radians
	controls.maxPolarAngle = Math.PI * 0.53; // radians
	controls.noZoom = true ;

	controls.minAzimuthAngle = - Math.PI * 0.3; // radians
	controls.maxAzimuthAngle = Math.PI * 0.3; // radians


	// ENVIRONMENT LIGHT
	hemiLight = new THREE.HemisphereLight( 0xffffff, 0xffffff, 0.5 );
	hemiLight.position.set( 0, 500, 0 );
	scene.add( hemiLight );

	// SHADOW LIGHT
	dirLight = new THREE.SpotLight( 0xffffff, 1 );
	dirLight.position.set( -1, 1.75, 1 );
	dirLight.position.multiplyScalar( 50 );
	scene.add( dirLight );

	dirLight.castShadow = true;
	dirLight.shadowMapWidth = 1024;
	dirLight.shadowMapHeight = 1024;

	// GROUND
	var groundGeo = new THREE.PlaneBufferGeometry( 1000, 1000 );

	//TRANSPARENT GROUND
	var groundMat = new THREE.ShadowMaterial();
	groundMat.opacity = 0.3;

	var ground = new THREE.Mesh( groundGeo, groundMat );
	ground.rotation.x = -Math.PI/2;
	ground.position.y = -53;
	scene.add( ground );

	ground.receiveShadow = true;

	// MODEL
	var onProgress = function ( xhr ) {
		if ( xhr.lengthComputable ) {
			var percentComplete = xhr.loaded / xhr.total * 100;
			console.log( Math.round(percentComplete, 2) + '% downloaded' );
		}
	};
	var onError = function ( xhr ) {
		console.log("error: " + xhr);
		};

	logotipo = object;

	logotipo.traverse( function ( child ) {

		if ( child instanceof THREE.Mesh ) {
			if (bumpMap) {
				child.material.bumpScale = 0.01;
			} else {
				child.material.bumpScale = 0;
			}

			child.castShadow = true;
			child.receiveShadow = true;


		}

	} );

	logotipo.scale.set( 0.05, 0.05, 0.05 );
	logotipo.position.x = 0;
	logotipo.position.y = 10;
	logotipo.position.z = -50;
	logotipo.rotation.x = 0;
	logotipo.rotation.y = 0;
	logotipo.rotation.z = 0;

	logotipo.castShadow = true;
	logotipo.receiveShadow = true;
	scene.add( logotipo );



	//TEXT
	function addText(text,x,y,z,size,height,font) {
			var geometry = new THREE.TextGeometry( text, {
				font: font,
				size: size,
				height: height,
				curveSegments: 6
			});
			geometry.computeBoundingBox();
			var centerOffset = -0.5 * ( geometry.boundingBox.max.x - geometry.boundingBox.min.x );
			var material = new THREE.MultiMaterial( [
				new THREE.MeshBasicMaterial( { color: primaryColor} ),
				new THREE.MeshBasicMaterial( { color: secundaryColor } )
			] );

			var mesh = new THREE.Mesh( geometry, material );
			mesh.position.x = centerOffset;
			mesh.position.x += x;
			mesh.position.y = y;
			mesh.position.z = z;
			mesh.castShadow = true;
			mesh.receiveShadow = true;

			scene.add( mesh );
			return mesh;
	}


	function onFontLoaded(font) {
			if (title) titleObject = addText(title,0,-25,0,8,5,font);
			if (subtitle) subTitleObject = addText(subtitle,15,-30,10,3,5,font);
	}

	if (title || subtitle) {
		//fontLoader.load("webgl/fonts/helvetiker_regular.typeface.json", onFontLoaded );
		fontLoader.load("webgl/fonts/gentilis_regular.typeface.json", onFontLoaded );
	}


	// RENDERER
	renderer = new THREE.WebGLRenderer( { antialias: antialias, alpha: true  } );
	renderer.setPixelRatio( window.devicePixelRatio );
	//renderer.setSize( window.innerWidth, window.innerHeight );
	if (!container || !container.offsetWidth) return;
	renderer.setSize( container.offsetWidth, elementHeigth );
	container.appendChild( renderer.domElement );

	renderer.gammaInput = true;
	renderer.shadowMap.enabled = true;

	if (shadowMap) renderer.shadowMap.type = THREE.PCFSoftShadowMap;

	window.addEventListener( 'resize', onWindowResize, false );

	animate();

}

function onWindowResize() {

	//camera.aspect = window.innerWidth / window.innerHeight;
	if (!container || !container.offsetWidth) {
		window.removeEventListener( 'resize', onWindowResize );
		return;
	}
	camera.aspect = container.offsetWidth / elementHeigth;
	camera.updateProjectionMatrix();

	//renderer.setSize( window.innerWidth, window.innerHeight );
	renderer.setSize( container.offsetWidth, elementHeigth );

}

function removeFromScene(element) {
	if (!element) return;
	if (!scene) return;

	scene.remove(element);
	element = null;
}

function disposeWebGL() {

	log("disposeWebGL()");
	if (!animation) return;
	cancelAnimationFrame( animation );
	animation = null;

	removeFromScene(camera);
	removeFromScene(titleObject);
	removeFromScene(subTitleObject);

	removeFromScene(dirLight);
	removeFromScene(hemiLight);
	removeFromScene(logotipo);

	renderer.clear();
	renderer = null;

	scene = null;
	controls = null;
	fontLoader = null;
	clock = null;

	if (container)  {
		container.parentNode.removeChild(container);
	}
	container = null;
}

function animate() {
	try {
		if (!container || !container.offsetWidth) {
			disposeWebGL();
			return;
		}
	
		animation = requestAnimationFrame( animate );
	
		var delta = clock.getDelta();
	
		if (logotipo) {
			logotipo.rotation.y += delta * -0.5;
		}
	
		if (controls) controls.update();
	
		render();
	}  catch (error) {
		disposeWebGL();
		alternative();
	}
}

function render() {
	if (renderer) renderer.render( scene, camera );

}

