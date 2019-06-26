// Constants
const leftValue = -300;
const rightValue = 300;
const yValue = 100;

function createSuperSquare(left, right, yValue, cache) {

    var bottomLeft = new Point(left, yValue, left);
    var bottomRight = new Point(left, yValue, right);
    var topRight = new Point(right, yValue,right);
    var topLeft = new Point(right, yValue, left);

    var leftTr = new Triangle([topLeft,bottomLeft,bottomRight]);
    var rightTr = new Triangle([topLeft,topRight,bottomRight]);

    var diagonal = new Edge(topLeft, bottomRight, leftTr, rightTr);
    var leftEdge = new Edge(topLeft, bottomLeft, leftTr, null);
    var rightEdge = new Edge(topRight, bottomRight, rightTr, null);
    var topEdge = new Edge(topLeft, topRight, rightTr, null);
    var bottomEdge = new Edge(bottomLeft, bottomRight, leftTr, null);


    leftTr.edges = [leftEdge, bottomEdge, diagonal];
    rightTr.edges = [topEdge, rightEdge, diagonal];

    cache.updateSuper(leftTr,true);
    cache.updateSuper(rightTr,false);

    return [leftTr, rightTr];
}

function satisfiesDelaunayCondition(p1, p2, p3, node) {
    var angleSum = modifiedCheckOfOppositeAnglesSum(node, p1, p2, p3);
    var sa = angleSum.sa;
    var sb = angleSum.sb;

    if (sa < 0 && sb < 0) return false;
    if (sa >= 0 && sb >= 0) return true;
    return this.oppositeAnglesSum(node, p1, p2, p3, sa, sb) >= 0;
}

function oppositeAnglesSum(p0, p1, p2, p3, sa, sb) {
    return Math.abs( ((p0.x - p1.x) * (p0.z - p3.z) - (p0.x - p3.x) * (p0.z - p1.z)) )* sb +
        sa * Math.abs( ((p2.x - p1.x) * (p2.z - p3.z) - (p2.x - p3.x) * (p2.z - p1.z)) );
}

function modifiedCheckOfOppositeAnglesSum(p0, p1, p2, p3) {
    return {
        sa: (p0.x - p1.x) * (p0.x - p3.x) + (p0.z - p1.z) * (p0.z - p3.z),
        sb: (p2.x - p1.x) * (p2.x - p3.x) + (p2.z - p1.z) * (p2.z - p3.z)
    }
}

function putPointOnOutsideEdge(edge, point, newTriangles) {
    var triangle;
    if (edge.triangle1 != null) {
        triangle = edge.triangle1;
    } else if (edge.triangle2 != null) {
        triangle = edge.triangle2;
    } else {
        throw new Error("Not found triangle");
    }

    var a = edge.a;
    var b = edge.b;
    var c = triangle.getOppositeNode(edge);

    var obc = new Triangle([point,b,c]);
    var bc = triangle.getOppositeEdge(a);

    var oc = new Edge(point, c, triangle, obc);
    var ob = new Edge(point, b, obc, null);

    if (bc.triangle1.isEqual(triangle))
        bc.triangle1 = obc;
    else
        bc.triangle2 = obc;

    obc.edges = [ob,bc,oc];

    var ao = triangle.edges[0];

    if (triangle.edges[0].b.isEqual(b))
        triangle.edges[0].b = point;
    else
        triangle.edges[0].a = point;
    triangle.edges[1] = oc;
    triangle.points[1] = point;

    triangle.updateValue();
    obc.updateValue();

    edge.b = point;

    newTriangles = [obc];
    var modifiedTriangles = [triangle];

    return {
        new: newTriangles,
        modified: modifiedTriangles
    }
}


