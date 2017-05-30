package br.ufms.mechsmasher;

import br.ufms.mechsmasher.physics.PhysicalObject;
import com.badlogic.gdx.physics.box2d.*;

/**
 * A detecção de colisão da Box2D funciona com base em respostas a contatos.
 * Este objeto converte um contato numa ação entre objetos físicos e deixa
 * que estes tratem a colisão.
 */
public class GlobalContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        PhysicalObject objectA;
        PhysicalObject objectB;

        // Se algum objeto da colisão não for um PhysicalObject, ignora
        // colisão.
        if (!(contact.getFixtureA().
                getBody().getUserData() instanceof PhysicalObject) ||
                !(contact.getFixtureB().
                        getBody().getUserData() instanceof PhysicalObject)) {
            return;
        }

        objectA = (PhysicalObject) contact.getFixtureA()
                .getBody().getUserData();
        objectB = (PhysicalObject) contact.getFixtureB()
                .getBody().getUserData();

        objectA.contact(objectB);
        objectB.contact(objectA);
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
