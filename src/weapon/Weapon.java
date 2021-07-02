package weapon;

import client.ClientClass;
import controller.ImageController;
import gameobj.GameObject;
import utils.Global;

import java.awt.*;

public class Weapon extends GameObject {
    private int atkUp;
    private int coinPrice;
    private int diamondPrice;
    private boolean isInStore;
    private WeaponType weaponType;
    private int id;
    private Image attackImg;
    private Image weaponImg;
    private Global.Direction direction;
    private State state;


    public enum WeaponType{
        WEAPON1(0,false,0,0,"/weapon/WEAPON1.png","/weapon/WIND.png"),
        WEAPON2(0,true,0,6,"/weapon/WEAPON2.png","/weapon/WIND.png","5"),
        WEAPON3(10,true,0,5,"/weapon/WEAPON3.png","/weapon/WIND.png","2");


        private int atkUp;
        private boolean isInStore;
        private String imgPathNormal;
        private String imgPathWind;
        private int coinsPrice;
        private int diamondPrice;
        private String detail;
        WeaponType(int atkUp,boolean isInStore,int coinsPrice,int diamondPrice,String imgPathNormal,String imgPathWind){
            this.atkUp=atkUp;
            this.isInStore=isInStore;
            this.imgPathNormal=imgPathNormal;
            this.imgPathWind=imgPathWind;
            this.coinsPrice=coinsPrice;
            this.diamondPrice=diamondPrice;
        }
        WeaponType(int atkUp,boolean isInStore,int coinsPrice,int diamondPrice,String imgPathNormal,String imgPathWind,String detail){
            this.atkUp=atkUp;
            this.isInStore=isInStore;
            this.imgPathNormal=imgPathNormal;
            this.imgPathWind=imgPathWind;
            this.coinsPrice=coinsPrice;
            this.diamondPrice=diamondPrice;
            this.detail=detail;
        }
    }
    public enum State{
        WEAPON,
        ATTACK;
    }



    public Weapon (WeaponType weaponType,State state,int x, int y, Global.Direction direction ) {
        super(x, y, Global.CENTER_WIDTH, Global.CENTER_HEIGHT);
        this.atkUp=weaponType.atkUp;
        this.coinPrice=weaponType.coinsPrice;
        this.diamondPrice= weaponType.diamondPrice;
        this.isInStore=weaponType.isInStore;
        this.attackImg = ImageController.getInstance().tryGet(weaponType.imgPathWind);
        this.weaponImg=ImageController.getInstance().tryGet(weaponType.imgPathNormal);
        this.weaponType=weaponType;
        this.direction=direction;
        this.state=state;
        this.id=ClientClass.getInstance().getID();
    }
    public Weapon (WeaponType weaponType,State state,int x, int y, Global.Direction direction,int id ) {
        super(x, y, Global.CENTER_WIDTH, Global.CENTER_HEIGHT);
        this.atkUp=weaponType.atkUp;
        this.coinPrice=weaponType.coinsPrice;
        this.diamondPrice= weaponType.diamondPrice;
        this.isInStore=weaponType.isInStore;
        this.attackImg = ImageController.getInstance().tryGet(weaponType.imgPathWind);
        this.weaponImg=ImageController.getInstance().tryGet(weaponType.imgPathNormal);
        this.weaponType=weaponType;
        this.direction=direction;
        this.state=state;
        this.id=id;
    }


    public boolean isInStore(){
        return  isInStore;
    }
    public WeaponType getWeaponType(){
        return  weaponType;
    }
    public  boolean isOutOfBound( ){
       return !getCollider().compareToTopAndLeft( );
    }


    @Override
    public void paintComponent(Graphics g) {
        switch (state){
            case ATTACK:
                g.drawImage(this.attackImg,painter().left(), painter().top(),
                        painter().width(),painter().height(),null);
                break;
            case WEAPON:
                g.drawImage(this.weaponImg,painter().left(), painter().top(),
                        painter().width(),painter().height(),null);
                break;
        }

    }


    @Override
    public void update() {
          move();
    }
    public void move(){
        switch (direction){
            case UP:
                translateY(-Global.CENTER_HEIGHT);
                break;
            case DOWN:
                translateY(Global.CENTER_HEIGHT);
                break;
            case LEFT:
                translateX(-Global.CENTER_WIDTH);
                break;
            case RIGHT:
                translateX(Global.CENTER_WIDTH);
                break;
        }
    }
    public int getAtkUp(){
        return  atkUp;
    }
    public Image getWeaponImg(){
        return  weaponImg;
    }
    public void setState(State state){
        this.state=state;
    }
    public int getCoinPrice(){
        return  coinPrice;
    }
    public int getDiamondPrice(){
        return  diamondPrice;
    }
    public void setDiamondPriceToZero(){
        this.isInStore=false;
        this.diamondPrice=0;
    }
    public int getId(){
        return  id;
    }



}
