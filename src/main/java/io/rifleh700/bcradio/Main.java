package io.rifleh700.bcradio;

import io.rifleh700.bcapi.api.HubApi;
import io.rifleh700.bcapi.api.MobileApi;
import io.rifleh700.bcradio.cli.ArgsHandler;
import io.rifleh700.bcradio.cli.InputStreamArgsListener;
import io.rifleh700.bcradio.cli.PlayByTagArgsHandler;
import io.rifleh700.bcradio.client.ClientProxyFactory;
import io.rifleh700.bcradio.client.resteasy.RestEasyClientProxyFactory;
import io.rifleh700.bcradio.player.SingleTrackPlayer;

public class Main {

    private static final String BANDCAMP_API_URL = "https://bandcamp.com/api";

    public static void main(String[] args) {

        SingleTrackPlayer player = new SingleTrackPlayer();
        ClientProxyFactory clientProxyFactory = new RestEasyClientProxyFactory(BANDCAMP_API_URL);
        ArgsHandler argsHandler =
                new PlayByTagArgsHandler(
                        player,
                        clientProxyFactory.build(HubApi.class),
                        clientProxyFactory.build(MobileApi.class));

        new Thread(new InputStreamArgsListener(System.in, argsHandler)).start();
    }
}
