package displayButton;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public abstract class BackgroundType {
    private static Graphics2D ovalSrcIn;
    private static BufferedImage imageSrcIn;

    public BackgroundType() {
        imageSrcIn = new BufferedImage(784, 561, 2);
        ovalSrcIn = imageSrcIn.createGraphics();
    }

    public abstract <T> T getBackground();

    public abstract void paintBackground(Graphics var1, boolean var2, boolean var3, int var4, int var5, int var6, int var7);

    public static class BackgroundImage extends BackgroundType {
        private Image img;

        public BackgroundImage(Image image) {
            this.img = image;
        }

        public Image getBackground() {
            return this.img;
        }

        public void paintBackground(Graphics g, boolean isOval, boolean isFill, int x, int y, int width, int height) {
            if (isOval) {
                BackgroundType.ovalSrcIn.fillOval(0, 0, width, height);
                BackgroundType.ovalSrcIn.setComposite(AlphaComposite.SrcIn);
                BackgroundType.ovalSrcIn.drawImage(this.img, 0, 0, width, width, (ImageObserver)null);
                g.drawImage(BackgroundType.imageSrcIn, x, y, (ImageObserver)null);
            } else {
                g.drawImage(this.img, x, y, width, height, (ImageObserver)null);
            }

        }
    }

    public static class BackgroundColor extends BackgroundType {
        private Color color;

        public BackgroundColor(Color color) {
            this.color = color;
        }

        public Color getBackground() {
            return this.color;
        }

        public void paintBackground(Graphics g, boolean isOval, boolean isFill, int x, int y, int width, int height) {
            g.setColor(this.color);
            if (isOval) {
                if (isFill) {
                    g.fillOval(x, y, width, height);
                } else {
                    g.drawOval(x, y, width, height);
                }
            } else if (isFill) {
                g.fillRect(x, y, width, height);
            } else {
                g.drawRect(x, y, width, height);
            }

        }
    }
}
