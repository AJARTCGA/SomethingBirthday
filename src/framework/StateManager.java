package framework;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StateManager 
{
    long win;
    GameLoop mainLoop;
    
    public StateManager(long w)
    {
        win = w;
        mainMenu();
    }
    
    public void mainMenu()
    {
        mainLoop = null;
        System.gc();
        try {
            mainLoop = new MainMenu(win, this);
            mainLoop.runLoop();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void resultScreen(int candy, int kids)
    {
        mainLoop = null;
        System.gc();
        try {
            mainLoop = new ResultScreen(candy, kids, win, this);
            mainLoop.runLoop();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void launchGame()
    {
        mainLoop = null;
        System.gc();
        try {
            mainLoop = new GameLoop(win, this);
            mainLoop.runLoop();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