function putPointOnInnerEdge(edge, point, newTriangles) {
    var a = edge.a;
    var b = edge.b;

    var abc = edge.triangle1;
    var abd = edge.triangle2;

    var c = abc.getOppositeNode(edge);
    var d = abd.getOppositeNode(edge);

    var bc = abc.getOppositeEdge(a);
    var bd = abd.getOppositeEdge(a);

    var obd = new Triangle([point,b,d]);
    var obc = new Triangle([point,b,c]);

    var oc = new Edge(point, c, abc, obc);
    var od = new Edge(point, d, abd, obd);
    var ob = new Edge(point, b, obd, obc);

    if (bc.triangle1.isEqual(abc))
        bc.triangle1 = obc;
    else
        bc.triangle2 = obc;

    if (bd.triangle1.isEqual(abd))
        bd.triangle1 = obd;
    else
        bd.triangle2 = obd;

    obd.edges = [ob,bd,od];
    obc.edges = [ob,bc,oc];

    abc.edges[1] = oc;
    abc.points[1] = point;
    abd.edges[1] = od;
    abd.points[1] = point;

    edge.b = point;

    obc.updateValue();
    obd.updateValue();
    abc.updateValue();
    abd.updateValue();

    newTriangles = [obd,obc];
    var modifiedTriangles = [abc,abd];

    return {
        new: newTriangles,
        modified: modifiedTriangles
    }
}

function putPointInTriangle(triangle, point, newTriangles){
    var a = triangle.points[0];
    var b = triangle.points[1];
    var c = triangle.points[2];

    var ab = triangle.edges[0];
    var bc = triangle.edges[1];
    var ac = triangle.edges[2];

    var aob = new Triangle([a,point,b]);
    var obc = new Triangle([point,b,c]);

    var ao = new Edge(a, point, aob, triangle);
    var ob = new Edge(point, b, aob, obc);
    var oc = new Edge(point, c, obc, triangle);

    if (ab.triangle1.isEqual(triangle))
        ab.triangle1 = aob;
    else
        ab.triangle2 = aob;

    if (bc.triangle1.isEqual(triangle))
        bc.triangle1 = obc;
    else
        bc.triangle2 = obc;

    triangle.edges[0] = ao;
    triangle.edges[1] = oc;
    triangle.points[1] = point;

    aob.edges = [ao,ob,ab];
    obc.edges = [ob,bc,oc];

    newTriangles = [aob, obc];

    triangle.updateValue();
    aob.updateValue();
    obc.updateValue();

    // triangle = aoc

    var modifiedTriangles = [triangle, aob, obc];
    return {
        new: newTriangles,
        modified: modifiedTriangles
    }
}

function findTriangleBySeparatingEdges(point, triangle) {
    var separatingEdge = getSeparatingEdge(triangle, point);
    while (separatingEdge != null) {
        triangle = separatingEdge.triangle1.isEqual(triangle) ? separatingEdge.triangle2 : separatingEdge.triangle1;
        separatingEdge = getSeparatingEdge(triangle, point);
    }
    return triangle;
}

function getSeparatingEdge(triangle, targetPoint) {
    for (var i = 0; i < triangle.edges.length; i++) {
        var edge = triangle.edges[i];
        if (isSeparated(edge.a, edge.b, targetPoint, triangle.getOppositeNode(edge))) {
            return edge;
        }
    }
    return null;
}

