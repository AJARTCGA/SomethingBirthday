package framework;

import framework.math3d.mat4;
import framework.math3d.math3d;
import framework.math3d.vec3;

public class Grass 
{
    private final Mesh mesh = new Mesh("assets/grass.obj.mesh");
    
    public Grass()
    {
        
    }
    
    public void update(float dt)
    {
        
    }
    
    public void draw(Program prog)
    {
        mat4 worldMat = mat4.identity();
        worldMat = worldMat.mul(math3d.scaling(new vec3(Util.PLANE_SIZE, 6, Util.PLANE_SIZE)));
        prog.setUniform("worldMatrix", worldMat);
        mesh.draw(prog);
    }
}
