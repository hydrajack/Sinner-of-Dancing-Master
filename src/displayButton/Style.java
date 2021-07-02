package displayButton;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public abstract class Style {
    protected String text = "";
    protected Color textColor;
    protected Font textFont;
    protected boolean isHaveBorder = false;
    protected Color borderColor;
    protected int borderThickness;
    protected BackgroundType objectBackground;
    protected int height;
    protected int width;

    public abstract void paintComponent(Graphics var1, int var2, int var3);

    public Style() {
        this.textColor = Color.white;
        this.borderColor = Color.black;
        this.borderThickness = 1;
        this.textFont = new Font("TimesRoman", 2, 50);
    }

    public Style setText(String text) {
        this.text = text;
        return this;
    }

    public int getStyleWidth() {
        return this.width;
    }

    public int getStyleHeight() {
        return this.height;
    }

    public Style setTextColor(Color textColor) {
        this.textColor = textColor;
        return this;
    }

    public Style setTextFont(Font textFont) {
        this.textFont = textFont;
        return this;
    }

    public Style setHaveBorder(boolean isHaveBorder) {
        this.isHaveBorder = isHaveBorder;
        return this;
    }

    public Style setBorderColor(Color BorderColor) {
        this.borderColor = BorderColor;
        return this;
    }

    public Style setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public Color getTextColor() {
        return this.textColor;
    }

    public Font getTextFont() {
        return this.textFont;
    }

    public boolean isHaveBorder() {
        return this.isHaveBorder;
    }

    public static class StyleOval extends Style.StyleRect {
        public StyleOval(int width, int height, boolean isFill, BackgroundType backgroundType) {
            super(width, height, isFill, backgroundType);
        }

        public StyleOval(int width, int height, BackgroundType backgroundType) {
            super(width, height, true, backgroundType);
        }

        public void paintComponent(Graphics g, int x, int y) {
            if (super.isHaveBorder()) {
                g.setColor(super.getBorderColor());
                g.fillOval(x - super.getBorderThickness(), y - super.getBorderThickness(), super.getWidth() + super.getBorderThickness() * 2, super.getHeight() + super.getBorderThickness() * 2);
            }

            this.objectBackground.paintBackground(g, true, this.isFill, x, y, this.width, this.height);
            g.setFont(this.textFont);
            g.setColor(super.getTextColor());
            int stringWidth = g.getFontMetrics().stringWidth(super.text);
            g.drawString(super.text, x + (this.width - stringWidth) / 2, y + this.height / 2 + g.getFontMetrics().getDescent());
        }
    }

    public static class StyleRect extends Style {
        protected boolean isFill;

        public StyleRect(int width, int height, boolean isFill, BackgroundType background) {
            this.isFill = isFill;
            this.height = height;
            this.width = width;
            this.objectBackground = background;
        }

        public StyleRect(int width, int height, BackgroundType background) {
            this(width, height, true, background);
        }

        public void paintComponent(Graphics g, int x, int y) {
            if (super.isHaveBorder) {
                g.setColor(super.borderColor);
                g.fillRect(x - super.borderThickness, y - super.borderThickness, this.width + super.borderThickness * 2, this.height + super.borderThickness * 2);
            }

            this.objectBackground.paintBackground(g, false, this.isFill, x, y, this.width, this.height);
            if (super.text != null) {
                g.setColor(super.textColor);
                g.setFont(super.textFont);
                int stringWidth = g.getFontMetrics().stringWidth(super.text);
                g.drawString(super.text, x + (this.width - stringWidth) / 2, y + this.height / 2 + g.getFontMetrics().getDescent());
            }

        }

        public boolean isHaveBorder() {
            return super.isHaveBorder;
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        public Color getBorderColor() {
            return super.borderColor;
        }

        public Color getObjectColor() {
            return (Color)this.objectBackground.getBackground();
        }

        public int getBorderThickness() {
            return super.borderThickness;
        }
    }
}

