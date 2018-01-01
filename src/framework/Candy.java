/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import framework.math3d.vec4;

/**
 *
 * @author ajart
 */
public class Candy extends Sprite
{
    static float gravity = -9.8f;
    vec4 mVel;
    public Candy(Texture tex, vec4 pos, float rot, float scale)
    {
        super(tex,pos,rot,scale);
    }
    public void setVel(vec4 v)
    {
        mVel = v;
    }
}
