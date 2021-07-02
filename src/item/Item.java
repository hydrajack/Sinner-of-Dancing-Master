package item;

import controller.ImageController;
import gameobj.GameObject;
import utils.Global;

import java.awt.*;

public class Item extends GameObject {
    private int hpUp;
    private int moneyUp;
    private int diamondUp;
    private int coinPrice;
    private int diamondPrice;
    private int defenseUp;
    private boolean isInStore;
    private String detail;
    public enum ItemType {
        HP(10, 0, 0, 0,0, 0, false, "/item/HP.png"),
        HP1(20, 0, 0, 0,0, 0, false, "/item/HP1.png"),
        HP2(30, 0, 0, 25,0, 0, true, "/item/HP2.png","+HP"),
        HP22(30, 0, 0, 40,0, 0, true, "/item/HP2.png","+HP"),
        HP2DIAMOND(30, 0, 0, 0,1, 0, true, "/item/HP2.png","+HP"),
        COIN1(0, 10, 0, 0,0, 0, false, "/item/COIN1.png"),
        DIAMOND(0, 0, 1, 0,0, 0, false, "/item/DIAMOND.png"),
        DEFENSE(0, 0, 0, 40,0, 5, true, "/item/DEFENSE.png","+DEF"),
        OLDMAN(0,0,0,10000000,100000000,0,true,"/item/OLDMAN.png"),
        COIN(0,5,0,0,0,0,false,"/item/SMALLCOIN.png"),
        COINONE(0,1,0,0,0,0,false,"/item/COIN$1.png"),
        COINTWO(0,2,0,0,0,0,false,"/item/COIN$2.png"),
        COINTHREE(0,3,0,0,0,0,false,"/item/COIN$3.png"),
        COINFOUR(0,4,0,0,0,0,false,"/item/COIN$4.png"),
        COINFIVE(0,5,0,0,0,0,false,"/item/COIN$5.png"),
        COINSIX(0,6,0,0,0,0,false,"/item/COIN$6.png"),
        COINSEVEN(0,7,0,0,0,0,false,"/item/COIN$7.png"),
        COINEIGHT(0,8,0,0,0,0,false,"/item/COIN$8.png"),
        CHAOS(0,0,0,0,0,0,false,"/item/CHAOS.png"),
        EMPTYHEART(0,0,0,0,3,0,true,"/item/EMPTYHEART.png","+HP LIMIT"),
        CHICKEN(10000,0,0,0,1,0,true,"/item/CHICKEN.png","+FULL HP"),
        POWERUP(0,0,0,0,3,0,true,"/item/POWERUP.png","+ATTACK");


        private int hpUp;
        private int moneyUp;
        private int diamondUp;
        private int coinsPrice;
        private int diamondPrice;
        private int defenseUp;
        private String imgPathNormal;
        private boolean isInStore;
        private String detail;

        ItemType(int hpUp, int moneyUp, int diamondUp, int coinPrice,int diamondPrice, int defenseUp, boolean isInStore, String imgPathNormal) {
            this.hpUp = hpUp;
            this.moneyUp = moneyUp;
            this.diamondUp = diamondUp;
            this.defenseUp = defenseUp;
            this.isInStore = isInStore;
            this.imgPathNormal = imgPathNormal;
            this.coinsPrice =coinPrice;
            this.diamondPrice=diamondPrice;
        }
        ItemType(int hpUp, int moneyUp, int diamondUp, int coinPrice,int diamondPrice, int defenseUp, boolean isInStore, String imgPathNormal,String detail) {
            this.hpUp = hpUp;
            this.moneyUp = moneyUp;
            this.diamondUp = diamondUp;
            this.defenseUp = defenseUp;
            this.isInStore = isInStore;
            this.imgPathNormal = imgPathNormal;
            this.coinsPrice =coinPrice;
            this.diamondPrice=diamondPrice;
            this.detail=detail;

        }
    }

    private ItemType itemType;
    private Image image;


    public Item(ItemType itemType, int x, int y) {
        super(x, y, Global.CENTER_WIDTH, Global.CENTER_HEIGHT);
        this.image = ImageController.getInstance().tryGet(itemType.imgPathNormal);
        this.itemType = itemType;
        this.hpUp=itemType.hpUp;
        this.diamondUp=itemType.diamondUp;
        this.moneyUp=itemType.moneyUp;
        this.coinPrice=itemType.coinsPrice;
        this.diamondPrice=itemType.diamondPrice;
        this.defenseUp=itemType.defenseUp;
        this.isInStore=itemType.isInStore;
        this.detail=itemType.detail;


    }


    public Image getImage(){return image;}

    public ItemType getItemType() {
        return itemType;
    }

    public int getHpUp() {
        return  hpUp;
    }

    public int getDefenseUp() {
        return  defenseUp;
    }

    public int getMoneyUp() {
        return  moneyUp;
    }

    public int getDiamondUp() {
        return  diamondUp;
    }

    public boolean isInStore(){
        return  isInStore;
    }

    public int getCoinPrice(){
        return   coinPrice;
    }
    public int getDiamondPrice(){
        return  diamondPrice;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, painter().left(), painter().top(), painter().width(), painter().height(), null);
    }

    @Override
    public void update() {

    }
}
