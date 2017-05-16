package br.ufms.mechsmasher;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class ProjectileManager {
    private World world;

    private static ProjectileManager instance = null;

    private ProjectileManager(World world) {
        this.world = world;
    }

    public Projectile fire(Vector2 from, Vector2 dir) {
        return new Projectile(world, from, dir);
    }

    public static void initManager(World world) {
        instance = new ProjectileManager(world);
    }

    public static ProjectileManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("Instance not initialized yet");
        }

        return instance;
    }
}
