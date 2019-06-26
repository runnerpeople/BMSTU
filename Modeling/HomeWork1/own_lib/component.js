function Point(x,y,z) {
    this.x = x;
    this.y = y;
    this.z = z;
}

Point.prototype.isEqual = function(other) {
    return this.x === other.x &&
           // this.y === other.y &&
           this.z === other.z;
};

Point.prototype.isInEpsilonAreaPoint = function(point) {
    return isInEpsilonArea(point.x, this.x) && isInEpsilonArea(point.z, this.z);
};

Point.prototype.toString = function() {
    return "("+ this.x + "," + this.y + "," + this.z + ")";
};

function Edge(a,b,triangle1,triangle2) {
    this.a = a;
    this.b = b;
    this.triangle1 = triangle1;
    this.triangle2 = triangle2;
}

Edge.prototype.isEqual = function(other) {
    return this.a.isEqual(other.a) && this.b.isEqual(other.b);
};

Edge.prototype.swap = function() {
    var buffer = this.a;
    this.a = this.b;
    this.b = buffer;
};

Edge.prototype.toString = function() {
    return "[" + this.a.toString() + "]->[" + this.b.toString() + "]";
};


function Triangle(points) {
    this.points = points;
    this.edges = new Array(3);

    this._triangle = new THREE.Triangle(this.points[0],this.points[1],this.points[2]);
}

Triangle.prototype = {

    getOppositeNode: function(edge) {
        var difference = Array.from(new Set(this.points.filter(x => !new Set([edge.a,edge.b]).has(x))));
        return difference[0];
    },

    getAdjacentEdgeIndex: function(triangle) {
        for (var i = 0; i < 3; i++) {
            if ((this.edges[i].triangle1 !== null && this.edges[i].triangle1.isEqual(triangle)) ||
                (this.edges[i].triangle2 !== null && this.edges[i].triangle2.isEqual(triangle)))
                return i;
        }
        return -1;
    },

    getEdgeIndex: function(edge) {
        return this.edges.indexOf(edge);
    },

    getNodeIndex: function(node) {
        return this.points.indexOf(node);
    },

    getOppositeEdge: function(point) {
        var index = this.points.indexOf(point);
        switch (index) {
            case 0: {
                return this.edges[1];
            }
            case 1: {
                return this.edges[2];
            }
            case 2: {
                return this.edges[0];
            }
            default:
                throw new Error("Don't have this point");
        }
    },

    getOppositeNodeIndex: function(edgeIndex) {
        switch (edgeIndex) {
            case 0: return 2;
            case 1: return 0;
            case 2: return 1;
        }

    },

    getExceptIndex: function(firstIndex, secondIndex) {
        var a = Math.min(firstIndex, secondIndex);
        var b = Math.max(firstIndex, secondIndex);
        if (a === 0 && b === 1)
            return 2;
        else if (a === 0 && b === 2)
            return 1;
        else if (a === 1 && b === 2)
            return 0;
    },

    getEdgeIndices: function(indexEdge) {
        switch (indexEdge) {
            case 0: {
                return [0, 1];
            }
            case 1:{
                return [1, 2];
            }
            case 2: {
                return [0, 2];
            }
        }
    },

    swapPoints: function(firstIndex, secondIndex) {
        var buffer = this.points[firstIndex];
        this.points[firstIndex] = this.points[secondIndex];
        this.points[secondIndex] = buffer;
    },

    getEdgeIndexForPoints: function(A, B) {
        var a = Math.min(A, B);
        var b = Math.max(A, B);
        if (a === 0) {
            if (b === 1)
                return 0;
            return 2;
        }
        return 1;
    },

    center: function () {
        var result = new THREE.Vector3(0,0,0);
        this._triangle.getMidpoint(result);
        return result;
    },

    isNode: function(x,y,z) {
        var vector = new THREE.Vector3(x,y,z);
        return this._triangle.a === vector || this._triangle.b === vector || this._triangle.c === vector
    },

    isEdge: function(x,y,z) {
        var result = -1;
        for (var i = 0;i<3;i++) {
            var x0 = (((this.points[(i + 1) % 2].x - this.points[i].x) === 0) ? 0
                : (x - this.points[i].x) / (this.points[(i + 1) % 2].x - this.points[i].x));
            var y0 = (((this.points[(i + 1) % 2].y - this.points[i].y) === 0) ? 0
                : (y - this.points[i].y) / (this.points[(i + 1) % 2].y - this.points[i].y));
            var z0 = (((this.points[(i + 1) % 2].z - this.points[i].z) === 0) ? 0
                : (z - this.points[i].z) / (this.points[(i + 1) % 2].z - this.points[i].z));

            if ((x0 === y0 === z0) || (y0 === z0) || (x0 === z0) || (x0 === y0) )
                result = i;
        }
        return result;
    },

    contains: function (x,y,z) {
        return this._triangle.containsPoint(new THREE.Vector3(x,y,z))
    },

    updateValue: function() {
        for (var i = 0;i<3;i++) {
            // for (var j = 0;j<3;j++)
            // edge = new Edge(this.points[i],this.points[(i+1)%2])
            if (i === 2) {
                var a = this.points[0];
                var b = this.points[2];
                var edge = this.edges[i];

                if (edge.a.isEqual(b))
                    edge.swap();
                else if (!edge.a.isEqual(a)) {
                    console.log(edge.toString());
                    console.log(a.toString());
                    console.log(b.toString());
                    console.log("Something error");

                }
            }
            else {
                var a = this.points[i];
                var b = this.points[i+1];
                var edge = this.edges[i];

                if (edge.a.isEqual(b))
                    edge.swap();
                else if (!edge.a.isEqual(a)) {
                    console.log(edge.toString());
                    console.log(a.toString());
                    console.log(b.toString());
                    console.log("Something error");

                }
            }
        }

        this._triangle = new THREE.Triangle(this.points[0],this.points[1],this.points[2]);
    },

    isEqual: function (other) {
        result = true;
        for (var i=0;i<this.points.length;i++) {
            if (i < other.points.length)
                result = result && this.points[i].isEqual(other.points[i]);
            else
                result = result && false;
        }

        for (var j=0;j<this.edges.length;j++) {
            if (j < other.edges.length)
                result = result && this.edges[j].isEqual(other.edges[j]);
            else
                result = result && false;
        }
        return result;
    },

    toString: function () {
        return "[" + this.points[0].toString() +"]->[" + this.points[1].toString() + "]->[" + this.points[2].toString() + "]";
    }

};