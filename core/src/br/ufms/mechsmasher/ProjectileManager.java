package br.ufms.mechsmasher;

import br.ufms.mechsmasher.physics.WorldController;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayDeque;

import java.util.ArrayList;

/**
 * Gerencia projéteis de um jogador, minimizando gargabe collections e 
 * garantindo que um delay entre tiros.
 */
public class ProjectileManager {
    private WorldController worldController;
    private long lastShot;
    private final ArrayList<Projectile> firedBullets;
    /**
     * Usado para reduzir a garbage collection.
     */
    private final ArrayDeque<Projectile> availableBullets;

    public ProjectileManager() {
        this.worldController = null;
        this.lastShot = 0L;
        this.firedBullets = new ArrayList<>();
        this.availableBullets = new ArrayDeque<>();
    }

    public Projectile fire(Vector2 from, Vector2 dir) {
        final long now = System.currentTimeMillis();
        if (now - lastShot < 200) {
            return null;
        }

        lastShot = now;
        Projectile projectile;
        
        if (this.availableBullets.isEmpty()) {
            projectile = new Projectile(this);
            worldController.register(projectile);
        } else {
            projectile = this.availableBullets.pop();
            
        }

        // Reseta corpo do projétil
        projectile.getBody().setAngularVelocity(0.0f);
        projectile.getBody().setLinearVelocity(0.0f, 0.0f);
        
        // Coloca-o na nova trajetória
        projectile.getBody().setTransform(from.x, from.y,
                dir.angleRad() - 0.5f * (float) Math.PI);
        
        projectile.getBody().applyLinearImpulse(dir,
                projectile.getBody().getWorldCenter(), true);
        
        // Reativa corpo, caso esteja desativado.
        projectile.getBody().setActive(true);

        this.firedBullets.add(projectile);

        return projectile;
    }

    public void removeProjectile(Projectile projectile) {
        this.firedBullets.remove(projectile);
        this.availableBullets.add(projectile);
    }

    public ArrayList<Projectile> getFiredBullets() {
        return firedBullets;
    }

    public void setWorldController(WorldController worldController) {
        this.worldController = worldController;
    }
}