function calcTriangulationTr(point,cache,resultTriangles) {
    var initTriangle = cache.get(point);
    // console.log(cache.toString());
    var targetTriangle = findTriangleBySeparatingEdges(point, initTriangle);

    for (i = 0; i < targetTriangle.points.length; i++) {
        var point1 = targetTriangle.points[i];
        if (point.isInEpsilonAreaPoint(point1)) {
            return null;
        }
    }

    var modifiedTriangles = [];
    var newTriangles = [];

    var targetTriangleEdges = targetTriangle.edges;
    for (var i = 0; i < targetTriangleEdges.length; i++) {
        if (isInEpsilonArea(distanceToLine(targetTriangleEdges[i].a, targetTriangleEdges[i].b, point), 0)) {

            var edge = targetTriangleEdges[i];

            var triangles;
            var newEdgeTriangles = [];
            if (edge.triangle1 === null || edge.triangle2 === null) {
                triangles = putPointOnOutsideEdge(edge, point, newEdgeTriangles);
                newTriangles = triangles.new;
                modifiedTriangles = triangles.modified;
            } else {
                triangles = putPointOnInnerEdge(edge, point, newEdgeTriangles);
                newTriangles = triangles.new;
                modifiedTriangles = triangles.modified;
            }

            for (var i=0;i<newTriangles.length;i++) {
                resultTriangles.push(newTriangles[i]);
            }

            modifiedTriangles = modifiedTriangles.concat(newTriangles);
        }
    }

    if (newTriangles.length === 0) {
        var newInnerTriangles = [];
        var triangles = putPointInTriangle(targetTriangle, point, newInnerTriangles);
        modifiedTriangles = triangles.modified;
        newTriangles = triangles.new;

        for (var i=0;i<newTriangles.length;i++) {
            resultTriangles.push(newTriangles[i]);
        }

    }

    for (i = 0; i < newTriangles.length; i++) {
        cache.update(newTriangles[i]);
    }

    return modifiedTriangles;
}

function checkAndFlip(triangle, uncheckedTriangles) {
    var triangleFlip;
    var flip = this.flipRequired(triangle, triangleFlip);
    triangleFlip = flip.triangle;

    if (!flip.required) {
        uncheckedTriangles.remove(triangleFlip);
        return;
    }

    //var flip = true;

    // var super_structure = [new Point(-300,100,-300),new Point(-300,100,300),new Point(300,100,300),new Point(300,100,-300)];
    // for (var point in super_structure) {
    //     for (var tr in triangle.points) {
    //         if (tr.isEqual(point))
    //             flip = false;
    //     }
    //     for (var trF in triangleFlip.points) {
    //         if (trF.isEqual(point))
    //             flip = false;
    //     }
    // }
    //
    // if (flip)
        flipFunction(triangle, triangleFlip);
    if (uncheckedTriangles.indexOf(triangle) !== -1)
        uncheckedTriangles.push(triangle);
}

function flipRequired(triangle, Flip) {
    Flip = null;
    for (var i = 0; i < triangle.edges.length; i++) {
        var edge = triangle.edges[i];

        if (edge == null)
            continue;

        if (edge.triangle1.isEqual(triangle))
            Flip = edge.triangle2;
        else
            Flip = edge.triangle1;

        if (Flip === null)
            continue;

        var node = Flip.getOppositeNode(edge);
        var p1 = edge.a;
        var p2 = triangle.getOppositeNode(edge);
        var p3 = edge.b;

        if (!this.satisfiesDelaunayCondition(p1, p2, p3, node))
            return {
                required: true,
                triangle: Flip
            }
    }
    return {
        required: false,
        triangle: Flip
    }
}


