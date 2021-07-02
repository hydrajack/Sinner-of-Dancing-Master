package scene;

import controller.ImageController;
import controller.SceneController;
import gameobj.Hero;
import utils.CommandSolver;
import utils.Delay;
import utils.GameWindow;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class Victory extends Scene {

    private Image victory;
    private ArrayList<Hero> heroes;
    private Delay backToMenu;

    public Victory(ArrayList heroes) {
        this.heroes = heroes;

    }

    @Override
    public void sceneBegin() {

        GameWindow.getInstance().setWindowSize(1280,800);
        this.victory= ImageController.getInstance().tryGet("/GUI/VICTORY.png");
        backToMenu=new Delay(120);
        backToMenu.play();
    }

    @Override
    public void sceneEnd() {

    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(victory,0,0,1280,800,null);
    }

    @Override
    public void update() {
        if(backToMenu.count()){
            SceneController.getInstance().change(new MenuScene(heroes));
        }
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
}
