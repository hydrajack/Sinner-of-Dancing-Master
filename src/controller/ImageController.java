package controller;

import utils.Global;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageController {//懶散單例模式
    //所有圖片由ImageController創建
    //相同圖片不會重複創建

    private static ImageController imageController;
    //傳入keyPair之前有兩個Arraylist<path>,<image>把兩個陣列存起來
    private HashMap<String, Image> imageMap;

    private ImageController() {
        imageMap = new HashMap<>();
    }

    public static ImageController getInstance() {//推遲到第一次呼叫getInstance()才創建實體
        if (imageController == null) {
            imageController = new ImageController();

        }
        return imageController;
    }

    public Image tryGet(final String path) {//傳入圖片路徑
        if (imageMap.containsKey(path)) {
            return imageMap.get(path);
        }

        return add(path);//若沒有就把路徑傳入add的方法中
    }

    public Image add(final String path) {//用String路徑創建圖片
        Image image = null;
        try {
            image = ImageIO.read(getClass().getResource(path));//創建新圖片
            imageMap.put(path, image);
        } catch (final IOException | IllegalArgumentException e) {
            if (Global.IS_DEBUG) {
//                e.printStackTrace();
            }
        }
        return image;//回傳圖片
    }


}