function flipFunction(triangle1, triangle2) {
    var t1AdjacentEdgeIndex = triangle1.getAdjacentEdgeIndex(triangle2);
    var adjacentEdge = triangle1.edges[t1AdjacentEdgeIndex];
    var t2AdjacentEdgeIndex = triangle2.getEdgeIndex(adjacentEdge);

    var c_index = triangle1.getOppositeNodeIndex(t1AdjacentEdgeIndex);
    var c = triangle1.points[c_index];
    var d_index = triangle2.getOppositeNodeIndex(t2AdjacentEdgeIndex);
    var d = triangle2.points[d_index];

    var cd = new Edge(c, d, triangle1, triangle2);

    var a = triangle2.getNodeIndex(triangle1.edges[t1AdjacentEdgeIndex].a);
    var b = triangle1.getNodeIndex(triangle1.edges[t1AdjacentEdgeIndex].b);

    var t1CB = triangle1.getEdgeIndexForPoints(c_index,b);
    var cb = triangle1.edges[t1CB];
    var t2DA = triangle2.getEdgeIndexForPoints(d_index,a);
    var da = triangle2.edges[t2DA];


    if (cb.triangle1 !== null && cb.triangle1.isEqual(triangle1))
        cb.triangle1 = triangle2;
    else if (cb.triangle2 !== null && cb.triangle2.isEqual(triangle1))
        cb.triangle2 = triangle2;
    else
        console.log("Some error");

    if (da.triangle1 !== null && da.triangle1.isEqual(triangle2))
        da.triangle1 = triangle1;
    else if (da.triangle2 !== null && da.triangle2.isEqual(triangle2))
        da.triangle2 = triangle1;
    else
        console.log("Some error");


    triangle1.points[b] = d;
    triangle2.points[a] = c;
    if (da.a.isEqual(triangle2.points[a]))
        da.swap();
    if (cb.a.isEqual(triangle1.points[b]))
        cb.swap();

    var edges = [triangle1.edges[0],triangle1.edges[1],triangle1.edges[2],
                 triangle2.edges[0],triangle2.edges[1],triangle2.edges[2], cd];
    var triangle1_edges = [];
    var triangle2_edges = [];
    for (var i = 0;i<3;i++) {
        if (i === 2) {
            var a_point = triangle1.points[0];
            var b_point = triangle1.points[2];
            for (var j = 0; j<edges.length;j++) {
                if (edges[j].a.isEqual(b_point) && edges[j].b.isEqual(a_point)) {
                    edges[j].swap();
                    triangle1_edges.push(edges[j]);
                    break;
                }
                else if (edges[j].a.isEqual(a_point) && edges[j].b.isEqual(b_point)) {
                    triangle1_edges.push(edges[j]);
                    break;
                }
            }
        }
        else {
            var a_point = triangle1.points[i];
            var b_point = triangle1.points[i+1];
            for (var j = 0; j<edges.length;j++) {
                if (edges[j].a.isEqual(b_point) && edges[j].b.isEqual(a_point)) {
                    edges[j].swap();
                    triangle1_edges.push(edges[j]);
                    break;
                }
                else if (edges[j].a.isEqual(a_point) && edges[j].b.isEqual(b_point)) {
                    triangle1_edges.push(edges[j]);
                    break;
                }
            }
        }
    }

    for (var i = 0;i<3;i++) {
        if (i === 2) {
            var a_point = triangle2.points[0];
            var b_point = triangle2.points[2];
            for (var j = 0; j<edges.length;j++) {
                if (edges[j].a.isEqual(b_point) && edges[j].b.isEqual(a_point)) {
                    edges[j].swap();
                    triangle2_edges.push(edges[j]);
                    break;
                }
                else if (edges[j].a.isEqual(a_point) && edges[j].b.isEqual(b_point)) {
                    triangle2_edges.push(edges[j]);
                    break;
                }
            }
        }
        else {
            var a_point = triangle2.points[i];
            var b_point = triangle2.points[i+1];
            for (var j = 0; j<edges.length;j++) {
                if (edges[j].a.isEqual(b_point) && edges[j].b.isEqual(a_point)) {
                    edges[j].swap();
                    triangle2_edges.push(edges[j]);
                    break;
                }
                else if (edges[j].a.isEqual(a_point) && edges[j].b.isEqual(b_point)) {
                    triangle2_edges.push(edges[j]);
                    break;
                }
            }
        }
    }
    triangle1.edges = triangle1_edges;
    triangle2.edges = triangle2_edges;

    triangle1.updateValue();
    triangle2.updateValue();
}


function triangulation(points) {

    var size = Math.ceil((Math.random() * 0.3 + 0.6) * Math.pow(points.length,3/8));
    if (points.length === 1)
        size = 2;
    var cache = new StaticCache(size,-300,300,-300,300);

    var triangles = createSuperSquare(leftValue,rightValue,yValue,cache);

    for (var i = 0; i < points.length; i++) {
        var uncheckedTriangles = calcTriangulationTr(points[i],cache,triangles);
        if (uncheckedTriangles == null)
            return;

        while (uncheckedTriangles.length > 0) {
            var triangle = uncheckedTriangles.shift();
            checkAndFlip(triangle, uncheckedTriangles);
        }
    }

    return triangles
}