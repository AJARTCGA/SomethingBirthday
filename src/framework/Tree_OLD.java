package framework;

import framework.math3d.vec4;

public class Tree_OLD extends Sprite
{
    vec4 W, U;
    Pinata mPinata;
    
    public Tree_OLD(Texture tex, vec4 pos, float rot, float scale)
    {
        super(tex,pos,rot,scale);
    }
    
    public void addPinata(Sprite p)
    {
        mPinata = (Pinata)p;
        mPinata.mPos = mPos.add(U.mul(0.1f));//.add(Util.Y_AXIS .mul(-0.4f)));
    }
    
    public void update()
    {
        if(mPinata != null)
        {
            mPinata.mPos = mPos.add(U.mul(0.5f).add(Util.Y_AXIS .mul(-0.4f)));
        }
    }
}
