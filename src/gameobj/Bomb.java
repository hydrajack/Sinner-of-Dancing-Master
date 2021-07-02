package gameobj;

import controller.ImageController;
import utils.Delay;

import java.awt.*;

public class Bomb   extends GameObject {
    private Delay delayForChangeImgBomb;
    private Delay delayForChangeImgEXP;
    private Image imageTrap;
    private Image imageBomb;
    private Image imageBombExp;
    private int countForBomb;
    private int countForExp;
    private boolean firstTime;
    private boolean firstTimeBoom;

    private boolean firstTimeColiForPic;
    private boolean firstTimeColiForColiHero;
    private State state;
    public Bomb(int x, int y) {
        super( x-64,y-64,192,192,x,y,64,64);
        state=State.TRAP;
        imageTrap=ImageController.getInstance().tryGet("/weapon/BOMBTRAP.png");
        imageBomb= ImageController.getInstance().tryGet("/weapon/BOMB.png");
        imageBombExp=ImageController.getInstance().tryGet("/weapon/BOMBEXPLOSION.png");
        this.delayForChangeImgBomb=new Delay(20);
        this.delayForChangeImgBomb.play();
        delayForChangeImgEXP=new Delay(10);
        countForBomb=0;
        countForExp=1;
        firstTime=true;
        firstTimeBoom=true;
        firstTimeColiForPic =true;
        firstTimeColiForColiHero=true;
    }
    public enum State{
        TRAP,
        BOMB,
        EXP,
        DEAD;
    }
    public void setState(State state) {
        this.state = state;
    }
    public State getState() {
        return state;
    }

    @Override
    public void paintComponent(Graphics g) {
        switch (state){
            case TRAP:
                g.drawImage(imageTrap,painter().left(), painter().top(),painter().right(),painter().bottom(),0,0,14,14,null);
                break;
            case BOMB:
                g.drawImage(imageBomb, painter().left(), painter().top(),painter().right(),painter().bottom(),0+countForBomb*24,0,24+countForBomb*24,24,null);
                break;
            case EXP:
                if(firstTimeBoom){
                    painter().setLeft(painter().left()-64);
                    painter().setTop(painter().top()-64);
                    painter().setRight(painter().right()+64);
                    painter().setBottom(painter().bottom()+64);
                    System.out.println(painter().left()+""+painter().top()+""+painter().width()+""+painter().height());
                    firstTimeBoom=false;
                }
                g.drawImage(imageBombExp, painter().left(), painter().top(),painter().right(),painter().bottom(),0+countForExp*74,0,74+countForExp*74,74,null);
                break;
        }



    }

    @Override
    public void update() {
        switch (state){
            case BOMB:
                if(delayForChangeImgBomb.count()&&countForBomb<=4){
                    this.countForBomb=++this.countForBomb;
                    delayForChangeImgBomb.play();
                }
                if(countForBomb>4){
                    state=State.EXP;
                }
                break;
            case EXP:
                if(delayForChangeImgEXP.isStop()&& firstTime){
                    this.countForExp=++this.countForExp;
                    delayForChangeImgEXP.play();
                    firstTime=false;
                }
                if(delayForChangeImgEXP.count()&&countForExp<=7){
                    this.countForExp=++this.countForExp;
                    delayForChangeImgEXP.play();
                }
                if(countForExp>7){
                    firstTimeBoom=false;
                    state=State.DEAD;
                }
        }
    }
    public void changeState(State state){
        this.state=state;
    }

    public boolean getFirstTimeColiForPic(){
        return firstTimeColiForPic;
    }
    public void setFirstTimeColiForPic(boolean firstTimeColiForPic){
        this.firstTimeColiForPic = firstTimeColiForPic;
    }
    public boolean getFirstTimeColiForColiHero(){
        return firstTimeColiForColiHero;
    }
    public void setFirstTimeColiForColiHero(boolean firstTimeColiForColiHero){
        this.firstTimeColiForColiHero=firstTimeColiForColiHero;
    }

}
