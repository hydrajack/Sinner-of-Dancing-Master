package gameobj;

import controller.ImageController;
import item.Item;
import utils.Global;
import weapon.Weapon;

import java.awt.*;
import java.util.ArrayList;

public class GUI {
    public enum DemonstrateType {
        FRAME2(50, 50),
        GOLD(50, 50),
        DIAMOND1(50, 50),
        HEART(50, 50);

        private int width;
        private int height;

        DemonstrateType(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public Image getImg() {
            return ImageController.getInstance().tryGet("/item/" + this.name() + ".png");
        }
    }

    private int left;
    private int top;
    private int right;
    private int bottom;
    private Font font;
    private Font fontForStore;
    private ArrayList<String> itemKeys= new ArrayList<>();
    public GUI(int left, int top, int right, int bottom) {
        font = new Font("Curlz MT", Font.BOLD, 30);
        fontForStore=new Font("Curlz MT", Font.BOLD, 20);
        itemKeys.add("Q");
        itemKeys.add("W");
        itemKeys.add("E");
        itemKeys.add("R");
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void paintHeroInfo(Graphics g, Hero hero) {
        // diamond
        g.drawImage(DemonstrateType.DIAMOND1.getImg(), right - 100, top + 55
                , DemonstrateType.DIAMOND1.width, DemonstrateType.DIAMOND1.height, null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(hero.getDiamond()), right - 40, top + 90);
        g.drawImage(DemonstrateType.GOLD.getImg(), right - 100, top + 102,
                DemonstrateType.GOLD.width, DemonstrateType.GOLD.height, null);
        g.drawString(String.valueOf(hero.getCoins()), right - 40, top + 140);
        int hp = hero.getHp();
        int n = 0;
        while (hp > 0) {
            g.drawImage(DemonstrateType.HEART.getImg(), right - 360 + n * 50, top + 20,
                    right - 360 + n * 50 + (Math.min(hp, 10)) * 5, top + 20 + DemonstrateType.HEART.height,
                    0, 0, (int) ((Math.min(hp, 10)) * 2.5), 24, null);
            hp -= 10;
            n++;
        }

        for (int i = 0; i < hero.getItems().size(); i++) {
            g.drawImage(DemonstrateType.FRAME2.getImg(), left + 5, top + 15 + i * 60,
                    60, 60, null);
            g.drawImage(hero.getItems().get(i).getImage(), left + 10, top + 20 + i * 60,
                    50, 50, null);
            g.drawString(itemKeys.get(i),left+65,top + 60 + i * 60);
        }
        g.drawImage(hero.getWeapon().getWeaponImg(),left+5+88,top+20, 50,50,null);
        g.drawImage(DemonstrateType.FRAME2.getImg(),left+88,top+15,60,60,null);
        g.drawString("ATK",left+152,top+15+32);

    }
    public void paintStoreItemForCoin(Graphics g, ArrayList<Item>items){
        g.setFont(fontForStore);
        g.setColor(Color.WHITE);
        for(int i=0;i<items.size();i++){
            if(items.get(i).isInStore()&&items.get(i).getItemType()!= Item.ItemType.OLDMAN){
               g.drawString("$"+items.get(i).getCoinPrice()+"",items.get(i).collider().left(),items.get(i).collider().top());
                g.drawString(items.get(i).getDetail(),items.get(i).collider().left(),items.get(i).collider().top()+64);
            }
        }
    }
    public void paintStoreWeaponForDiamond(Graphics g, ArrayList<Weapon>weapons){
        g.setFont(fontForStore);
        g.setColor(Color.WHITE);
        for(int i=0;i<weapons.size();i++){
            if(weapons.get(i).isInStore()){
                g.drawImage(DemonstrateType.DIAMOND1.getImg(),weapons.get(i).collider().left(),weapons.get(i).collider().top()-15,15,15,null);
                g.drawString( weapons.get(i).getDiamondPrice()+"",weapons.get(i).collider().left()+17,weapons.get(i).collider().top());

            }
        }
    }
    public void paintStoreItemForDiamond(Graphics g, ArrayList<Item>items){
        g.setFont(fontForStore);
        g.setColor(Color.WHITE);
        for(int i=0;i<items.size();i++){
            if(items.get(i).isInStore()&&items.get(i).getItemType()!= Item.ItemType.OLDMAN){
                g.drawImage(DemonstrateType.DIAMOND1.getImg(),items.get(i).collider().left(),items.get(i).collider().top()-15,15,15,null);
                g.drawString(items.get(i).getDiamondPrice()+"",items.get(i).collider().left()+17,items.get(i).collider().top());
                g.drawString(items.get(i).getDetail(),items.get(i).collider().left()-5,items.get(i).collider().top()+90);
            }
        }
    }

}
