package br.ufms.mechsmasher;

import java.util.ArrayDeque;

public class PhysicsLifecycleManager {
    private ArrayDeque<PhysicalObject> deactivateQueue;
    private ArrayDeque<PhysicalObject> activateQueue;

    public PhysicsLifecycleManager() {
        this.deactivateQueue = new ArrayDeque<>();
        this.activateQueue = new ArrayDeque<>();
    }

    public void deactivate(PhysicalObject object) {
        deactivateQueue.offer(object);
    }

    public void activate(PhysicalObject object) {
        activateQueue.offer(object);
    }

    public void update() {
        PhysicalObject object;

        while ((object = deactivateQueue.poll()) != null) {
            object.getBody().setActive(false);

            if (object.destroyIfInactive()) {
                object.dispose();
            }
        }

        while ((object = activateQueue.poll()) != null) {
            object.getBody().setActive(true);
        }
    }
}
