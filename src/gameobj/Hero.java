package gameobj;

import item.Item;
import utils.Delay;
import utils.Global;
import weapon.Weapon;

import java.util.ArrayList;

public class Hero extends Actor {
    private int internetID;
    private int hp;
    private int atk;

    private int defense;
    private int hpLimit;
    private HeroType heroType;
    private Weapon weapon;
    private ArrayList<Item> items;//道具欄 QWER(lu:更新此欄位+QWER)
    private int diamonds;
    private int coins;
    private int comboCount;
    private HeroState heroState;
    private ItemState itemState;
    private Delay delayForChangeImg;


    public enum HeroType {
        WARRIOR(10, 0, 50, "/Hero/WARRIOR.png", "/Hero/WARRIORBLOOD.png","/Hero/RIP.png"),
        WIZARD(10,0,50,"/Hero/WIZARD.png","/Hero/WIZARDBLOOD.png","/Hero/RIP.png"),
        SCIENCE(10,0,50,"/Hero/SCIENCE.png","/Hero/SCIENCEBLOOD.png","/Hero/RIP.png"),
        CAT(10,0,50,"/Hero/CAT.png","/Hero/CATBLOOD.png","/Hero/RIP.png");
        private int hpLimit;
        private int atk;
        private int defense;
        private String imgPathNormal;
        private String imgPathBlood;
        private String imagePathRip;
        HeroType(int atk, int defense, int hpLimit, String imgPathNormal, String imgPathBlood,String imagePathRip) {
            this.atk = atk;
            this.defense = defense;
            this.hpLimit = hpLimit;
            this.imgPathNormal = imgPathNormal;
            this.imgPathBlood = imgPathBlood;
            this.imagePathRip=imagePathRip;
        }
    }

    public enum HeroState {
        NORMAL,
        BLOOD,
        RIP;
    }
    public enum ItemState{
        NORMAL,
        CHAOS;
    }


    public Hero(int x, int y, HeroType heroType) {
        super(x, y, Global.CENTER_WIDTH, Global.CENTER_HEIGHT, heroType.imgPathNormal);
        this.hp=heroType.hpLimit;
        this.hpLimit=heroType.hpLimit;
        this.heroType = heroType;
        setWeapon(new Weapon(Weapon.WeaponType.WEAPON1

                , Weapon.State.WEAPON, x, y, Global.Direction.UP));
        this.delayForChangeImg = new Delay(65);
        this.heroState = HeroState.NORMAL;
        this.itemState=ItemState.NORMAL;
        this.items = new ArrayList<>();
        this.atk=heroType.atk;
        this.defense=heroType.defense;

    }

    public void update() {
        if (heroState == HeroState.BLOOD && delayForChangeImg.isStop()) {
            delayForChangeImg.play();
        }
        if (delayForChangeImg.count()) {
            changeState(HeroState.NORMAL);
        }
        if(hp<=0){
            changeState(HeroState.RIP);
        }
    }


    public void setHp(int point) {
        this.hp += point;
        if (this.hp >= hpLimit) {
            this.hp =  hpLimit;
        }
    }
    public void setHpForInterNet(int point){
        this.hp=point;
    }

    public void setDefense(int point) {
        this.defense += point;
    }

    public int getDefense() {
        return defense;
    }

    public int getHp() {
        return this.hp;
    }

    public void buyItemByCoins(int coins){
        this.coins-=coins;
    }
    public void buyItemByDiamond(int diamonds){
        this.diamonds-=diamonds;
    }


    public int getCoins() {
        return coins;
    }

    public int getDiamond(){
        return  diamonds;
    }



    public int getHpLimit() {
        return  hpLimit;
    }

    public void setWeapon(Weapon weapon) {
        if (this.weapon != null) {
            setAtk(-this.weapon.getAtkUp());
        }
        this.weapon = weapon;
        setAtk(this.weapon.getAtkUp());

    }


    public Weapon getWeapon() {
        return this.weapon;
    }

    public int getAtk(){
        return atk;
    }

    private void setAtk(int point) {
        this.atk += point;
    }

    public void setAtkForInternet(int point){
        this.atk=point;
    }

    public void attack(Enemy enemy) {
        enemy.setHp(-atk);
    }

