package framework;

import framework.math3d.mat4;
import framework.math3d.math3d;
import static framework.math3d.math3d.*;
import framework.math3d.vec3;
import framework.math3d.vec4;
import java.util.ArrayList;
import java.util.Random;

public class Tree
{
    vec4 W, U;
    Pinata mPinata;
    Mesh mMesh;
    vec4 mPos;
    float mRad = 1.0f;
    int numKids;
    float maxTimer = 5.0f;
    float mTimer = 5.0f;
    float mCandy;
    final Random rand = new Random();
    
    public Tree(Mesh mesh, vec4 pos)
    {
        mMesh = mesh;
        mPos = pos;
        mCandy = 0;
    }
    
    public void addPinata(Sprite p)
    {
        mPinata = (Pinata)p;
        ((Pinata)p).isOnTree = true;
        mPinata.mPos = mPos.add(Util.Z_AXIS.mul(-0.45f)).add(Util.Y_AXIS.mul(1f));
    }
    
    public void addKid(Sprite k)
    {
        float x = -0.5f + rand.nextFloat();
        //float y = 0.5f + (rand.nextFloat() * 0.2f);
        float z = -1.5f + rand.nextFloat();
        
        ((Kid)k).mPos = mPos.add(Util.X_AXIS.mul(x)).add(Util.Y_AXIS.mul(0.25f)).add(Util.Z_AXIS.mul(z));
        ((Kid)k).mPlaced = true;
        numKids++;
    }
    
    public void update(float elapsed)
    {
        if(mPinata != null)
        {
            mPinata.mPos = mPos.add(Util.Z_AXIS.mul(-0.75f)).add(Util.Y_AXIS.mul(1f));
            float deltaCandy = elapsed * numKids * numKids / 5;
            mPinata.curFill -= deltaCandy;
            mCandy += deltaCandy;
        }
        
//        if(mPinata != null)
//        {
//            mTimer -= elapsed * numKids;
//            if(mTimer <= 0)
//            {
//                mTimer = maxTimer;
//            }
//        }
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
    
    void draw(Program prog)
    {
        mat4 worldMat = mat4.identity();
        worldMat = worldMat.mul(math3d.scaling(new vec3(0.3,0.3,0.3)));
        prog.setUniform("worldMatrix", worldMat);
        mMesh.draw(prog);
    }
}
