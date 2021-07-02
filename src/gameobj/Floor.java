package gameobj;

import controller.ImageController;
import utils.Delay;

import java.awt.*;

public class Floor extends UnCollisionObject {//地板
    public enum FloorType {
        Z01,
        Z02,
        Z11,
        Z12,
        INT1,
        ROCK1,
        STAGE,
        FLOOR6,
        GRAY,
        GREEN,
        BRICK,
        ROCK;
    }
    private FloorType floorType;
    private Image floor;
    private Image floorReverse;
    private Delay delay;

    public Floor(FloorType floorType,final int x, final int y, final int width, final int height) {
        super(x, y, width, height);
        this.floor = ImageController.getInstance().tryGet("/floor/"+ floorType.name() + ".png");
        this.floorReverse = ImageController.getInstance().tryGet("/floor/"+ floorType.name() + "_R.png");
        this.floorType = floorType;
        delay = new Delay(28);
        delay.loop();
    }

    @Override
    public void paintComponent(Graphics g) {
        if(delay.count() && floorReverse != null) {//temp
            Image img = floor;
            floor = floorReverse;
            floorReverse = img;
        }
        g.drawImage(this.floor, painter().left(), painter().top(), painter().width(), painter().height(), null);
    }

    @Override
    public void update() {
    }

}
