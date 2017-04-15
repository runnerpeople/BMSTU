attribute vec3 coord;
attribute vec3 color;
attribute vec2 text_coord;
attribute vec3 norm_coord;

varying vec3 var_color;
varying vec2 var_text_coord;
varying vec3 var_norm_coord;
//varying vec3 var_coord;

uniform mat4 matrix_projection;
uniform mat4 matrix_modelview;

void main() {
    gl_Position = matrix_projection * matrix_modelview * vec4(coord, 1.0);
    var_color = color;
    var_text_coord = text_coord;
//    var_norm_coord = matrix_modelview *
}

