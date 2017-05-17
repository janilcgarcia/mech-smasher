package br.ufms.mechsmasher;

import br.ufms.mechsmasher.physics.PhysicalObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Walls extends PhysicalObject {
    private float width;
    private float height;

    public Walls(float windowWidth, float windowHeight) {
        this.width = windowWidth;
        this.height = windowHeight;
    }

    @Override
    protected Body createBody() {
        BodyDef wallsDef = new BodyDef();
        wallsDef.type = BodyDef.BodyType.StaticBody;
        wallsDef.position.set(0, 0);

        ChainShape chainShape = new ChainShape();
        Vector2[] chainVertices = new Vector2[4];
        chainVertices[0] = new Vector2(0, height);
        chainVertices[1] = new Vector2(0, 0);
        chainVertices[2] = new Vector2(width, 0);
        chainVertices[3] = new Vector2(width, height);
        chainShape.createLoop(chainVertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.shape = chainShape;

        Body body = getWorld().createBody(wallsDef);
        body.createFixture(fixtureDef);

        chainShape.dispose();
        return body;
    }
}
