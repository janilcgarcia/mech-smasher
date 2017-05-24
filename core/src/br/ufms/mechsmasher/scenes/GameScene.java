package br.ufms.mechsmasher.scenes;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameScene implements Disposable {
    private SceneManager sceneManager;

    public GameScene() {
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public abstract void create();

    public abstract void draw(SpriteBatch batch);

    public abstract void update(long diffTime);
}
