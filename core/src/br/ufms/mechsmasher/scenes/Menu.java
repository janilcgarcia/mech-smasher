package br.ufms.mechsmasher.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Cena do Menu inicial.
 */
public class Menu extends GameScene {
    private Texture titulo;
    private Texture novoJogo;
    private Texture tutorial;
    private Texture sair;
    private Texture fundo;

    private Sprite tituloSprite;
    private Sprite novoJogoSprite;
    private Sprite tutorialSprite;
    private Sprite sairSprite;

    private class GameSceneInput extends InputAdapter {
        private final Vector3 touchPosition;
        private final Rectangle novoJogoRectangle;
        private final Rectangle tutorialRectangle;
        private final Rectangle sairRectangle;

        private GameSceneInput() {
            touchPosition = new Vector3();
            novoJogoRectangle = novoJogoSprite.getBoundingRectangle();
            tutorialRectangle = tutorialSprite.getBoundingRectangle();
            sairRectangle = sairSprite.getBoundingRectangle();
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            touchPosition.set(screenX, screenY, 0);
            getSceneManager().getCamera().unproject(touchPosition);

            if (novoJogoRectangle.contains(touchPosition.x, touchPosition.y)) {
                getSceneManager().swapScene(new Game());
            }

            if (tutorialRectangle.contains(touchPosition.x, touchPosition.y)) {
                getSceneManager().swapScene(new Tutorial());
            }

            if (sairRectangle.contains(touchPosition.x, touchPosition.y)) {
                Gdx.app.exit();
            }

            return false;
        }
    }

    @Override
    public void create() {
        titulo = new Texture("titulo.png");
        novoJogo = new Texture("novo-jogo.png");
        tutorial = new Texture("tutorial.png");
        sair = new Texture("sair.png");
        fundo = new Texture("concrete.jpg");

        tituloSprite = new Sprite(titulo);
        novoJogoSprite = new Sprite(novoJogo);
        tutorialSprite = new Sprite(tutorial);
        sairSprite = new Sprite(sair);

        halfScreen(titulo, tituloSprite, 5, 26);
        halfScreen(novoJogo, novoJogoSprite, 3, 19);
        halfScreen(tutorial, tutorialSprite, 3, 13);
        halfScreen(sair, sairSprite, 3, 7);

        Gdx.input.setInputProcessor(new GameSceneInput());
    }

    /**
     * Posiciona sprite no meio da tela, centralizado horizontalmente.
     */
    private void halfScreen(Texture texture, Sprite sprite,
            final float height, final float y) {
        final float ratio = texture.getWidth() / texture.getHeight();
        final float width = height * ratio;
        sprite.setSize(width, height);
        sprite.setPosition(60 / 2 - width / 2, y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(fundo, 0, 0, getSceneManager().getCamera().viewportWidth,
                getSceneManager().getCamera().viewportHeight);
        tituloSprite.draw(batch);
        novoJogoSprite.draw(batch);
        tutorialSprite.draw(batch);
        sairSprite.draw(batch);
    }

    @Override
    public void update(long diffTime) {
    }

    @Override
    public void dispose() {
        fundo.dispose();
        titulo.dispose();
        novoJogo.dispose();
        tutorial.dispose();
        sair.dispose();
    }
}
