package br.ufms.mechsmasher.physics;

import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayDeque;
import java.util.Queue;

public class WorldController {
    private World world;
    private Queue<PhysicalObject> deactivateQueue;
    private Queue<PhysicalObject> destroyQueue;

    public WorldController(World world) {
        this.world = world;
        this.deactivateQueue = new ArrayDeque<>();
        this.destroyQueue = new ArrayDeque<>();
    }

    public void register(PhysicalObject physicalObject) {
        physicalObject.setWorldController(this);
        if (physicalObject.getBody() == null) {
            physicalObject.createBody(world);
        }
    }

    public void unregister(PhysicalObject physicalObject) {
        physicalObject.setWorldController(null);
    }

    public World getWorld() {
        return world;
    }

    public void deactivate(PhysicalObject object, boolean destroy) {
        deactivateQueue.add(object);

        if (destroy) {
            destroyQueue.add(object);
        }
    }

    public void update() {
        PhysicalObject object;

        while (!deactivateQueue.isEmpty()) {
            object = deactivateQueue.poll();
            object.getBody().setActive(false);
        }

        while (!destroyQueue.isEmpty()) {
            object = destroyQueue.poll();

            if (object.getBody().isActive()) {
                deactivateQueue.add(object);
            } else {
                object.dispose();
            }
        }
    }
}
