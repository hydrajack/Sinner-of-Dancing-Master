package gameobj;

import java.awt.*;

public  abstract class UnCollisionObject extends GameObject {
    public UnCollisionObject(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void paintComponent(Graphics g) {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean isCenterCollision(GameObject object){
        return false;
    }

}