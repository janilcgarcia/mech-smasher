package br.ufms.mechsmasher;

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
    private Body[] walls;
    private Fixture[] wallsFixture;

    private PhysicsLifecycleManager physicsLifecycleManager;

    public MechSmasher() {
        players = new Player[2];
        playerControllers = new PlayerController[2];
    }

    private void buildWalls(float width, float height) {
        walls = new Body[4];
        wallsFixture = new Fixture[4];

        BodyDef wallsDef = new BodyDef();
        wallsDef.type = BodyDef.BodyType.StaticBody;
        wallsDef.position.set(0.0f, height / 2.0f);
        walls[0] = world.createBody(wallsDef);

        wallsDef.position.set(width, height / 2.0f);
        walls[1] = world.createBody(wallsDef);

        wallsDef.position.set(width / 2.0f, 0.0f);
        walls[2] = world.createBody(wallsDef);

        wallsDef.position.set(width / 2.0f, height);
        walls[3] = world.createBody(wallsDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2.0f, height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.shape = shape;

        wallsFixture[0] = walls[0].createFixture(fixtureDef);
        wallsFixture[1] = walls[1].createFixture(fixtureDef);

        shape.setAsBox(width, 2.0f);
        wallsFixture[2] = walls[2].createFixture(fixtureDef);
        wallsFixture[3] = walls[3].createFixture(fixtureDef);
        shape.dispose();

        for (Body body : walls) {
            body.setUserData("Wall");
        }
    }

    @Override
    public void create() {
        final float w = Gdx.graphics.getWidth();
        final float h = Gdx.graphics.getHeight();

        batch = new SpriteBatch();

        lastTime = System.currentTimeMillis();
        world = new World(new Vector2(0.0f, 0.0f), true);
        ProjectileManager.initManager(world);
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(500, 500 * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        players[0] = new Player(world, 40.0f, 20.0f);

        players[1] = new Player(world, 200.0f, 230.0f);

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
        buildWalls(camera.viewportWidth, camera.viewportHeight);

        physicsLifecycleManager = new PhysicsLifecycleManager();

        world.setContactListener(new GlobalContactListener(physicsLifecycleManager, walls));
        debugRenderer.setDrawBodies(true);
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

            physicsLifecycleManager.update();
            Arrays.stream(playerControllers).forEach(c -> c.act());
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
