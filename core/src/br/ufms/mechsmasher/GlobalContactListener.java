package br.ufms.mechsmasher;

import br.ufms.mechsmasher.physics.PhysicalObject;
import com.badlogic.gdx.physics.box2d.*;

public class GlobalContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        PhysicalObject objectA;
        PhysicalObject objectB;

        if (!(contact.getFixtureA().getBody().getUserData() instanceof PhysicalObject) ||
                !(contact.getFixtureB().getBody().getUserData() instanceof PhysicalObject)) {
            return;
        }

        objectA = (PhysicalObject) contact.getFixtureA().getBody().getUserData();
        objectB = (PhysicalObject) contact.getFixtureB().getBody().getUserData();

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
