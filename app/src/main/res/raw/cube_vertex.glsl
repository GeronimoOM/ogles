uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

attribute vec3 aPosition;
attribute vec2 aTexCoord;

varying vec2 vTexCoord;

void main() {
    vTexCoord = aTexCoord;

    mat4 modelViewProjection = uProjection * uView * uModel;
    gl_Position = modelViewProjection * vec4(aPosition, 1);
}