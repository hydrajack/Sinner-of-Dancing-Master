package scene;

import controller.ImageController;
import controller.SceneController;
import gameobj.Hero;
import utils.CommandSolver;
import utils.GameWindow;

import java.awt.*;
import java.util.ArrayList;

public class Rule extends Scene{

    private Image rule;
    private ArrayList<Hero> heroes;
    private Image title;
    private Image r1;
    private Image r2;
    private Image r3;
    private Image r4;
    private Image r5;
    private Image r6;
    private Image r7;
    private Image r8;
    private Image r9;
    private Image r10;
    private Image r11;

    public Rule(ArrayList heroes) {
        this.heroes=heroes;
    }

    @Override
    public void sceneBegin() {

        GameWindow.getInstance().setWindowSize(850,800);
        rule= ImageController.getInstance().tryGet("/GUI/bg.png");
        title=ImageController.getInstance().tryGet("/c/5.png");
        r1=ImageController.getInstance().tryGet("/c/6.png");
        r2=ImageController.getInstance().tryGet("/c/7.png");
        r3=ImageController.getInstance().tryGet("/c/8.png");
        r4=ImageController.getInstance().tryGet("/c/10.png");
        r5=ImageController.getInstance().tryGet("/c/9.png");
        r6=ImageController.getInstance().tryGet("/c/12.png");
        r7=ImageController.getInstance().tryGet("/c/11.png");
        r8=ImageController.getInstance().tryGet("/c/14.png");
        r9=ImageController.getInstance().tryGet("/c/13.png");
        r10=ImageController.getInstance().tryGet("/c/15.png");
        r11=ImageController.getInstance().tryGet("/c/17.png");
    }

    @Override
    public void sceneEnd() {

    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(rule,0,0,850,950,null);
        g.drawImage(title,270,70,null);
        g.drawImage(r1,100,190,null);
        g.drawImage(r2,360,180,null);
        g.drawImage(r3,360,310,null);
        g.drawImage(r4,100,400,null);
        g.drawImage(r5,160,400,null);
        g.drawImage(r6,90,530,null);
        g.drawImage(r7,180,540,null);
        g.drawImage(r8,100,660,null);
        g.drawImage(r9,170,660,null);
        g.drawImage(r10,705,660,null);
        g.drawImage(r11,710,620,null);
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
                    SceneController.getInstance().change(new MenuScene(heroes));
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {

            }
        };
    }

}
