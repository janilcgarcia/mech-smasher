package br.ufms.mechsmasher;

import br.ufms.mechsmasher.physics.WorldController;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Arrays;

public class MechSmasher extends ApplicationAdapter {
    SpriteBatch batch;
    private Player[] players;
    private PlayerController[] playerControllers;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private long lastTime;
    private Camera camera;
    private Walls walls;
    private Fixture[] wallsFixture;

    private WorldController worldController;

    public MechSmasher() {
        players = new Player[2];
        playerControllers = new PlayerController[2];
    }

    @Override
    public void create() {
        // Set up camera
        final float w = Gdx.graphics.getWidth();
        final float h = Gdx.graphics.getHeight();
        final float viewportWidth = 1024;
        final float viewportHeight = viewportWidth * (h / w);

        camera = new OrthographicCamera(viewportWidth, viewportHeight);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        // Initialize Sprite Batch
        batch = new SpriteBatch();

        // Set up Phyics engine
        lastTime = System.currentTimeMillis();

        world = new World(new Vector2(0.0f, 0.0f), true);
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawBodies(true);
        worldController = new WorldController(world);

        walls = new Walls(viewportWidth, viewportHeight);
        worldController.register(walls);

        world.setContactListener(new GlobalContactListener());

        // Create players
        for (int i = 0; i < 2; i++) {
            players[i] = new Player("Player " + (i + 1), null);
            worldController.register(players[i]);
        }

        players[0].getBody().setTransform(30, 30, 0);
        players[1].getBody().setTransform(viewportWidth - 30, viewportHeight - 30, (float) Math.PI);

        // Set up controllers
        KeyboardListener listener = new KeyboardListener();
        playerControllers[0] = new PlayerController(
                Input.Keys.W,
                Input.Keys.D,
                Input.Keys.S,
                Input.Keys.A,
                Input.Keys.SPACE,
                players[0]
        );
        playerControllers[1] = new PlayerController(
                Input.Keys.UP,
                Input.Keys.RIGHT,
                Input.Keys.DOWN,
                Input.Keys.LEFT,
                Input.Keys.SEMICOLON,
                players[1]
        );
        listener.getControllers().addAll(Arrays.asList(playerControllers));

        Gdx.input.setInputProcessor(listener);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.7f, 0.4f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        long newTime = System.currentTimeMillis();

        while (newTime - lastTime > 1000 / 45) {
            world.step(1000 / 45, 6, 2);
            lastTime += 1000 / 45;

            Arrays.stream(playerControllers).forEach(PlayerController::act);
            worldController.update();
        }

        debugRenderer.render(world, camera.combined);

        batch.begin();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
    }
}
