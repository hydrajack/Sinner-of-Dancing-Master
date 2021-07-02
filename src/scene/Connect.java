package scene;

import client.ClientClass;
import controller.ImageController;
import controller.MapController;
import controller.SceneController;
import displayButton.BackgroundType;
import displayButton.EditText;
import displayButton.Buttons;
import displayButton.Style;
import displayButton.menu.MouseTriggerImpl;
import gameobj.ChangeDoor;
import gameobj.Floor;
import gameobj.Hero;
import gameobj.Wall;
import server.Server;
import utils.CommandSolver;
import utils.GameKernel;
import utils.GameWindow;
import utils.Global;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connect extends Scene {
    private ArrayList<Hero> heroes;
    private List<Floor> floors;
    private List<Wall> walls;
    private boolean isPress;
    private ArrayList<Buttons> buttons;
    private EditText editText;
    private NextScene[] nextScenes;
    private Font font;
    private String str;
    private Image host;

    public Connect(ArrayList<Hero>heroes){
        this.heroes=heroes;
    }

    @Override
    public void sceneBegin() {
        GameWindow.getInstance().setWindowSize(640,640);
        heroes.get(0).painter().setCenter(96,96);
        heroes.get(0).collider().setCenter(96,96);
        MapController mapController = new MapController(MapController.Key.MUD);
        floors = mapController.getFloors();
        walls = mapController.getWalls();
        buttons = new ArrayList<>();
        buttons.add(new Buttons(256, 128, Buttons.NameButton.SERVER));
        buttons.add(new Buttons(256, 320, Buttons.NameButton.CLIENT));
        editText = new EditText(256, 448, "IP:number");
        Style focus = new Style.StyleRect(192, 64, true,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/Buttons/SC.png")))
                .setTextColor(Color.WHITE)
                .setTextFont(new Font("Curlz MT", Font.BOLD, 40));
        editText.setStyleFocus(focus);
        editText.setStyleNormal(focus);
        editText.setEditLimit(12);
        editText.changeCursorIsLight();
        nextScenes = NextScene.values();
        this.str="MENU";
        this.font=new Font("Broadway", Font.BOLD, 25);
        host=ImageController.getInstance().tryGet("/c/18.png");
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        this.floors.forEach(a -> a.paint(g));
        this.walls.forEach(a -> a.paint(g));
        buttons.forEach(a -> a.paint(g));
        heroes.forEach(a -> a.paint(g));

        for (int i = 0; i < nextScenes.length; i++) {
            nextScenes[i].paint(g);
        }

        if (heroes.get(0).isCollision(buttons.get(1))) {
            editText.isFocus();
            editText.paint(g);
        }
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(str,60,510);
        g.drawImage(host,240,64,null);
    }

    @Override
    public void update() {
        checkButtons();
        wallCheckRemove();
        wallCheckActorReturn();
        doorCheckNextScene();
    }


    public void checkButtons() {
        for (int i = 0; i < buttons.size(); i++) {
            if (heroes.get(0).isCollision(buttons.get(i))) {
                buttons.get(i).getNameButton().changeIsTouch(true);
                buttons.get(i).getAnimation().getDelay().play();

            } else {
                buttons.get(i).getNameButton().changeIsTouch(false);
                buttons.get(i).getAnimation().getDelay().stop();
            }
        }
    }
    private void doorCheckNextScene() {
        for(int k=0;k<heroes.size();k++){
            for (int i = 0; i < nextScenes.length; i++) {
                if (nextScenes[i].stairs.isCenterCollision(heroes.get(k))) {
                    SceneController.getInstance().change(new MenuScene(heroes));
                }
            }
        }
    }
    private void wallCheckRemove() {
        for (int i = 0; i < walls.size(); i++) {
            if (walls.get(i).isCenterCollision(heroes.get(0)) && walls.get(i).isDestructible()) {
                walls.remove(i);
                i--;
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

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            MouseTriggerImpl.mouseTrig(editText, e, state);
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if (isPress) {
                    Global.Direction direction = Global.Direction.getDirection(commandCode);
                    heroes.get(0).setCurrentDirection(direction);
                    heroes.get(0).move();
                    heroes.get(0).update();
                    isPress = false;
                }

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                heroes.get(0).setCurrentDirection(Global.Direction.getDirection(4));
                isPress = true;
                if (commandCode == Global.ENTER) {
                    if (heroes.get(0).isCollision(buttons.get(0))) {
                        Global.isServer = true;
                        Server.instance().create(12345);
                        Server.instance().start();
                        Global.id=Server.instance().getLocalAddress()[0];
                        System.out.println(Global.id);
                        isPress = false;
                        try {
                            //連接本地Server端，host ip 直接使用"127.0.0.1",即可連至本地
                            ClientClass.getInstance().connect("127.0.0.1", 12345); //連接(host IP,port)
                            SceneController.getInstance().change(new MenuScene(heroes));
                        } catch (IOException ex) {
                            Logger.getLogger(MenuScene.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (heroes.get(0).isCollision(buttons.get(1))) {

                        Scanner sc = new Scanner(System.in);
                        try {
                            ClientClass.getInstance().connect(editText.getEditText(), 12345); // ("SERVER端IP", "SERVER端PORT")
                            Global.isClient=true;
                            SceneController.getInstance().change(new MenuScene(heroes));
                        } catch (IOException ex) {
                            Logger.getLogger(MenuScene.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }


            @Override
            public void keyTyped(char c, long trigTime) {
                System.out.println("keyTyped: "+c);
                editText.keyTyped(c);
            }
        };
    }
    public enum NextScene implements GameKernel.PaintInterface {
        //往level2的地方
        ONE(64, 512, 64, 64);

        private ChangeDoor stairs;

        NextScene(int x, int y, int width, int height) {
            stairs = new ChangeDoor(x, y, width, height, ChangeDoor.DoorType.STAIRS);
        }

        @Override
        public void paint(Graphics g) {
            stairs.paint(g);
        }
    }

}
