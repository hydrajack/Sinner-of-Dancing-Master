package utils;

import controller.ImageController;

import java.awt.*;

public class Animator {
    private static final int[] ACTOR_WALK = {0, 1, 2, 1};
    private Image image;
    private int count;
    private Delay delay;//為何delay變成屬性?因為要包括上一次的狀態
    private int arrowKeyPic;


    public Animator(String path) {//如果裡面要多張圖 需要參數來決定是哪種圖
        this.image = ImageController.getInstance().tryGet(path);
        this.delay = new Delay(20);
        this.delay.loop();
        this.count = 0;
    }

    public void paint(final Graphics g, Global.Direction direction, final int left, final int top, final int right, final int bottom) {
        //不繼承paintInterface的原因是因為方法長得不一樣
        if (this.delay.count()) {
            this.count = ++this.count % 4;
        }
        setArrowKeyPic(direction);
        g.drawImage(this.image, left, top, right, bottom,
                0 + ACTOR_WALK[this.count] * 32, 0+arrowKeyPic*32,
                0 + ACTOR_WALK[this.count] *32 + 32, 0 + 32+arrowKeyPic*32, null);

    }

    private void setArrowKeyPic(Global.Direction direction) {
        switch (direction){
            case UP:
                arrowKeyPic=3;
                break;
            case DOWN:
                arrowKeyPic=0;
                break;
            case LEFT:
                arrowKeyPic=1;
                break;
            case RIGHT:
                arrowKeyPic=2;
                break;

        }
    }
    public void changeImage(String path){
        this.image=ImageController.getInstance().tryGet(path);
    }


}
