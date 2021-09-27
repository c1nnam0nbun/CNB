#version 410

layout(location=0) in vec2 vUV;
layout(location=1) in vec3 vSurfaceNormal;
layout(location=2) in vec3 vToLightVector;
layout(location=3) in vec3 vViewDir;

out vec4 out_Color;

struct Material {
    vec3 diffuseColor;
    vec3 specular;
    vec3 ambient;
    float shininess;
};

uniform Material material;
uniform sampler2D diffuseTexture;
uniform vec3 lightColor;
uniform vec3 attenuation;

void main() {
    vec4 ambient = vec4(lightColor * material.ambient, 1.0);

    vec3 unitNormal = normalize(vSurfaceNormal);
    vec3 unitLightVector = normalize(vToLightVector);
    float distance = length(vToLightVector);
    float attenuationFactor = attenuation.x + (attenuation.y * distance) + (attenuation.z * distance * distance);
    float nlDot = dot(unitNormal, unitLightVector);
    float brightness = max(nlDot, 0.0);
    vec4 diffuse = vec4((brightness * lightColor * material.diffuseColor)/attenuationFactor, 1.0) * (texture(diffuseTexture, vUV) + vec4(1.0, 1.0, 1.0, 1.0));

    vec3 viewDir = normalize(vViewDir);
    vec3 reflectDir = reflect(-unitLightVector, unitNormal);
    float spec = pow(max(dot(viewDir, reflectDir), 1.0), material.shininess);
    vec4 specular = vec4((lightColor * spec * material.specular)/attenuationFactor, 1.0);

    vec4 result = ambient + diffuse + specular;

    out_Color = result;
}