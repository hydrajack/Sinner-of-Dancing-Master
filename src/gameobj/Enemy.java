package gameobj;

import utils.Delay;
import utils.Global;

public class Enemy extends Actor{
    private EnemyCharacter enemyCharacter;
    private Delay delayForMovePath;
    private Delay delayForChangeImg;
    private Delay delayForChaos;
    private int delayCount;
    private EnemyState enemyState;
    private MoveState moveState;
    private int hp;
    private int []moveSizeArr;
    private int []chaoArr;
    private boolean canDamage;

    private boolean firstTimeColiForColiEnemy;
    public enum EnemyCharacter{
        GREEN2(30,new int[]{1,1,0,0},new int[0],10,10,"/Enemy/GREEN2.png","/Enemy/GREEN2BLOOD.png"),//X
        GREEN2a(30,new int[]{0,0,1,1},new int[0],10,10,"/Enemy/GREEN2.png","/Enemy/GREEN2BLOOD.png"),//X
        ENEMYQUEEN(30,new int[]{0,1},new int[0],10,10,"/Enemy/ENEMYQUEEN.png","/Enemy/ENEMYQUEENBLOOD.png"),
        ENEMYQUEENa(30,new int[]{1,0},new int[0],10,10,"/Enemy/ENEMYQUEEN.png","/Enemy/ENEMYQUEENBLOOD.png"),
        SOLDIER2(30,new int[]{3,2},new int[0],20,10,"/Enemy/SOLDIER2.png","/Enemy/SOLDIER2BLOOD.png"),//X
        SOLDIER(30,new int[]{2,3},new int[0],20,10,"/Enemy/SOLDIER.png","/Enemy/SOLDIERBLOOD.png"),//X
        SOLDIERa(30,new int[]{2,2,2,3,3,3},new int[0],20,10,"/Enemy/SOLDIER.png","/Enemy/SOLDIERBLOOD.png"),
        PUMPKIN(30, new int[]{1,2,0,3},new int[0],20,10,"/Enemy/PUMPKIN.png","/Enemy/PUMPKINBLOOD.png"),
        PUMPKINa(30, new int[]{2,2,0,3,3,1},new int[0],20,10,"/Enemy/PUMPKIN.png","/Enemy/PUMPKINBLOOD.png"),
        PURPLE(30,new int[]{1,3,0,2},new int[0],10,10,"/Enemy/PURPLE.png","/Enemy/PURPLEBLOOD.png"),
        BONE(30,new int[]{1,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0},new int[0],10,10,"/Enemy/BONE.png","/Enemy/BONEBLOOD.png"),//X
        BONEb(30,new int[]{1,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0},new int[0],10,10,"/Enemy/BONE.png","/Enemy/BONEBLOOD.png"),//X
        BONEa(30,new int[]{1,0,1,0},new int[0],20,10,"/Enemy/BONE.png","/Enemy/BONEBLOOD.png"),//X
        GREEN(30,new int[]{0,3,1,2},new int[0],20,10,"/Enemy/GREEN.png","/Enemy/GREENBLOOD.png"),
        GREENa(30,new int[]{0,2,1,3},new int[0],20,10,"/Enemy/GREEN.png","/Enemy/GREENBLOOD.png"),
        BLACK(30,new int[]{2,0,1,3},new int[0],20,10,"/Enemy/BLACK.png","/Enemy/BLACKBLOOD.png"),
        BLACKa(30,new int[]{1,0},new int[0],20,10,"/Enemy/BLACK.png","/Enemy/BLACKBLOOD.png"),
        BLACKb(30,new int[]{0,1},new int[0],20,10,"/Enemy/BLACK.png","/Enemy/BLACKBLOOD.png"),

