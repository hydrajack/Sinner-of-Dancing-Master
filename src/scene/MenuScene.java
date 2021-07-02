package scene;

import camera.Camera;
import camera.MapInformation;
import client.ClientClass;
import controller.ImageController;
import controller.MapController;
import controller.SceneController;
import displayButton.BackgroundType;
import displayButton.EditText;
import displayButton.Style;
import gameobj.*;
import item.Item;
import utils.CommandSolver;
import utils.GameKernel;
import utils.GameWindow;
import utils.Global;
import weapon.Weapon;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static utils.Global.*;


public class MenuScene extends Scene {
    private ArrayList<Hero>heroes;
    private ArrayList<Weapon> weaponsForMap;
    private ArrayList<Item>items;
    private int heroSize;
    private Camera camera;
    private GUI gui;
    private List<Floor> floors;
    private List<Wall> walls;
    private List<ChangeDoor> doors;
    private NextScene[] nextScenes;
    private boolean isKeyPress;
    private Title[] title;
    private EditText editText;
    private Image mud;
    private Image role;
    private Image exit;
    private Image rule;
    private int random;
    private boolean alreadyRandom;

    public MenuScene(){

    }

    public MenuScene(ArrayList<Hero> heroes){
        this.heroes = heroes;
    }
    @Override
    public void sceneBegin() {
        GameWindow.getInstance().setWindowSize(960,960);

        if(heroes==null){
            heroes=new ArrayList<>();
            heroes.add(new Hero(448,320, Hero.HeroType.WARRIOR));
        }
        heroSize=0;
        heroes.get(0).collider().setCenter(480,352);
        heroes.get(0).painter().setCenter(480,352);
        heroes.get(heroSize++).setInternetID(ClientClass.getInstance().getID());
        weaponsForMap = new ArrayList<>();
        items=new ArrayList<>();
        randomStoreItem();
        items.add(new Item(Item.ItemType.OLDMAN,448,64));
        doors = new ArrayList<>();
        //menu
        title = Title.values();
        //change scene
        nextScenes = NextScene.values();
        //map->menu(level0)
        MapController mapController = new MapController(MapController.Key.LEVEL0);
        floors = mapController.getFloors();
        walls = mapController.getWalls();
        //MapInformation.setMapInfo必須先設定
        MapInformation.setMapInfo(0, 0, 1280, 1280);
        this.camera = new Camera.Builder(GameWindow.getInstance().getWindowWidth(), GameWindow.getInstance().getWindowHeight()).setChaseObj(heroes.get(0))
                .setCameraStartLocation(0, 0)
                .gen();
        //internet
        editText = new EditText(128, 320, Global.id);
        Style focus = new Style.StyleRect(192, 64, false,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/Buttons/SC.png")))
                .setTextFont(new Font("Curlz MT", Font.BOLD, 40))
                .setTextColor(Color.BLACK);
        this.gui = new GUI(0, 0, 964, 964);
        editText.setCursorSize(0);
        editText.setStyleFocus(focus);
        editText.setStyleNormal(focus);
        //chinese
        mud=ImageController.getInstance().tryGet("/c/1.png");
        role=ImageController.getInstance().tryGet("/c/2.png");
        exit=ImageController.getInstance().tryGet("/c/3.png");
        rule=ImageController.getInstance().tryGet("/c/4.png");
        alreadyRandom=true;



    }

    @Override
    public void sceneEnd() {
    }

    @Override
    public void paint(Graphics g) {
        //開鏡頭
        camera.start(g);
        // 繪製地圖
        this.floors.forEach(a -> a.paint(g));
        this.walls.forEach(a -> a.paint(g));
        //internet
        if(isServer==true){
            editText.isFocus();
            editText.paint(g);
        }
        //chinese
        g.drawImage(mud,130,430,null);
        g.drawImage(role,385,430,null);
        g.drawImage(exit,635,428,635+192,500,0,-6,192,64,null);
        g.drawImage(rule,64,64,null);
        // menu
        for (int i = 0; i < title.length; i++) {
            title[i].paint(g);
        }
        for (int i = 0; i < nextScenes.length; i++) {
            nextScenes[i].paint(g);
        }
//       ;
        //角色登場
        weaponsForMap.forEach(weapon -> weapon.paint(g));
        items.forEach(item -> item.paint(g));
        heroes.forEach(a->a.paint(g));
        gui.paintStoreWeaponForDiamond(g,weaponsForMap);
        gui.paintStoreItemForDiamond(g,items);
        camera.paint(g);
        camera.end(g);
        gui.paintHeroInfo(g,heroes.get(0));
    }

    private void cantNotTouchWall(){
        for(int i=0;i<walls.size();i++){
            if(walls.get(i).isCenterCollision(heroes.get(0))){
                switch (heroes.get(0).getCurrentDirection()){
                    case UP:
                        heroes.get(0).translateY(CENTER_HEIGHT);
                        break;
                    case DOWN:
                        heroes.get(0).translateY(-CENTER_HEIGHT);
                        break;
                    case LEFT:
                        heroes.get(0).translateX(CENTER_WIDTH);
                        break;
                    case RIGHT:
                        heroes.get(0).translateX(-CENTER_WIDTH);
                        break;
                }
            }
        }
    }

    private void wallCheckRemove() {
        for(int k=0;k<heroes.size();k++){
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(heroes.get(k)) && walls.get(i).isDestructible()) {
                    walls.remove(i);
                    i--;
                }
            }
        }
    }
    private void wallCheckActorReturn() {
        for(int k=0;k<heroes.size();k++){
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).isCenterCollision(heroes.get(k)) && !walls.get(i).isDestructible()) {
                    heroes.get(k).isCollision();
                }
            }
        }

    }

    private void doorCheckNextScene() {
        ArrayList<String>strs=new ArrayList<>();
        for (int i = 0; i < nextScenes.length; i++) {
            if (nextScenes[i].door.isCenterCollision(heroes.get(0))) {
                switch (nextScenes[i]) {
                    case RULE:
                        SceneController.getInstance().change(new Rule(heroes));
                        break;
                    case ROLE:
                        SceneController.getInstance().change(new Role(heroes));
                        break;
                    case MUD:
                        SceneController.getInstance().change(new Connect(heroes));
                        break;
                    case LEVEL1:
                        strs.add("3");
                        ClientClass.getInstance().sent(NetCommandCode.HERO_SCENECHANGE,strs);
                        SceneController.getInstance().change(new SceneLevel1(heroes));
                        break;
                    case LEVEL3:
                        strs.add("4");
                        ClientClass.getInstance().sent(NetCommandCode.HERO_SCENECHANGE,strs);
                        SceneController.getInstance().change(new SceneLevel2(heroes));
                        break;
                    case LEVEL4:
                        strs.add("5");
                        ClientClass.getInstance().sent(NetCommandCode.HERO_SCENECHANGE,strs);
                        SceneController.getInstance().change(new SceneLevel3(heroes));
                        break;
                    case EXIT:
                        ClientClass.getInstance().sent(NetCommandCode.HERO_DISCONNECTFOREXIT,strs);
                        System.exit(1);
                        break;
                }
            }
        }
    }
    private void getWeapon() {
        for(int k=0;k<heroes.size();k++){
            for (int i = 0; i < weaponsForMap.size(); i++) {
                if (weaponsForMap.get(i).isCenterCollision(heroes.get(k))&&heroes.get(k).getIsWeapon()){
                    if(weaponsForMap.get(i).getCoinPrice()>heroes.get(k).getCoins()||
                            weaponsForMap.get(i).getDiamondPrice()>heroes.get(k).getDiamond()){
                        heroes.get(k).isCollision();
                        break;
                    }
                    if (weaponsForMap.get(i).isInStore()) {
                        heroes.get(k).buyItemByCoins(weaponsForMap.get(i).getCoinPrice());
                        heroes.get(k).buyItemByDiamond(weaponsForMap.get(i).getDiamondPrice());
                    }

                    Weapon temp = heroes.get(k).getWeapon();//把武器丟出來
                    weaponsForMap.get(i).setDiamondPriceToZero();//把武器預設價格變0 //
                    heroes.get(k).setWeapon(weaponsForMap.get(i));//設定拿到的武器
                    weaponsForMap.remove(i);
                    temp.getCollider().setCenter(heroes.get(k).getCollider().centerX()
                            , heroes.get(k).getCollider().centerY());
                    temp.getPainter().setCenter(heroes.get(k).getPainter().centerX()
                            , heroes.get(k).painter().centerY());
                    weaponsForMap.add(temp);
                    heroes.get(k).setIsWeapon(false);
                }
            }
        }

    }
    private void connect(){
        ArrayList<String>strForHero=new ArrayList<>();
        if(otherStillAtGame==false){
            strForHero.add(heroes.get(0).painter().centerX()+"");//0,x
            strForHero.add(heroes.get(0).painter().centerY()+"");//1,y
            strForHero.add(heroes.get(0).getHeroType().name()+"");//2,hero造型
            strForHero.add(heroes.get(0).getCurrentDirection().name());//3
            strForHero.add(heroes.get(0).getHp()+"");//更新血量//4
            strForHero.add(heroes.get(0).getAtk()+"");//更新攻擊力//5//
            strForHero.add(heroes.get(0).getDefense()+"");//更新防禦力//6
            strForHero.add(heroes.get(0).getHpLimitForInternet()+"");//7更新血量上限
            ClientClass.getInstance().sent(NetCommandCode.HERO_CONNECT,strForHero);
            ClientClass.getInstance().sent(NetCommandCode.HERO_MOVE,strForHero);
        }

        ClientClass.getInstance().consume((int serialNum, int commandCode, ArrayList<String> strs) -> {
            if(serialNum == heroes.get(0).getInternetID()){
                return;
            }
            switch(commandCode){
                case NetCommandCode.HERO_CONNECT:
                    if(otherStillAtGame==true){
                        return;
                    }
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
                case NetCommandCode.HERO_DISCONNECT:
                    otherStillAtGame=false;
                    break;
                case NetCommandCode.HERO_DISCONNECTFOREXIT:
                    for(int i=0;i<heroes.size();i++){
                        if(heroes.get(i).getInternetID()==serialNum){
                            heroes.remove(i);
                            heroSize--;
                            otherStillAtGame=false;
                            i--;
                        }
                    }
                    break;
                case NetCommandCode.HERO_MOVE:
                    if(otherStillAtGame==true){
                        return;
                    }
                    for (int i = 0; i <heroes.size(); i++) {
                        if (heroes.get(i).getInternetID()  == serialNum) {
                            heroes.get(i).setCurrentDirection(Direction.valueOf(strs.get(3)));
                            heroes.get(i).painter().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                            heroes.get(i).collider().setCenter(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)));
                            heroes.get(i).changeHero(Hero.HeroType.valueOf(strs.get(2)));
                            heroes.get(i).setHpForInterNet(Integer.parseInt(strs.get(4)));
                            heroes.get(i).setAtkForInternet(Integer.parseInt(strs.get(5)));
                            heroes.get(i).setDefenseForInternet(Integer.parseInt(strs.get(6)));
                            heroes.get(i).setHpLimitForInternet(Integer.parseInt(strs.get(7)));
                        }
                    }
                    break;
                case NetCommandCode.HERO_SCENECHANGE:
                    switch (strs.get(0)){
                        case "1":
                            SceneController.getInstance().change(new Role(heroes));
                            break;
                        case "3":
                            SceneController.getInstance().change(new SceneLevel1(heroes));
                            break;
                        case "4":
                            SceneController.getInstance().change(new SceneLevel2(heroes));
                            break;
                        case "5":
                            SceneController.getInstance().change(new SceneLevel3(heroes));
                            break;
                    }
                case NetCommandCode.HERO_MOVEFORINTERNET:
                    for(int i=0;i<heroes.size();i++){
                        if(heroes.get(i).getInternetID()==serialNum){
                            heroes.get(i).moveForInternet();
                        }
                    }
                    break;
            }
        });
    }

    private void randomStoreItem(){
        if(Store>2){
            Store=0;
        }
        switch (Store++){
            case 0:
                items.add(new Item(Item.ItemType.EMPTYHEART,384,128));
                weaponsForMap.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.WEAPON,448,128,Direction.UP));
                items.add(new Item(Item.ItemType.CHICKEN,512,128));
                break;
            case 1:
                items.add(new Item(Item.ItemType.CHICKEN,384,128));
                weaponsForMap.add(new Weapon(Weapon.WeaponType.WEAPON3, Weapon.State.WEAPON,448,128,Direction.UP));
                items.add(new Item(Item.ItemType.POWERUP,512,128));
                break;
            case 2:
                items.add(new Item(Item.ItemType.CHICKEN,384,128));
                weaponsForMap.add(new Weapon(Weapon.WeaponType.WEAPON2, Weapon.State.WEAPON,448,128,Direction.UP));
                items.add(new Item(Item.ItemType.EMPTYHEART,512,128));
                break;
        }

    }
    private void heroCheckItems() {
        for(int k=0;k<heroes.size();k++){
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).isCenterCollision(heroes.get(k))) {
                    if (items.get(i).getItemType() == Item.ItemType.DIAMOND
                            || items.get(i).getItemType() == Item.ItemType.COIN1||items.get(i).getItemType()== Item.ItemType.COIN) {
                        heroes.get(k).pickUpItem(items.get(i));
                        items.remove(i);
                        i--;
                        break;
                    }
                    if (heroes.get(k).getItems().size() >= 4 || heroes.get(k).getDiamond() < items.get(i).getDiamondPrice()) {
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


    @Override
    public void update() {
        connect();

        camera.update();
        heroes.forEach(hero -> hero.update());
        heroCheckItems();
        getWeapon();
        wallCheckRemove();
        //只有menu有這個方法
        cantNotTouchWall();
        doorCheckNextScene();
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
                if (isKeyPress) {
                    Direction direction = Global.Direction.getDirection(commandCode);
                    heroes.get(0).setCurrentDirection(direction);
                    heroes.get(0).move();
                    heroes.get(0).useItem(commandCode);
                    isKeyPress = false;
                    ArrayList<String>strings1=new ArrayList<>();
                    ClientClass.getInstance().sent(NetCommandCode.HERO_MOVEFORINTERNET,strings1);
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
    public enum Title implements GameKernel.PaintInterface {

        LEVEL1("LEVEL1", 164, 660, Color.WHITE),
        LEVEL3("LEVEL2", 418, 660, Color.WHITE),
        LEVEL4("LEVEL3", 676, 660, Color.WHITE);
        private String str;
        private Font font = new Font("Curlz MT", Font.BOLD, 40);
        private int x;
        private int y;
        private Color color;

        Title(String str, int x, int y, Color color) {
            this.str = str;
            this.x = x;
            this.y = y;
            this.color = color;
        }

        @Override
        public void paint(Graphics g) {
            g.setFont(font);
            g.setColor(color);
            g.drawString(str, x, y);

        }
    }

    public enum NextScene implements GameKernel.PaintInterface {

        RULE(128,128,64,64),
        MUD(192, 512, 64, 64),
        ROLE(448, 512, 64, 64),
        EXIT(704, 512, 64, 64),
        LEVEL1(192, 704, 64, 64),
        LEVEL3(448, 704, 64, 64),
        LEVEL4(704, 704, 64, 64);

        private ChangeDoor door;

        NextScene(int x, int y, int width, int height) {
            door = new ChangeDoor(x, y, width, height, ChangeDoor.DoorType.STAIRS);
        }

        @Override
        public void paint(Graphics g) {
            door.paint(g);
        }
    }
}


