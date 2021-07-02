package utils;

import controller.SceneController;
import scene.Cover;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static utils.Global.IS_DEBUG;

public class GameWindow {
    private int windowHeight;
    private int windowWidth;

    private JFrame jFrame;
    private GameKernel gameKernel;

    private static GameWindow gameWindow;

    public static GameWindow getInstance() {
        if (gameWindow == null) {
            gameWindow = new GameWindow();
        }
        return gameWindow;
    }

    private GameWindow() {
        jFrame = new JFrame();//視窗本體
        jFrame.setTitle("Client");//視窗標題
        setWindowSize(2560, 1280);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //關閉視窗時程式
    }

    public GameWindow initKernel(){
        SceneController temp = SceneController.getInstance();
        temp.change(new Cover());
        gameKernel = new GameKernel.Builder()
                .input(new CommandSolver.BuildStream().keyboardTrack()
                        .add(KeyEvent.VK_ENTER, 4)
                        .add(KeyEvent.VK_LEFT, 2)
                        .add(KeyEvent.VK_RIGHT, 3)
                        .add(KeyEvent.VK_UP, 0)
                        .add(KeyEvent.VK_DOWN, 1)
                        .add(KeyEvent.VK_Q, 5)
                        .add(KeyEvent.VK_W, 6)
                        .add(KeyEvent.VK_E, 7)
                        .add(KeyEvent.VK_R, 8)
                        .add(KeyEvent.VK_S, 10)
                        .next().trackChar().keyCleanMode().subscribe(temp))
                .paint(temp)
                .update(temp)
                .gen();
        jFrame.add(gameKernel);
        return this;
    }

    public GameWindow show() {
        jFrame.setVisible(true);
        return this;
    }

    public GameWindow run() {
        gameKernel.run(IS_DEBUG);
        return this;
    }

    public void setWindowSize(int width, int height) {
        windowWidth = width + 16;
        windowHeight = height + 39;
        this.jFrame.setSize(windowWidth, windowHeight);
    }

    public int getWindowHeight() {
        return this.windowHeight;
    }

    public int getWindowWidth() {
        return this.windowWidth;
    }
}
