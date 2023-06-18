package io.rifleh700.bcradio.player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class AdvancedPlayerThread extends Thread {

    private final AdvancedPlayer advancedPlayer;

    public AdvancedPlayerThread(AdvancedPlayer advancedPlayer) {

        this.advancedPlayer = advancedPlayer;
    }

    @Override
    public void run() {

        try {
            advancedPlayer.play();
        } catch (JavaLayerException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void interrupt() {

        super.interrupt();
        advancedPlayer.close();
    }
}
