package utils;

import controller.SceneController;
import scene.Cover;
import scene.MenuScene;
import scene.Warning;
import utils.CommandSolver;
import utils.GameKernel;
import utils.Global;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static utils.Global.*;


public class Main {

    public static void main(final String[] args) {//test
        GameWindow.getInstance()
                .initKernel()
                .show().run();
    }
}




