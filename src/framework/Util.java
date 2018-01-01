package framework;

import framework.math3d.vec3;
import framework.math3d.vec4;

public class Util 
{
    public static final vec4 X_AXIS = new vec4(1,0,0,1);
    public static final vec4 Y_AXIS = new vec4(0,1,0,1);
    public static final vec4 Z_AXIS = new vec4(0,0,1,1);
    public static float PLANE_SIZE = 10;
    public static String vs = "#version 150\n" +
"\n" +
"in vec3 a_position;\n" +
"in vec2 a_texcoord;\n" +
"in vec3 a_normal;\n" +
"\n" +
"out vec2 v_texcoord;\n" +
"out vec3 v_normal;\n" +
"out vec3 v_pw;\n" +
"\n" +
"uniform mat4 projMatrix;\n" +
"uniform mat4 viewMatrix;\n" +
"uniform mat4 worldMatrix;\n" +
"\n" +
"uniform vec3 cameraU;\n" +
"uniform vec3 cameraV;\n" +
"uniform float isBillboard;\n" +
"uniform float billboardScale;\n" +
"uniform vec3 eyePos;\n" +
"\n" +
"void main()\n" +
"{\n" +
"\n" +
"	if(isBillboard == 0.0)\n" +
"	{\n" +
"		v_texcoord = a_texcoord;\n" +
"		\n" +
"		vec4 p = vec4( a_position.xyz, 1.0 );\n" +
"		p = p * worldMatrix;\n" +
"		v_pw = p.xyz;\n" +
"		p = p * viewMatrix;\n" +
"		p = p * projMatrix;\n" +
"		v_normal = (vec4(a_normal,0.0) * worldMatrix).xyz;\n" +
"		gl_Position = p;\n" +
"	}\n" +
"	else\n" +
"	{\n" +
"		v_texcoord = a_texcoord;  \n" +
"		vec2 t = (a_texcoord - vec2(0.5)) * billboardScale;\n" +
"		vec4 p = vec4(0,0,0,1);\n" +
"		p = p * worldMatrix;\n" +
"		v_pw = p.xyz;\n" +
"    		vec4 n = vec4(a_normal.xyz, 0.0) * worldMatrix;\n" +
"		p = p + vec4(cameraU * t.x,0) + vec4(cameraV * t.y,0);\n" +
"		p = p * viewMatrix;\n" +
"		p = p * projMatrix;\n" +
"		v_normal = normalize(eyePos - v_pw);//(vec4(a_normal,0.0) * worldMatrix).xyz;\n" +
"		gl_Position = p;\n" +
"	}\n" +
"}";
    public static String fs = "#version 150\n" +
"\n" +
"uniform vec3 lightPos;\n" +
"uniform vec3 eyePos;\n" +
"uniform sampler2D diffuse_texture;\n" +
"uniform float lighting;\n" +
"uniform float fillAmount;\n" +
"uniform float isBillboard;\n" +
"\n" +
"in vec2 v_texcoord;\n" +
"in vec3 v_normal;\n" +
"in vec3 v_pw;\n" +
"\n" +
"out vec4 color;\n" +
"\n" +
"void main(){\n" +
"    vec4 tc = texture(diffuse_texture,v_texcoord);\n" +
"	if(tc.a < 0.1)\n" +
"	{\n" +
"		discard;\n" +
"	}\n" +
"    \n" +
"    if (lighting == 1.0)\n" +
"    {\n" +
"        vec3 N = normalize(v_normal);\n" +
"        vec3 V = normalize(eyePos - v_pw);\n" +
"        vec3 L = (lightPos - v_pw);\n" +
"        float Ldist = length(L);\n" +
"        L = 1.0/Ldist * L;\n" +
"        float dp = dot(L,N);\n" +
"        if(isBillboard == 1.0)\n" +
"        {\n" +
"            dp += (1 - dp) * 0.7;\n" +
"        }\n" +
"        dp = clamp(dp,0.0,1.0);\n" +
"        vec3 R = reflect(-L,N);\n" +
"        //float sp = dot(V,R);\n" +
"        //sp *= sign(dp);\n" +
"        //sp = pow(sp,32.0); \n" +
"        //sp = clamp(sp,0.0,1.0);\n" +
"        color = vec4( dp*tc.rgb, tc.a);// + vec3(sp)  ,tc.a );\n" +
"        if(fillAmount > 0.0)\n" +
"        {\n" +
"            vec4 tmp = vec4(mix(vec3(1,0,0), vec3(0,1,0), fillAmount), tc.a);\n" +
"	    color = vec4(mix(tmp.rgb, color.rgb, 0.25f), tc.a);\n" +
"        }\n" +
"    }\n" +
"    else\n" +
"    {\n" +
"        color = tc;\n" +
"    }\n" +
"}";
}
