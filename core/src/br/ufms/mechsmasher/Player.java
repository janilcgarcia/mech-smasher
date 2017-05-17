package br.ufms.mechsmasher;

import br.ufms.mechsmasher.physics.PhysicalObject;
import br.ufms.mechsmasher.physics.WorldController;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends PhysicalObject {
    private String name;
    private ProjectileManager projectiles;

    private Vector2 direction;

    private Animation<TextureRegion> animation;

    private final static int LEFT = 0;
    private final static int DOWN = 1;
    private final static int RIGHT = 2;
    private final static int UP = 3;

    private int hp;

    public Player(String name, Animation<TextureRegion> animation) {
        //projectiles = ProjectileManager.getInstance();
        direction = new Vector2();
        projectiles = new ProjectileManager();
        this.name = name;

        this.hp = 650;
        this.animation = animation;
    }

    @Override
    protected Body createBody() {
        Body body;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.angularDamping = 1.0f;
        bodyDef.linearDamping = 1.0f;
        bodyDef.fixedRotation = false;
        bodyDef.awake = true;
        bodyDef.allowSleep = false;

        body = getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(12, 12);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    private void calcDirection() {
        direction.x = (float) -Math.sin(getBody().getAngle());
        direction.y = (float) Math.cos(getBody().getAngle());
    }

    private void applyMovement(float dir) {
        calcDirection();

        direction.scl(800.f * dir);
        getBody().applyLinearImpulse(direction, getBody().getWorldCenter(), true);
    }

    public void moveForward() {
        applyMovement(1.0f);
    }

    public void moveBackward() {
        applyMovement(-1.0f);
    }

    public void rotateLeft() {
        getBody().applyAngularImpulse(1600.0f, true);
    }
    public void rotateRight() {
        getBody().applyAngularImpulse(-1600.0f, true);
    }

    public void attack() {
        calcDirection();

        Vector2 pos = new Vector2(getBody().getPosition());
        Vector2 vel = new Vector2(direction);

        pos.add(new Vector2(direction).scl(15));
        vel.scl(100);

        projectiles.fire(pos, vel);
    }

    @Override
    public void setWorldController(WorldController controller) {
        super.setWorldController(controller);
        projectiles.setWorldController(controller);
    }

    @Override
    public void contact(PhysicalObject object) {
        if (object instanceof Projectile) {
            hp -= 10;
            System.out.println(name + ", HP: " + hp);
            if (hp <= 0) {
                System.out.println(name + " is dead");
            }
        }
    }
}
