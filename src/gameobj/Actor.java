package gameobj;

import utils.Animator;
import utils.Global;

import java.awt.*;

import static utils.Global.CENTER_HEIGHT;
import static utils.Global.CENTER_WIDTH;

public abstract class Actor extends GameObject {
    private Global.Direction currentDirection;
    private Animator Animator;
    private boolean isMove;//怪物移動後 可再次對主角造成傷害
    private boolean isWeapon;//避免重複吃武器.
    private boolean isCollision;

    public Actor(int x, int y, int width, int height,String path) {
        super(x, y, width, height);
        currentDirection = Global.Direction.DOWN;
        Animator =new Animator(path);
        isMove=true;
        isWeapon=true;
        isCollision=true;

    }
    public Global.Direction getCurrentDirection() {
        return this.currentDirection;
    }

    public void setCurrentDirection(Global.Direction direction) {
        this.currentDirection = direction;
    }
    @Override
    public void paintComponent(Graphics g) {//painter
        Animator.paint(g,currentDirection,painter().left(),painter().top(),
                painter().right(), painter().bottom());
    }

    @Override
    public abstract void update();

    public Animator getAnimator() {
        return Animator;
    }

    protected void setAnimator(String path){
        Animator = new Animator(path);
    }

    public void move() {
        switch (currentDirection) {
            case UP:
                translateY(-Global.CENTER_HEIGHT);
                isWeapon=true;
                setIsCollision(true);
                break;
            case DOWN:
                translateY(Global.CENTER_HEIGHT);
                isWeapon=true;
                setIsCollision(true);
                break;
            case LEFT:
                translateX(-Global.CENTER_WIDTH);
                isWeapon=true;
                setIsCollision(true);
                break;
            case RIGHT:
                translateX(Global.CENTER_WIDTH);
                isWeapon=true;
                setIsCollision(true);
                break;
        }
    }
    public void moveForInternet(){
        isWeapon=true;
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

    public boolean getIsWeapon(){
        return isWeapon;
    }
    public void  setIsWeapon(boolean isWeapon){
        this.isWeapon=isWeapon;
    }
    public void  setIsCollision(boolean isCollision){
        this.isCollision=isCollision;
    }
    public boolean getIsCollision(){
        return  this.isCollision;
    }

}
