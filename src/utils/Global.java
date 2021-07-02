/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 * @author user1
 */
public class Global {

    //  視野大小

    public static final int CAMERA_WIDTH = 576;
    public static final int CAMERA_HEIGHT = 576;


    //物件大小
    public static final int CENTER_WIDTH = 64;
    public static final int CENTER_HEIGHT = 64;
    public static final int MAP_SIZE = 64;

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NO,
        STOP;

        public static Direction getDirection(int value) {
            if (value >= Direction.values().length || value < 0) {
                return Direction.NO;
            }
            return Direction.values()[value];
        }
    }

    public static final boolean IS_DEBUG =false;

    public static void log(String str) {
        if (IS_DEBUG) {
            System.out.println(str);
        }
    }


    // 資料刷新時間
    public static final int UPDATE_TIMES_PER_SEC = 60;// 每秒更新60次遊戲邏輯
    public static final int NANOSECOND_PER_UPDATE = 1000000000 / UPDATE_TIMES_PER_SEC;// 每一次要花費的奈秒數
    // 畫面更新時間
    public static final int FRAME_LIMIT = 60;
    public static final int LIMIT_DELTA_TIME = 1000000000 / FRAME_LIMIT;


    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int ENTER = 4;
    public static final int SPACE = 5;
    public static final int A = 6;
    public static final int D = 7;
    public static final int SINGLE_MODE = 10;
    public static final int CONNECT_NET_MODE = 9;
    public static  int Store=0;


    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static boolean random(int rate) {
        return random(1, 100) <= rate;
    }

    //網路
    public static boolean isServer = false;
    public static boolean isClient = false;
    public static  boolean otherStillAtGame=false;
    public static boolean  localStillAtGame=false;
    public static String id;


    public static class NetCommandCode {
        public static final int HERO_CONNECT = 0;
        public static final int HERO_MOVE = 1;
        public static final int HERO_DISCONNECT = 2;
        public static final int HERO_SCENECHANGE = 3;
        public static final int HERO_WEAPON = 4;
        public static final int HERO_WALLREMOVE = 5;
        public static final int HERO_ENEMYREMOVE = 6;
        public static final int HERO_COMBO =7;
        private static final int ADDCOIN=8;
        public static final int HERO_BOSSREMOVE=9;
        public static final int HERO_MOVEFORINTERNET=10;
        public static  final int HERO_DISCONNECTFOREXIT=11;
        public static  final int BOSS_UPDATE=12;
        public static final int BOSS_FINALBOSS=13;
        public static final int STORE_RANDOM=14;



    }

}
