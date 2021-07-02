package gameobj;

import controller.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Marker extends GameObject{//節奏棒
    private Image image;
    private Delay delay;
    private Global.Direction dir;
    public Marker(int x, int y, Global.Direction dir) {
        super(x, y, 12, 64);
        image= ImageController.getInstance().tryGet("/MARKER.png");
        delay=new Delay(30);
        this.dir=dir;
        delay.loop();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, painter().left(), painter().top(),
                painter().width(), painter().height(), null);
    }

    @Override
    public void update() {
        switch (dir){
            case LEFT:
                    translateX(-4);
                break;
            case RIGHT:
                    translateX(4);
            case STOP:
                    translateX(0);
                break;
        }
    }
    public void changeDir(Global.Direction dir){
        this.dir=dir;
    }
}
