function Vector(a, b) {
    this.x = b.x - a.x;
    this.y = b.y - a.y;
    this.z = b.z - a.z;
}

function isInEpsilonArea(x, y) {
    var epsilon = 10e-6;
    return Math.abs(x - y) <= epsilon;
}

Vector.prototype.length = function() {
    return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
};

Array.prototype.remove = function() {
    var what, a = arguments, L = a.length, ax;
    while (L && this.length) {
        what = a[--L];
        while ((ax = this.indexOf(what)) !== -1) {
            this.splice(ax, 1);
        }
    }
    return this;
};

function angleBetweenVectors(vectorA, vectorB) {
    return (vectorA.x * vectorB.x + vectorA.y * vectorB.y + vectorA.z * vectorB.z) / (vectorA.length() * vectorB.length());
}


function isLeftHanded(A, O, B) {
    return this.crossProductZ(O, B, O, A) < 0;
}

function isClockWiseOrdered(A, B, C) {
    return this.crossProductZ(C, A, C, B) < 0;
}


function crossProductZ(aStart, aEnd, bStart, bEnd) {
    return (aEnd.x - aStart.x) * (bEnd.z - bStart.z) - (aEnd.z - aStart.z) * (bEnd.x - bStart.x);
}

function isSeparated(O, A, X, Y) {
    var OA = new Vector(O, A);
    var oxSign = Math.sign(this.pseudoscalarVectorProduct(OA, new Vector(O, X)));
    var oySign = Math.sign(this.pseudoscalarVectorProduct(OA, new Vector(O, Y)));
    if (oxSign === 0 || oySign === 0)
        return false;
    return oxSign !== oySign;
}

function pseudoscalarVectorProduct(a, b) {
    return a.x * b.z - a.z * b.x
}

function distanceToLine(O, X, P) {
    var A = O.z - X.z;
    var B = X.x - O.x;
    var C = O.x * X.z - X.x * O.z;

    return Math.abs((A * P.x + B * P.z + C) / Math.sqrt(A * A + B * B));
}
