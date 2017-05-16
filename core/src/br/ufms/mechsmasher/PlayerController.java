package br.ufms.mechsmasher;

import java.util.Arrays;
import java.util.HashMap;

public class PlayerController implements Controller {
    private enum ButtonAction {
        UP, LEFT, DOWN, RIGHT, ATTACK
    }

    private class ButtonStatus {
        public boolean pressed;
        public ButtonAction action;

        public ButtonStatus(ButtonAction action) {
            this.action = action;
        }
    }

    private HashMap<Integer, ButtonStatus> buttons;
    private ButtonStatus[] statuses;
    private Player player;

    public PlayerController(int up, int right, int down, int left, int attack, Player player) {
        this.statuses = new ButtonStatus[5];
        this.statuses[0] = new ButtonStatus(ButtonAction.UP);
        this.statuses[1] = new ButtonStatus(ButtonAction.RIGHT);
        this.statuses[2] = new ButtonStatus(ButtonAction.DOWN);
        this.statuses[3] = new ButtonStatus(ButtonAction.LEFT);
        this.statuses[4] = new ButtonStatus(ButtonAction.ATTACK);

        this.buttons = new HashMap<>();
        this.buttons.put(up, this.statuses[0]);
        this.buttons.put(right, this.statuses[1]);
        this.buttons.put(down, this.statuses[2]);
        this.buttons.put(left, this.statuses[3]);
        this.buttons.put(attack, this.statuses[4]);

        this.player = player;
    }

    @Override
    public void press(int key) {
        ButtonStatus status = buttons.getOrDefault(key, null);
        if (status != null) {
            status.pressed = true;
        }
    }

    @Override
    public void release(int key) {
        ButtonStatus status = buttons.getOrDefault(key, null);

        if (status != null) {
            status.pressed = false;

            if (status.action == ButtonAction.ATTACK) {
                this.player.attack();
            }
        }
    }

    private void executeAction(ButtonAction action) {
        switch (action) {
            case UP:
                this.player.moveForward();
                break;

            case DOWN:
                this.player.moveBackward();
                break;

            case LEFT:
                this.player.rotateLeft();
                break;

            case RIGHT:
                this.player.rotateRight();
                break;
        }
    }

    public void act() {
        Arrays.stream(statuses).filter(e -> e.pressed).forEach(e -> executeAction(e.action));
    }
}
