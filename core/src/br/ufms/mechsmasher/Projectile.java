package br.ufms.mechsmasher;

import br.ufms.mechsmasher.physics.PhysicalObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.*;

public class Projectile extends PhysicalObject {
    private ProjectileManager manager;

    public static final float WIDTH = 0.3f;
    public static final float HEIGHT = 0.9f;

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
        shape.setAsBox(WIDTH, HEIGHT);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 3.0f;
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

        manager.removeProjectile(this);
    }

    public void draw(Batch batch, Texture bulletTexture) {
        batch.draw(bulletTexture, getBody().getPosition().x - WIDTH / 2,
                getBody().getPosition().y - HEIGHT / 2, WIDTH / 2, HEIGHT / 2,
                WIDTH, HEIGHT, 1, 1,
                getBody().getAngle() * 180.0f / (float) Math.PI, 0, 0,
                bulletTexture.getWidth(), bulletTexture.getHeight(),
                false, false);
    }
}
