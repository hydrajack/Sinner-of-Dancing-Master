package scene;

import controller.AudioResourceController;
import controller.ImageController;
import controller.SceneController;
import gameobj.Hero;
import utils.CommandSolver;
import utils.Delay;
import utils.GameWindow;

import java.awt.*;
import java.util.ArrayList;

public class Warning extends Scene {
    private Image warning;
    private ArrayList<Hero> heroes;
    private Delay goToLevel3;
    private int width;

    public Warning(ArrayList heroes) {
        this.heroes = heroes;
    }

    @Override
    public void sceneBegin() {

        GameWindow.getInstance().setWindowSize(1280,800);
        this.warning = ImageController.getInstance().tryGet("/GUI/warning.png");
        goToLevel3 = new Delay(120);
        goToLevel3.play();
        width = 1280;
        AudioResourceController.getInstance().play("/warn.wav");
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(warning, width, 0, 1280, 800, null);
    }

    @Override
    public void update() {
        if (width > 0) {
            width -= 25;
            if (width < 25) {
                width = 0;
            }
        }
        if (width == 0 && goToLevel3.count()){
            SceneController.getInstance().change(new SceneLevel3(heroes));
        }
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

}
