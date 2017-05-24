package br.ufms.mechsmasher;

import br.ufms.mechsmasher.scenes.*;
import br.ufms.mechsmasher.scenes.Game;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class MechSmasher extends ApplicationAdapter {
    private SpriteBatch batch;
    private Camera camera;
    private SceneManager sceneManager;
    private long lastTime;

    public MechSmasher() {
    }

    @Override
    public void create() {
        super.create();

        // Set up camera
        final float w = Gdx.graphics.getWidth();
        final float h = Gdx.graphics.getHeight();
        final float viewportWidth = 60;
        final float viewportHeight = viewportWidth * (h / w);

        batch = new SpriteBatch();
        camera = new OrthographicCamera(viewportWidth, viewportHeight);
        camera.position.set(viewportWidth / 2, viewportHeight / 2, 0);

        sceneManager = new SceneManager(new Menu(), camera, batch);

        lastTime = TimeUtils.millis();
    }

    @Override
    public void render() {
        super.render();
        long newTime = TimeUtils.millis();

        sceneManager.update(newTime - lastTime);
        sceneManager.draw();

        lastTime = newTime;
    }
}
