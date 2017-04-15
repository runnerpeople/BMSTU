varying vec3 var_color;

uniform bool global_color;

void main() {
    if (global_color) {
        gl_FragColor = vec4(0.3,0.0,0.5,1.0);
    } else {
        gl_FragColor = vec4(var_color,1.0);
    }
}
