package br.ufms.mechsmasher.scenes;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by anarchean on 23/05/17.
 */
public class SceneManager {
    private GameScene currentScene;
    private GameScene nextScene;
    private Camera camera;
    private SpriteBatch batch;

    public SceneManager(GameScene initialScene, Camera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;

        this.currentScene = initialScene;
        this.currentScene.setSceneManager(this);
        this.currentScene.create();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Camera getCamera() {
        return camera;
    }

    public void swapScene(GameScene newScene) {
        nextScene = newScene;
    }

    private void swapScene() {
        if (this.nextScene != null) {
            this.currentScene.dispose();
            this.currentScene = this.nextScene;
            this.nextScene = null;

            this.currentScene.setSceneManager(this);
            this.currentScene.create();
        }
    }

    public void draw() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        this.currentScene.draw(batch);
        batch.end();
    }

    public void update(long diffTime) {
        this.currentScene.update(diffTime);

        swapScene();
    }

    public void dispose() {
        if (this.nextScene != null)
            this.nextScene.dispose();

        this.currentScene.dispose();
        batch.dispose();
    }
}
