package scene;

import camera.Camera;
import camera.MapInformation;
import client.ClientClass;
import controller.AudioResourceController;
import controller.ImageController;
import controller.MapController;
import controller.SceneController;
import displayButton.Buttons;
import gameobj.*;
import item.Item;
import utils.CommandSolver;
import utils.Delay;
import utils.GameWindow;
import utils.Global;
import weapon.Weapon;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static utils.Global.*;
import static utils.Global.CENTER_WIDTH;

public class SceneLevel3 extends Scene {
    private ArrayList<Hero> heroes;
    private int heroSize;//for internet
    private Hero heroForCoin;
    private ArrayList<Weapon> weapons;
    private ArrayList<Weapon> weaponsForMap;
    private ArrayList<Enemy>enemiesForWhiteGhost;//part final;
    private ArrayList<Enemy>enemiesForPartOnePointOne;//part1-1;
    private ArrayList<Boss>enemiesForPartOnePointTwo;//part 1-2;
    private ArrayList<Enemy>enemiesForPartTwoPointOne;//part 2-1;
    private ArrayList<Enemy>enemiesForPartTwoPointTwo;//part 2-2;
    private boolean isForWinButtom;
    private boolean isForWhiteGhost;
    private boolean isForPartOnePointOne;//part1-1
    private boolean isForPartOnePointTwo;//part1-2
    private boolean isForPartTwoPointOne;//part2-1;
    private boolean isForPartTwoPointTwo;//part2-2;
    private boolean isForChangeHeroCenterForPartOnePointOne;//part1-1;
    private boolean isForChangeHeroCenterForPartOnePointTwo;//part1-2
    private boolean isForChangeHeroCenterForPartTwoPointOne;//part2-1
    private boolean isForChangeHeroCenterForPartTwoPointTwo;//part 2-2
    private ArrayList<Boss>bossDoors;
    private ArrayList<Boss> bosses;
    private ArrayList<Item> items;
    private ArrayList<Bomb>bombs;
    private ArrayList<Marker> markers;
    private ArrayList<Marker> markers2;
    private GUI gui;
    private Heart heart;
    private Combo combo;
    private int comboCount;
    private Camera camera;
    //    private SmallMap smallMap;
    private List<Floor> floors;
    private List<Wall> walls;
    private boolean isKeyPress;
    private Delay delayForMarkerMove;
    private Delay missCount;
    private Delay delay;//for兩根棒子碰到後
    private boolean isForMarker;
    //shakeScreen
    private Delay shakeDelay;
    //WIN
    private ArrayList<Buttons> winButtons;
    private Image award;
    private Delay victoryDelay;
    private BloodBar bloodBar;
    private Delay delayForDeath;
    private Delay delayForFinalBoss;
    private boolean isContinue;

    public SceneLevel3(ArrayList heroes) {
        this.heroes = heroes;
        if(heroes.size()>1){
            otherStillAtGame=true;
            isContinue=false;
        }
        if(heroes.size()==1){
            isContinue=true;
        }

    }

    @Override
    public void sceneBegin() {
        GameWindow.getInstance().setWindowSize(576,576);
        heroSize = 0;
        enemiesForWhiteGhost = new ArrayList<>();
        enemiesForPartOnePointOne=new ArrayList<>();
        enemiesForPartOnePointTwo=new ArrayList<>();
        enemiesForPartTwoPointOne=new ArrayList<>();
        enemiesForPartTwoPointTwo=new ArrayList<>();
        isForWhiteGhost=true;
        isForPartOnePointOne=true;
        isForPartOnePointTwo=true;
        isForPartTwoPointOne=true;
        isForPartTwoPointTwo=true;
        isForPartTwoPointTwo=true;
        isForChangeHeroCenterForPartOnePointOne =true;
        isForChangeHeroCenterForPartOnePointTwo=true;
        isForChangeHeroCenterForPartTwoPointOne=true;
        isForChangeHeroCenterForPartTwoPointTwo=true;
        bossDoors=new ArrayList<>();
        bossDoors.add(new Boss(704,1024, Boss.BossCharacter.BOSSDOOR));//24
        bossDoors.add(new Boss(64,640, Boss.BossCharacter.BOSSDOOR));//25
        bossDoors.add(new Boss(1024,640, Boss.BossCharacter.BOSSDOOR));//26

        bombs=new ArrayList<>();
        bosses = new ArrayList<>();
        markers = new ArrayList<>();
        markers2 = new ArrayList<>();



        weapons = new ArrayList<>();
        items = new ArrayList<>();
        items.add(new Item(Item.ItemType.HP,192,640));
        items.add(new Item(Item.ItemType.HP22,192,704));
        items.add(new Item(Item.ItemType.HP22,1152,704));
        items.add(new Item(Item.ItemType.HP,1152,640));
        weaponsForMap = new ArrayList<>();
        heart = new Heart(576 / 2, 512);
        combo = new Combo(576 / 2 - 32, 460);
        start();
        heroes.get(heroSize++).setInternetID(ClientClass.getInstance().getID());
        isKeyPress = true;//按鍵一次只能按一次 不能壓下去連按
        shakeDelay = new Delay(15);
        delayForMarkerMove = new Delay(30);
        missCount = new Delay(30);
        delay = new Delay(4);
        delayForMarkerMove.loop();
        isForMarker = false;
        AudioResourceController.getInstance().loop("/zone3.wav",5);
        MapController mapController = new MapController(MapController.Key.LEVEL4);
        floors = mapController.getFloors();
        walls = mapController.getWalls();
        //MapInformation.setMapInfo必須先設定
        MapInformation.setMapInfo(0, 0, 1280,1280);
        this.camera = new Camera.Builder(GameWindow.getInstance().getWindowWidth(), GameWindow.getInstance().getWindowHeight()).setChaseObj(heroes.get(0))
                .setCameraStartLocation(640, 0)
                .gen();
        this.gui = new GUI(0, 0, 576, 576);
        //win
        winButtons = new ArrayList<>();
        isForWinButtom=true;
        award = ImageController.getInstance().tryGet("/WIN11.png");
        victoryDelay = new Delay(60);
        delayForDeath=new Delay(60);
        delayForFinalBoss=new Delay(1000);
        bloodBar=new BloodBar();

    }

    @Override
    public void sceneEnd() {
        AudioResourceController.getInstance().stop("/zone3.wav");
    }
    public void start(){
        if(isServer){
            heroes.get(0).painter().setCenter(672, 1184);
            heroes.get(0).collider().setCenter(672, 1184);
            return;
        }
        if(isClient){
            heroes.get(0).painter().setCenter(608, 1184);
            heroes.get(0).collider().setCenter(608, 1184);
            return;
        }
        heroes.get(0).painter().setCenter(672, 1184);
        heroes.get(0).collider().setCenter(672, 1184);
    }

    @Override
    public void paint(Graphics g) {
        if (shakeDelay.isPlaying()) {
            int x = Global.random(-9, 9);
            int y = Global.random(-9, 9);
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(x, y);
        }
        //開鏡頭
        camera.start(g);
        // 繪製地圖
        this.floors.forEach(a -> a.paint(g));
        this.walls.forEach(a -> a.paint(g));
        //win

        bombs.forEach(bomb -> bomb.paint(g));
        winButtons.forEach(a -> a.paint(g));
        heroCheckWinButtoms(g);
        items.forEach(a -> a.paint(g));
        gui.paintStoreItemForCoin(g,items);
        weaponsForMap.forEach(weapon -> weapon.paint(g));
        heroes.forEach(hero -> hero.paint(g));
        enemiesForPartOnePointOne.forEach(a->a.paint(g));
        enemiesForPartOnePointOne.forEach(enemiesForPartOnePointOne->bloodBar.paint(g,enemiesForPartOnePointOne));
        enemiesForPartOnePointTwo.forEach(a->a.paint(g));
        enemiesForPartOnePointTwo.forEach(enemiesForPartOnePointTwo->bloodBar.paint(g,enemiesForPartOnePointTwo));
        enemiesForPartTwoPointOne.forEach(a->a.paint(g));
        enemiesForPartTwoPointOne.forEach(enemiesForPartTwoPointOne->bloodBar.paint(g,enemiesForPartTwoPointOne));
        enemiesForPartTwoPointTwo.forEach(a->a.paint(g));
        enemiesForPartTwoPointTwo.forEach(enemiesForPartTwoPointTwo->bloodBar.paint(g, enemiesForPartTwoPointTwo));
        enemiesForWhiteGhost.forEach(a -> a.paint(g));
        enemiesForWhiteGhost.forEach( enemiesForWhiteGhost->bloodBar.paint(g,enemiesForWhiteGhost));
        bosses.forEach(a -> a.paint(g));
        bosses.forEach(boss -> bloodBar.paint(g,boss));
        bossDoors.forEach(a->a.paint(g));
        weapons.forEach(weapon -> weapon.paint(g));
        camera.paint(g);
        camera.end(g);
        combo.paint(g);
        gui.paintHeroInfo(g, heroes.get(0));
        markers.forEach(marker -> marker.paint(g));
        markers2.forEach(marker -> marker.paint(g));
        heart.paint(g);




    }

