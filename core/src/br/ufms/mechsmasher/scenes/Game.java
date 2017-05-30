package br.ufms.mechsmasher.scenes;

import br.ufms.mechsmasher.*;
import br.ufms.mechsmasher.physics.WorldController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;

/**
 * Esta cena é o jogo em si. É aqui onde os jogares interagem com o jogo,
 * os personagens e HUD são desenhados, etc...
 */
public class Game extends GameScene {
    /**
     * Players na cena
     */
    private Player[] players;
    private PlayerController[] playerControllers;

    /**
     * Mundo físico, necessário para a utilização da Box2D.
     */
    private World world;
    /**
     * Renderiza objetos físicos para debugging. Desativado em produção.
     */
    private Box2DDebugRenderer debugRenderer;
    
    /**
     * Último vez que o método de update foi executado.
     */
    private long lastTime;
    /**
     * Camera dedicada para exibição do HUD. Tem dimensões diferentes das do
     * jogo em si.
     */
    private Camera textCamera;
    /**
     * Paredes do cenário.
     */
    private Walls walls;

    private WorldController worldController;

    // Textures úteis na construção do cenário.
    /**
     * Texture de concreto, usado no background do cenário
     */
    private Texture concrete;
    /**
     * Textura da bala.
     */
    private Texture bullet;
    
    private Texture winner0, winner1;
    
    /**
     * ShapeRenderer utilizado para desenhar as barras de HP dos jogadores.
     */
    private ShapeRenderer shapeRenderer;

    // Variáveis utilizadas para determinar quando o jogo acaba.
    private boolean running;    
    private int winner;
    
    private class RestartGameProcessor extends InputAdapter {
        @Override
        public boolean keyUp(int keycode) {
            if (keycode == Keys.ESCAPE) {
                getSceneManager().swapScene(new Menu());
            } else {
                getSceneManager().swapScene(new Game());
            }
            
            return true;
        }
    }

    /**
     * Cria um novo cenário de jogo.
     */
    public Game() {
        running = true;
        winner = -1;
    }

    /**
     * Carrega recursos do cenário, inicializa jogares, controles, a engine
     * física, etc...
     */
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
            players[i] = new Player(new Animation<>(1 / 5.f, textureRegions));
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

        
        // Carrega texturas
        concrete = new Texture(Gdx.files.internal("concrete.jpg"));
        bullet = new Texture(Gdx.files.internal("bullet.png"));

        // Prepara uma camera pra texto com a largura da tela = 500
        textCamera = new OrthographicCamera(500, 500 * (h / w));
        textCamera.position.set(textCamera.viewportWidth / 2, textCamera.viewportHeight / 2, 0.0f);
        textCamera.update();
        
        // Initializa ShapeRenderer
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        
        // Carrega texturas de mensagem de vitória
        winner0 = new Texture("player1win.png");
        winner1 = new Texture("player2win.png");
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Limpa fundo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Desenha background e players
        batch.draw(concrete, 0, 0, getSceneManager().getCamera().viewportWidth,
                getSceneManager().getCamera().viewportHeight);

        for (Player p : players) {
            p.draw(batch);
            p.getProjectiles().getFiredBullets().forEach((projectile) -> {
                projectile.draw(batch, bullet);
            });
        }
        
        //batch.end();
        //debugRenderer.render(world, getSceneManager().getCamera().combined);
        //batch.begin();
        
        if (running) {
            // É necessário finalizar o batch anterior pois ele pode guardar
            // um buffer, que pode afetar a ordem de renderização do HUD.
            batch.end();

            // Imprime HUD
            shapeRenderer.setProjectionMatrix(textCamera.combined);
            shapeRenderer.begin();
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.2f, 0.5f, 0.2f, 1.0f);
            shapeRenderer.rect(20, 20, 120 * (players[0].getHp() / 650f), 10);
            shapeRenderer.setColor(new Color(0.5f, 0.2f, 0.2f, 1.0f));
            shapeRenderer.rect(360, 20, 120 * (players[1].getHp() / 650f), 10);
            shapeRenderer.end();

            // Reinicialização o SpriteBatch para que a execução possa ocorrer
            // normalmente no SceneManager
            batch.begin();
        } else {
            final float x = 0.0f;
            final float y = 0.0f;
            final float width = getSceneManager().getCamera().viewportWidth;
            final float height = getSceneManager().getCamera().viewportHeight;
            
            if (winner == 0)
                batch.draw(winner0, x, y, width, height);
            else
                batch.draw(winner1, x, y, width, height);
        }
    }

    /**
     * Elimina todos recursos carregados.
     */
    @Override
    public void dispose() {
        players[0].dispose();
        players[1].dispose();
        walls.dispose();
        concrete.dispose();
        bullet.dispose();
        world.dispose();
    }

    /**
     * Atualiza a cena. Invoca integrador da engine física, executa operações
     * dos controles.
     * @param diffTime 
     */
    @Override
    public void update(long diffTime) {
        long newTime = System.currentTimeMillis();

        while (running && newTime - lastTime > 1000 / 45) {
            // Atualiza mundo físico
            world.step(1000 / 45.f, 6, 2);
            lastTime += 1000 / 45;

            // Executa ações do usuário
            Arrays.stream(playerControllers).forEach(PlayerController::act);
            worldController.update();
            
            // Atualiza jogadores
            for (Player player : players)
                player.update(1000 / 45);
            
            // Decide se algum dos jogadores ganhou o jogo, se ganhou, para
            // de executar este método de atualização.
            if (players[0].getHp() <= 0) {
                setUpEndGame(1);
            } else if (players[1].getHp() <= 0) {
                setUpEndGame(0);
            }
        }
    }
    
    private void setUpEndGame(int winner) {
        this.winner = winner;
        running = false;
        Gdx.input.setInputProcessor(new RestartGameProcessor());
    }
}
