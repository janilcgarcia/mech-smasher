package br.ufms.mechsmasher;

import br.ufms.mechsmasher.physics.PhysicalObject;
import br.ufms.mechsmasher.physics.WorldController;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Um jogador, pode se mover e atirar.
 */
public class Player extends PhysicalObject {
    private final Vector2 direction;
    
    private final ProjectileManager projectiles;

    private final Animation<TextureRegion> animation;
    private long animationTime;
    private int hp;

    // Algumas constantes
    private static final float LINEAR_SPEED = 0.3f;
    private static final float ANGULAR_SPPED = 0.2f;

    private boolean moving;

    public Player(Animation<TextureRegion> animation) {
        direction = new Vector2();
        projectiles = new ProjectileManager();

        this.hp = 650;
        this.animation = animation;
        animationTime = 0;

        moving = false;
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
        shape.setAsBox(0.6f, 1.2f);

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

        direction.scl(LINEAR_SPEED * dir);
        getBody().applyLinearImpulse(direction, getBody().getWorldCenter(), true);
    }

    public void moveForward() {
        applyMovement(1.0f);
    }

    public void moveBackward() {
        applyMovement(-1.0f);
    }

    public void rotateLeft() {
        getBody().applyAngularImpulse(ANGULAR_SPPED, true);
    }

    public void rotateRight() {
        getBody().applyAngularImpulse(-ANGULAR_SPPED, true);
    }

    public void startMoving() {
        moving = true;
    }

    public void stopMoving() {
        moving = false;
    }

    public void attack() {
        calcDirection();

        Vector2 pos = new Vector2(getBody().getPosition());
        Vector2 vel = new Vector2(direction).scl(0.1f);

        pos.add(new Vector2(direction).scl(2.5f * Projectile.HEIGHT));

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
        }
    }

    public int getHp() {
        return hp;
    }

    public void update(long delta) {
        animationTime += delta;

        if (animationTime >= 1000) {
            animationTime = animationTime % 1000;
        }
    }

    public void draw(SpriteBatch batch) {
        final Vector2 centerPos = getBody().getWorldCenter();

        batch.draw(moving ?
                        animation.getKeyFrame(animationTime / 1000.f, true) :
                        animation.getKeyFrames()[0],
                centerPos.x - 0.3f, centerPos.y - 0.6f,
                0.35f, 0.6f, 0.6f, 1.2f, 3, 3,
                getBody().getAngle() * 180.0f / (float) Math.PI + 180.f);
    }

    public ProjectileManager getProjectiles() {
        return projectiles;
    }
}
