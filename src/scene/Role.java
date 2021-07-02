package scene;

import camera.Camera;
import camera.MapInformation;
import controller.MapController;
import controller.SceneController;
import displayButton.Buttons;
import gameobj.ChangeDoor;
import gameobj.Floor;
import gameobj.Hero;
import gameobj.Wall;
import utils.CommandSolver;
import utils.GameKernel;
import utils.GameWindow;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class Role extends Scene {
    private ArrayList<Hero> heroes;
    private ArrayList<Hero> fourHeroes;
    // private ArrayList<Camera> camArr; 多顆鏡頭管理 單顆可不用陣列
    private Camera camera;
    // private SmallMap smallMap;
    private java.util.List<Floor> floors;
    private java.util.List<Wall> walls;
    private NextScene[] nextScenes;
    private boolean isKeyPress;
    //button
    private ArrayList<Buttons> roleButtons;
    private Hero currentChoose;

    private Font font;
    private String str1;
    private String str2;

    public Role(ArrayList heroes){
        this.heroes=heroes;
    }

    @Override
    public void sceneBegin() {

        GameWindow.getInstance().setWindowSize(704,704);
        fourHeroes=new ArrayList<>();
        heroes.get(0).collider().setCenter(96,96);
        heroes.get(0).painter().setCenter(96,96);
        fourHeroes.add(new Hero(128,256, Hero.HeroType.WARRIOR));
        fourHeroes.add(new Hero(256,256, Hero.HeroType.CAT));
        fourHeroes.add(new Hero(384,256, Hero.HeroType.SCIENCE));
        fourHeroes.add(new Hero(512,256, Hero.HeroType.WIZARD));

        nextScenes = NextScene.values();
        MapController mapController = new MapController(MapController.Key.ROLE);
        floors = mapController.getFloors();
        walls = mapController.getWalls();

        //button
        roleButtons=new ArrayList<>();
        roleButtons.add(new Buttons(132, 330, Buttons.NameButton.ALLEN));
        roleButtons.add(new Buttons(260,330, Buttons.NameButton.BELLA));
        roleButtons.add(new Buttons(388,330, Buttons.NameButton.CAROL));
        roleButtons.add(new Buttons(516,330, Buttons.NameButton.JASON));
        //roleButtons.add(new RoleButtons(300,525, RoleButtons.NameButton.MENU));
        //MapInformation.setMapInfo必須先設定
        MapInformation.setMapInfo(0, 0, GameWindow.getInstance().getWindowWidth(), GameWindow.getInstance().getWindowHeight());
        this.camera = new Camera.Builder(768, 768).setChaseObj(heroes.get(0))
                .setCameraStartLocation(0, 0)
                .gen();
        this.str1="CLOSE  THE  ROLE,";
        this.str2="PRESS  THE  ENTER  CHOOSE  WHO  YOU  WANT";
        this.font=new Font("Broadway", Font.BOLD, 20);
    }

    @Override
    public void paint(Graphics g) {
        //開鏡頭
        camera.start(g);
        // 繪製地圖
        this.floors.forEach(a -> a.paint(g));
        this.walls.forEach(a -> a.paint(g));
        // MENU
        for (int i = 0; i < nextScenes.length; i++) {
            nextScenes[i].paint(g);
        }
        //
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(str1,250,180);
        g.drawString(str2,102,210);

        roleButtons.forEach(a -> a.paint(g));
        //角色登場
        heroes.get(0).paint(g);
        fourHeroes.forEach(a -> a.paint(g));
        camera.paint(g);
        camera.end(g);
    }

    @Override
    public void update() {
        camera.update();
        wallCheckActorReturn();
        checkButtons();
        doorCheckNextScene();
    }

    @Override
    public void sceneEnd() {

    }

    public void checkButtons(){
        for(int i=0;i<fourHeroes.size();i++) {
            if (heroes.get(0).isCenterCollision(fourHeroes.get(i))){
                roleButtons.get(i).getNameButton().changeIsTouch(true);
                roleButtons.get(i).getAnimation().getDelay().play();
                currentChoose = fourHeroes.get(i);

            }else {
                roleButtons.get(i).getNameButton().changeIsTouch(false);
                roleButtons.get(i).getAnimation().getDelay().stop();
            }
        }
    }




    private void wallCheckActorReturn() {
        for (int i = 0; i < walls.size(); i++) {
            if (walls.get(i).isCenterCollision(heroes.get(0)) && !walls.get(i).isDestructible()) {
                heroes.get(0).isCollision();
            }
        }
    }

        private void doorCheckNextScene(){
        for(int i=0;i<nextScenes.length;i++){
            if(nextScenes[i].door.isCenterCollision(heroes.get(0))){
                SceneController.getInstance().change(new MenuScene(heroes));
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
                if (isKeyPress) {
                    Global.Direction direction = Global.Direction.getDirection(commandCode);
                    heroes.get(0).setCurrentDirection(direction);
                    heroes.get(0).move();
                    heroes.get(0).update();
                    heroes.get(0).useItem(commandCode);
                    isKeyPress = false;
                }
                camera.keyPressed(commandCode, trigTime);
            }

            @Override
            //放開鍵盤
            public void keyReleased(int commandCode, long trigTime) {
                heroes.get(0).setCurrentDirection(Global.Direction.getDirection(4));
                isKeyPress = true;
                if(commandCode == Global.ENTER && currentChoose != null){
                    heroes.get(0).changeHero(currentChoose.getHeroType());
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {
            }
        };
    }
    public enum NextScene implements GameKernel.PaintInterface {
        //往menu的地方
        ONE(294,524,116,40);

        private ChangeDoor door;

        NextScene( int x, int y, int width, int height) {
            door=new ChangeDoor(x,y,width,height,ChangeDoor.DoorType.MENU1);
        }

        @Override
        public void paint(Graphics g) {
            door.paint(g);
        }
    }

}
