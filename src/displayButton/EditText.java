package displayButton;

import utils.Delay;

import java.awt.*;

public class EditText extends Label {
    private String defaultText;
    private Color defaultTextColor;
    private boolean isEditable;
    private Delay cursorSpeed;
    private Color cursorColor;
    private boolean isCursorLight;
    private int cursorWidth;
    private int cursorHeight;
    private int editLimit;
    private String editText;

    private void init(String hint) {
        this.isEditable = true;
        this.defaultText = hint;
        this.defaultTextColor = Color.LIGHT_GRAY;
        this.cursorWidth = 3;
        this.cursorHeight = 15;
        this.cursorColor = Color.BLACK;
        this.cursorSpeed = new Delay(30);
        this.cursorSpeed.loop();
        this.isCursorLight = false;
        this.editLimit = -1;
        this.editText = "";
    }

    public EditText(int x, int y, String hint, Style styleRect) {
        super(x, y, styleRect);
        this.init(hint);
    }

    public EditText(int x, int y, String hint, Theme theme) {
        super(x, y, theme);
        this.init(hint);
    }

    public EditText(int x, int y, String hint) {
        super(x, y);
        this.init(hint);
    }

    public void changeCursorIsLight() {
        this.isCursorLight = !this.isCursorLight;
    }

    public void setCursorSize(int width) {
        this.cursorWidth = width;
    }

    public void setCursorSpeed(Delay cursorSpeed) {
        this.cursorSpeed = cursorSpeed;
    }

    public void setCursorColor(Color cursorColor) {
        this.cursorColor = cursorColor;
    }

    public String getDefaultText() {
        return this.defaultText;
    }

    public void setDefaultText(String hint) {
        this.defaultText = hint;
    }

    public void isFocus() {
        super.isFocus();
    }

    public void unlockEdit() {
        this.isEditable = true;
    }

    public void lockEdit() {
        this.isEditable = false;
    }

    public void setEditLimit(int n) {
        this.editLimit = n;
    }

    private boolean isOverEditLimit() {
        return this.editLimit != -1 && this.editText.length() >= this.editLimit;
    }

    private boolean isExcepion(char c) {
        return c >= ')' && c <= '@' || c >= '[' && c <= '_' || c >= '|' && c < '~';
    }

    private boolean isLegalRange(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private boolean isNumberpad(char c) {
        return c >= '`' && c <= 'o';
    }

    private int keyNumbpadToNum(char c) {
        if (c >= '`' && c <= 'i') {
            return c - 48;
        } else {
            switch (c) {
                case 'j':
                    return 42;
                case 'k':
                    return 43;
                case 'l':
                    return 13;
                case 'm':
                    return 45;
                case 'n':
                    return 46;
                case 'o':
                    return 47;
                default:
                    return -1;
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(this.getPaintStyle().getTextFont());
        g.setColor(this.editText.length() == 0 ? this.defaultTextColor : this.getPaintStyle().getTextColor());
        g.drawString(this.editText.length() == 0 && this.getIsFocus() ? this.defaultText : this.editText, super.getX(), super.getY() + super.height() / 2 + g.getFontMetrics().getDescent());
        if (super.getIsFocus() && this.isEditable) {
            if (this.cursorSpeed.count()) {
                this.changeCursorIsLight();
            }

            if (this.isCursorLight) {
                g.setColor(this.cursorColor);
                int stringWidth = g.getFontMetrics().stringWidth(this.editText);
                this.cursorHeight = g.getFontMetrics().getAscent();
                g.fillRect(super.getX() + 1 + stringWidth, super.getY() + super.height() / 2 - (this.cursorHeight + g.getFontMetrics().getDescent()) / 2, this.cursorWidth, this.cursorHeight);
            }
        }

    }

    public String getEditText() {
        return this.editText;
    }

    public void update() {
    }

    public void keyTyped(char c) {
        System.out.println("char1 " + c);
        if (this.getIsFocus() && this.isEditable) {
            if (c == '\n') {
                super.unFocus();
            }

            if (!this.isOverEditLimit() && this.isExcepion(c)) {
                this.editText = this.editText + c;
                System.out.println("char2 " + c);
            } else if (!this.isOverEditLimit() && this.isNumberpad(c)) {
                String var10001 = this.editText;
                this.editText = var10001 + (char) this.keyNumbpadToNum(c);
            } else if (c == '\b') {
                if (this.editText.length() > 0) {
                    this.editText = this.editText.substring(0, this.editText.length() - 1);
                }
            } else if (!this.isOverEditLimit() && this.isLegalRange(c) && Toolkit.getDefaultToolkit().getLockingKeyState(20)) {
                this.editText = this.editText + c;
                System.out.println("char3 " + c);
            } else if (!this.isOverEditLimit() && c >= 'A' && c <= 'Z') {
                this.editText = this.editText + (char) (c + 32);
                System.out.println("char4 " + c);
            }
        }

    }
}