    @Override
    public void update() {
        connect();

        if(delayForFinalBoss.count()){
            Boss boss=new Boss(576,64, Boss.BossCharacter.DRADONFIANL);
            boss.changeBossState2LOCK();
            bosses.add(boss);
        }
        if(bosses.size()>0){
            if(bosses.get(0).collider().centerX()==640 && bosses.get(0).collider().centerY()==704){
                bosses.get(0).changeArr();
            }
        }
        if (shakeDelay.count()) {
            shakeDelay.stop();
        }
        screenShake();
        if (delay.count()) {
            isForMarker = false;
        }

        if (missCount.count()) {
            missCount.stop();
        }
        if (victoryDelay.count()) {
            if(heroes.size()>1){
                heroes.remove(1);
            }
            ArrayList<String>strings=new ArrayList<>();
            ClientClass.getInstance().sent(NetCommandCode.HERO_DISCONNECT,strings);
            heroes.get(0).resetCoins();
            heroes.get(0).resetItems();
            heroes.get(0).resetHp();
            SceneController.getInstance().change(new Victory(heroes));
        }
        enemyCheckDied();
        bossCheckDied();
        wallCheckRemove();
        heroes.forEach(hero -> hero.update());
        heroCheckBossDoor();
        bossDoors.forEach(bossDoors->bossDoors.update());
        bombs.forEach(bomb -> bomb.update());
        heroCheckBoom();
        boomAudio();
        boomCheckHero();
//        boomCheckEnemy();
        removeBoom();
        camera.update();

        wallCheckEnemyRemove();
        wallCheckBossRemove();
        wallCheckActorReturn();
        wallCheckEnemiesReturn();
        wallCheckBossReturn();
        enemiesForPartOnePointOne.forEach(enemy -> enemy.update());
        enemiesForPartOnePointTwo.forEach(boss -> boss.update());
        enemiesForPartTwoPointOne.forEach(enemy -> enemy.update());
        enemiesForPartTwoPointTwo.forEach(enemy -> enemy.update());
        enemiesForWhiteGhost.forEach(enemy -> enemy.update());
        bosses.forEach(boss -> boss.update());

        addEnemyForLevel3OnePointOne();
        addEnemyForLevel3OnePointTwo();
        addEnemiesForPartTwoPointOne();
        addEnemiesForPartTwoPointTwo();
        addEnemiesForWhiteGhost();
        changeHeroCenterForPart();
        weapons.forEach(weapon -> weapon.update());
        heart.update();
        combo.update();
        markers.forEach(marker -> marker.update());
        markers2.forEach(marker -> marker.update());
        addMarker();
        markCheckMark2();
        getWeapon();
        weaponIsCollision();
        enemyCheckHero();
        bossCheckHero();
        heroCheckItems();
        heroDie();
        checkWinButtons();
    }



    public void heroDie(){//lu
        if(heroes.get(0).getHp()<=0){
            if(heroes.size()>1){
                heroes.remove(1);
            }
            ArrayList<String>strings=new ArrayList<>();
            ClientClass.getInstance().sent(NetCommandCode.HERO_DISCONNECT,strings);
            heroes.get(0).resetCoins();
            heroes.get(0).resetItems();
            heroes.get(0).resetHp();
            heroes.get(0).changeState(Hero.HeroState.NORMAL);
            SceneController.getInstance().change(new GameOver(heroes));

        }

    }



    public void checkWinButtons() {//lu
        for(int k=0;k<heroes.size();k++){
            for (int i = 0; i < winButtons.size(); i++) {
                if (heroes.get(k).isCenterCollision(winButtons.get(i))) {
                    winButtons.get(i).getNameButton().changeIsTouch(true);
                    if(victoryDelay.isStop()){
                        victoryDelay.play();
                    }
                }
            }
        }

    }

