uniform mat4 uMvpMatrix;
uniform mat4 uMvMatrix;
uniform vec3 uLightPos;
uniform vec4 uColor;

attribute vec4 aPosition;
attribute vec3 aNormal;
attribute vec3 aEyeVector;

varying vec4 vColor;

void main() {
    vec3 modelVertex = vec3(uMvMatrix * aPosition);
    vec3 modelNormal = normalize(vec3(uMvMatrix * vec4(aNormal, 0.0)));

    float distance = length(uLightPos - modelVertex);
    vec3 lightVector = normalize(uLightPos - modelVertex);

    float cosine = max(dot(modelNormal, lightVector), 0.0);
    float diffuse = cosine / (1 + (0.1 * distance * distance));

    vec3 eyeVector = -modelVertex;
    vec3 lightDir = uLightPos.xyz - modelVertex;
    vec3 eyeDir = normalize(eyeVector);
    vec3 reflection = reflect(-lightDir, modelNormal);
    float cosAlpha = clamp( dot( eyeDir, reflection ), 0, 1 );
    float specular = cosAlpha / (distance * distance);

    vec4 ambientLight = vec4(0.1, 0.1, 0.1, 1.0);
    vec4 lightColor = vec4(1.0, 1.0, 1.0, 1.0);
    vColor = uColor *
        (ambientLight +
         lightColor * diffuse +
         lightColor * specular * pow(cosAlpha, 5));
    gl_Position = uMvpMatrix * aPosition;
}