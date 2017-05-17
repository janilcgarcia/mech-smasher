package br.ufms.mechsmasher;

import br.ufms.mechsmasher.physics.WorldController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class ProjectileManager {
    private World world;
    private WorldController worldController;
    private long lastShot;

    public ProjectileManager() {
        this.worldController = null;
        this.lastShot = 0L;
    }

    public Projectile fire(Vector2 from, Vector2 dir) {
        final long now = System.currentTimeMillis();
        if (now - lastShot < 100) {
            return null;
        }

        lastShot = now;
        Projectile projectile = new Projectile(this);
        worldController.register(projectile);

        projectile.getBody().setTransform(from.x, from.y, dir.angleRad() - 0.5f * (float) Math.PI);
        projectile.getBody().setLinearVelocity(dir);

        return projectile;
    }

    public void setWorldController(WorldController worldController) {
        this.worldController = worldController;
    }
}
