/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.translation;
import framework.math3d.vec3;
import java.util.Random;
import framework.math3d.vec4;

/**
 *
 * @author ajart
 */
public class Pinata extends Sprite
{
    public float curFill;
    public boolean isOnTree = false;
    final Random rand = new Random();
    
    public Pinata (Texture tex, vec4 pos, float rot, float scale)
    {
        super(tex, pos, rot, scale);
        float tmp = rand.nextFloat()*100;
        curFill = (tmp)*(tmp)/10;
    }
    public void draw(Program prog)
    {
        prog.setUniform("worldMatrix", mul(axisRotation(new vec3(0,0,1), mRot),translation(mPos)));
        prog.setUniform("isBillboard", 1.0f);
        prog.setUniform("billboardScale", mScale);
        prog.setUniform("fillAmount", curFill / 1000);
        mMesh.draw(prog);
    }
}
