/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufms.mechsmasher.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Cena exibindo um pequeno tutorial de como jogar o jogo.
 */
public class Tutorial extends GameScene {
    private Texture background;
    private Texture text;

    @Override
    public void create() {
        background = new Texture("concrete.jpg");
        text = new Texture("tutorial-texto.png");
        
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    // Volta para o menu inicial caso o usuário pressione
                    // ESC
                    getSceneManager().swapScene(new Menu());
                    return true;
                }
                
                return false;
            }
        });
    }

    @Override
    public void draw(SpriteBatch batch) {
        final float x = 0.0f;
        final float y = 0.0f;
        final float width = getSceneManager().getCamera().viewportWidth;
        final float height = getSceneManager().getCamera().viewportHeight;
        
        batch.draw(background, x, y, width, height);
        batch.draw(text, x, y, width, height);
    }

    @Override
    public void update(long diffTime) {
    }

    @Override
    public void dispose() {
        background.dispose();
        text.dispose();
    }
}