    private void wallCheckRemove() {
        for (int k = 0; k < heroes.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(heroes.get(k)) && walls.get(i).isDestructible()) {
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(i + "");
                    ClientClass.getInstance().sent(NetCommandCode.HERO_WALLREMOVE, strings);
                    walls.remove(i);
                    i--;
                }
            }
        }
    }

    private void wallCheckEnemyRemove() {
        for (int k = 0; k < enemiesForWhiteGhost.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(enemiesForWhiteGhost.get(k)) && walls.get(i).isDestructible()) {
                    walls.remove(i);
                    i--;
                }
            }
        }
        for (int k = 0; k < enemiesForPartOnePointOne.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(enemiesForPartOnePointOne.get(k),4) && walls.get(i).isDestructible()) {
                    walls.remove(i);
                    i--;
                }
            }
        }

    }

    private void wallCheckBossRemove() {
        for (int k = 0; k < bosses.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(bosses.get(k), 4) && walls.get(i).isDestructible()) {
                    walls.remove(i);
                    i--;
                }
            }
        }

    }

    private void wallCheckActorReturn() {
        for (int k = 0; k < heroes.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(heroes.get(k)) && !walls.get(i).isDestructible()) {
                    heroes.get(k).isCollision();
                }
            }
        }

    }

    private void wallCheckEnemiesReturn() {
        for (int k = 0; k < enemiesForPartOnePointOne.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(enemiesForPartOnePointOne.get(k)) && !walls.get(i).isDestructible()) {
                    enemiesForPartOnePointOne.get(k).isCollision();
                }
            }
        }
        for (int k = 0; k < enemiesForPartOnePointTwo.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(enemiesForPartOnePointTwo.get(k),4) && !walls.get(i).isDestructible()) {
                    enemiesForPartOnePointTwo.get(k).isCollision();
                }
            }
        }
        for (int k = 0; k < enemiesForPartTwoPointOne.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(enemiesForPartTwoPointOne.get(k)) && !walls.get(i).isDestructible()) {
                    enemiesForPartTwoPointOne.get(k).isCollision();
                }
            }
        }
        for (int k = 0; k < enemiesForPartTwoPointTwo.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(enemiesForPartTwoPointTwo.get(k)) && !walls.get(i).isDestructible()) {
                    enemiesForPartTwoPointTwo.get(k).isCollision();
                }
            }
        }
        for (int k = 0; k < enemiesForWhiteGhost.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(enemiesForWhiteGhost.get(k)) && !walls.get(i).isDestructible()) {
                    enemiesForWhiteGhost.get(k).isCollision();
                }
            }
        }


    }

    private void wallCheckBossReturn() {
        for (int k = 0; k < bosses.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(bosses.get(k), 4) && !walls.get(i).isDestructible()) {
                    bosses.get(k).isCollision();
                }
            }
        }

    }

    public void screenShake() {//lu
        for (int i = 0; i < walls.size(); i++) {
            if (walls.get(i).isCenterCollision(heroes.get(0))) {
                shakeDelay.play();
            }

        }
    }

    private void heroCheckItems() {
        for (int k = 0; k < heroes.size(); k++) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).isCenterCollision(heroes.get(k))) {
                    if (items.get(i).getItemType() == Item.ItemType.DIAMOND
                            || items.get(i).getItemType() == Item.ItemType.COIN1 || items.get(i).getItemType() == Item.ItemType.COIN) {
                        heroes.get(k).pickUpItem(items.get(i));
                        items.remove(i);
                        i--;
                        break;
                    }
                    if (heroes.get(k).getItems().size() >= 4 || heroes.get(k).getCoins() < items.get(i).getCoinPrice()) {
                        heroes.get(k).isCollision();
                        break;
                    }
                    if (items.get(i).isInStore()) {
                        heroes.get(k).buyItemByCoins(items.get(i).getCoinPrice());
                        heroes.get(k).buyItemByDiamond(items.get(i).getDiamondPrice());
                    }
                    heroes.get(k).pickUpItem(items.get(i));
                    items.remove(i);
                    i--;
                }
            }
        }

    }
    private void heroCheckWinButtoms(Graphics g){
        for(int i=0;i<heroes.size();i++){
            for(int k=0;k<winButtons.size();k++){
                if (heroes.get(i).isCollision(winButtons.get(k))) {
                    g.drawImage(award, 384, 832, null);
                }
            }
        }
    }

    private void getWeapon() {//網路bug
        for (int k = 0; k < heroes.size(); k++) {
            for (int i = 0; i < weaponsForMap.size(); i++) {
                if (weaponsForMap.get(i).isCenterCollision(heroes.get(k)) && heroes.get(k).getIsWeapon()) {
                    if (weaponsForMap.get(i).getCoinPrice() > heroes.get(k).getCoins() ||
                            weaponsForMap.get(i).getDiamondPrice() > heroes.get(k).getDiamond()) {
                        heroes.get(k).isCollision();
                        break;
                    }
                    if (weaponsForMap.get(i).isInStore()) {
                        heroes.get(k).buyItemByCoins(weaponsForMap.get(i).getCoinPrice());
                        heroes.get(k).buyItemByDiamond(weaponsForMap.get(i).getDiamondPrice());
                    }

                    Weapon temp = heroes.get(k).getWeapon();
                    weaponsForMap.get(i).setDiamondPriceToZero();
                    heroes.get(k).setWeapon(weaponsForMap.get(i));
                    temp.getCollider().setCenter(heroes.get(k).getCollider().centerX()
                            , heroes.get(k).getCollider().centerY());
                    temp.getPainter().setCenter(heroes.get(k).getPainter().centerX()
                            , heroes.get(k).painter().centerY());
                    weaponsForMap.remove(weaponsForMap.get(i));
                    weaponsForMap.add(temp);
                    heroes.get(k).setIsWeapon(false);
                }
            }
        }

    }

    private void enemyCheckHero() {

        for (int i = 0; i < enemiesForPartOnePointOne.size(); i++) {
            for (int k = 0; k < heroes.size(); k++) {
                if (enemiesForPartOnePointOne.get(i).isCenterCollision(heroes.get(k)) && enemiesForPartOnePointOne.get(i).getIsMove()) {
                    AudioResourceController.getInstance().shot("/attack.wav");
                    enemiesForPartOnePointOne.get(i).isCollision();
                    enemiesForPartOnePointOne.get(i).attack(heroes.get(k));
                    heroes.get(k).changeState(Hero.HeroState.BLOOD);
                    enemiesForPartOnePointOne.get(i).isMove();
                }
            }

        }
        for(int i=0;i<enemiesForPartOnePointTwo.size();i++){
            for (int k = 0; k < heroes.size(); k++) {
                if (enemiesForPartOnePointTwo.get(i).isCenterCollision(heroes.get(k),4) && enemiesForPartOnePointTwo.get(i).getIsMove()) {
                    AudioResourceController.getInstance().shot("/attack.wav");
                    enemiesForPartOnePointTwo.get(i).isCollision();
                    enemiesForPartOnePointTwo.get(i).attack(heroes.get(k));
                    heroes.get(k).changeState(Hero.HeroState.BLOOD);
                    enemiesForPartOnePointTwo.get(i).isMove();
                }
            }

        }
        for(int i=0;i<enemiesForPartTwoPointOne.size();i++){
            for (int k = 0; k < heroes.size(); k++) {
                if (enemiesForPartTwoPointOne.get(i).isCenterCollision(heroes.get(k)) && enemiesForPartTwoPointOne.get(i).getIsMove()) {
                    AudioResourceController.getInstance().shot("/attack.wav");
                    enemiesForPartTwoPointOne.get(i).isCollision();
                    enemiesForPartTwoPointOne.get(i).attack(heroes.get(k));
                    heroes.get(k).changeState(Hero.HeroState.BLOOD);
                    enemiesForPartTwoPointOne.get(i).isMove();
                }
            }

        }
        for(int i=0;i<enemiesForPartTwoPointTwo.size();i++){
            for (int k = 0; k < heroes.size(); k++) {
                if (enemiesForPartTwoPointTwo.get(i).isCenterCollision(heroes.get(k)) && enemiesForPartTwoPointTwo.get(i).getIsMove()) {
                    AudioResourceController.getInstance().shot("/attack.wav");
                    enemiesForPartTwoPointTwo.get(i).isCollision();
                    enemiesForPartTwoPointTwo.get(i).attack(heroes.get(k));
                    heroes.get(k).changeState(Hero.HeroState.BLOOD);
                    enemiesForPartTwoPointTwo.get(i).isMove();
                }
            }

        }
        for (int i = 0; i < enemiesForWhiteGhost.size(); i++) {
            for (int k = 0; k < heroes.size(); k++) {
                if (enemiesForWhiteGhost.get(i).isCenterCollision(heroes.get(k)) && enemiesForWhiteGhost.get(i).getIsMove()) {
                    AudioResourceController.getInstance().shot("/attack.wav");
                    enemiesForWhiteGhost.get(i).isCollision();
                    enemiesForWhiteGhost.get(i).attack(heroes.get(k));
                    heroes.get(k).changeState(Hero.HeroState.BLOOD);
                    enemiesForWhiteGhost.get(i).isMove();
                }
            }

        }
    }

    private void bossCheckHero() {
        for (int i = 0; i < bosses.size(); i++) {
            for (int k = 0; k < heroes.size(); k++) {
                if (bosses.get(i).isCenterCollision(heroes.get(k), 4) && bosses.get(i).getIsMove()) {
                    heroes.get(k).isCollision();
                    bosses.get(i).isCollision();
                    bosses.get(i).attack(heroes.get(k));
                    heroes.get(k).changeState(Hero.HeroState.BLOOD);
                    bosses.get(i).isMove();
                }
            }
        }
    }

    private void enemyCheckDied() {
        for(int i=0;i<enemiesForPartOnePointOne.size();i++){
            if (enemiesForPartOnePointOne.get(i).getHp() <= 0) {
                if(isServer==true){
                    int centerX=enemiesForPartOnePointOne.get(i).collider().centerX();
                    int centerY=enemiesForPartOnePointOne.get(i).collider().centerY();
                    comboForEnemyCoin(enemiesForPartOnePointOne.get(i),heroForCoin,centerX,centerY);
                    ArrayList<String>strings=new ArrayList<>();
                    strings.add(i+"");//0
                    strings.add(centerX+"");//1
                    strings.add(centerY+"");//2
                    strings.add(heroForCoin.getInternetID()+"");//3
                    strings.add("enemiesForPartOnePointOne");
                    ClientClass.getInstance().sent(NetCommandCode.HERO_ENEMYREMOVE,strings);
                    enemiesForPartOnePointOne.remove(i);
                    i--;
                    continue;

                }
                if(isClient==true && isContinue==false){
                    continue;
                }
                int centerX=enemiesForPartOnePointOne.get(i).collider().centerX();
                int centerY=enemiesForPartOnePointOne.get(i).collider().centerY();
                comboForEnemyCoin(enemiesForPartOnePointOne.get(i),heroForCoin,centerX,centerY);
                enemiesForPartOnePointOne.remove(i);
                i--;
            }
        }
        for(int i=0;i<enemiesForPartOnePointTwo.size();i++){//boss
            if (enemiesForPartOnePointTwo.get(i).getHp() <= 0) {
                if(isServer==true){
                    ArrayList<String>strings=new ArrayList<>();
                    strings.add(i+"");//0
                    strings.add("enemiesForPartOnePointTwo");//1
                    ClientClass.getInstance().sent(NetCommandCode.HERO_BOSSREMOVE,strings);
                    enemiesForPartOnePointTwo.remove(i);
                    i--;
                    continue;
                }
                if(isClient==true && isContinue==false){
                    continue;
                }
                enemiesForPartOnePointTwo.remove(i);
                i--;

            }
        }
        for(int i=0;i<enemiesForPartTwoPointOne.size();i++){
            if (enemiesForPartTwoPointOne.get(i).getHp() <= 0) {

                if(isServer==true){
                    int centerX=enemiesForPartTwoPointOne.get(i).collider().centerX();
                    int centerY=enemiesForPartTwoPointOne.get(i).collider().centerY();
                    comboForEnemyCoin(enemiesForPartTwoPointOne.get(i),heroForCoin,centerX,centerY);
                    ArrayList<String>strings=new ArrayList<>();
                    strings.add(i+"");//0
                    strings.add(centerX+"");//1
                    strings.add(centerY+"");//2
                    strings.add(heroForCoin.getInternetID()+"");//3
                    strings.add("enemiesForPartTwoPointOne");//4
                    ClientClass.getInstance().sent(NetCommandCode.HERO_ENEMYREMOVE,strings);
                    enemiesForPartTwoPointOne.remove(i);
                    i--;
                    continue;
                }
                if(isClient==true && isContinue==false){
                    continue;
                }
                int centerX=enemiesForPartTwoPointOne.get(i).collider().centerX();
                int centerY=enemiesForPartTwoPointOne.get(i).collider().centerY();
                comboForEnemyCoin(enemiesForPartTwoPointOne.get(i),heroForCoin,centerX,centerY);
                enemiesForPartTwoPointOne.remove(i);
                i--;
            }
        }
        for(int i=0;i<enemiesForPartTwoPointTwo.size();i++){
            if (enemiesForPartTwoPointTwo.get(i).getHp() <= 0) {

                if(isServer==true){
                    int centerX=enemiesForPartTwoPointTwo.get(i).collider().centerX();
                    int centerY=enemiesForPartTwoPointTwo.get(i).collider().centerY();
                    comboForEnemyCoin(enemiesForPartTwoPointTwo.get(i),heroForCoin,centerX,centerY);
                    ArrayList<String>strings=new ArrayList<>();
                    strings.add(i+"");//0
                    strings.add(centerX+"");//1
                    strings.add(centerY+"");//2
                    strings.add(heroForCoin.getInternetID()+"");//3
                    strings.add("enemiesForPartTwoPointTwo");
                    ClientClass.getInstance().sent(NetCommandCode.HERO_ENEMYREMOVE,strings);
                    enemiesForPartTwoPointTwo.remove(i);
                    i--;
                    continue;

                }
                if(isClient==true && isContinue==false){
                    continue;
                }
                int centerX=enemiesForPartTwoPointTwo.get(i).collider().centerX();
                int centerY=enemiesForPartTwoPointTwo.get(i).collider().centerY();
                comboForEnemyCoin(enemiesForPartTwoPointTwo.get(i),heroForCoin,centerX,centerY);
                enemiesForPartTwoPointTwo.remove(i);
                i--;
            }
        }
        for (int i = 0; i < enemiesForWhiteGhost.size(); i++) {
            if (enemiesForWhiteGhost.get(i).getHp() <= 0) {

                if(isServer==true){
                    int centerX=enemiesForWhiteGhost.get(i).collider().centerX();
                    int centerY=enemiesForWhiteGhost.get(i).collider().centerY();
                    comboForEnemyCoin(enemiesForWhiteGhost.get(i),heroForCoin,centerX,centerY);
                    ArrayList<String>strings=new ArrayList<>();
                    strings.add(i+"");//0
                    strings.add(centerX+"");//1
                    strings.add(centerY+"");//2
                    strings.add(heroForCoin.getInternetID()+"");//3
                    strings.add("enemiesForWhiteGhost");
                    ClientClass.getInstance().sent(NetCommandCode.HERO_ENEMYREMOVE,strings);
                    enemiesForWhiteGhost.remove(i);
                    i--;
                    continue;
                }
                if(isClient==true && isContinue==false){
                    continue;
                }
                int centerX=enemiesForWhiteGhost.get(i).collider().centerX();
                int centerY=enemiesForWhiteGhost.get(i).collider().centerY();
                comboForEnemyCoin(enemiesForWhiteGhost.get(i),heroForCoin,centerX,centerY);
                enemiesForWhiteGhost.remove(i);
                i--;
            }
        }
    }

    private void bossCheckDied() {
        for (int i = 0; i < bosses.size(); i++) {
            if (bosses.get(i).getHp() <= 0) {
                if(isServer==true){
                    ArrayList<String>strings=new ArrayList<>();
                    strings.add(i+"");//0
                    strings.add("bosses");//1
                    ClientClass.getInstance().sent(NetCommandCode.HERO_BOSSREMOVE,strings);
                    bosses.remove(i);
                    i--;
                    continue;
                }
                if(isClient==true && isContinue==false){
                    continue;
                }
                bosses.remove(i);
                i--;
            }
        }
    }

    private void weaponIsCollision() {
        for (int i = 0; i < weapons.size(); i++) {
            if (weapons.get(i).isOutOfBound()) {
                weapons.remove(i);
                i--;
                continue;
            }

            for(int k=0;k<enemiesForPartOnePointOne.size();k++){
                if (weapons.get(i).isCenterCollision(enemiesForPartOnePointOne.get(k))&&enemiesForPartOnePointOne.get(k).isCanDamage()) {
                    AudioResourceController.getInstance().shot("/at2.wav");
                    for (int r = 0; r < heroes.size(); r++) {
                        if (weapons.get(i).getId() == heroes.get(r).getInternetID()) {
                            heroes.get(r).isCollision();
                            enemiesForPartOnePointOne.get(k).isCollision();
                            heroes.get(r).attack(enemiesForPartOnePointOne.get(k));
                            heroForCoin=heroes.get(r);
                            enemiesForPartOnePointOne.get(k).changeEnemyState(Enemy.EnemyState.BLOOD);
                            enemiesForPartOnePointOne.get(k).cantDamage();
                            shakeDelay.play();
                            break;
                        }
                    }
                }
            }
            for(int k=0;k<enemiesForPartOnePointTwo.size();k++){
                if (weapons.get(i).isCenterCollision(enemiesForPartOnePointTwo.get(k),4)&&enemiesForPartOnePointTwo.get(k).isCanDamage()) {
                    AudioResourceController.getInstance().shot("/at2.wav");
                    for (int r = 0; r < heroes.size(); r++) {
                        if (weapons.get(i).getId() == heroes.get(r).getInternetID()) {
                            heroes.get(r).isCollision();
                            enemiesForPartOnePointTwo.get(k).isCollision();
                            heroes.get(r).attack(enemiesForPartOnePointTwo.get(k));
//                            enemiesForPartOnePointTwo.get(k).isCollision();
                            enemiesForPartOnePointTwo.get(k).changeState(Boss.BossState.BLOOD);
                            heroForCoin=heroes.get(r);
                            enemiesForPartOnePointTwo.get(k).cantDamage();
                            shakeDelay.play();
                            break;
                        }
                    }
                }
            }
            for(int k=0;k<enemiesForPartTwoPointOne.size();k++){
                if (weapons.get(i).isCenterCollision(enemiesForPartTwoPointOne.get(k))&& enemiesForPartTwoPointOne.get(k).isCanDamage()) {
                    AudioResourceController.getInstance().shot("/at2.wav");
                    for (int r = 0; r < heroes.size(); r++) {
                        if (weapons.get(i).getId() == heroes.get(r).getInternetID()) {
                            heroes.get(r).isCollision();
                            enemiesForPartTwoPointOne.get(k).isCollision();
                            heroes.get(r).attack(enemiesForPartTwoPointOne.get(k));
                            enemiesForPartTwoPointOne.get(k).changeEnemyState(Enemy.EnemyState.BLOOD);
                            heroForCoin=heroes.get(r);
                            enemiesForPartTwoPointOne.get(k).cantDamage();
                            shakeDelay.play();
                            break;
                        }
                    }
                }
            }
            for(int k=0;k<enemiesForPartTwoPointTwo.size();k++){
                if (weapons.get(i).isCenterCollision(enemiesForPartTwoPointTwo.get(k))&&enemiesForPartTwoPointTwo.get(k).isCanDamage()) {
                    AudioResourceController.getInstance().shot("/at2.wav");
                    for (int r = 0; r < heroes.size(); r++) {
                        if (weapons.get(i).getId() == heroes.get(r).getInternetID()) {
                            heroes.get(r).isCollision();
                            enemiesForPartTwoPointTwo.get(k).isCollision();
                            heroes.get(r).attack(enemiesForPartTwoPointTwo.get(k));
                            enemiesForPartTwoPointTwo.get(k).changeEnemyState(Enemy.EnemyState.BLOOD);
                            heroForCoin=heroes.get(r);
                            enemiesForPartTwoPointTwo.get(k).cantDamage();
                            shakeDelay.play();
                            break;
                        }
                    }
                }
            }


            for (int k = 0; k < bosses.size(); k++) {
                if (weapons.get(i).isCenterCollision(bosses.get(k), 4)&&bosses.get(k).isCanDamage()) {
                    AudioResourceController.getInstance().shot("/at2.wav");
                    for (int r = 0; r < heroes.size(); r++) {
                        if (weapons.get(i).getId() == heroes.get(r).getInternetID()) {
                            heroes.get(r).isCollision();
                            bosses.get(k).isCollision();
                            if(bosses.get(k).getBossState2()== Boss.BossState2.NORMAL){
                                heroes.get(r).attack(bosses.get(k));
                            }
                            bosses.get(k).changeState(Boss.BossState.BLOOD);
                            shakeDelay.play();
                            bosses.get(k).cantDamage();
                            break;
                        }
                    }
                }
            }
            for (int k = 0; k < enemiesForWhiteGhost.size(); k++) {
                if (weapons.get(i).isCenterCollision(enemiesForWhiteGhost.get(k))&&enemiesForWhiteGhost.get(k).isCanDamage()) {
                    AudioResourceController.getInstance().shot("/at2.wav");
                    for (int r = 0; r < heroes.size(); r++) {
                        if (weapons.get(i).getId() == heroes.get(r).getInternetID()) {
                            heroes.get(r).isCollision();
                            enemiesForWhiteGhost.get(k).isCollision();
                            heroes.get(r).attack(enemiesForWhiteGhost.get(k));
                            heroForCoin=heroes.get(r);
                            enemiesForWhiteGhost.get(k).changeEnemyState(Enemy.EnemyState.BLOOD);
                            shakeDelay.play();
                            enemiesForWhiteGhost.get(k).cantDamage();
                            break;
                        }
                    }
                }
            }
        }
    }

    private void addWeapon() {
        switch (heroes.get(0).getWeapon().getWeaponType()) {
            case WEAPON1:
                weapons.add(new Weapon(Weapon.WeaponType.WEAPON1, Weapon.State.ATTACK,
                        heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                break;
            case WEAPON2:
                switch (heroes.get(0).getCurrentDirection()) {
                    case UP:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left() + CENTER_WIDTH, heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left() - CENTER_WIDTH, heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        break;
                    case DOWN:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left() - CENTER_WIDTH, heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left() + CENTER_WIDTH, heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        break;
                    case LEFT:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top() - CENTER_HEIGHT, heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top() + CENTER_HEIGHT, heroes.get(0).getCurrentDirection()));
                        break;
                    case RIGHT:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top() + CENTER_HEIGHT, heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top() - CENTER_HEIGHT, heroes.get(0).getCurrentDirection()));
                        break;
                }
                break;
            case WEAPON3:
                switch (heroes.get(0).getCurrentDirection()) {
                    case UP:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top() - CENTER_HEIGHT, heroes.get(0).getCurrentDirection()));
                        break;
                    case DOWN:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top() + CENTER_HEIGHT, heroes.get(0).getCurrentDirection()));
                        break;
                    case LEFT:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left() - CENTER_WIDTH, heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        break;
                    case RIGHT:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left(), heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                heroes.get(0).getPainter().left() + CENTER_WIDTH, heroes.get(0).getPainter().top(), heroes.get(0).getCurrentDirection()));
                        break;
                }
                break;



        }

    }

    private void addWeaponForInterNet(Weapon.WeaponType weaponType, int left, int top, Global.Direction direction, int id) {
        switch (weaponType) {
            case WEAPON1:
                weapons.add(new Weapon(Weapon.WeaponType.WEAPON1, Weapon.State.ATTACK,
                        left, top, direction, id));
                break;
            case WEAPON2:
                switch (direction) {
                    case UP:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left + CENTER_WIDTH, top, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left - CENTER_WIDTH, top, direction, id));
                        break;
                    case DOWN:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left - CENTER_WIDTH, top, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left + CENTER_WIDTH, top, direction, id));
                        break;
                    case LEFT:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top - CENTER_HEIGHT, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top + CENTER_HEIGHT, direction, id));
                        break;
                    case RIGHT:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top + CENTER_HEIGHT, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top - CENTER_HEIGHT, direction, id));
                        break;
                }
                break;
            case WEAPON3:
                switch (direction) {
                    case UP:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top - CENTER_HEIGHT, direction, id));
                        break;
                    case DOWN:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top + CENTER_HEIGHT, direction, id));
                        break;
                    case LEFT:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left - CENTER_WIDTH, top, direction, id));
                        break;
                    case RIGHT:
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left, top, direction, id));
                        weapons.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.ATTACK,
                                left + CENTER_WIDTH, top, direction, id));
                        break;
                }
                break;

        }

    }

    private void addMarker() {
        if (markers2.size() < 10 && delayForMarkerMove.count()) {
            markers.add(new Marker(-182, 512, Direction.RIGHT));//由左往右
        }
        if (markers2.size() < 10 && delayForMarkerMove.count()) {
            markers2.add(new Marker(688, 512, Direction.LEFT));//由右往左

        }
    }

    private void markCheckMark2() {
        for (int i = 0; i < markers.size(); i++) {
            for (int k = 0; k < markers2.size(); k++) {
                if (markers.get(i).isCollision(markers2.get(k))) {
                    markers2.remove(k);
                    markers.remove(i);
                    k--;
                    i--;
                    if (delay.isStop()) {
                        delay.play();
                    }
                    break;
                }
                if (markers.get(i).isCollision(heart) && markers2.get(k).isCollision(heart)) {//碰到心臟外圍的碰撞框
                    isForMarker = true;
                    heart.changeState(Heart.State.JUMP);
                }
            }
        }
    }

    private void connect(){
        ArrayList<String>strForHero=new ArrayList<>();
        strForHero.add(heroes.get(0).collider().centerX()+"");
        strForHero.add(heroes.get(0).collider().centerY()+"");
        strForHero.add(heroes.get(0).getHeroType().name()+"");
        strForHero.add(heroes.get(0).getCurrentDirection().name());
        strForHero.add(heroes.get(0).getHp()+"");//更新血量//4
        strForHero.add(heroes.get(0).getAtk()+"");//更新攻擊力//5//
        strForHero.add(heroes.get(0).getDefense()+"");//更新防禦力//6
        strForHero.add(heroes.get(0).getHpLimitForInternet()+"");//7更新血量上限
        ClientClass.getInstance().sent(NetCommandCode.HERO_CONNECT,strForHero);
        ClientClass.getInstance().sent(NetCommandCode.HERO_MOVE,strForHero);
        if(isServer && enemiesForPartOnePointTwo.size()==1){
            ArrayList<String>strings=new ArrayList<>();
            strings.add(enemiesForPartOnePointTwo.get(0).collider().centerX()+"");
            strings.add(enemiesForPartOnePointTwo.get(0).collider().centerY()+"");
            ClientClass.getInstance().sent(NetCommandCode.BOSS_UPDATE,strings);
        }
        if(isServer && enemiesForPartOnePointTwo.size()==2){
            ArrayList<String>strings=new ArrayList<>();
            strings.add(enemiesForPartOnePointTwo.get(0).collider().centerX()+"");
            strings.add(enemiesForPartOnePointTwo.get(0).collider().centerY()+"");
            strings.add(enemiesForPartOnePointTwo.get(1).collider().centerX()+"");
            strings.add(enemiesForPartOnePointTwo.get(1).collider().centerY()+"");
            ClientClass.getInstance().sent(NetCommandCode.BOSS_UPDATE,strings);
        }

        if(isServer && bosses.size()==1){
            ArrayList<String>strings=new ArrayList<>();
            strings.add(bosses.get(0).collider().centerX()+"");
            strings.add(bosses.get(0).collider().centerY()+"");
            ClientClass.getInstance().sent(NetCommandCode.BOSS_FINALBOSS,strings);
        }
        if(isServer && bosses.size()==2){
            ArrayList<String>strings=new ArrayList<>();
            strings.add(bosses.get(0).collider().centerX()+"");
            strings.add(bosses.get(0).collider().centerY()+"");
            strings.add(bosses.get(1).collider().centerX()+"");
            strings.add(bosses.get(1).collider().centerY()+"");
            ClientClass.getInstance().sent(NetCommandCode.BOSS_FINALBOSS,strings);
        }


        ArrayList<String> strings=new ArrayList<>();
        strings.add(heroes.get(0).getCombo()+"");
        ClientClass.getInstance().sent(NetCommandCode.HERO_COMBO,strings);
        ClientClass.getInstance().consume((int serialNum, int commandCode, ArrayList<String> strs) -> {
            if(serialNum == heroes.get(0).getInternetID()){
                return;
            }
            switch(commandCode){
                case NetCommandCode.HERO_CONNECT:
                    boolean isBorn = false;
                    for (int i = 0; i < heroes.size(); i++) {
                        if (heroes.get(i).getInternetID() == serialNum) {
                            isBorn = true;
                            break;
                        }
                    }
                    if (!isBorn) {
                        heroes.add(new Hero(Integer.parseInt(strs.get(0)), Integer.parseInt(strs.get(1)), Hero.HeroType.valueOf(strs.get(2))));
                        heroes.get(heroSize++).setInternetID(serialNum);
                    }
                    break;
                case NetCommandCode.HERO_MOVE:
                    for (int i = 0; i <heroes.size(); i++) {
                        if (heroes.get(i).getInternetID()  == serialNum){
                            heroes.get(i).painter().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                            heroes.get(i).collider().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                            heroes.get(i).setCurrentDirection(Direction.valueOf(strs.get(3)));
                            heroes.get(i).changeHero(Hero.HeroType.valueOf(strs.get(2)));
                            heroes.get(i).setHpForInterNet(Integer.parseInt(strs.get(4)));
                            heroes.get(i).setAtkForInternet(Integer.parseInt(strs.get(5)));
                            heroes.get(i).setDefenseForInternet(Integer.parseInt(strs.get(6)));
                            heroes.get(i).setHpLimitForInternet(Integer.parseInt(strs.get(7)));
                        }
                    }
                    break;
                case NetCommandCode.HERO_DISCONNECT:
                    for(int i=0;i<heroes.size();i++){
                        if(heroes.get(i).getInternetID()==serialNum){
                            heroes.remove(i);
                            isContinue=true;
                            otherStillAtGame=false;
                            i--;
                        }
                    }
                    break;
                case NetCommandCode.HERO_WEAPON:
                    addWeaponForInterNet(Weapon.WeaponType.valueOf(strs.get(0)),Integer.parseInt(strs.get(1)),
                            Integer.parseInt(strs.get(2)),Direction.valueOf(strs.get(3)),serialNum);
                    break;
                case NetCommandCode.HERO_WALLREMOVE:
                    walls.remove(Integer.parseInt(strs.get(0)));
                    break;
                case NetCommandCode.HERO_ENEMYREMOVE:
                    switch (strs.get(4)){
                        case"enemiesForPartOnePointOne":
                            for(int i=0;i<heroes.size();i++){
                                if(heroes.get(i).getInternetID()==Integer.parseInt(strs.get(3))){
                                    comboForEnemyCoin(enemiesForPartOnePointOne.get(Integer.parseInt(strs.get(0))),
                                            heroes.get(i),Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2)));
                                }
                            }
                            enemiesForPartOnePointOne.remove(Integer.parseInt(strs.get(0)));
                            break;
                        case "enemiesForPartTwoPointOne":
                            for(int i=0;i<heroes.size();i++){
                                if(heroes.get(i).getInternetID()==Integer.parseInt(strs.get(3))){
                                    comboForEnemyCoin(enemiesForPartTwoPointOne.get(Integer.parseInt(strs.get(0))),
                                            heroes.get(i),Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2)));
                                }
                            }
                            enemiesForPartTwoPointOne.remove(Integer.parseInt(strs.get(0)));
                            break;
                        case "enemiesForPartTwoPointTwo":
                            for(int i=0;i<heroes.size();i++){
                                if(heroes.get(i).getInternetID()==Integer.parseInt(strs.get(3))){
                                    comboForEnemyCoin(enemiesForPartTwoPointTwo.get(Integer.parseInt(strs.get(0))),
                                            heroes.get(i),Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2)));
                                }
                            }
                            enemiesForPartTwoPointTwo.remove(Integer.parseInt(strs.get(0)));
                            break;
                        case "enemiesForWhiteGhost":
                            for(int i=0;i<heroes.size();i++){
                                if(heroes.get(i).getInternetID()==Integer.parseInt(strs.get(3))){
                                    comboForEnemyCoin(enemiesForWhiteGhost.get(Integer.parseInt(strs.get(0))),
                                            heroes.get(i),Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2)));
                                }
                            }
                            enemiesForWhiteGhost.remove(Integer.parseInt(strs.get(0)));
                            break;
                    }
                    break;
                case NetCommandCode.HERO_BOSSREMOVE:
                    switch (strs.get(1)){
                        case"enemiesForPartOnePointTwo":
                            enemiesForPartOnePointTwo.remove(Integer.parseInt(strs.get(0)));
                            break;
                        case"bosses":
                            bosses.remove(Integer.parseInt(strs.get(0)));
                            break;
                    }
                    break;
                case NetCommandCode.HERO_COMBO:
                    for(int i=0;i<heroes.size();i++){
                        if(heroes.get(i).getInternetID()==serialNum){
                            heroes.get(i).setCombo(Integer.parseInt(strs.get(0)));
                        }
                    }
                    break;
                case NetCommandCode.HERO_MOVEFORINTERNET:
                    for(int i=0;i<heroes.size();i++){
                        if(heroes.get(i).getInternetID()==serialNum){
                            heroes.get(i).moveForInternet();
                        }
                    }
                    break;
                case NetCommandCode.BOSS_UPDATE:
                    if(enemiesForPartOnePointTwo.size()==1){
                        enemiesForPartOnePointTwo.get(0).painter().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                        enemiesForPartOnePointTwo.get(0).collider().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                    }
                    if(enemiesForPartOnePointTwo.size()==2){
                        enemiesForPartOnePointTwo.get(0).painter().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                        enemiesForPartOnePointTwo.get(0).collider().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                        enemiesForPartOnePointTwo.get(1).painter().setCenter(Integer.parseInt(strs.get(2)),Integer.parseInt(strs.get(3)));
                        enemiesForPartOnePointTwo.get(1).collider().setCenter(Integer.parseInt(strs.get(2)),Integer.parseInt(strs.get(3)));
                    }
                    break;
                case NetCommandCode.BOSS_FINALBOSS:
                    if(bosses.size()==1){
                        bosses.get(0).painter().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                        bosses.get(0).collider().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                    }
                    if(bosses.size()==2){
                        bosses.get(0).painter().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                        bosses.get(0).collider().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                        bosses.get(1).painter().setCenter(Integer.parseInt(strs.get(2)),Integer.parseInt(strs.get(3)));
                        bosses.get(1).collider().setCenter(Integer.parseInt(strs.get(2)),Integer.parseInt(strs.get(3)));
                    }

                    break;
            }
        });
    }

    //--------------------------------------------------------------------------------------
    private void heroCheckBossDoor(){
        if(bossDoors.get(0).isCenterCollision(heroes.get(0),4)){
            if(isServer){
                heroes.get(0).painter().setCenter(96, 992);
                heroes.get(0).collider().setCenter(96, 992);
                return;
            }
            if(isClient){
                heroes.get(0).painter().setCenter(160, 992);
                heroes.get(0).collider().setCenter(160, 992);
                return;
            }
            heroes.get(0).painter().setCenter(96, 992);
            heroes.get(0).collider().setCenter(96, 992);
        }
        if(bossDoors.get(1).isCenterCollision(heroes.get(0),4)){//傳送門第一點
            if(isServer){
                heroes.get(0).painter().setCenter(928, 416);
                heroes.get(0).collider().setCenter(928 ,416);
                return;
            }
            if(isClient){
                heroes.get(0).painter().setCenter(1120, 416);
                heroes.get(0).collider().setCenter(1120 ,416);
                return;
            }
            heroes.get(0).painter().setCenter(928, 416);
            heroes.get(0).collider().setCenter(928 ,416);
        }
        if(bossDoors.get(2).isCenterCollision(heroes.get(0),4)){
            if(isServer){
                heroes.get(0).painter().setCenter(608,736);
                heroes.get(0).collider().setCenter(608, 736);
                return;
            }
            if(isClient){
                heroes.get(0).painter().setCenter(672,736);
                heroes.get(0).collider().setCenter(672, 736);
                return;
            }
            heroes.get(0).painter().setCenter(608,736);
            heroes.get(0).collider().setCenter(608, 736);
        }

    }
    private void addEnemyForLevel3OnePointOne(){
        for(int k=0;k<heroes.size();k++){
            if(((heroes.get(k).collider().centerX()==96 && heroes.get(k).collider().centerY()==992)||
                    (heroes.get(k).collider().centerX()==160 && heroes.get(k).collider().centerY()==992))&& isForPartOnePointOne==true){
                enemiesForPartOnePointOne.add(new Enemy(64,1024, Enemy.EnemyCharacter.ENEMYQUEEN));
                enemiesForPartOnePointOne.add(new Enemy(128,1152, Enemy.EnemyCharacter.ENEMYQUEENa));
                enemiesForPartOnePointOne.add(new Enemy(192,1024, Enemy.EnemyCharacter.ENEMYQUEEN));
                enemiesForPartOnePointOne.add(new Enemy(256,1152, Enemy.EnemyCharacter.ENEMYQUEENa));
                isForPartOnePointOne=false;
            }
        }
    }
    private void addEnemyForLevel3OnePointTwo(){
        for(int k=0;k<heroes.size();k++){
            if(((heroes.get(k).collider().centerX()==160 && heroes.get(k).collider().centerY()==352)||
                    (heroes.get(k).collider().centerX()==224 && heroes.get(k).collider().centerY()==352))
            && isForPartOnePointTwo==true){
                enemiesForPartOnePointTwo.add(new Boss(64,64, Boss.BossCharacter.DRAGON1b));
                isForPartOnePointTwo=false;
            }
        }
    }
    private void addEnemiesForPartTwoPointOne(){
        for(int k=0;k<heroes.size();k++){
            if(((heroes.get(k).collider().centerX()==928 && heroes.get(k).collider().centerY()==416) ||
                    (heroes.get(k).collider().centerX()==1120&& heroes.get(k).collider().centerY()==416))
                            && isForPartTwoPointOne==true){
                enemiesForPartTwoPointOne.add(new Enemy(896,256, Enemy.EnemyCharacter.ENEMYQUEEN));//6
                enemiesForPartTwoPointOne.add(new Enemy(960,256, Enemy.EnemyCharacter.ENEMYQUEEN));//7
                enemiesForPartTwoPointOne.add(new Enemy(1024,256, Enemy.EnemyCharacter.ENEMYQUEEN));//8
                enemiesForPartTwoPointOne.add(new Enemy(1088,256, Enemy.EnemyCharacter.ENEMYQUEEN));//9
                enemiesForPartTwoPointOne.add(new Enemy(896,512, Enemy.EnemyCharacter.ENEMYQUEENa));//10
                enemiesForPartTwoPointOne.add(new Enemy(960,512, Enemy.EnemyCharacter.ENEMYQUEENa));//11
                enemiesForPartTwoPointOne.add(new Enemy(1024,512, Enemy.EnemyCharacter.ENEMYQUEENa));//12
                enemiesForPartTwoPointOne.add(new Enemy(1088,512, Enemy.EnemyCharacter.ENEMYQUEENa));//13
                isForPartTwoPointOne=false;
            }
        }
    }
    private void addEnemiesForPartTwoPointTwo(){
        for(int k=0;k<heroes.size();k++){
            if(((heroes.get(k).collider().centerX()==1056 && heroes.get(k).collider().centerY()==1120)||
                    (heroes.get(k).collider().centerX()==1120 && heroes.get(k).collider().centerY()==1120))
                    && isForPartTwoPointTwo==true){
                enemiesForPartTwoPointTwo.add(new Enemy(960,1024, Enemy.EnemyCharacter.LITTLEGIRL4));//14
                enemiesForPartTwoPointTwo.add(new Enemy(1024,1024, Enemy.EnemyCharacter.LITTLEGIRL5));//15
                enemiesForPartTwoPointTwo.add(new Enemy(1088,1024, Enemy.EnemyCharacter.LITTLEGIRL6));//16
                enemiesForPartTwoPointTwo.add(new Enemy(1152,1024, Enemy.EnemyCharacter.LITTLEGIRL7));//17
                enemiesForPartTwoPointTwo.add(new Enemy(960,1088, Enemy.EnemyCharacter.LITTLEGIRL8));//18
                enemiesForPartTwoPointTwo.add(new Enemy(1152,1088, Enemy.EnemyCharacter.LITTLEGIRL9));//19
                enemiesForPartTwoPointTwo.add(new Enemy(960,1152, Enemy.EnemyCharacter.LITTLEGIRL10));//20
                enemiesForPartTwoPointTwo.add(new Enemy(1024,1152, Enemy.EnemyCharacter.LITTLEGIRL11));//21
                enemiesForPartTwoPointTwo.add(new Enemy(1088,1152, Enemy.EnemyCharacter.LITTLEGIRL12));//22
                enemiesForPartTwoPointTwo.add(new Enemy(1152,1152, Enemy.EnemyCharacter.LITTLEGIRL13));//23
                if(delayForFinalBoss.isStop()){
                    delayForFinalBoss.play();
                }
                isForPartTwoPointTwo=false;
            }
        }
    }

    private void addEnemiesForWhiteGhost(){
        for(int k=0;k<heroes.size();k++){
            if(((heroes.get(k).collider().centerX()==608 &&heroes.get(k).collider().centerY()==736)
                    ||(heroes.get(k).collider().centerX()==672 &&heroes.get(k).collider().centerY()==736))&& isForWhiteGhost==true){
                enemiesForWhiteGhost.add(new Enemy(576, 128, Enemy.EnemyCharacter.WHITEGHOST));//1
                enemiesForWhiteGhost.add(new Enemy(640, 128, Enemy.EnemyCharacter.WHITEGHOSTa));//2
                enemiesForWhiteGhost.add(new Enemy(448, 64, Enemy.EnemyCharacter.WHITEGHOSTb));//3
                enemiesForWhiteGhost.add(new Enemy(384, 128, Enemy.EnemyCharacter.WHITEGHOSTc));//4
                enemiesForWhiteGhost.add(new Enemy(320, 64, Enemy.EnemyCharacter.WHITEGHOSTd));//5
                enemiesForWhiteGhost.add(new Enemy(256, 128, Enemy.EnemyCharacter.WHITEGHOSTe));//6
                enemiesForWhiteGhost.add(new Enemy(768, 64, Enemy.EnemyCharacter.WHITEGHOSTf));//7
                enemiesForWhiteGhost.add(new Enemy(832, 128, Enemy.EnemyCharacter.WHITEGHOSTg));//8
                enemiesForWhiteGhost.add(new Enemy(896, 64, Enemy.EnemyCharacter.WHITEGHOSTh));//9
                enemiesForWhiteGhost.add(new Enemy(960, 128, Enemy.EnemyCharacter.WHITEGHOSTi));//10
                isForWhiteGhost=false;
            }
        }

    }
    private void changeHeroCenterForPart(){
        if(enemiesForPartOnePointOne.size()==0 && isForPartOnePointOne==false&& isForChangeHeroCenterForPartOnePointOne ==true){
            if(isServer){
                heroes.get(0).painter().setCenter(160, 352);
                heroes.get(0).collider().setCenter(160, 352);
                isForChangeHeroCenterForPartOnePointOne =false;
                return;
            }
            if(isClient){
                heroes.get(0).painter().setCenter(224, 352);
                heroes.get(0).collider().setCenter(224, 352);
                isForChangeHeroCenterForPartOnePointOne =false;
                return;

            }
            heroes.get(0).painter().setCenter(160, 352);
            heroes.get(0).collider().setCenter(160, 352);
            isForChangeHeroCenterForPartOnePointOne =false;
        }
        if(enemiesForPartOnePointTwo.size()==0 && isForPartOnePointTwo==false && isForChangeHeroCenterForPartOnePointTwo==true){
            if(isServer){
                heroes.get(0).painter().setCenter(224, 864);
                heroes.get(0).collider().setCenter(224, 864);
                isForChangeHeroCenterForPartOnePointTwo=false;
                return;
            }
            if(isClient){
                heroes.get(0).painter().setCenter(160, 864);
                heroes.get(0).collider().setCenter(160, 864);
                isForChangeHeroCenterForPartOnePointTwo=false;
                return;
            }
            heroes.get(0).painter().setCenter(224, 864);
            heroes.get(0).collider().setCenter(224, 864);
            isForChangeHeroCenterForPartOnePointTwo=false;

        }
        if(enemiesForPartTwoPointOne.size()==0 && isForPartTwoPointOne==false && isForChangeHeroCenterForPartTwoPointOne==true){
            if(isServer){
                heroes.get(0).painter().setCenter(1056, 1120);
                heroes.get(0).collider().setCenter(1056, 1120);
                isForChangeHeroCenterForPartTwoPointOne=false;
                return;
            }
            if(isClient){
                heroes.get(0).painter().setCenter(1120, 1120);
                heroes.get(0).collider().setCenter(1120, 1120);
                isForChangeHeroCenterForPartTwoPointOne=false;
                return;
            }
            heroes.get(0).painter().setCenter(1056, 1120);
            heroes.get(0).collider().setCenter(1056, 1120);
            isForChangeHeroCenterForPartTwoPointOne=false;

        }
        if(enemiesForPartTwoPointTwo.size()==0 && isForPartTwoPointTwo==false && isForChangeHeroCenterForPartTwoPointTwo==true) {
            if(isServer){
                heroes.get(0).painter().setCenter(1184, 864);
                heroes.get(0).collider().setCenter(1184, 864);
                isForChangeHeroCenterForPartTwoPointTwo = false;
                return;
            }
            if(isClient){
                heroes.get(0).painter().setCenter(1120, 864);
                heroes.get(0).collider().setCenter(1120, 864);
                isForChangeHeroCenterForPartTwoPointTwo = false;
                return;
            }
            heroes.get(0).painter().setCenter(1184, 864);
            heroes.get(0).collider().setCenter(1184, 864);
            isForChangeHeroCenterForPartTwoPointTwo = false;
        }
        if(enemiesForWhiteGhost.size()==0 && isForWhiteGhost==false){
            if(bosses.size()>0){
                bosses.get(0).changeBossState2Normal();
            }
        }
        if(enemiesForWhiteGhost.size()==0 && bosses.size()==0 && isForWhiteGhost==false&& isForWinButtom==true){
            winButtons.add(new Buttons(512, 896, Buttons.NameButton.WIN));
            isForWinButtom=false;
        }
    }

    private void comboForEnemyCoin(Enemy enemy ,Hero hero,int centerX,int centerY){
        Item item;
        if(hero.getCombo()>20){
            item=new Item(Item.ItemType.COINSEVEN,enemy.collider().left(),enemy.collider().top());
            item.painter().setCenter(centerX,centerY);
            item.collider().setCenter(centerX,centerY);
            items.add(item);
        }else if(hero.getCombo()>15){
            item=new Item(Item.ItemType.COINFIVE,enemy.collider().left(),enemy.collider().top());
            item.painter().setCenter(centerX,centerY);
            item.collider().setCenter(centerX,centerY);
            items.add(item);
        }else if(hero.getCombo()>10){
            item=new Item(Item.ItemType.COINTHREE,enemy.collider().left(),enemy.collider().top());
            item.painter().setCenter(centerX,centerY);
            item.collider().setCenter(centerX,centerY);
            items.add(item);
        }else if(hero.getCombo()>5){
            item=new Item(Item.ItemType.COINTWO,enemy.collider().left(),enemy.collider().top());
            item.painter().setCenter(centerX,centerY);
            item.collider().setCenter(centerX,centerY);
            items.add(item);
        }else{
            item=new Item(Item.ItemType.COINONE,enemy.collider().left(),enemy.collider().top());
            item.painter().setCenter(centerX,centerY);
            item.collider().setCenter(centerX,centerY);
            items.add(item);
        }
    }
    private void removeBoom(){
        for(int i=0;i<bombs.size();i++){
            if(bombs.get(i).getState()== Bomb.State.DEAD){
                bombs.remove(i);
                i--;
            }
        }
    }
    private void heroCheckBoom(){
        for(int k=0;k<heroes.size();k++){
            for(int i=0;i<bombs.size();i++){
                if(heroes.get(k).isCenterCollision(bombs.get(i))&&bombs.get(i).getFirstTimeColiForPic()){
                    bombs.get(i).changeState(Bomb.State.BOMB);
                    bombs.get(i).setFirstTimeColiForPic(false);
                }
            }
        }
    }
    private void boomCheckHero(){
        for(int i=0;i<bombs.size();i++){
            for(int k=0;k<heroes.size();k++){
                if(bombs.get(i).isCenterCollision(heroes.get(k),9)&&bombs.get(i).getState()== Bomb.State.EXP
                        &&bombs.get(i).getFirstTimeColiForColiHero()){
                    heroes.get(k).setHp(-20);
                    bombs.get(i).setFirstTimeColiForColiHero(false);
                }
            }
        }
    }
    //    private void boomCheckEnemy(){
