package br.ufms.mechsmasher.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

public abstract class PhysicalObject implements Disposable {
    private World world;
    private Body body;
    private WorldController worldController;

    public PhysicalObject() {
        this.world = null;
        this.body = null;
        this.worldController = null;
    }

    protected abstract Body createBody();

    public void createBody(World world) {
        this.world = world;
        this.body = this.createBody();
        this.body.setUserData(this);
    }

    public void setWorldController(WorldController controller) {
        this.worldController = controller;
    }

    public WorldController getWorldController() {
        return this.worldController;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void dispose() {
        if (body != null) {
            for (Fixture fixture : body.getFixtureList()) {
                body.destroyFixture(fixture);
            }

            world.destroyBody(body);
        }
    }

    public World getWorld() {
        return world;
    }

    public void contact(PhysicalObject object) { }
}
