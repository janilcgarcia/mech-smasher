package br.ufms.mechsmasher;

import com.badlogic.gdx.physics.box2d.*;

public class GlobalContactListener implements ContactListener {
    private Body[] walls;
    private PhysicsLifecycleManager lifecycleManager;

    public GlobalContactListener(PhysicsLifecycleManager lifecycleManager, Body[] walls) {
        this.walls = walls;
        this.lifecycleManager = lifecycleManager;
    }

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

        objectA.onContact(objectB);
        objectB.onContact(objectA);
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