//        for(int i=0;i<bombs.size();i++){
//            for(int k=0;k<enemies.size();k++){
//                if(bombs.get(i).isCenterCollision(enemies.get(k),9)&&bombs.get(i).getState()== Bomb.State.EXP
//                        && enemies.get(k).getFirstTimeColiForColiEnemy()){
//                    enemies.get(k).setHp(-20);
//                    enemies.get(k).setFirstTimeColiForColiEnemy(false);
//                }
//            }
//        }
//    }
    private void boomAudio(){
        for(int i=0;i<bombs.size();i++){
            if(bombs.get(i).getState()== Bomb.State.EXP){
                AudioResourceController.getInstance().play("/boom.wav");
            }
        }
    }



    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            //按住鍵盤
            public void keyPressed(int commandCode, long trigTime) {
                if (commandCode >= 0 && commandCode <= 3) {
                    if (isKeyPress) {//一次只能動一格
                        if (isForMarker) {//心臟框同時被兩根markers碰撞時打開
                            if (!missCount.isPlaying()) {
                                Direction direction = Global.Direction.getDirection(commandCode);
                                heroes.get(0).setCurrentDirection(direction);
                                ArrayList<String>strings=new ArrayList<>();
                                strings.add(heroes.get(0).getWeapon().getWeaponType().name());
                                strings.add(heroes.get(0).collider().left()+"");
                                strings.add(heroes.get(0).collider().top()+"");
                                strings.add(heroes.get(0).getCurrentDirection().name());
                                ClientClass.getInstance().sent(NetCommandCode.HERO_WEAPON,strings);
                                addWeapon();
                                heroes.get(0).move();
                                ArrayList<String>strings1=new ArrayList<>();
                                ClientClass.getInstance().sent(NetCommandCode.HERO_MOVEFORINTERNET,strings1);
                                heroes.get(0).addCombo();
                                combo.changeState(Combo.State.GREAT);

                            }
                        } else {
                            heroes.get(0).resetCombo();
                            combo.changeState(Combo.State.MISS);
                            missCount.stop();
                            missCount.play();
                        }
                        isKeyPress = false;
                    }
                }
                if (isKeyPress) {
                    if (commandCode >= 5 && commandCode <= 8) {
                        heroes.get(0).useItem(commandCode);
                        isKeyPress = false;
                    }
                }
                camera.keyPressed(commandCode, trigTime);
            }

            @Override
            //放開鍵盤
            public void keyReleased(int commandCode, long trigTime) {
                isKeyPress = true;
            }

            @Override
            public void keyTyped(char c, long trigTime) {
            }
        };
    }

}
