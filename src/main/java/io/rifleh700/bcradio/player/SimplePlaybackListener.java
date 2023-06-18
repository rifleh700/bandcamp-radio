package io.rifleh700.bcradio.player;

import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class SimplePlaybackListener extends PlaybackListener {

    private final Runnable onStartedHandler;
    private final Runnable onFinishedHandler;

    public SimplePlaybackListener(Runnable onStartedHandler, Runnable onFinishedHandler) {

        this.onStartedHandler = onStartedHandler;
        this.onFinishedHandler = onFinishedHandler;
    }

    @Override
    public void playbackStarted(PlaybackEvent evt) {

        onStartedHandler.run();
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {

        onFinishedHandler.run();
    }

}
