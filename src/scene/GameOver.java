package scene;

import controller.AudioResourceController;
import controller.ImageController;
import controller.SceneController;
import gameobj.Hero;
import utils.CommandSolver;
import utils.GameWindow;

import java.awt.*;
import java.util.ArrayList;

public class GameOver extends  Scene{
    private Image cover;
    private ArrayList<Hero>heroes;

    public GameOver(ArrayList heroes){
        this.heroes=heroes;
    }

    @Override
    public void sceneBegin() {

        GameWindow.getInstance().setWindowSize(1280,800);
        this.cover= ImageController.getInstance().tryGet("/GUI/DIE.png");
        AudioResourceController.getInstance().play("/die.wav");
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {

            @Override
            public void keyTyped(char c, long trigTime) {

            }

            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if(commandCode==4){
                    SceneController.getInstance().change(new MenuScene(heroes));
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {

            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(cover,0,0,1280,800,null);

    }

    @Override
    public void update() {

    }
}
