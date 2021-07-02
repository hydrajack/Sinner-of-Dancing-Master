package scene;

import camera.Camera;
import camera.MapInformation;
import client.ClientClass;
import controller.AudioResourceController;
import controller.MapController;
import controller.SceneController;
import gameobj.*;
import item.Item;
import utils.*;
import weapon.Weapon;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static utils.Global.*;

public class SceneLevel2 extends Scene {
    private ArrayList<Hero> heroes;
    private Hero heroForCoin;//for internet
    private int heroSize;
    private ArrayList<Weapon> weapons;
    private ArrayList<Weapon> weaponsForMap;
    private ArrayList<Enemy> enemies;
    private boolean enemiesPart1;//11
    private boolean enemiesPart2;//12
    private boolean enemiesPart3;//13 14
    private boolean enemiesPart4;//23 24
    private boolean enemiesPart5;
    private boolean enemiesPart6;
    private int enemiesPart6Count;
    private ArrayList<Boss> bosses;
    private ArrayList<Item> items;
    private ArrayList<Bomb> bombs;
    private ArrayList<Marker> markers;
    private ArrayList<Marker> markers2;
    private GUI gui;
    private Heart heart;
    private Combo combo;
    private int comboCount;
    private Camera camera;
    private java.util.List<Floor> floors;
    private List<Wall> walls;
    private NextScene[] nextScenes;
    private boolean isKeyPress;
    private Delay delayForMarkerMove;
    private Delay missCount;
    private Delay delay;//for兩根棒子碰到後
    private boolean isForMarker;
    //shakeScreen
    private Delay shakeDelay;
    private Delay delayForDeath;
    private BloodBar bloodBar;
    private Delay delayForEnemiesPart6;
    private boolean isContinue;


    public SceneLevel2(ArrayList heroes) {
        this.heroes = heroes;
        if (heroes.size() > 1) {
            otherStillAtGame = true;
            isContinue = false;
        }
        if (heroes.size() == 1) {
            isContinue = true;
        }

    }

    @Override
    public void sceneBegin() {

        GameWindow.getInstance().setWindowSize(576, 576);
        //level3 exit
        heroSize = 0;
        enemies = new ArrayList<>();
        bosses = new ArrayList<>();
        bombs = new ArrayList<>();
        enemies.add(new Enemy(128, 128, Enemy.EnemyCharacter.GREEN2a));
        enemies.add(new Enemy(256, 128, Enemy.EnemyCharacter.GREEN2a));
        enemies.add(new Enemy(192, 256, Enemy.EnemyCharacter.GREEN2));
        enemies.add(new Enemy(320, 256, Enemy.EnemyCharacter.GREEN2));
        enemies.add(new Enemy(704, 320, Enemy.EnemyCharacter.PUMPKIN));
        enemies.add(new Enemy(448, 576, Enemy.EnemyCharacter.GREEN));
        enemies.add(new Enemy(704, 576, Enemy.EnemyCharacter.GREENa));
        enemies.add(new Enemy(960, 192, Enemy.EnemyCharacter.PURPLE));//25
        items = new ArrayList<>();
        items.add(new Item(Item.ItemType.DIAMOND, 64, 64));//15
        items.add(new Item(Item.ItemType.DIAMOND, 768, 1088));
        items.add(new Item(Item.ItemType.COIN1, 896, 576));
        items.add(new Item(Item.ItemType.COIN1, 896, 640));
        items.add(new Item(Item.ItemType.HP2, 512, 128));
        items.add(new Item(Item.ItemType.HP2, 576, 128));
        items.add(new Item(Item.ItemType.DEFENSE, 640, 128));
        items.add(new Item(Item.ItemType.OLDMAN, 576, 64));//22
        enemiesPart1 = true;
        enemiesPart2 = true;
        enemiesPart3 = true;
        enemiesPart4 = true;
        enemiesPart5 = true;
        enemiesPart6=true;
        markers = new ArrayList<>();
        markers2 = new ArrayList<>();
        weapons = new ArrayList<>();
        weaponsForMap = new ArrayList<>();
        heart = new Heart(576 / 2, 512);
        combo = new Combo(576 / 2 - 32, 460);
        start();
        heroForCoin = heroes.get(0);
        heroes.get(heroSize++).setInternetID(ClientClass.getInstance().getID());

        isKeyPress = true;//按鍵一次只能按一次 不能壓下去連按
        delayForMarkerMove = new Delay(30);
        missCount = new Delay(30);
        delay = new Delay(5);
        delayForMarkerMove.loop();
        isForMarker = false;
        AudioResourceController.getInstance().loop("/zone2.wav",5);

        nextScenes = NextScene.values();
        MapController mapController = new MapController(MapController.Key.LEVEL3);
        floors = mapController.getFloors();
        walls = mapController.getWalls();
        //MapInformation.setMapInfo必須先設定
        shakeDelay = new Delay(20);
        MapInformation.setMapInfo(0, 0, 1280, 1280);
        this.camera = new Camera.Builder(GameWindow.getInstance().getWindowWidth(), GameWindow.getInstance().getWindowHeight()).setChaseObj(heroes.get(0))
                .setCameraStartLocation(0, 0)
                .gen();
        this.gui = new GUI(0, 0, 576, 576);
        bloodBar = new BloodBar();
        delayForDeath = new Delay(60);
        delayForEnemiesPart6=new Delay(110);
    }

