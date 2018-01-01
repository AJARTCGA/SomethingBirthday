package framework;

import static framework.Util.Z_AXIS;
import framework.math3d.mat4;
import framework.math3d.math3d;
import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.translation;
import framework.math3d.vec3;
import framework.math3d.vec4;

public class Kid extends Sprite
{
    boolean mHeld = false;
    boolean mPlaced = false;
    
    public Kid (Texture tex, vec4 pos, float rot, float scale)
    {
        super(tex, pos, rot, scale);
    }
    
    public void draw(Program prog)
    {
        mat4 worldMat = mat4.identity();
        worldMat = worldMat.mul(axisRotation(Z_AXIS.xyz(), mRot));
        prog.setUniform("worldMatrix", mul(worldMat,translation(mPos)));
        prog.setUniform("isBillboard", 1.0f);
        prog.setUniform("billboardScale", mScale);
        mMesh.draw(prog);
    }
}