    public void attack(Boss boss) {
        boss.setHp(-atk);
    }

    public void pickUpItem(Item item) {//判斷吃到啥
        switch (item.getItemType()) {
            case DIAMOND:
                diamonds += item.getDiamondUp();
                return;
            case COIN1:
            case COINONE:
            case COINTWO:
            case COINTHREE:
            case COINFOUR:
            case COINFIVE:
            case COINSIX:
            case COINSEVEN:
            case COINEIGHT:
            case COIN:
                coins += item.getMoneyUp();
                return;
            case EMPTYHEART:
                hpLimit+=10;
                return;
            case POWERUP:
                atk+=10;
                break;
            default:
                if (items.size() < 4) {
                    items.add(item);
                    return;
                }
                if (items.size() > 4) {
                    itemIsFull();
                }
                break;
        }
    }


    private void itemIsFull() {
        isCollision();
    }

    public void useItem(int commandCode) {
        switch (commandCode) {
            case 5:
                if (items.size() == 0) {
                    return;
                }
                if (items.get(0).getHpUp() > 0 && getHp() >= getHpLimit()) {
                    return;
                }
                if(items.get(0).getItemType()== Item.ItemType.CHAOS){
                    itemState=ItemState.CHAOS;
                }

                setHp(items.get(0).getHpUp());
                setDefense(items.get(0).getDefenseUp());
                items.remove(0);
                break;
            case 6:
                if (items.size() <= 1) {
                    return;
                }
                if (items.get(1).getHpUp() > 0 && getHp() >= getHpLimit()) {
                    return;
                }
                if(items.get(1).getItemType()== Item.ItemType.CHAOS){
                    itemState=ItemState.CHAOS;
                }
                setHp(items.get(1).getHpUp());
                setDefense(items.get(1).getDefenseUp());
                items.remove(1);
                break;
            case 7:
                if (items.size() <= 2) {
                    return;
                }
                if (items.get(2).getHpUp() > 0 && getHp() >= getHpLimit()) {
                    return;
                }
                if(items.get(2).getItemType()== Item.ItemType.CHAOS){
                    itemState=ItemState.CHAOS;
                }
                setHp(items.get(2).getHpUp());
                setDefense(items.get(2).getDefenseUp());
                items.remove(2);
                break;
            case 8:
                if (items.size() <= 3) {
                    return;
                }
                if (items.get(3).getHpUp() > 0 && getHp() >= getHpLimit()) {
                    return;
                }
                if(items.get(3).getItemType()== Item.ItemType.CHAOS){
                    itemState=ItemState.CHAOS;
                }
                setHp(items.get(3).getHpUp());
                setDefense(items.get(3).getDefenseUp());
                items.remove(3);
                break;
        }
    }


    public ArrayList<Item> getItems() {
        return items;
    }

    //換照片
    public void changeState(HeroState heroState) {
        this.heroState = heroState;
        changeImg();
    }

    private void changeImg() {
        switch (heroState) {
            case NORMAL:
                getAnimator().changeImage(heroType.imgPathNormal);
                break;
            case BLOOD:
                getAnimator().changeImage(heroType.imgPathBlood);
                break;
            case RIP:
                getAnimator().changeImage(heroType.imagePathRip);
                break;
        }
    }
    public void setInternetID(int internetID){
        this.internetID=internetID;
    }
    public int getInternetID(){
        return internetID;
    }
    public void setDefenseForInternet(int point){
        this.defense=point;
    }
    public HeroType getHeroType(){
        return heroType;
    }
    public void changeHero(HeroType heroType){//換角色方法
        setAnimator(heroType.imgPathNormal);
        this.heroType = heroType;
    }
    public void addCombo(){
        this.comboCount++;
    }
    public void resetCombo(){
        comboCount=0;
    }
    public int getCombo(){
        return comboCount;
    }
    public void setCombo(int combo){
        this.comboCount=combo;
    }
    public void resetCoins(){
        this.coins=0;
    }
    public void resetHp(){
        this.hp=hpLimit;
    }
    public void resetItems(){
        items=new ArrayList<>();
    }
    public void setHpLimitForInternet(int point){
        this.hpLimit=point;
    }
    public int getHpLimitForInternet(){
        return hpLimit;
    }


}
