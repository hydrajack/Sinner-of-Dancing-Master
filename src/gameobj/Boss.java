package gameobj;

import controller.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

import static utils.Global.CENTER_HEIGHT;
import static utils.Global.CENTER_WIDTH;

public class Boss extends GameObject {
    private BossCharacter bossCharacter;
    private Delay delayForMovePath;
    private Delay delayForChangeImg;
    private int delayCount;
    private BossState bossState;
    private BossState2 bossState2;
    private Global.Direction currentDirection;
    private int hp;
    private boolean isCollision;
    private boolean isMove;//怪物移動後 可再次對主角造成傷害
    private boolean canDamage;
    private int[] moveSizeArr;
    public enum BossCharacter{
        //for SCENELEVEL1
       DRAGON1(60,new int[]{1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2},new int[]{3,1,2,0},30,10,"/Boss/DRAGON1.png","/Boss/DRAGON2.png","/Boss/DRAGON3.png","/Boss/DRAGON4.png"),
        //for SCENELEVEL2
       DRAGON1a(30,new int[]{3,3,3,0,0,0,0,0,0,0,2,2,2,2,2,1,3,3,0,2,1,3,3,0,2,1,3,3,0,0,1,1,3,1,1,1,1,1,1,1,1,2,2},new int[]{3,1,2,0},30,10,"/Boss/DRAGON1.png","/Boss/DRAGON2.png","/Boss/DRAGON3.png","/Boss/DRAGON4.png"),
        DRAGON1bb(30,new int[]{3,3,3,0,0,0,0,0,0,0,0,2,2,2,2,2,1,3,3,0,2,1,3,3,0,2,1,3,3,0,0,1,1,3,1,1,1,1,1,1,1,1,2,2},new int[]{3,1,2,0},30,10,"/Boss/DRAGON1.png","/Boss/DRAGON2.png","/Boss/DRAGON3.png","/Boss/DRAGON4.png"),
       DRAGON1c(30,new int[]{1,0,3,3,0,3,0,2,1,2,2,1,0},new int[]{3,1,2,0},30,10,"/Boss/DRAGON1.png","/Boss/DRAGON2.png","/Boss/DRAGON3.png","/Boss/DRAGON4.png"),
       BOSSDOOR(60,new int[]{0,1},new int[]{3,1,2,0},10,0,"/Boss/BOSSDOOR.png","/Boss/BOSSDOOR2.png","/Boss/BOSSDOOR3.png","/Boss/BOSSDOOR2.png"),
        //for SceneLevel3
        DRAGON1b(60,new int[]{1,1,1,3,1,1,3,1,1,3,2,0,0,2,0,0,2,0,0},new int[]{3,1,2,0},30,10,"/Boss/DRAGON1.png","/Boss/DRAGON2.png","/Boss/DRAGON3.png","/Boss/DRAGON4.png"),
        DRADONFIANL(60,new int[]{1,1,1,1,1,1,1,1,1,1,2,3,2,3,3,3},new int[]{3,3,3,1,1,1,2,2,2,0,0,0},50,10,"/Boss/ORANGUTAN1.png","/Boss/ORANGUTAN2.png","/Boss/ORANGUTAN1_BLOOD.png","/Boss/ORANGUTAN2_BLOOD.png");

        private int countLimit;
        private int []moveSizeArr;
        private int[]moveSizeArr2;
        private int hpLimit;
        private int atk;
        private Image imgPathLeft;
        private Image imgPathRight;
        private Image imgPathLeft2;
        private Image imgPathRight2;
        BossCharacter(int countLimit,int [] moveSizeArr,int[]moveSizeArr2,
                      int hpLimit,int atk,String imgPathLeft,String imgPathRight,String imgPathLeft2,String imgPathRight2){
            this.countLimit =countLimit;
            this.moveSizeArr=moveSizeArr;
            this.moveSizeArr2=moveSizeArr2;
            this.hpLimit=hpLimit;
            this.atk=atk;
            this.imgPathLeft = ImageController.getInstance().tryGet(imgPathLeft);
            this.imgPathRight=ImageController.getInstance().tryGet(imgPathRight);
            this.imgPathLeft2=ImageController.getInstance().tryGet(imgPathLeft2);
            this.imgPathRight2=ImageController.getInstance().tryGet(imgPathRight2);
        }


        }
    public enum BossState {
        NORMAL,
        BLOOD,
    }
    public enum BossState2{
        NORMAL,
        LOCK;
    }
    public Boss(int x, int y, BossCharacter bossCharacter) {
        super(x,y,Global.CENTER_WIDTH*2,Global.CENTER_HEIGHT*2,x,y,Global.CENTER_WIDTH*2,Global.CENTER_HEIGHT*2);
        this.bossCharacter=bossCharacter;
        this.currentDirection= Global.Direction.UP;
        this.delayForMovePath=new Delay(bossCharacter.countLimit);
        this.delayForChangeImg=new Delay(20);
        this.bossState=BossState.NORMAL;
        this.bossState2=BossState2.NORMAL;
        this.hp=bossCharacter.hpLimit;
        this.moveSizeArr =bossCharacter.moveSizeArr;
        isCollision=true;
        isMove=true;
        delayForMovePath.loop();
    }

