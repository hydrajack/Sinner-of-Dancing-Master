package gameobj;

import controller.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Combo extends GameObject{
    private Image image;
    private Image image2;
    private Image image3;
    private State state;
    private Delay delay;
    public enum State{
        NORMAL,
        GREAT,
        MISS;

    }
    public Combo(int x, int y ) {
        super(x, y, Global.CENTER_WIDTH, Global.CENTER_HEIGHT/2);
        image= ImageController.getInstance().tryGet("/NORMAL.png");
        image2=ImageController.getInstance().tryGet("/GREAT.png");
        image3=ImageController.getInstance().tryGet("/MISS.png");
        this.state=State.NORMAL;
        delay=new Delay(30);
    }

    @Override
    public void paintComponent(Graphics g) {
        switch (state){
            case NORMAL:
                g.drawImage(image, painter().left(), painter().top(),
                        painter().width(), painter().height(), null);
                break;
            case GREAT:
                g.drawImage(image2, painter().left(), painter().top(),
                        painter().width(), painter().height(), null);
                break;
            case MISS:
                g.drawImage(image3, painter().left(), painter().top(),
                        painter().width(), painter().height(), null);
                break;
        }
    }

    @Override
    public void update() {
        if((state==State.GREAT || state==State.MISS)&& delay.isStop()){
            delay.play();
        }
        if(delay.count()){
            changeState(State.NORMAL);
        }

    }
    public void changeState(State state){

        this.state=state;
    }
}