    @Override
    public void sceneEnd() {
        AudioResourceController.getInstance().stop("/zone2.wav");
    }

    public void start(){
        if(isServer){
            heroes.get(0).painter().setCenter(1120, 96);
            heroes.get(0).collider().setCenter(1120, 96);
            return;
        }
        if(isClient){
            heroes.get(0).painter().setCenter(1184, 96);
            heroes.get(0).collider().setCenter(1184, 96);
            return;
        }
        heroes.get(0).painter().setCenter(1120, 96);
        heroes.get(0).collider().setCenter(1120, 96);
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
        //LEVEL1 EXIT
        for (int i = 0; i < nextScenes.length; i++) {
            nextScenes[i].paint(g);
        }
        bombs.forEach(bomb -> bomb.paint(g));
        items.forEach(a -> a.paint(g));
        gui.paintStoreItemForCoin(g, items);
        weaponsForMap.forEach(weapon -> weapon.paint(g));
        heroes.forEach(hero -> hero.paint(g));
        enemies.forEach(a -> a.paint(g));
        bosses.forEach(a -> a.paint(g));
        for (int i = 0; i < enemies.size(); i++) {
            bloodBar.paint(g, enemies.get(i));
        }
        for (int i = 0; i < bosses.size(); i++) {
            bloodBar.paint(g, bosses.get(i));
        }
        weapons.forEach(weapon -> weapon.paint(g));
        camera.paint(g);
        camera.end(g);
        gui.paintHeroInfo(g, heroes.get(0));
        markers.forEach(marker -> marker.paint(g));
        markers2.forEach(marker -> marker.paint(g));
        heart.paint(g);
        combo.paint(g);
    }

    @Override
    public void update() {
        connect();
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
        if(delayForEnemiesPart6.count()&&enemiesPart6Count<5){
            enemies.add(new Enemy(576,1152, Enemy.EnemyCharacter.BONE));
            enemies.add(new Enemy(640,1152, Enemy.EnemyCharacter.BONEb));
            enemiesPart6Count++;
        }
        enemyCheckDied();
        bossCheckDied();
        wallCheckRemove();
        heroes.forEach(hero -> hero.update());
        camera.update();
        bombs.forEach(bomb -> bomb.update());
        heroCheckBoom();
        boomAudio();
        boomCheckHero();
        boomCheckEnemy();
        removeBoom();

        wallCheckEnemyRemove();
        wallCheckBossRemove();
        wallCheckActorReturn();
        wallCheckEnemiesReturn();
        wallCheckBossReturn();
        addEnemyForLevel2();
        enemies.forEach(enemy -> enemy.update());
        bosses.forEach(boss -> boss.update());
        weapons.forEach(weapon -> weapon.update());
        heart.update();
        combo.update();
        markers.forEach(marker -> marker.update());
        markers2.forEach(marker -> marker.update());
        addMarker();
        markCheckMark2();
        getWeapon();
        weaponIsCollision();
//        upDateForBoss();
        enemyCheckHero();
        bossCheckHero();
        heroCheckItems();
        heroDie();
        doorCheckNextScene();
    }


