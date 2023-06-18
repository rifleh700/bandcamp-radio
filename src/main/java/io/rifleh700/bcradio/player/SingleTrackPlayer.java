package io.rifleh700.bcradio.player;

import javazoom.jl.player.advanced.AdvancedPlayer;

import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SingleTrackPlayer {

    private final BlockingQueue<AdvancedPlayer> players;

    public SingleTrackPlayer() {

        this.players = new ArrayBlockingQueue<>(1);
    }

    public synchronized void play(String url,
                                  Runnable onStartedHandler,
                                  Runnable onFinishedHandler) {

        stop();

        try {
            AdvancedPlayer player = new AdvancedPlayer(new URL(url).openStream());
            player.setPlayBackListener(new SimplePlaybackListener(onStartedHandler, onFinishedHandler));
            players.put(player);
            new AdvancedPlayerThread(player).start();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public synchronized void stop() {

        AdvancedPlayer player = players.poll();
        if (player == null) return;

        player.close();
    }
}