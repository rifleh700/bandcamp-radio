package io.rifleh700.bcradio;

import io.rifleh700.bcradio.cli.ArgsHandler;
import io.rifleh700.bcradio.cli.InputStreamArgsListener;
import io.rifleh700.bcradio.cli.PlayByTagArgsHandler;
import io.rifleh700.bcradio.client.BandcampClient;
import io.rifleh700.bcradio.client.ResteasyBandcampClient;
import io.rifleh700.bcradio.player.SingleTrackPlayer;

public class Main {

    public static void main(String[] args) {

        SingleTrackPlayer player = new SingleTrackPlayer();
        BandcampClient bandcampClient = new ResteasyBandcampClient();
        ArgsHandler argsHandler = new PlayByTagArgsHandler(player, bandcampClient);

        new Thread(new InputStreamArgsListener(System.in, argsHandler)).start();
    }
}
