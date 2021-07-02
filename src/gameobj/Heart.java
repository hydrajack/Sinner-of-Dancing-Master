package gameobj;

import controller.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Heart extends GameObject{
    private Image image;
    private Image image2;
    private Delay delay;
    private State state;
    public enum State{
         JUMP,
         NORMAL;

    }
    public Heart(int x, int y) {
        super(x-43-36+8, y, Global.CENTER_WIDTH+80 , Global.CENTER_HEIGHT,x+5-48,y-20,
                Global.CENTER_WIDTH+25,Global.CENTER_HEIGHT+25);
        image= ImageController.getInstance().tryGet("/HEART.png");
        image2=ImageController.getInstance().tryGet("/HEART2.png");
        delay=new Delay(20);
        this.state=State.NORMAL;
    }

    @Override
    public void paintComponent(Graphics g) {
        switch (state){
            case NORMAL:
                g.drawImage(image2, painter().left(), painter().top(),
                        painter().width(), painter().height(), null);
                break;
            case JUMP:
                g.drawImage(image, painter().left(), painter().top(),
                        painter().width(), painter().height(), null);
                break;
        }
    }

    @Override
    public void update() {
         if(state==State.JUMP && delay.isStop()){
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
