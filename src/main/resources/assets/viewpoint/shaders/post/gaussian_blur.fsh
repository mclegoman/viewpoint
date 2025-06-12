#version 150

#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;

layout(std140) uniform BlurConfig {
    vec2 BlurDir;
    float Radius;
};

in vec2 texCoord;
in vec2 sampleStep;

out vec4 fragColor;

float gaussian(float x, float sigma) {
    return exp(-(x * x) / (2.0 * sigma * sigma));
}

void main() {
    vec4 blurred = vec4(0.0);
    float weightSum = 0.0;

    float actualRadius = Radius >= 0.5 ? round(Radius) : float(MenuBlurRadius);
    float sigma = actualRadius / 2.0;

    for (float a = -actualRadius + 0.5; a <= actualRadius; a += 2.0) {
        float weight = gaussian(a, sigma);
        blurred += texture(InSampler, texCoord + sampleStep * a) * weight;
        weightSum += weight;
    }

    float lastWeight = gaussian(actualRadius, sigma) / 2.0;
    blurred += texture(InSampler, texCoord + sampleStep * actualRadius) * lastWeight;
    weightSum += lastWeight;

    fragColor = blurred / weightSum;
}
