package displayButton;

import controller.ImageController;
import gameobj.GameObject;
import utils.Delay;

import java.awt.*;

public class StartButton extends GameObject {
    private Animation animation;

    public StartButton(int x, int y) {
        super(x, y, 244, 96);
        this.animation=new Animation();
    }

    @Override
    public void paintComponent(Graphics g) {
        this.animation.paint(g, painter().left(), painter().top(), painter().right(), painter().bottom());
    }

    @Override
    public void update() {

    }
    public static class Animation {//內部類
        private static final int[] ACTOR_ARR = {1,0};
        private int count;
        private Delay delay;
        private Image img;

        public Animation() {
            this.delay = new Delay(30);
            this.delay.loop();
            this.count = 0;
            this.img= ImageController.getInstance().tryGet("/Buttons/START.png");

        }

        public void paint(final Graphics g, final int left, final int top, final int right, final int bottom) {
            if (this.delay.count()) {
                this.count = ++this.count % 2;
            }
            g.drawImage(this.img, left, top, right, bottom,
                    ACTOR_ARR[this.count] * 244,
                    0,
                    244 + ACTOR_ARR[this.count] * 244,
                    96,
                    null);
        }
    }
}
