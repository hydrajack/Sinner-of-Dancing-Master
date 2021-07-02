package displayButton;

import controller.ImageController;
import gameobj.GameObject;
import gameobj.Hero;
import gameobj.Rect;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Buttons extends GameObject {
    private Animation animation;
    private NameButton nameButton;


    public Buttons(int x, int y, NameButton nameButton) {
        super(x, y, nameButton.width, nameButton.height);
        this.nameButton = nameButton;
        this.animation = new Animation();
    }

    public NameButton getNameButton() {
        return nameButton;
    }

    public Animation getAnimation() {
        return animation;
    }


    @Override
    public void paintComponent(Graphics g) {
        this.animation.paint(g, painter().left(), painter().top(),
                painter().right(), painter().bottom());
    }

    @Override
    public void update() {

    }

    public enum NameButton {
        WIN(65,64,"WIN"),
        SERVER(165,60,"SERVER"),
        CLIENT(153,60,"CLIENT"),
        MENU(110,50,"MENU"),
        ALLEN(55, 30, "ALLEN"),
        BELLA(55,30,"BELLA"),
        CAROL(55,30,"CAROL"),
        JASON(55,30,"JASON");


        private Image img;
        public int width;
        public int height;
        private boolean isTouch;
        NameButton(int width, int height, String path) {
            this.img = ImageController.getInstance().tryGet("/Buttons/" + path + ".png");
            this.isTouch = false;
            this.width = width;
            this.height = height;
        }

        public void changeIsTouch(boolean isTouch) {
            this.isTouch = isTouch;
        }
        public boolean getIsTouch(){
            return isTouch;
        }

    }

    public class Animation {//內部類
        private final int[] ACTOR_ARR = {1, 0};
        private int count;
        private Delay delay;
        private Delay once;
        //private NameButton nameButton;

        public Animation() {
            this.delay = new Delay(30);
            //this.delay.loop();
            this.count = 0;
            this.once=new Delay(120);
            once.pause();

        }

        public void paint(final Graphics g, final int left, final int top, final int right, final int bottom) {
            if(once.count() || once.isPause()) {
                if (this.delay.count()) {
                    this.count = ++this.count % 2;
                }
                g.drawImage(nameButton.img, left, top, right, bottom,
                        ACTOR_ARR[this.count] * nameButton.width,
                        0,
                        nameButton.width + ACTOR_ARR[this.count] * nameButton.width,
                        nameButton.height,
                        null);
            }
        }

        public Delay getDelay() {
            return delay;
        }

        public Delay getOnce(){
            return once;
        }

        public void setOnce(){
            once.play();
        }

    }


}
