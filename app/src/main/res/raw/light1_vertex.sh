uniform mat4 uMvpMatrix;
attribute vec4 aPosition;
void main()
{
    gl_Position = uMvpMatrix * aPosition;
    gl_PointSize = 10.0;
}