        //for Scene 2 13,14,23,24
        LITTLEGIRL(30,new int[]{2,2,2,2,2,0,0,0,0,0,1,1,1,1,1,3,3,3,3,3},new int[0],20,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL1(30,new int[]{2,2,2,2,2,2,0,0,0,0,0,0,1,1,1,1,1,1,3,3,3,3,3,3},new int[0],20,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL2(30,new int[]{1,1,1,1,1,1,3,3,3,3,3,3,2,2,2,2,2,2,0,0,0,0,0,0},new int[0],20,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL3(30,new int[]{1,1,1,1,1,3,3,3,3,3,2,2,2,2,2,0,0,0,0,0},new int[0],20,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        //FOR SCENE 3
        WHITEGHOST(30,new int[]{1,1,1,1,1,1,1,1,1,2,2,2,2,1,1,1,1,3,3,3,3,3,3,3,3,3,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,2},new int[0],10,10,"/Enemy/WHITEGHOST.png","/Enemy/WHITEGHOSTBLOOD.png"),
        WHITEGHOSTa(30,new int[]{1,1,1,1,1,1,1,1,1,3,3,3,3,1,1,1,1,2,2,2,2,2,2,2,2,2,0,0,0,0,3,3,3,3,0,0,0,0,0,0,0,0,3},new int[0],10,10,"/Enemy/WHITEGHOST.png","/Enemy/WHITEGHOSTBLOOD.png"),
        WHITEGHOSTb(30,new int[]{3,3,3,1,1,1,1,1,1,1,1,1,1,2,2,2,2,1,1,1,1,3,3,3,3,3,3,3,3,3,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,2,2,0,2},new int[0],10,10,"/Enemy/WHITEGHOST.png","/Enemy/WHITEGHOSTBLOOD.png"),
        WHITEGHOSTc(30,new int[]{3,0,3,3,3,1,1,1,1,1,1,1,1,1,1,2,2,2,2,1,1,1,1,3,3,3,3,3,3,3,3,3,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,2,2,0,2,1,2},new int[0],10,10,"/Enemy/WHITEGHOST.png","/Enemy/WHITEGHOSTBLOOD.png"),
        WHITEGHOSTd(30,new int[]{3,1,3,0,3,3,3,1,1,1,1,1,1,1,1,1,1,2,2,2,2,1,1,1,1,3,3,3,3,3,3,3,3,3,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,2,2,0,2,1,2,0,2},new int[0],10,10,"/Enemy/WHITEGHOST.png","/Enemy/WHITEGHOSTBLOOD.png"),
        WHITEGHOSTe(30,new int[]{0,3,3,1,3,0,3,3,3,1,1,1,1,1,1,1,1,1,1,2,2,2,2,1,1,1,1,3,3,3,3,3,3,3,3,3,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,2,2,0,2,1,2,0,2,2,1},new int[0],10,10,"/Enemy/WHITEGHOST.png","/Enemy/WHITEGHOSTBLOOD.png"),
        WHITEGHOSTf(30,new int[]{2,2,2,1,1,1,1,1,1,1,1,1,1,3,3,3,3,1,1,1,1,2,2,2,2,2,2,2,2,2,0,0,0,0,3,3,3,3,0,0,0,0,0,0,0,0,3,0,3,3,3},new int[0],10,10,"/Enemy/WHITEGHOST.png","/Enemy/WHITEGHOSTBLOOD.png"),
        WHITEGHOSTg(30,new int[]{2,0,2,2,2,1,1,1,1,1,1,1,1,1,1,3,3,3,3,1,1,1,1,2,2,2,2,2,2,2,2,2,0,0,0,0,3,3,3,3,0,0,0,0,0,0,0,0,3,0,3,3,3,1,3},new int[0],10,10,"/Enemy/WHITEGHOST.png","/Enemy/WHITEGHOSTBLOOD.png"),
        WHITEGHOSTh(30,new int[]{2,1,2,0,2,2,2,1,1,1,1,1,1,1,1,1,1,3,3,3,3,1,1,1,1,2,2,2,2,2,2,2,2,2,0,0,0,0,3,3,3,3,0,0,0,0,0,0,0,0,3,0,3,3,3,1,3,0,3},new int[0],10,10,"/Enemy/WHITEGHOST.png","/Enemy/WHITEGHOSTBLOOD.png"),
        WHITEGHOSTi(30,new int[]{2,0,2,1,2,0,2,2,2,1,1,1,1,1,1,1,1,1,1,3,3,3,3,1,1,1,1,2,2,2,2,2,2,2,2,2,0,0,0,0,3,3,3,3,0,0,0,0,0,0,0,0,3,0,3,3,3,1,3,0,3,1,3},new int[0],10,10,"/Enemy/WHITEGHOST.png","/Enemy/WHITEGHOSTBLOOD.png"),
        LITTLEGIRL4(30,new int[]{3,3,3,3,1,1,2,2,2,0,0},new int[0],10,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL5(30,new int[]{3,3,3,1,1,2,2,2,0,0,2},new int[0],10,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL6(30,new int[]{3,3,1,1,2,2,2,0,0,2,2},new int[0],10,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL7(30,new int[]{1,1,1,2,2,2,0,0,2,2,2},new int[0],10,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL8(30,new int[]{0,0,3,3,3,1,1,2,2,2,0},new int[0],10,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL9(30,new int[]{1,1,2,2,2,0,0,3,3,3,1},new int[0],10,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL10(30,new int[]{0,0,0,3,3,3,1,1,2,2,2},new int[0],10,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL11(30,new int[]{2,2,2,0,0,3,3,3,1,1,2},new int[0],10,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL12(30,new int[]{2,2,2,0,0,3,3,3,1,1,2},new int[0],10,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png"),
        LITTLEGIRL13(30,new int[]{2,2,2,2,0,0,3,3,3,1,1},new int[0],10,10,"/Enemy/LITTLEGIRL.png","/Enemy/LITTLEGIRLBLOOD.png");






        private int countLimit;
        private int []moveSizeArr;
        private int []chaosArr;
        private int hpLimit;
        private int atk;
        private String imgPathNormal;
        private String imgPathBlood;
        EnemyCharacter(int countLimit,int [] moveSizeArr,int[]chaosArr,
                       int hpLimit,int atk,String imgPathNormal,String imgPathBlood){
            this.countLimit =countLimit;
            this.moveSizeArr=moveSizeArr;
            this.chaosArr=chaosArr;
            this.hpLimit=hpLimit;
            this.atk=atk;
            this.imgPathBlood=imgPathBlood;
            this.imgPathNormal=imgPathNormal;
        }
    }
    public enum EnemyState {
        NORMAL,
        BLOOD;
    }
    public enum MoveState{
        NORMAL,
        CHAOS;
    }


    public Enemy(int x, int y, EnemyCharacter enemyCharacter) {
        super(x, y, Global.CENTER_WIDTH, Global.CENTER_HEIGHT,  enemyCharacter.imgPathNormal);
        this.moveSizeArr=enemyCharacter.moveSizeArr;
        this.chaoArr=enemyCharacter.chaosArr;
        this.enemyCharacter=enemyCharacter;
        this.hp=enemyCharacter.hpLimit;
        this.delayForMovePath =new Delay(enemyCharacter.countLimit);
        this.delayForChangeImg=new Delay(15);
        this.delayForChaos=new Delay(120);
        this.enemyState = EnemyState.NORMAL;
        this.moveState = MoveState.NORMAL;
        delayForMovePath.loop();
        this.firstTimeColiForColiEnemy=true;
    }
    public void move() {
        switch (moveState){
            case NORMAL:
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
                break;
            case CHAOS:
                switch (Global.Direction.getDirection(chaoArr[delayCount])) {
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

    }
    @Override
    public void update() {
        if(enemyState == EnemyState.BLOOD && delayForChangeImg.isStop()){
            delayForChangeImg.play();
        }
        if(delayForChangeImg.count()){
            changeEnemyState(EnemyState.NORMAL);
        }
        if(delayForMovePath.count()){
            this.delayCount = ++this.delayCount % enemyCharacter.moveSizeArr.length;
            move();
            canMove();
            canDamage();
        }
        if(moveState==MoveState.CHAOS && delayForChaos.isStop()){
            delayForChaos.play();
        }
        if(delayForChaos.count()){
            changeMoveState(MoveState.NORMAL);
        }

    }
    public int getHp(){
        return this.hp;
    }
    public void setHp(int point){
        this.hp+=point;
    }
    public void changeEnemyState(EnemyState state){
        this.enemyState =state;
        changeImg();
    }
    public void changeMoveState(MoveState moveState){
        this.moveState=moveState;
    }
    public void attack(Hero hero){
        hero.setHp(-enemyCharacter.atk+hero.getDefense());
    }
    private void changeImg(){
        switch (enemyState){
            case NORMAL:
                getAnimator().changeImage(enemyCharacter.imgPathNormal);
                break;
            case BLOOD:
                getAnimator().changeImage(enemyCharacter.imgPathBlood);
                break;
        }
    }
    public boolean getFirstTimeColiForColiEnemy(){
        return  firstTimeColiForColiEnemy;
    }
    public void setFirstTimeColiForColiEnemy(boolean firstTimeColiForColiEnemy){
        this.firstTimeColiForColiEnemy=firstTimeColiForColiEnemy;
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


}
