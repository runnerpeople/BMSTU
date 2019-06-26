var scene, camera, renderer;

var maxX = 300;
var minX = -300;

var Y = 100;

var maxZ = 300;
var minZ = -300;

var points = [];
var triangles = [];

// var props = {'type': changeType };
// var mat = new THREE.MeshLambertMaterial({ color: 0xBC8F8F, side: props['type']});
// var gui = new dat.GUI();

var color_line = 0xBC8F8F;

init();
animate();


// function changeType() {
//     var type = THREE.Math.randInt(0,2);
//     console.log(type);
//     mat.side = type;
//     return type;
// }

function init() {
    var WIDTH = window.innerWidth, HEIGHT = window.innerHeight;

    scene = new THREE.Scene();
    scene.background = new THREE.Color( 0x000000 );

    camera = new THREE.PerspectiveCamera(45, WIDTH / HEIGHT, 1, 10000);
    camera.position.set(0,250,400);
    scene.add(camera);

    var ambientLight = new THREE.AmbientLight( 0xf0f0f0, 0.5 );
    scene.add(ambientLight);

    var light = new THREE.DirectionalLight( 0xffffff);
    light.position.set( 100, 200, 100 );
    light.castShadow = true;
    light.shadow = new THREE.LightShadow( new THREE.PerspectiveCamera( 60, 1, 1, 1000 ) );
    light.shadow.bias = -0.000222;
    light.shadow.mapSize.width = 1024;
    light.shadow.mapSize.height = 1024;
    scene.add( light );

    // var helper = new THREE.DirectionalLightHelper( light, 15 );
    // scene.add( helper );
    // shadowCameraHelper = new THREE.CameraHelper( light.shadow.camera );
    // scene.add( shadowCameraHelper );


    renderer = new THREE.WebGLRenderer({antialias:true});
    renderer.setPixelRatio( window.devicePixelRatio );
    renderer.setSize(WIDTH, HEIGHT);
    renderer.shadowMap.enabled = true;
    document.body.appendChild(renderer.domElement);

    var controls = new THREE.OrbitControls( camera, renderer.domElement );
    controls.damping = 0.2;
    controls.addEventListener( 'change', render);
    controls.addEventListener( 'start', function() {
        cancelHideTransorm();
    } );
    controls.addEventListener( 'end', function() {
        delayHideTransform();
    } );
    transformControl = new THREE.TransformControls( camera, renderer.domElement );
    transformControl.addEventListener( 'change', render);
    scene.add( transformControl );

    transformControl.addEventListener( 'change', function( e ) {
        cancelHideTransorm();
    } );
    transformControl.addEventListener( 'mouseDown', function( e ) {
        cancelHideTransorm();
    } );
    transformControl.addEventListener( 'mouseUp', function( e ) {
        delayHideTransform();
    } );
    var hiding;
    function delayHideTransform() {
        cancelHideTransorm();
        hideTransform();
    }
    function hideTransform() {
        hiding = setTimeout( function() {
            transformControl.detach( transformControl.object );
        }, 2500 )
    }
    function cancelHideTransorm() {
        if ( hiding ) clearTimeout( hiding );
    }

    window.addEventListener('resize', function() {
        var WIDTH = window.innerWidth,
            HEIGHT = window.innerHeight;
        renderer.setSize(WIDTH, HEIGHT);
        camera.aspect = WIDTH / HEIGHT;
        camera.updateProjectionMatrix();
    });

    // gui.add(props, "type");

    var pointsCount = 5000;
    for (let i = 0; i < pointsCount; i++) {
        let x = THREE.Math.randFloatSpread(maxX);
        let z = THREE.Math.randFloatSpread(maxZ);
        let y = noise.perlin2(x / maxX * 5, z / maxZ * 5) * 50;
        points.push(new Point(x,y,z));
    }

    console.log(points);

    // pointsLess = [];
    //
    // for (let i = 0; i < points.length; i++) {
    //     if (points[i].y <= 0) {
    //         var elem = points.splice(i, 1);
    //         elem[0].y -= 20;
    //         pointsLess.push(elem[0]);
    //     }
    // }

    calcTriangulation();

    // points = pointsLess;
    //
    // color_line = 0x3f9b0b;
    // calcTriangulation();

}
function animate() {
    requestAnimationFrame(animate);
    renderer.render(scene, camera);
}

function render() {
    renderer.render(scene, camera);
}

function calcTriangulation() {
    triangles = triangulation(points);
    drawTriangles(triangles);
}

