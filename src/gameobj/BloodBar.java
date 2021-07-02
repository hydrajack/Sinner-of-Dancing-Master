package gameobj;

import controller.ImageController;

import java.awt.*;

public class BloodBar {
    private Image blood;
    private Image bloodLock;
    public BloodBar() {
        blood = ImageController.getInstance().tryGet("/heart_empty.png");
        bloodLock=ImageController.getInstance().tryGet("/HEARTLOCK.png");
    }

    public void paint(Graphics g, Enemy enemy) {
        for (int i = 0; i < enemy.getHp() / 10; i++) {

            g.drawImage(blood, enemy.painter().left()+i*30, enemy.painter().top() - 15, null);
        }
    }
    public void paint(Graphics g, Boss boss) {
        switch (boss.getBossState2()){
            case NORMAL:
                for (int i = 0; i < boss.getHp() / 10; i++) {
                    g.drawImage(blood, boss.painter().left()+i*30, boss.painter().top() - 15, null);
                }
                break;
            case LOCK:
                for (int i = 0; i < boss.getHp() / 10; i++) {
                    g.drawImage(bloodLock, boss.painter().left()+i*30, boss.painter().top() - 15, null);
                }
                break;
        }

    }
}