    private void heroDie() {//lu
        if (heroes.get(0).getHp() <= 0) {
            if (heroes.size() > 1) {
                heroes.remove(1);
            }
            ArrayList<String> strings = new ArrayList<>();
            ClientClass.getInstance().sent(NetCommandCode.HERO_DISCONNECT, strings);
            heroes.get(0).resetCoins();
            heroes.get(0).resetItems();
            heroes.get(0).resetHp();
            heroes.get(0).changeState(Hero.HeroState.NORMAL);
            SceneController.getInstance().change(new GameOver(heroes));
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
        for (int k = 0; k < enemies.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(enemies.get(k)) && walls.get(i).isDestructible()) {
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
        for (int k = 0; k < enemies.size(); k++) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(enemies.get(k)) && !walls.get(i).isDestructible()) {
                    enemies.get(k).isCollision();
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

    private void doorCheckNextScene() {
        for (int k = 0; k < heroes.size(); k++) {
            for (int i = 0; i < nextScenes.length; i++) {
                if (nextScenes[i].door.isCenterCollision(heroes.get(k))) {
                    SceneController.getInstance().change(new Warning(heroes));
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

    private void getWeapon() {
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
        for (int i = 0; i < enemies.size(); i++) {
            for (int k = 0; k < heroes.size(); k++) {
                if (enemies.get(i).isCenterCollision(heroes.get(k)) && enemies.get(i).getIsMove()) {
                    AudioResourceController.getInstance().shot("/attack.wav");
                    enemies.get(i).isCollision();
                    enemies.get(i).attack(heroes.get(k));
                    heroes.get(k).changeState(Hero.HeroState.BLOOD);
                    enemies.get(i).isMove();
                }
            }

        }
    }

    private void bossCheckHero() {
        for (int i = 0; i < bosses.size(); i++) {
            for (int k = 0; k < heroes.size(); k++) {
                if (bosses.get(i).isCenterCollision(heroes.get(k), 4) && bosses.get(i).getIsMove()) {
                    bosses.get(i).isCollision();
                    bosses.get(i).attack(heroes.get(k));
                    heroes.get(k).changeState(Hero.HeroState.BLOOD);
                    bosses.get(i).isMove();
                    shakeDelay.play();
                }
            }
        }
    }

    private void enemyCheckDied() {
        for (int i = 0; i < enemies.size(); i++) {
            if((enemies.get(i).collider().centerX()==672 && enemies.get(i).collider().centerY()==352)
            ||(enemies.get(i).collider().centerX()==736 && enemies.get(i).collider().centerY()==352)){
                enemies.remove(i);
                i--;
                continue;
            }
            if (enemies.get(i).getHp() <= 0) {
                if (isServer == true) {
                    int centerX = enemies.get(i).collider().centerX();
                    int centerY = enemies.get(i).collider().centerY();
                    comboForEnemyCoin(enemies.get(i), heroForCoin, centerX, centerY);
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(i + "");//0
                    strings.add(centerX + "");//1
                    strings.add(centerY + "");//2
                    strings.add(heroForCoin.getInternetID() + "");//3
                    ClientClass.getInstance().sent(NetCommandCode.HERO_ENEMYREMOVE, strings);
                    enemies.remove(i);
                    i--;
                    continue;
                }
                if (isClient == true && isContinue == false) {
                    continue;
                }
                int centerX = enemies.get(i).collider().centerX();
                int centerY = enemies.get(i).collider().centerY();
                comboForEnemyCoin(enemies.get(i), heroForCoin, centerX, centerY);
                enemies.remove(i);
                i--;
            }

        }
    }

    private void bossCheckDied() {
        for (int i = 0; i < bosses.size(); i++) {
            if (bosses.get(i).getHp() <= 0) {
                if (isServer == true) {
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(i + "");//0
                    ClientClass.getInstance().sent(NetCommandCode.HERO_BOSSREMOVE, strings);
                    bosses.remove(i);
                    i--;
                    continue;
                }
                if (isClient == true && isContinue == false) {
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
            for (int k = 0; k < enemies.size(); k++) {
                if (weapons.get(i).isCenterCollision(enemies.get(k)) && enemies.get(k).isCanDamage()) {
                    AudioResourceController.getInstance().shot("/at2.wav");
                    for (int r = 0; r < heroes.size(); r++) {
                        if (weapons.get(i).getId() == heroes.get(r).getInternetID()) {
                            heroes.get(r).attack(enemies.get(k));
                            heroes.get(r).isCollision();
                            enemies.get(k).isCollision();
                            heroForCoin = heroes.get(r);
                            enemies.get(k).changeEnemyState(Enemy.EnemyState.BLOOD);
                            enemies.get(k).cantDamage();
                            shakeDelay.play();
                            break;
                        }
                    }
                }
            }
            for (int k = 0; k < bosses.size(); k++) {
                if (weapons.get(i).isCenterCollision(bosses.get(k), 4) && bosses.get(k).isCanDamage()) {
                    AudioResourceController.getInstance().shot("/at2.wav");
                    for(int r=0;r<heroes.size();r++){
                        if(weapons.get(i).getId()==heroes.get(r).getInternetID()){
                            heroes.get(r).attack(bosses.get(k));
                            heroes.get(r).isCollision();
                            bosses.get(k).isCollision();
                            bosses.get(k).changeState(Boss.BossState.BLOOD);
                            bosses.get(k).cantDamage();
                            shakeDelay.play();
                            continue;
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
//                }
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

        if(isServer && bosses.size()==1){
            ArrayList<String>strings=new ArrayList<>();
            strings.add(bosses.get(0).collider().centerX()+"");
            strings.add(bosses.get(0).collider().centerY()+"");
            ClientClass.getInstance().sent(NetCommandCode.BOSS_UPDATE,strings);
        }
        if(isServer && bosses.size()==2){
            ArrayList<String>strings=new ArrayList<>();
            strings.add(bosses.get(0).collider().centerX()+"");
            strings.add(bosses.get(0).collider().centerY()+"");
            strings.add(bosses.get(1).collider().centerX()+"");
            strings.add(bosses.get(1).collider().centerY()+"");
            ClientClass.getInstance().sent(NetCommandCode.BOSS_UPDATE,strings);
        }
        if(isServer && bosses.size()==3){
            ArrayList<String>strings=new ArrayList<>();
            strings.add(bosses.get(0).collider().centerX()+"");
            strings.add(bosses.get(0).collider().centerY()+"");
            strings.add(bosses.get(1).collider().centerX()+"");
            strings.add(bosses.get(1).collider().centerY()+"");
            strings.add(bosses.get(2).collider().centerX()+"");
            strings.add(bosses.get(2).collider().centerY()+"");
            ClientClass.getInstance().sent(NetCommandCode.BOSS_UPDATE,strings);
        }


        ArrayList<String> strings=new ArrayList<>();
        strings.add(heroes.get(0).getCombo()+"");
        ClientClass.getInstance().sent(NetCommandCode.HERO_COMBO,strings);
        ClientClass.getInstance().consume((int serialNum, int commandCode, ArrayList<String> strs) -> {
            if (serialNum == heroes.get(0).getInternetID()) {
                return;
            }
            switch (commandCode) {
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
                    for (int i = 0; i < heroes.size(); i++) {
                        if (heroes.get(i).getInternetID() == serialNum) {
                            heroes.get(i).painter().setCenter(Integer.parseInt(strs.get(0)), Integer.parseInt(strs.get(1)));
                            heroes.get(i).collider().setCenter(Integer.parseInt(strs.get(0)), Integer.parseInt(strs.get(1)));
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
                    for (int i = 0; i < heroes.size(); i++) {
                        if (heroes.get(i).getInternetID() == serialNum) {
                            heroes.remove(i);
                            isContinue = true;
                            otherStillAtGame = false;
                            i--;
                        }
                    }
                    break;
                case NetCommandCode.HERO_WEAPON:
                    addWeaponForInterNet(Weapon.WeaponType.valueOf(strs.get(0)), Integer.parseInt(strs.get(1)),
                            Integer.parseInt(strs.get(2)), Direction.valueOf(strs.get(3)), serialNum);
                    break;
                case NetCommandCode.HERO_WALLREMOVE:
                    walls.remove(Integer.parseInt(strs.get(0)));
                    break;
                case NetCommandCode.HERO_ENEMYREMOVE:
                    for (int i = 0; i < heroes.size(); i++) {
                        if (heroes.get(i).getInternetID() == Integer.parseInt(strs.get(3))) {
                            comboForEnemyCoin(enemies.get(Integer.parseInt(strs.get(0))),
                                    heroes.get(i), Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)));
                        }
                    }
                    enemies.remove(Integer.parseInt(strs.get(0)));
                    break;
                case NetCommandCode.HERO_COMBO:
                    for (int i = 0; i < heroes.size(); i++) {
                        if (heroes.get(i).getInternetID() == serialNum) {
                            heroes.get(i).setCombo(Integer.parseInt(strs.get(0)));
                        }
                    }
                    break;
                case NetCommandCode.HERO_BOSSREMOVE:
                    bosses.remove(Integer.parseInt(strs.get(0)));
                    break;
                case NetCommandCode.HERO_MOVEFORINTERNET:
                    for (int i = 0; i < heroes.size(); i++) {
                        if (heroes.get(i).getInternetID() == serialNum) {
                            heroes.get(i).moveForInternet();
                        }
                    }
                    break;
                case NetCommandCode.BOSS_UPDATE:
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
                 if(bosses.size()==3){
                     bosses.get(0).painter().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                     bosses.get(0).collider().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                     bosses.get(1).painter().setCenter(Integer.parseInt(strs.get(2)),Integer.parseInt(strs.get(3)));
                     bosses.get(1).collider().setCenter(Integer.parseInt(strs.get(2)),Integer.parseInt(strs.get(3)));
                     bosses.get(2).painter().setCenter(Integer.parseInt(strs.get(3)),Integer.parseInt(strs.get(4)));
                     bosses.get(2).collider().setCenter(Integer.parseInt(strs.get(3)),Integer.parseInt(strs.get(4)));
                 }
                    break;
            }
        });
    }

    private void addEnemyForLevel2() {
        if (enemiesPart1 == true) {
            for (int i = 0; i < heroes.size(); i++) {
                if ((heroes.get(i).collider().centerX() > 1024 && heroes.get(i).collider().centerX() < 1216)
                        && (heroes.get(i).collider().centerY() > 128 && heroes.get(i).collider().centerY() < 320)
                ) {
                    bosses.add(new Boss(960, 576, Boss.BossCharacter.DRAGON1a));
                    enemiesPart1 = false;
                }
            }
        }
        if (enemiesPart2 == true) {
            for (int i = 0; i < heroes.size(); i++) {
                if ((heroes.get(i).collider().centerX() > 1024 && heroes.get(i).collider().centerX() < 1216)
                        && (heroes.get(i).collider().centerY() > 576 && heroes.get(i).collider().centerY() < 768)
                ) {
                    bosses.add(new Boss(960, 1088, Boss.BossCharacter.DRAGON1bb));
                    enemiesPart2 = false;
                }
            }
        }
        if (enemiesPart3 == true) {
            for (int i = 0; i < heroes.size(); i++) {
                if ((heroes.get(i).collider().centerX() > 64 && heroes.get(i).collider().centerX() < 192)
                        && (heroes.get(i).collider().centerY() > 832 && heroes.get(i).collider().centerY() < 896)
                ) {
                    enemies.add(new Enemy(448, 768, Enemy.EnemyCharacter.LITTLEGIRL));
                    enemies.add(new Enemy(448, 832, Enemy.EnemyCharacter.LITTLEGIRL1));
                    enemiesPart3 = false;
                }
            }
        }
        if (enemiesPart3 == true) {
            for (int i = 0; i < heroes.size(); i++) {
                if ((heroes.get(i).collider().centerX() > 192 && heroes.get(i).collider().centerX() < 256)
                        && (heroes.get(i).collider().centerY() > 448 && heroes.get(i).collider().centerY() < 576)
                ) {
                    enemies.add(new Enemy(448, 768, Enemy.EnemyCharacter.LITTLEGIRL));
                    enemies.add(new Enemy(448, 832, Enemy.EnemyCharacter.LITTLEGIRL1));
                    enemiesPart3 = false;
                }
            }
        }
        if (enemiesPart4 == true) {
            for (int i = 0; i < heroes.size(); i++) {
                if ((heroes.get(i).collider().centerX() > 64 && heroes.get(i).collider().centerX() < 192)
                        && (heroes.get(i).collider().centerY() > 832 && heroes.get(i).collider().centerY() < 896)
                ) {
                    enemies.add(new Enemy(64, 448, Enemy.EnemyCharacter.LITTLEGIRL2));
                    enemies.add(new Enemy(128, 448, Enemy.EnemyCharacter.LITTLEGIRL3));
                    enemiesPart4 = false;
                }
            }
        }
        if (enemiesPart4 == true) {
            for (int i = 0; i < heroes.size(); i++) {
                if ((heroes.get(i).collider().centerX() > 64 && heroes.get(i).collider().centerX() < 192)
                        && (heroes.get(i).collider().centerY() > 640 && heroes.get(i).collider().centerY() < 704)
                ) {
                    enemies.add(new Enemy(64, 448, Enemy.EnemyCharacter.LITTLEGIRL2));
                    enemies.add(new Enemy(128, 448, Enemy.EnemyCharacter.LITTLEGIRL3));
                    enemiesPart4 = false;
                }
            }
        }
        if (enemiesPart5 == true) {
            for (int i = 0; i < heroes.size(); i++) {
                if (((heroes.get(i).collider().centerX() > 64 && heroes.get(i).collider().centerX() < 192)
                        && (heroes.get(i).collider().centerY() > 960 && heroes.get(i).collider().centerY() < 1024))
                        || ((heroes.get(i).collider().centerX() > 384 && heroes.get(i).collider().centerX() < 448)
                        && (heroes.get(i).collider().centerY() > 960 && heroes.get(i).collider().centerY() < 1088)
                )) {
                    bosses.add(new Boss(64, 1088, Boss.BossCharacter.DRAGON1c));
                    enemiesPart5 = false;
                }
            }
        }
        if(enemiesPart6==true){
            for (int i = 0; i < heroes.size(); i++) {
                if (((heroes.get(i).collider().centerX() > 576 && heroes.get(i).collider().centerX() < 832)
                        && (heroes.get(i).collider().centerY() > 704 && heroes.get(i).collider().centerY() < 768))
                ) {
                    delayForEnemiesPart6.loop();
                }
            }
        }

    }

    private void comboForEnemyCoin(Enemy enemy, Hero hero, int centerX, int centerY) {
        Item item;
        if (hero.getCombo() > 20) {
            item = new Item(Item.ItemType.COINSEVEN, enemy.collider().left(), enemy.collider().top());
            item.painter().setCenter(centerX, centerY);
            item.collider().setCenter(centerX, centerY);
            System.out.println(item.collider().centerX() + "+" + item.collider().centerY());
            items.add(item);
        } else if (hero.getCombo() > 15) {
            item = new Item(Item.ItemType.COINFIVE, enemy.collider().left(), enemy.collider().top());
            item.painter().setCenter(centerX, centerY);
            item.collider().setCenter(centerX, centerY);
            System.out.println(item.collider().centerX() + "+" + item.collider().centerY());
            items.add(item);
        } else if (hero.getCombo() > 10) {
            item = new Item(Item.ItemType.COINTHREE, enemy.collider().left(), enemy.collider().top());
            item.painter().setCenter(centerX, centerY);
            item.collider().setCenter(centerX, centerY);
            System.out.println(item.collider().centerX() + "+" + item.collider().centerY());
            items.add(item);
        } else if (hero.getCombo() > 5) {
            item = new Item(Item.ItemType.COINTWO, enemy.collider().left(), enemy.collider().top());
            item.painter().setCenter(centerX, centerY);
            item.collider().setCenter(centerX, centerY);
            System.out.println(item.collider().centerX() + "+" + item.collider().centerY());
            items.add(item);
        } else {
            item = new Item(Item.ItemType.COINONE, enemy.collider().left(), enemy.collider().top());
            item.painter().setCenter(centerX, centerY);
            item.collider().setCenter(centerX, centerY);
            System.out.println(item.collider().centerX() + "+" + item.collider().centerY());
            items.add(item);
        }
    }

    private void removeBoom() {
        for (int i = 0; i < bombs.size(); i++) {
            if (bombs.get(i).getState() == Bomb.State.DEAD) {
                bombs.remove(i);
                i--;
            }
        }
    }

    private void heroCheckBoom() {
        for (int k = 0; k < heroes.size(); k++) {
            for (int i = 0; i < bombs.size(); i++) {
                if (heroes.get(k).isCenterCollision(bombs.get(i)) && bombs.get(i).getFirstTimeColiForPic()) {
                    bombs.get(i).changeState(Bomb.State.BOMB);
                    bombs.get(i).setFirstTimeColiForPic(false);
                }
            }
        }
    }

    private void boomCheckHero() {
        for (int i = 0; i < bombs.size(); i++) {
            for (int k = 0; k < heroes.size(); k++) {
                if (bombs.get(i).isCenterCollision(heroes.get(k), 9) && bombs.get(i).getState() == Bomb.State.EXP
                        && bombs.get(i).getFirstTimeColiForColiHero()) {
                    heroes.get(k).setHp(-20);
                    bombs.get(i).setFirstTimeColiForColiHero(false);
                }
            }
        }
    }

    private void boomCheckEnemy() {
        for (int i = 0; i < bombs.size(); i++) {
            for (int k = 0; k < enemies.size(); k++) {
                if (bombs.get(i).isCenterCollision(enemies.get(k), 9) && bombs.get(i).getState() == Bomb.State.EXP
                        && enemies.get(k).getFirstTimeColiForColiEnemy()) {
                    enemies.get(k).setHp(-20);
                    enemies.get(k).setFirstTimeColiForColiEnemy(false);
                }
            }
        }
    }

    private void boomAudio() {
        for (int i = 0; i < bombs.size(); i++) {
            if (bombs.get(i).getState() == Bomb.State.EXP) {
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
                                ArrayList<String> strings = new ArrayList<>();
                                strings.add(heroes.get(0).getWeapon().getWeaponType().name());
                                strings.add(heroes.get(0).collider().left() + "");
                                strings.add(heroes.get(0).collider().top() + "");
                                strings.add(heroes.get(0).getCurrentDirection().name());
                                ClientClass.getInstance().sent(NetCommandCode.HERO_WEAPON, strings);
                                addWeapon();
                                heroes.get(0).move();
                                ArrayList<String> strings1 = new ArrayList<>();
                                ClientClass.getInstance().sent(NetCommandCode.HERO_MOVEFORINTERNET, strings1);
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

    public enum NextScene implements GameKernel.PaintInterface {
        //往warning的地方
        ONE(256, 1152, 64, 64),
        TWO(320, 1152, 64, 64);

        private ChangeDoor door;

        NextScene(int x, int y, int width, int height) {
            door = new ChangeDoor(x, y, width, height, ChangeDoor.DoorType.DOOR_1);
        }

        @Override
        public void paint(Graphics g) {
            door.paint(g);
        }
    }

}
