package br.ufms.mechsmasher;

import br.ufms.mechsmasher.physics.WorldController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProjectileManager {
    private World world;
    private WorldController worldController;
    private long lastShot;
    private ArrayList<Projectile> firedBullets;

    public ProjectileManager() {
        this.worldController = null;
        this.lastShot = 0L;
        this.firedBullets = new ArrayList<>();
    }

    public Projectile fire(Vector2 from, Vector2 dir) {
        final long now = System.currentTimeMillis();
        if (now - lastShot < 250) {
            return null;
        }

        lastShot = now;
        Projectile projectile = new Projectile(this);
        worldController.register(projectile);

        projectile.getBody().setTransform(from.x, from.y, dir.angleRad() - 0.5f * (float) Math.PI);
        projectile.getBody().applyLinearImpulse(dir, projectile.getBody().getWorldCenter(), true);

        this.firedBullets.add(projectile);

        return projectile;
    }

    public void removeProjectile(Projectile projectile) {
        this.firedBullets.remove(projectile);
    }

    public ArrayList<Projectile> getFiredBullets() {
        return firedBullets;
    }

    public void setWorldController(WorldController worldController) {
        this.worldController = worldController;
    }
}
