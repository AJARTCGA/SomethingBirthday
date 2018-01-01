package framework;

import static JGL.JGL.GL_COLOR_BUFFER_BIT;
import static JGL.JGL.GL_DEPTH_BUFFER_BIT;
import static JGL.JGL.glClear;
import JSDL.JSDL;
import static JSDL.JSDL.*;
import TextRendering.DrawableString;
import framework.math3d.mat4;
import java.io.IOException;

public class MainMenu extends GameLoop 
{
    UnitSquare usq;
    ImageTexture menuTex = new ImageTexture("assets/MainMenu.png");
    DrawableString testString;
    public MainMenu(long w, StateManager sm) throws IOException 
    {
        super(w, sm);
        usq = new UnitSquare();
        testString = new DrawableString(" ", 100,100,20,kristenFont);
    }
     void runLoop()
    {
        JSDL.SDL_Event ev=new JSDL.SDL_Event();
        while(!done)
        {
            getInput();
            handleInput();
            render();
        }
    }
    void handleInput()
    {
        if( keys.contains(SDLK_RETURN))
            manager.launchGame();
        else if( keys.contains(SDLK_ESCAPE))
            System.exit(0);
    }
    void render()
    {
        testString = new DrawableString(" ", 100,100,20,kristenFont);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        prog.use();
        prog.setUniform("lighting", 1.0f);
        prog.setUniform("worldMatrix",mat4.identity());
        prog.setUniform("diffuse_texture", menuTex);
        usq.draw(prog);
        prog.setUniform("isBillboard", 0.0f);
        prog.setUniform("diffuse_texture",alphabetTex);
        prog.setUniform("lighting", 0.0f);
        testString.draw(prog);
        SDL_GL_SwapWindow(win);
    }
}
