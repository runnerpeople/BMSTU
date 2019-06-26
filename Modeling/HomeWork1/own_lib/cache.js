
function StaticCache(size, min_x,max_x,min_z,max_z) {

    this.min_x = min_x;
    this.max_x = max_x;
    this.x_size = (max_x - min_x) / size;

    this.min_z = min_z;
    this.max_z = max_z;
    this.z_size = (max_z - min_z) / size;


    this.cache = new Array(size);
    for (var i = 0; i < size; i++) {
        this.cache[i] = new Array(size);
    }
}

StaticCache.prototype.getColumn = function(value) {
    return Math.floor((value - this.min_x) / this.x_size);
};

StaticCache.prototype.getRow = function(value) {
    return Math.floor((value - this.min_z) / this.z_size);
};

StaticCache.prototype.update = function (T) {
    var center = T.center();
    this.cache[this.getColumn(center.x)][this.getRow(center.z)] = T;
};

StaticCache.prototype.updateSuper = function (T, isLeft) {
    if (isLeft) {
        for (i = 0; i < this.cache.length; i++)
            for (j = 0; j <= i; j++)
                if (i === j && Math.random() % 2 === 1)
                    this.cache[i][j] = T;
                else if (i !== j)
                    this.cache[i][j] = T;
    }
    else {
        for (i = 0; i < this.cache.length; i++)
            for (j = i; j < this.cache[i].length; j++)
                if (i === j && this.cache[i][j] === undefined)
                    this.cache[i][j] = T;
                else
                    this.cache[i][j] = T;
    }
};

StaticCache.prototype.get = function(node) {
    return this.cache[this.getColumn(node.x)][this.getRow(node.z)];
};

StaticCache.prototype.toString = function() {
    var str = "";
    for (var i = 0; i < this.cache.length; i++)
        for (var j = 0; j < this.cache.length; j++) {
            str += "[" + i + "][" + j + "]=" + this.cache[i][j].toString();
            str += "\n";
        }
    return str;
};
