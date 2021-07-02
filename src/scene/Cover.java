package scene;

import controller.ImageController;
import controller.SceneController;
import displayButton.StartButton;
import utils.CommandSolver;
import utils.GameWindow;

import java.awt.*;

public class Cover extends Scene{
    private Image cover;
    private StartButton startButton;
    @Override
    public void sceneBegin() {
        GameWindow.getInstance().setWindowSize(1280,800);
        this.cover= ImageController.getInstance().tryGet("/GUI/COVER.png");
        this.startButton=new StartButton(525,690);
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
       g.drawImage(cover,0,0,1280,800,null);
       startButton.paint(g);
    }

    @Override
    public void update() {

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
                      SceneController.getInstance().change(new MenuScene());
                  }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {

            }
        };
    }
}