function drawTriangles(triangles) {

    var wireFrame = true;

    if (wireFrame) {

        var geometry = new THREE.Geometry();
        var material = new THREE.LineBasicMaterial({color: color_line});

        triangles.forEach(function (triangle) {
            geometry = new THREE.Geometry();

            var vert1, vert2;
            var point1, point2;
            for (var i = 0; i < 3; i++) {
                vert1 = triangle.points[i % 3];
                point1 = new THREE.Vector3(vert1.x, vert1.y, vert1.z);

                vert2 = triangle.points[(i + 1) % 3];
                point2 = new THREE.Vector3(vert2.x, vert2.y, vert2.z);

                if (vert1.isEqual(new Point(minX, Y, minZ)) ||
                    vert1.isEqual(new Point(minX, Y, maxZ)) ||
                    vert1.isEqual(new Point(maxX, Y, minZ)) ||
                    vert1.isEqual(new Point(maxX, Y, maxZ)) ||
                    vert2.isEqual(new Point(minX, Y, minZ)) ||
                    vert2.isEqual(new Point(minX, Y, maxZ)) ||
                    vert2.isEqual(new Point(maxX, Y, minZ)) ||
                    vert2.isEqual(new Point(maxX, Y, maxZ))) {
                    break;
                }

                geometry.vertices.push(point1, point2);
                var line = new THREE.Line(geometry, material);
                scene.add(line);

            }
        })
    }
    else {
        var geom = new THREE.Geometry();

        var point_map = new Map();
        for (var i = 0;i<points.length;i++) {
            point_map.set(points[i], i);
            geom.vertices.push(new THREE.Vector3(points[i].x,points[i].y,points[i].z));
        }

        for( var i = 0; i < triangles.length; i++){
            var isDelete = false;
            for (var j = 0; j < 3; j++) {
                var point = triangles[i].points[j];
                if (point.isEqual(new Point(minX, Y, minZ)) ||
                    point.isEqual(new Point(minX, Y, maxZ)) ||
                    point.isEqual(new Point(maxX, Y, minZ)) ||
                    point.isEqual(new Point(maxX, Y, maxZ))) {
                    isDelete = true;
                    break;
                }
            }
            if (isDelete)
                triangles.splice(i, 1);
        }

        var faces = [];

        for (var i = 0;i<triangles.length;i++) {
            var normal_vector = new THREE.Vector3(0,0,0);
            triangles[i]._triangle.getNormal(normal_vector);
            var face = new THREE.Face3(point_map.get(triangles[i].points[0]),
                point_map.get(triangles[i].points[1]),point_map.get(triangles[i].points[2]),normal_vector);

            faces.push(face);
        }

        faces.sort(function(face1, face2) {
            if (face1.a === face2.a) {
                if (face1.b === face2.b)
                    return face1.c - face2.c;
                else
                    return face1.b - face2.b;
            }
            else
                return face1.a - face2.a;

        });

        faces.forEach(function(face) { geom.faces.push(face);});

        geom.mergeVertices();

        var vertices = new Array( geom.vertices.length );

        for ( v = 0, vl = geom.vertices.length; v < vl; v ++ ) {

            vertices[ v ] = new THREE.Vector3();

        }

        // vertex normals weighted by triangle areas
        // http://www.iquilezles.org/www/articles/normals/normals.htm

        var vA, vB, vC;
        var cb = new THREE.Vector3(), ab = new THREE.Vector3();

        for ( f = 0, fl = geom.faces.length; f < fl; f ++ ) {

            face = geom.faces[ f ];

            vA = geom.vertices[ face.a ];
            vB = geom.vertices[ face.b ];
            vC = geom.vertices[ face.c ];

            cb.subVectors( vC, vB );
            ab.subVectors( vA, vB );
            cb.cross( ab );

            vertices[ face.a ].add( cb );
            vertices[ face.b ].add( cb );
            vertices[ face.c ].add( cb );

        }

        for ( v = 0, vl = geom.vertices.length; v < vl; v ++ ) {
            vertices[ v ].normalize();
        }

        for ( f = 0, fl = geom.faces.length; f < fl; f ++ ) {
            face = geom.faces[ f ];
            var vertexNormals = face.vertexNormals;
            if ( vertexNormals.length === 3 ) {
                vertexNormals[ 0 ].copy( vertices[ face.a ] );
                vertexNormals[ 1 ].copy( vertices[ face.b ] );
                vertexNormals[ 2 ].copy( vertices[ face.c ] );

            } else {

                vertexNormals[ 0 ] = vertices[ face.a ].clone();
                vertexNormals[ 1 ] = vertices[ face.b ].clone();
                vertexNormals[ 2 ] = vertices[ face.c ].clone();

            }

        }


        var mesh = new THREE.Mesh(geom, mat);
        // gui.add(mesh.material, "wireframe");
        mesh.castShadow = true;

        scene.add( mesh );
    }
}

