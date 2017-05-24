package br.ufms.mechsmasher.scenes;

import br.ufms.mechsmasher.*;
import br.ufms.mechsmasher.physics.WorldController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;

import java.util.Arrays;

/**
 * Created by anarchean on 23/05/17.
 */
public class Game extends GameScene {
    private Player[] players;
    private PlayerController[] playerControllers;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private long lastTime;
    private Camera textCamera;
    private Walls walls;

    private WorldController worldController;

    private Texture concrete;
    private Texture bullet;

    private BitmapFont arial;

    @Override
    public void create() {
        final float w = Gdx.graphics.getWidth();
        final float h = Gdx.graphics.getHeight();

        final float viewportWidth = getSceneManager().getCamera().viewportWidth;
        final float viewportHeight = getSceneManager().getCamera().viewportHeight;

        players = new Player[2];
        playerControllers = new PlayerController[2];

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
            Texture tex = new Texture(Gdx.files.internal("player" + (i + 1) + ".png"));

            TextureRegion[] textureRegions =
                    TextureRegion.split(tex, tex.getWidth() / 3, tex.getHeight())[0];
            players[i] = new Player("Player " + (i + 1),
                    new Animation<>(1 / 2.f, textureRegions));
            worldController.register(players[i]);
        }

        players[0].getBody().setTransform(1.5f, 1.5f, 0);
        players[1].getBody().setTransform(viewportWidth - 1.5f, viewportHeight - 1.5f, (float) Math.PI);

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
                Input.Keys.CONTROL_RIGHT,
                players[1]
        );
        listener.getControllers().addAll(Arrays.asList(playerControllers));

        Gdx.input.setInputProcessor(listener);

        concrete = new Texture(Gdx.files.internal("concrete.jpg"));
        bullet = new Texture(Gdx.files.internal("bullet.png"));

        arial = new BitmapFont();

        textCamera = new OrthographicCamera(500, 500 * (h / w));
        textCamera.position.set(textCamera.viewportWidth / 2, textCamera.viewportHeight / 2, 0.0f);
        textCamera.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        Gdx.gl.glClearColor(0.7f, 0.4f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Desenha background e players
        batch.draw(concrete, 0, 0, getSceneManager().getCamera().viewportWidth,
                getSceneManager().getCamera().viewportHeight);

        for (Player p : players) {
            p.draw(batch);
            for (Projectile projectile : p.getProjectiles().getFiredBullets()) {
                projectile.draw(batch, bullet);
            }
        }
        batch.end();

        // Com uma camera diferente, mais adequada ao texto, imprime o texto
        // na tela. De verde para o jogador 1 e em vermelho para o jogador 2.
        batch.setProjectionMatrix(textCamera.combined);
        batch.begin();
        arial.setColor(new Color(0.2f, 0.5f, 0.2f, 12.0f));
        arial.draw(batch, "Player 1: " + players[0].getHp(), 30, 30);
        arial.setColor(new Color(0.5f, 0.2f, 0.2f, 1.0f));
        arial.draw(batch, "Player 2: " + players[1].getHp(), 470, 30, 0.0f,
                Align.right, false);
    }

    @Override
    public void dispose() {
        players[0].dispose();
        players[1].dispose();
        walls.dispose();
        concrete.dispose();
        bullet.dispose();
        world.dispose();
    }

    @Override
    public void update(long diffTime) {
        long newTime = System.currentTimeMillis();

        while (newTime - lastTime > 1000 / 45) {
            world.step(1000 / 45.f, 6, 2);
            lastTime += 1000 / 45;

            Arrays.stream(playerControllers).forEach(PlayerController::act);
            worldController.update();
            for (Player player : players)
                player.update(1000 / 45);
        }
    }
}
