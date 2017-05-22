package br.ufms.mechsmasher;

import br.ufms.mechsmasher.physics.PhysicalObject;
import com.badlogic.gdx.physics.box2d.*;

public class Projectile extends PhysicalObject {
    ProjectileManager manager;

    public Projectile(ProjectileManager manager) {
        this.manager = manager;
    }

    @Override
    protected Body createBody() {
        Body body;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 0.0f;
        bodyDef.angularDamping = 0.0f;
        bodyDef.bullet = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.03f, 0.1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 3f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        body = getWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }

    @Override
    public void contact(PhysicalObject object) {
        getWorldController().deactivate(this, true);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
