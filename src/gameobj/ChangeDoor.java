package gameobj;

import controller.ImageController;

import java.awt.*;

public class ChangeDoor extends GameObject {
    public enum DoorType {
        MENU1(true),
        STAIRS(true),
        DOOR_1(true);
        private boolean isDestructible;

        DoorType(boolean isDestructible) {
            this.isDestructible = isDestructible;
        }
    }

    private DoorType doorType;
    private Image door;

    public ChangeDoor(int x, int y, int width, int height, DoorType doorType) {
        super(x, y, width, height);
        this.door = ImageController.getInstance().tryGet("/" + doorType.name() + ".png");
        this.doorType = doorType;
    }

    public boolean isDestructible() {
        return doorType.isDestructible;
    }

    public DoorType getWallType() {
        return doorType;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(this.door, painter().left(), painter().top(), painter().width(), painter().height(), null);
    }

    @Override
    public void update() {

    }
}
