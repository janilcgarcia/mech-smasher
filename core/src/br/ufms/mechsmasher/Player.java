package br.ufms.mechsmasher;

import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Player {
    private Body body;
    private Fixture fixture;

    private int[] movKeyCodes;
    private ProjectileManager projectiles;

    private Vector2 direction;

    private final static int LEFT = 0;
    private final static int DOWN = 1;
    private final static int RIGHT = 2;
    private final static int UP = 3;

    public Player(World world, float initX, float initY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(initX, initY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.angularDamping = 1.0f;
        bodyDef.linearDamping = 1.0f;
        bodyDef.fixedRotation = false;
        bodyDef.awake = true;
        bodyDef.allowSleep = false;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(12, 12);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        fixture = body.createFixture(fixtureDef);

        projectiles = ProjectileManager.getInstance();
        direction = new Vector2();
    }

    private void calcDirection() {
        direction.x = (float) -Math.sin(body.getAngle());
        direction.y = (float) Math.cos(body.getAngle());
    }

    private void applyMovement(float dir) {
        calcDirection();

        direction.scl(400.0f * dir);
        body.applyLinearImpulse(direction, body.getWorldCenter(), true);
    }

    public void moveForward() {
        applyMovement(1.0f);
    }

    public void moveBackward() {
        applyMovement(-1.0f);
    }

    public void rotateLeft() {
        body.applyAngularImpulse(800.0f, true);
    }
    public void rotateRight() {
        body.applyAngularImpulse(-800.0f, true);
    }

    public void attack() {
        calcDirection();

        Vector2 pos = new Vector2(body.getPosition());
        Vector2 vel = new Vector2(direction);

        pos.add(new Vector2(direction).scl(15));
        vel.scl(100);

        projectiles.fire(pos, vel);
    }
}
