package br.ufms.mechsmasher;

import com.badlogic.gdx.InputAdapter;

import java.util.ArrayList;

public class KeyboardListener extends InputAdapter {
    private ArrayList<Controller> controllers;

    public KeyboardListener() {
        this.controllers = new ArrayList<>();
    }

    public ArrayList<Controller> getControllers() {
        return controllers;
    }

    @Override
    public boolean keyDown(final int keycode) {
        controllers.forEach(c -> c.press(keycode));
        return true;
    }

    @Override
    public boolean keyUp(final int keycode) {
        controllers.forEach(c -> c.release(keycode));
        return true;
    }
}
