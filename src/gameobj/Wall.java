package gameobj;

import controller.ImageController;

import java.awt.*;

public class Wall extends GameObject {

    public enum WallType {
        W4(true,100),
        WALL_4(true,100),
        DOOR_3(true,100),
        W1(true,100),
        W2(true,100),
        ACO(false,100),
        W3(false,1),
        WALL_2(false,1),
        BLUE(false,1),
        STONE(false, 1);

        private boolean isDestructible;
        private int renderPriority;

        WallType(boolean isDestructible, int renderPriority) {
            this.isDestructible = isDestructible;
            this.renderPriority = renderPriority;
        }

        public int compare(WallType wallType){
            return this.renderPriority - wallType.renderPriority;
        }
    }

    private WallType wallType;
    private Image wall;

    public Wall(WallType wallType, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.wall = ImageController.getInstance().tryGet("/wall/" + wallType.name() + ".png");
        this.wallType = wallType;
    }

    public boolean isDestructible(){
        return wallType.isDestructible;
    }

    public WallType getWallType(){
        return wallType;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(this.wall, painter().left(), painter().top(), painter().width(), painter().height(), null);
    }

    @Override
    public void update() {

    }

}
