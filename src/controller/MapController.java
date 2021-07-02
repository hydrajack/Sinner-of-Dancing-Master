package controller;

import gameobj.Floor;
import gameobj.GameObject;
import gameobj.Wall;
import maploader.MapInfo;
import maploader.MapLoader;
import scene.SceneLevel1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MapController {
    public enum Key {//選擇地圖讀取
        //如果要加新地圖就加在這邊
        MUD("mud"),
        ROLE("role"),
        LEVEL0("level0"),//menu
        LEVEL1("level1"),
        LEVEL3("level3"),
        LEVEL4("level4");

        private String path;

        Key(String path) {
            this.path = path;
        }
    }

    private Key key;

    public MapController(Key key) {
        this.key = key;//切換場景選擇
    }

    //初始化地圖的方法
    public List<Floor> getFloors() {
        List<Floor> floors = null;
        try {
            //test scene
            final MapLoader mapLoader = new MapLoader("/map/layer1_" + key.path + ".bmp", "/map/layer1_" + key.path + ".txt");
            final ArrayList<MapInfo> mapInfos = mapLoader.combineInfo();//使用MapLoader中的combineInfo()方法產生MapInfo陣列
            floors = new ArrayList<>();
            for(Floor.FloorType key : Floor.FloorType.values()){
                floors.addAll(mapLoader.creatObjectArray(key.name(), 64, mapInfos, new MapLoader.CompareClass() {
                    @Override
                    public GameObject compareClassName(final String gameObject, final String name, final maploader.MapInfo mapInfo, final int size) {
                        //進行name的比照並產生對應地圖物件
                        GameObject tmp = null;
                        if (gameObject.equals(name)) {
                            tmp = new Floor(key,
                                    mapInfo.getX() * size, mapInfo.getY() * size,
                                    mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                            return tmp;
                        }
                        return null;
                    }
                }).stream().map(obj -> (Floor) obj).collect(Collectors.toList()));
            }

        } catch (final IOException ex) {
            Logger.getLogger(SceneLevel1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return floors;
    }
    private void addWall(List<Wall> walls, int layer){
        try {
            MapLoader mapLoader = new MapLoader("/map/layer" + layer + "_" + key.path + ".bmp", "/map/layer" + layer + "_" + key.path + ".txt");
            ArrayList<maploader.MapInfo> mapInfos = mapLoader.combineInfo();//使用MapLoader中的combineInfo()方法產生MapInfo陣列
            for(Wall.WallType key : Wall.WallType.values()){
                walls.addAll(mapLoader.creatObjectArray(key.name(), 64, mapInfos, new MapLoader.CompareClass() {
                    @Override
                    public GameObject compareClassName(final String gameObject, final String name, final maploader.MapInfo mapInfo, final int size) {
                        //進行name的比照並產生對應地圖物件
                        GameObject tmp = null;
                        if (gameObject.equals(name)) {
                            tmp = new Wall(key,
                                    mapInfo.getX() * size, mapInfo.getY() * size,
                                    mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                            return tmp;
                        }
                        return null;
                    }
                })
                        .stream().map(obj -> (Wall) obj).collect(Collectors.toList()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Wall> getWalls() {
        List<Wall> walls = new ArrayList<>();
        addWall(walls, 2);
        addWall(walls, 3);
        walls.sort((o1, o2) -> o1.getWallType().compare(o2.getWallType()));
        return walls;
    }
}
