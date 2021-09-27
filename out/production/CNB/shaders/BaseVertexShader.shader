#version 410

layout(location=0) in vec3 position;
layout(location=1) in vec2 uv;
layout(location=2) in vec3 normal;

layout(location=0) out vec2 vUV;
layout(location=1) out vec3 vSurfaceNormal;
layout(location=2) out vec3 vToLightVector;
layout(location=3) out vec3 vViewDir;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 view;
uniform vec3 lightPosition;
uniform vec3 cameraPosition;

void main() {
    vec4 worldPosition = model * vec4(position, 1.0);
    vViewDir = cameraPosition - worldPosition.xyz;

    vUV = uv;
    gl_Position =  projection * view * worldPosition;

    vSurfaceNormal = (model * vec4(normal, 0.0)).xyz;
    vToLightVector = lightPosition - worldPosition.xyz;
}