package br.ufms.mechsmasher;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

public abstract class PhysicalObject implements Disposable {
    private World world;
    private Body body;
    private ArrayList<Fixture> fixtures;

    protected PhysicalObject(World world) {
        this.body = null;
        this.fixtures = new ArrayList<>();
        this.world = world;
    }

    protected void buildBody(BodyDef bodyDef, FixtureDef... fixtureDefs) {
        this.body = world.createBody(bodyDef);
        for (FixtureDef fDef : fixtureDefs) {
            this.fixtures.add(this.body.createFixture(fDef));
        }
        this.body.setUserData(this);
    }

    public Body getBody() {
        return body;
    }

    public ArrayList<Fixture> getFixtures() {
        return fixtures;
    }

    @Override
    public void dispose() {
        if (body != null) {
            for (Fixture fixture : fixtures) {
                body.destroyFixture(fixture);
            }

            world.destroyBody(body);
        }
    }

    public abstract boolean destroyIfInactive();

    public World getWorld() {
        return world;
    }

    public void onContact(PhysicalObject object) {
    }
}
