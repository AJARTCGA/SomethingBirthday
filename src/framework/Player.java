package framework;

import framework.math3d.vec2;
import framework.math3d.vec4;
import framework.Util.*;
import static framework.math3d.math3d.normalize;
import static framework.math3d.math3d.length;
import framework.math3d.vec3;
import java.util.ArrayList;
import java.util.LinkedList;


public class Player extends Sprite 
{
    Camera cam;
    float moveSpeed = 5;
    float rotSpeed = 5;
    float camDist;
    float frame;
    vec4 W = new vec4(0,0,1,0);
    vec4 U = new vec4(1,0,0,0);
    Sprite mHeldItem;
    Sprite blackListedItem;
    float mTimer = 0.0f;
    
    LinkedList<ImageTexture> move_up;
    LinkedList<ImageTexture> move_left;
    LinkedList<ImageTexture> move_right;
    LinkedList<ImageTexture> move_down;
    LinkedList<ImageTexture> idle;    
    LinkedList<ImageTexture> mAnim;
    
    public Player (Texture tex, vec4 pos, float rot, float scale)
    {
        super(tex, pos, rot, scale);
        
        move_up = new LinkedList<>();
        for (int i = 1; i<7; i++){
            move_up.add(new ImageTexture("assets/run_up/part" + i + ".png"));
        }
        
        move_left = new LinkedList<>();
        for (int i = 1; i<7; i++){
            move_left.add(new ImageTexture("assets/run_left/part" + i + ".png"));
        }
        
        move_right = new LinkedList<>();
        for (int i = 1; i<7; i++){
            move_right.add(new ImageTexture("assets/run_right/part" + i + ".png"));
        }
        
        move_down = new LinkedList<>();
        for (int i = 1; i<7; i++){
            move_down.add(new ImageTexture("assets/run_down/part" + i + ".png"));
        }
        
        idle = new LinkedList<>();
        idle.add(new ImageTexture("assets/idle.png"));
        mAnim = idle;
        
        cam = new Camera();
        cam.lookAt(mPos.add(new vec4(2,2,2,0)).xyz(), mPos.xyz(), Util.Y_AXIS.xyz());
        W = normalize(cam.eye.sub(mPos));
        U = new vec4(W.z, 0, -W.x, 0); 
        camDist = length(mPos.sub(cam.eye));
    }
    
   public void rotate(float elapsed)
   {
       W = cam.rotate(this.mPos, rotSpeed * elapsed);
       U = new vec4(W.z, 0, -W.x, 0); 
   }
   
   public void update()
   {
       cam.eye = mPos.add(W.mul(camDist));
       cam.lookAt(cam.eye.xyz(), mPos.xyz(), Util.Y_AXIS.xyz());
       
       if(mHeldItem != null)
       {
           mHeldItem.mPos = mPos.add(W.mul(0.1f)).add(Util.Y_AXIS.mul(0.35f));
       }
   }
   public void update(float elapsed)
   {
       frame += elapsed;
       if(frame >= 0.12f){
           ImageTexture tmp = mAnim.pop();
           mAnim.add(tmp);
           mMesh.texture = mAnim.peekFirst();
           frame = 0f;
       }
       
       cam.eye = mPos.add(W.mul(camDist));
       cam.lookAt(cam.eye.xyz(), mPos.xyz(), Util.Y_AXIS.xyz());
       
       if(mHeldItem != null)
       {
           mHeldItem.mPos = mPos.add(W.mul(0.1f)).add(Util.Y_AXIS.mul(0.05f + mHeldItem.mScale));
       }
       if(blackListedItem != null)
       {
           mTimer -= elapsed;
           if(mTimer <= 0)
           {
               blackListedItem = null;
           }
       }
   }
   
   public void move(float amount, float elapsed)
   {
       if (amount < 0f){
           if(!mAnim.equals(move_down)){
                mAnim = move_down;
            }
       }
       else{
            if(!mAnim.equals(move_up)){
                mAnim = move_up;
            }
       }
       
       vec2 temp = W.neg().xz().mul(moveSpeed * amount * elapsed);
       mPos = mPos.add(new vec4(temp.x, 0, temp.y, 0));
       checkBounds();
       update();
   }
   
   public void strafe(float amount, float elapsed)
   {
       
       if (amount < 0f){
           if(!mAnim.equals(move_left)){
                mAnim = move_left;
            }
       }
       else{
            if(!mAnim.equals(move_right)){
                mAnim = move_right;
            }
       }
       
       vec2 temp = U.xz().mul(moveSpeed * amount * elapsed);
       mPos = mPos.add(new vec4(temp.x, 0, temp.y, 0));
       checkBounds();
       update();
   }
   
   private void checkBounds()
   {
       if(mPos.x > Util.PLANE_SIZE - 0.1f)
       {
           mPos.x = Util.PLANE_SIZE - 0.1f;
       }
       else if(mPos.x < -Util.PLANE_SIZE + 0.1f)
       {
           mPos.x = -Util.PLANE_SIZE + 0.1f;
       }
       if(mPos.z > Util.PLANE_SIZE - 0.1f)
       {
           mPos.z = Util.PLANE_SIZE - 0.1f;
       }
       else if(mPos.z < -Util.PLANE_SIZE + 0.1f)
       {
           mPos.z = -Util.PLANE_SIZE + 0.1f;
       }
   }
   public void checkPlayerCollisions(ArrayList<Pinata> pinataList, ArrayList<Kid> kidList, Tree tree)
   {
       pinataCollisions(pinataList);
       kidCollisions(kidList);
       treeCollisions(tree);
   }
   
   private void pinataCollisions(ArrayList<Pinata> pinataList)
   {
       if(mHeldItem == null)
       {
            for(int i  = 0; i < pinataList.size(); i++)
            {
                if(blackListedItem != pinataList.get(i) && !pinataList.get(i).isOnTree)
                {
                    if(pinataList.get(i).checkCollision(this))
                    {
                        mHeldItem = pinataList.get(i);
                    }
                }
            }
       }
   }
   
   private void kidCollisions(ArrayList<Kid> kidList)
   {
       if(mHeldItem == null)
       {
            for(int i  = 0; i < kidList.size(); i++)
            {
                if(!kidList.get(i).mPlaced && blackListedItem != kidList.get(i)) 
                {
                    if(kidList.get(i).checkCollision(this))
                    {
                         mHeldItem = kidList.get(i);
                         kidList.get(i).mHeld = true;
                    }
                }
            }
       }
   }
   
   private void treeCollisions(Tree tree)
   {
       if(tree.checkCollision(this))
       {
           if(mHeldItem != null)
           {
               if(mHeldItem instanceof Pinata && tree.mPinata == null)
               {
                    tree.addPinata(mHeldItem);
                    mHeldItem = null;
               }
               else if (mHeldItem instanceof Kid)
               {
                   tree.addKid(mHeldItem);
                   ((Kid)mHeldItem).mHeld = false;
                   mHeldItem = null;
               }
           }
       }
   }
   
   public void zoom(float amount)
   {
       camDist += amount;
       if (camDist < 2.8284f)
           camDist = 2.8284f;
       if (camDist > 8)
           camDist = 8;
   }
   
   public void drop()
   {
       if(mHeldItem != null)
       {
            blackListedItem = mHeldItem; 
            if (mHeldItem instanceof Kid)
            {
                ((Kid)mHeldItem).mHeld = false;
            }
            mHeldItem.mPos.y = 0.25f;
            mHeldItem = null;
            mTimer = 2.0f;
       }
   }
   
   public void draw(Program prog)
   {       
       cam.draw(prog);
       super.draw(prog);
   }
}
