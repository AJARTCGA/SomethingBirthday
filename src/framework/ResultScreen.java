/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import static JGL.JGL.GL_COLOR_BUFFER_BIT;
import static JGL.JGL.GL_DEPTH_BUFFER_BIT;
import static JGL.JGL.glClear;
import JSDL.JSDL;
import static JSDL.JSDL.*;
import TextRendering.DrawableString;
import framework.math3d.mat4;
import framework.math3d.vec4;
import java.io.IOException;
import java.util.LinkedList;

public class ResultScreen extends GameLoop
{
    ImageTexture backgroundTex;
    LinkedList<ImageTexture> mAnim = new LinkedList<>();
    
    float frame = 0;
    Sprite background = new Sprite(backgroundTex, new vec4(0,0,-1,1));
    UnitSquare usq;
    int candyNum, kidNum;
    DrawableString mainText, line1, line2, line3;
    boolean firstCycle = true;
    int frameCheck = 1;
    StateManager manager; 
    
    public ResultScreen(int candy, int kids, long w, StateManager sm) throws IOException
    {
        super(w, sm);
        for (int i = 1; i<18; i++)
        {
            mAnim.add(new ImageTexture("assets/partyhatAnimation/" + i + ".png"));
            //mAnim.add(new ImageTexture("assets/idle.png"));
        }
        candyNum = candy;
        kidNum = kids;
        usq = new UnitSquare();
        mainText = new DrawableString("Party's Over!", 800,20,20, kristenFont);
    }
    
     void runLoop()
    {
        JSDL.SDL_Event ev=new JSDL.SDL_Event();
        while(!done)
        {
            getInput();
            handleInput();
            update();
            render();            
        }
    }
    void update()
    {
        float now = (float)(System.nanoTime()*1E-9);
        elapsed = now-prev;
        prev=now;
        frame +=elapsed;
        if (firstCycle)
        {
            if(frame >= .085f)
            {
               //ImageTexture tmp = mAnim.pop();
               //mAnim.add(tmp);
               backgroundTex = mAnim.get(frameCheck);
    
               if(frameCheck == 16)
                   firstCycle=false;
               else{
                   frameCheck++;
               }
               frame = 0f;
            }
        }
        else
        {
           if(frame >=.085f)
           {
               //ImageTexture tmp = mAnim.peekLast();
               //mAnim.add(tmp);
               backgroundTex = mAnim.get(frameCheck);
               
               if(frameCheck == 0)
                   firstCycle=true;
               else
                   frameCheck--;
               frame = 0f; 
           }
        }
        
        
    }
    void handleInput()
    {
        if( keys.contains(SDLK_w ))
            {
                if(keys.contains(SDLK_LSHIFT))
                {
                    player.move(5.0f, elapsed);
                }
                else
                {
                    player.move(1.0f, elapsed);
                }
            }
            if( keys.contains(SDLK_r))
                manager = new StateManager(win);
            if( keys.contains(SDLK_s))
                player.move(-1.0f, elapsed);
            if( keys.contains(SDLK_a))
                player.strafe(-1.0f, elapsed);
            if( keys.contains(SDLK_d))
                player.strafe(1.0f, elapsed);
            if( keys.contains(SDLK_RIGHT))
                player.rotate(-elapsed);
            if( keys.contains(SDLK_LEFT))
                player.rotate(elapsed);
            if( keys.contains(SDLK_UP))
                player.zoom(-2.0f * elapsed);
            if( keys.contains(SDLK_DOWN))
                player.zoom(2.0f * elapsed);
            if( keys.contains(SDLK_q))
            {
                player.drop();
                keys.remove(SDLK_q);
            }
            if(keys.contains(SDLK_ESCAPE))
            {
                System.exit(0);
            }
    }
    void render()
    {
        mainText = new DrawableString("Party's Over!", 800,20,20, kristenFont);
        
        line1 = new DrawableString("Little Jimmy grabbed " + candyNum + " pieces of candy", 600,800,20, kristenFont);
        line2 = new DrawableString("with the help of " + (kidNum - 1) + " friends!", 600,820,20, kristenFont);
        line3 = new DrawableString("R to restart! Escape to exit the game!", 600,840,20, kristenFont);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        prog.use();
        prog.setUniform("lighting", 1.0f);
        prog.setUniform("worldMatrix",mat4.identity());
        prog.setUniform("diffuse_texture", backgroundTex);
        usq.draw(prog);
        prog.setUniform("isBillboard", 0.0f);
        prog.setUniform("diffuse_texture",alphabetTex);
        prog.setUniform("lighting", 0.0f);
        mainText.draw(prog);
        line1.draw(prog);
        line2.draw(prog);
        line3.draw(prog);
        SDL_GL_SwapWindow(win);
    }
}
