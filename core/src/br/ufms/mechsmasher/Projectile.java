package br.ufms.mechsmasher;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Projectile extends PhysicalObject {
    public Projectile(World world, Vector2 initPos, Vector2 velocity) {
        super(world);
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(initPos);
        bodyDef.linearVelocity.set(velocity);
        bodyDef.angle = velocity.angleRad() - (float) Math.PI / 2;

        System.err.println("Velocity: " + velocity.len());

        Shape shape = getShape();

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 3f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        buildBody(bodyDef, fixtureDef);

        shape.dispose();
    }

    protected Shape getShape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.8f, 2.4f);
        return shape;
    }

    @Override
    public boolean destroyIfInactive() {
        return true;
    }
}
