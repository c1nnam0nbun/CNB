Shader "Vertex" {
    version: 400;
    attributes: {
        vec3 position;
        vec2 uv;
        vec3 normal;
    };
    output: {
        vec2 vUV;
        vec3 vSurfaceNormal;
        vec3 vToLightVector;
        vec3 vViewDir;
    };
    structs: {
    };
    uniforms: {
        mat4 projection;
        mat4 model;
        mat4 view;
        vec3 lightPosition;
        vec3 cameraPosition;
    };

    void main() {
        vec4 worldPosition = model * vec4(position, 1.0);
        vViewDir = cameraPosition - worldPosition.xyz;
        vUV = uv;
        gl_Position =  projection * view * worldPosition;
        vSurfaceNormal = (model * vec4(normal, 0.0)).xyz;
        vToLightVector = lightPosition - worldPosition.xyz;
    }
}

Shader "Fragment" {
    version: 400;
    attributes: {
        vec2 vUV;
        vec3 vSurfaceNormal;
        vec3 vToLightVector;
        vec3 vViewDir;
    };
    output: {
        vec4 out_Color;
    };
    structs: {
        Material(vec3 diffuseColor, vec3 specular, vec3 ambient, float shininess);
    };
    uniforms: {
        Material material;
        sampler2D diffuseTexture;
        vec3 lightColor;
        vec3 attenuation;
    };

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
}