package gameobj;


import utils.Global;

//物件長方形
public class Rect {

    private int left;
    private int top;
    private int right;
    private int bottom;
    private int originalLeft;
    private int originalTop;
    private int originalRight;
    private int originalBottom;

    public Rect(int left, int top, int right, int bottom) {
        this.left = left;
        originalLeft=left;
        this.top = top;
        originalTop=top;
        this.right = right;
        originalRight=right;
        this.bottom = bottom;
        originalBottom=bottom;
    }

    public Rect(Rect rect) {
        this.left = rect.left;
        this.top = rect.top;
        this.right = rect.right;
        this.bottom = rect.bottom;
    }

    public static Rect genWithCenter(int x, int y, int width, int height) {
        int left = x ;
        int right = left + width;
        int top = y ;
        int bottom = top + height;
        return new Rect(left, top, right, bottom);
    }

   public  boolean overlapWithCenter(Rect b){
        if(centerX()==b.centerX() && centerY() == b.centerY()){
            return  true;
        }
        return  false;
   }
   public boolean overlapWithCenter(Rect b,int num){
        switch (num){
            case 4:
                    if((centerX()-Global.CENTER_WIDTH/2 == b.centerX() && centerY()-Global.CENTER_HEIGHT/2 == b.centerY())||
                            (centerX()+Global.CENTER_WIDTH/2==b.centerX() && centerY()-Global.CENTER_HEIGHT/2==b.centerY())||
                            (centerX()-Global.CENTER_WIDTH/2==b.centerX()&& centerY()+Global.CENTER_HEIGHT/2 == b.centerY())||
                            (centerX()+Global.CENTER_WIDTH/2==b.centerX()&& centerY()+Global.CENTER_HEIGHT/2==b.centerY())){
                        return true;
                    }
            case 9:
                if((centerX()==b.centerX() && centerY()==b.centerY())||(centerX()-64==b.centerX() && centerY()-64==b.centerY())
                ||(centerX()==b.centerX() && centerY()-64==b.centerY())||(centerX()+64==b.centerX()&&centerY()-64==b.centerY())

                ||(centerX()-64==b.centerX() && centerY()==b.centerY())||(centerX()+64==b.centerX() && centerY()==b.centerY()

                ||(centerX()-64==b.centerX() && centerY()+64==b.centerY())||(centerX()==b.centerX()&&centerY()+64==b.centerY())||(centerX()+64==b.centerX()&&centerY()+64==b.centerY()))){
                    return  true;
            }

        }
        return false;
   }

    //碰撞判斷 傳參數
    public final boolean overlap(int left, int top, int right, int bottom) {
        if (this.left() > right) {
            return false;
        }
        if (this.right() < left) {
            return false;
        }
        if (this.top() > bottom) {
            return false;
        }
        if (this.bottom() < top) {
            return false;
        }
        return true;
    }

    //碰撞判斷 傳長方形
    public final boolean overlap(Rect b) {
        return overlap(b.left, b.top, b.right, b.bottom);
    }

    //  圖形的ｘ中心
    public int centerX() {
        return (left + right) / 2;
    }

    //  圖形的y中心
    public int centerY() {
        return (top + bottom) / 2;
    }

    //浮點數版本
    public float exactCenterX() {
        return (left + right) / 2f;
    }

    //浮點數版本
    public float exactCenterY() {
        return (top + bottom) / 2f;
    }

    //設定中心
    public final void setCenter(int x, int y) {
        translate(x - centerX(), y - centerY());
    }

    public final Rect translate(int dx, int dy) {
        this.left += dx;
        this.right += dx;
        this.top += dy;
        this.bottom += dy;
        return this;
    }

    public final Rect translateX(int dx) {
        this.left += dx;
        this.right += dx;
        return this;
    }

    public final Rect translateY(int dy) {
        this.top += dy;
        this.bottom += dy;
        return this;
    }

    public int left() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int top() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int right() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int bottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int width() {
        return this.right - this.left;
    }

    public int height() {
        return this.bottom - this.top;
    }

    public boolean compareToTopAndLeft( ){
        return (originalLeft-Global.CENTER_WIDTH*2<left && originalLeft +Global.CENTER_HEIGHT*2>left)
                &&
                (originalTop-Global.CENTER_HEIGHT*2<top && originalTop+Global.CENTER_HEIGHT*2>top);
    }

}
