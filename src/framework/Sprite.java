package framework;

import framework.math3d.*;
import static framework.math3d.math3d.*;

public class Sprite 
{
    public vec4 mPos;
    public float mRot;
    public float mScale;
    public Mesh mMesh = new Mesh("assets/SomethingTest.obj.mesh");
    public float mRad = 1; 
    
     //Basic, just a mesh
    public Sprite(Texture tex)
    {
        mMesh.texture = tex;
        mScale = 2.0f;
        mRot = 0;
        mPos = new vec4(0,0,0,1);
    }
    //Mesh + pos
    public Sprite(Texture tex, vec4 pos)
    {
        mMesh.texture = tex;
        mScale = 2.0f;
        mRot = 0;
        mPos = pos;
    }
    //Mesh + pos + rot
    public Sprite(Texture tex, vec4 pos, float rot)
    {
        mMesh.texture = tex;
        mScale = 2.0f;
        mRot = rot;
        mPos = pos;
    }
    //Mesh + pos + rot + scale
    public Sprite(Texture tex, vec4 pos, float rot, float scale)
    {
        mMesh.texture = tex;
        mScale = scale;
        mRad *=  scale;
        mRot = rot;
        mPos = pos;
    }
    
    public boolean checkCollision(Sprite other)
    {
        vec4 d = mPos.sub(other.mPos);
        if(length(d) < mRad + other.mRad)
        {
            return true;
        }
        return false;
    }
    
    public void draw(Program prog)
    {
        prog.setUniform("worldMatrix", mul(axisRotation(new vec3(0,0,1), mRot),translation(mPos)));
        prog.setUniform("isBillboard", 1.0f);
        prog.setUniform("billboardScale", mScale);
        mMesh.draw(prog);
    }
}
