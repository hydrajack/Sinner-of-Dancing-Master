package displayButton;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
import utils.GameKernel;

import java.awt.Graphics;

public class Label implements GameKernel.PaintInterface,GameKernel.UpdateInterface {
    private Label.ClickedAction clickAction;
    private int x;
    private int y;
    private Style styleNormal;
    private Style styleHover;
    private Style styleFocus;
    private boolean isFocus;
    private boolean isHover;

    public void setClickedActionPerformed(Label.ClickedAction a) {
        this.clickAction = a;
    }

    public void clickedActionPerformed() {
        this.clickAction.clickedActionPerformed(this.getX(), this.getY());
    }

    public Label.ClickedAction getClickedAction() {
        return this.clickAction;
    }

    public Label(int x, int y, Style styleNormal) {
        this.x = x;
        this.y = y;
        this.styleFocus = this.styleHover = this.styleNormal = styleNormal;
        this.isFocus = false;
    }

    public Label(int x, int y, Theme theme) {
        this.x = x;
        this.y = y;
        this.styleFocus = theme.focus();
        this.styleHover = theme.hover();
        this.styleNormal = theme.normal();
        this.isFocus = false;
    }

    public Label(int x, int y) {
        this.x = x;
        this.y = y;
        this.styleFocus = Theme.DEFAULT_THEME.focus();
        this.styleHover = Theme.DEFAULT_THEME.hover();
        this.styleNormal = Theme.DEFAULT_THEME.normal();
        this.isFocus = false;
    }

    public void setStyleFocus(Style styleFocus) {
        this.styleFocus = styleFocus;
    }

    public void setStyleHover(Style styleHover) {
        this.styleHover = styleHover;
    }

    public void setStyleNormal(Style styleNormal) {
        this.styleNormal = styleNormal;
    }

    public void isFocus() {
        this.isFocus = true;
    }

    public void unFocus() {
        this.isFocus = false;
    }

    public boolean getIsFocus() {
        return this.isFocus;
    }

    public void isHover() {
        this.isHover = true;
    }

    public void unHover() {
        this.isHover = false;
    }

    public Style getPaintStyle() {
        if (this.isFocus && this.styleFocus != null) {
            return this.styleFocus;
        } else {
            return this.isHover && this.styleHover != null ? this.styleHover : this.styleNormal;
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int width() {
        return this.getPaintStyle().getStyleWidth();
    }

    public int height() {
        return this.getPaintStyle().getStyleHeight();
    }

    public int left() {
        return this.getX();
    }

    public int right() {
        return this.getX() + this.width();
    }

    public int top() {
        return this.getY();
    }

    public int bottom() {
        return this.getY() + this.height();
    }

    public boolean getIsHover() {
        return this.isHover;
    }

    public void paint(Graphics g) {
        if (this.getPaintStyle() != null) {
            this.getPaintStyle().paintComponent(g, this.x, this.y);
        }

    }

    public void update() {
    }

    public interface ClickedAction {
        void clickedActionPerformed(int var1, int var2);
    }
}