    @Override
    public void paintComponent(Graphics g) {
        switch (bossState){
            case NORMAL:
            switch (this.currentDirection){
                case UP:
                case LEFT:
                    g.drawImage(this.bossCharacter.imgPathLeft, painter().left(), painter().top(),painter().width(),painter().height(),
                            null);
                    break;
                case DOWN:
                case RIGHT:
                    g.drawImage(this.bossCharacter.imgPathRight, painter().left(), painter().top(),painter().width(),painter().height(),
                            null);
                    break;
            }
            break;
            case BLOOD:
                switch (this.currentDirection){
                    case UP:
                    case LEFT:
                        g.drawImage(this.bossCharacter.imgPathLeft2, painter().left(), painter().top(),painter().width(),painter().height(),
                                null);
                        break;
                    case DOWN:
                    case RIGHT:
                        g.drawImage(this.bossCharacter.imgPathRight2, painter().left(), painter().top(),painter().width(),painter().height(),
                                null);
                        break;
                }
        }
    }
    public void changeState(BossState bossState){
        this.bossState =bossState;
    }


    @Override
    public void update() {
        if(bossState == BossState.BLOOD && delayForChangeImg.isStop()){
            delayForChangeImg.play();
        }
        if(delayForChangeImg.count()){
            changeState(BossState.NORMAL);
        }
        if(delayForMovePath.count()){
            this.delayCount = ++this.delayCount % moveSizeArr.length;
            move();
            canMove();
            canDamage();
        }
    }
    public Global.Direction getCurrentDirection() {
        return this.currentDirection;
    }
    public int getHp(){
        return  hp;
    }
    public void setHp(int point){
        this.hp+=point;
    }
    public void setHpForInterNet(int hp){
        this.hp=hp;
    }
    public void attack(Hero hero){
        hero.setHp(-bossCharacter.atk+hero.getDefense());
    }
    public void setCurrentDirection(Global.Direction direction) {
        this.currentDirection = direction;
    }
    public void move() {
        switch (Global.Direction.getDirection(moveSizeArr[delayCount])) {
            case UP:
                translateY(-Global.CENTER_HEIGHT);
                setCurrentDirection(Global.Direction.UP);
                setIsCollision(true);
                break;
            case DOWN:
                translateY(Global.CENTER_HEIGHT);
                setCurrentDirection(Global.Direction.DOWN);
                setIsCollision(true);
                break;
            case LEFT:
                translateX(-Global.CENTER_WIDTH);
                setCurrentDirection(Global.Direction.LEFT);
                setIsCollision(true);
                break;
            case RIGHT:
                translateX(Global.CENTER_WIDTH);
                setCurrentDirection(Global.Direction.RIGHT);
                setIsCollision(true);
                break;
        }
    }
    public void isCollision(){
        if(getIsCollision()){
            switch (currentDirection){
                case UP:
                    translateY(CENTER_HEIGHT);
                    setIsCollision(false);
                    break;
                case DOWN:
                    translateY(-CENTER_HEIGHT);

                    setIsCollision(false);
                    break;
                case LEFT:
                    translateX(CENTER_WIDTH);

                    setIsCollision(false);
                    break;
                case RIGHT:
                    translateX(-CENTER_WIDTH);
                    setIsCollision(false);
                    break;
            }
        }

    }
    public void  setIsCollision(boolean isCollision){
        this.isCollision=isCollision;
    }
    public boolean getIsCollision(){
        return  this.isCollision;
    }
    public void isMove(){
        this.isMove=false;
    }
    public void canMove(){
        isMove=true;
    }
    public boolean getIsMove(){
        return isMove;
    }
    public void cantDamage(){
        this.canDamage=false;
    }
    public void canDamage(){
        this.canDamage=true;
    }
    public boolean isCanDamage(){
        return canDamage;
    }
    public void changeArr(){
        this.moveSizeArr=bossCharacter.moveSizeArr2;
    }
    public void changeBossState2LOCK(){
            bossState2=BossState2.LOCK;
    }
    public void changeBossState2Normal(){
        bossState2=BossState2.NORMAL;
    }
    public BossState2 getBossState2(){
        return bossState2;
    }
}
