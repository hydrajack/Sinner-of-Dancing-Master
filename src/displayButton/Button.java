package displayButton;

import java.awt.*;

public class Button extends Label {
    public Button(int x, int y, Style style) {
        super(x, y, style);
    }

    public Button(int x, int y, Theme theme) {
        super(x, y, theme);
    }

    public Button(int x, int y) {
        super(x, y);
    }

    public void paint(Graphics g) {
        if (super.getPaintStyle() != null) {
            super.getPaintStyle().paintComponent(g, super.getX(), super.getY());
        }

    }

    public void update() {
    }
}

