/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import static JGL.JGL.*;
import JSDL.JSDL;
import static JSDL.JSDL.*;
import TextRendering.*;
import static framework.Util.Y_AXIS;
import framework.math3d.*;
import static framework.math3d.math3d.add;
import static framework.math3d.math3d.length;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.scaling;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GameLoop 
{
    
    int[] tmp = new int[1];
    long win;
    int vao = tmp[0];
    Set<Integer> keys = new TreeSet<>();
    Camera cam;
    Program prog;
    Program blurprog;
    float prev, elapsed;
    ImageTexture basicCharTex = new ImageTexture("assets/idle.png");
    ImageTexture treeTex = new ImageTexture("assets/TreeTex.png");
    ImageTexture pinataTex = new ImageTexture("assets/pig.png");
    ImageTexture kidTex = new ImageTexture("assets/kid.png");
    ImageTexture timTex = new ImageTexture("assets/Timmy.png");
    ImageTexture alphabetTex = new ImageTexture("assets/Kristen.png");
    Mesh boundary = new Mesh("assets/boundary.obj.mesh");
    Mesh skyBox = new Mesh("assets/SkyBox.obj.mesh");
    Mesh treeMesh = new Mesh("assets/tree.obj.mesh");
    Player player = new Player(basicCharTex, new vec4(0,0.33f,2,1), 0, 0.5f);
    //Tree tree= new Tree(treeTex, new vec4(0,1,0,1), 0, 3.0f);
    Tree tree = new Tree(treeMesh, new vec4(0,0,0,1));
    UnitSquare usq;
    Framebuffer fbo1;
    Framebuffer fbo2;
    Texture2D dummytex = new SolidTexture(GL_UNSIGNED_BYTE,0,0,0,0);
    ArrayList<Pinata> pinataList = new ArrayList<>();
    ArrayList<Kid> kidList = new ArrayList<>();
    //mesh stuff goes here for now
    Grass grass = new Grass();
    SDL_Event ev=new SDL_Event();
    Font kristenFont;
    DrawableString candyText, kidsText, timeText;
    int minutes;
    float seconds;
    float spawner;
    Random rand = new Random();
    StateManager manager;
    boolean done = false;

    //------------------------------
    
    //mesh stuff goes here for now
    public GameLoop(long w, StateManager sm) throws IOException
    {
        glGenVertexArrays(1,tmp);
        glBindVertexArray(vao);

        //glClearColor(191f/255f,1f,198f/255f,1.0f);
        glClearColor(0f,0f,0f,1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        prog = new Program("vs.txt","fs.txt");



        prev = (float)(System.nanoTime()*1E-9);
        win = w;
        manager = sm;
        
        Kid Timmy = new Kid(timTex, new vec4(0,0,0,1), 0, 0.5f); 
        tree.addKid(Timmy);
        Timmy.mPos = new vec4(0,0.3,-0.7,1);
        kidList.add(Timmy);
        for(int i = 0; i < 8; i++)
        {
            spawnKid(6,20);
            if (i % 2 == 0)
                spawnPinata(6,20);
        }
        try {
            kristenFont = new Font("Kristen.fnt");
        } catch (IOException ex) {
            Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
        }
        minutes = 2;
        seconds = 0f;
    }
    
    private void spawnKid(float minDist, float maxDist)
    {
        vec4 v = new vec4(-maxDist / 2 + rand.nextFloat() * maxDist, .25f,-maxDist / 2 + rand.nextFloat() * maxDist,1);
        while(length(v) < minDist)
        {
            v = new vec4(-maxDist / 2 + rand.nextFloat() * maxDist, .25f,-maxDist / 2 + rand.nextFloat() * maxDist,1);
        }
        kidList.add(new Kid(kidTex, v, 0, 0.3f));
    }
    
    private void spawnPinata(float minDist, float maxDist){
        vec4 v = new vec4(-maxDist / 2 + rand.nextFloat() * maxDist, .25f,-maxDist / 2 + rand.nextFloat() * maxDist,1);
        while(length(v) < minDist)
        {
            v = new vec4(-maxDist / 2 + rand.nextFloat() * maxDist, .25f,-maxDist / 2 + rand.nextFloat() * maxDist,1);
        }
        pinataList.add(new Pinata(pinataTex, v, 0, 0.3f));
    }
    
    void runLoop()
    {
        JSDL.SDL_Event ev=new JSDL.SDL_Event();
        while(!done)
        {
            getInput();
            
            float now = (float)(System.nanoTime()*1E-9);
            elapsed = now-prev;
            prev=now;
            seconds -= elapsed;
            spawner += elapsed;
            
            if (spawner >= 1.5f){
                float chance = rand.nextFloat();
                if (0.5f < chance && chance < 0.9f){
                    spawnKid(2,20);
                }
                else if (chance >= 0.9f){
                    spawnPinata(2,20);
                }
                spawner = 0f;
            }
            
            if(seconds <= 0)
            {
                if(minutes <= 0)
                {
                    done = true;
                    manager.resultScreen(Math.round(tree.mCandy), tree.numKids);
                }
                minutes--;
                seconds = 60.0f;
            }
            player.update(elapsed);
            tree.U = player.U;
            tree.update(elapsed);
            checkPinata();
            player.checkPlayerCollisions(pinataList, kidList, tree);
            kidsText = new DrawableString("Kids: " + tree.numKids, 20,20,20, kristenFont);
            candyText = new DrawableString("Candy: " + Math.round(tree.mCandy), 20,40,20, kristenFont);
            String timeString = "Time: " + minutes + ":" + String.format("%02d", (int)seconds);
            timeText = new DrawableString(timeString, 20,60,20, kristenFont);
            handleInput();
            render();
        }
    }
    
    void checkPinata()
    {
        if(tree.mPinata != null && tree.mPinata.curFill < 0)
        {
            pinataList.remove(tree.mPinata);
            tree.mPinata = null;
        }
    }
    
    void getInput()
    {
        while(true)
        {
            int rv = SDL_PollEvent(ev);
            if( rv == 0 )
                break;
            //System.out.println("Event "+ev.type);
            if( ev.type == SDL_QUIT )
                System.exit(0);
            if( ev.type == SDL_MOUSEMOTION ){
                //System.out.println("Mouse motion "+ev.motion.x+" "+ev.motion.y+" "+ev.motion.xrel+" "+ev.motion.yrel);
            }
            if( ev.type == SDL_KEYDOWN ){
                //System.out.println("Key press "+ev.key.keysym.sym+" "+ev.key.keysym.sym);
                keys.add(ev.key.keysym.sym);
            }
            if( ev.type == SDL_KEYUP ){
                keys.remove(ev.key.keysym.sym);
            }
        }
    }
    void handleInput()
    {
        if( keys.contains(SDLK_w ))
            player.move(1.0f, elapsed);
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
            done = true;
            manager.resultScreen(Math.round(tree.mCandy), tree.numKids);
        }
        
        if(!keys.contains(SDLK_w ) && !keys.contains(SDLK_a ) && !keys.contains(SDLK_s ) && !keys.contains(SDLK_d )){
            player.mAnim = player.idle;
        }
    }
    void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        prog.use();
        prog.setUniform("lighting", 1.0f);
        prog.setUniform("worldMatrix",mat4.identity());
        player.draw(prog);
        prog.setUniform("isBillboard", 0.0f);
        prog.setUniform("lighting", 0.0f);
        prog.setUniform("worldMatrix",scaling(100,100,100));
        skyBox.draw(prog);
        prog.setUniform("lighting", 1.0f);
        prog.setUniform("worldMatrix",mat4.identity());
        grass.draw(prog);
        boundary.draw(prog);
        //tree.draw(prog);
        tree.draw(prog);
        for(Pinata p : pinataList)
        {
            p.draw(prog);
        }
        prog.setUniform("fillAmount", 0.0f);
        for(Kid k : kidList)
        {
            k.draw(prog);
        }
        prog.setUniform("isBillboard", 0.0f);
        prog.setUniform("diffuse_texture",alphabetTex);
        prog.setUniform("lighting", 0.0f);
        kidsText.draw(prog);
        candyText.draw(prog);
        timeText.draw(prog);
        SDL_GL_SwapWindow(win);
    }
}
