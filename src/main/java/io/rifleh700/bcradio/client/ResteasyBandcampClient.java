package io.rifleh700.bcradio.client;

import io.rifleh700.bcapi.api.HubApi;
import io.rifleh700.bcapi.api.MobileApi;
import io.rifleh700.bcapi.model.DigDeeperRq;
import io.rifleh700.bcapi.model.DigDeeperRs;
import io.rifleh700.bcapi.model.TralbumDetailsRs;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientBuilder;

public class ResteasyBandcampClient implements BandcampClient {

    private static final String BANDCAMP_API_URL = "https://bandcamp.com/api";

    private final HubApi hub;
    private final MobileApi mobile;

    public ResteasyBandcampClient() {

        ResteasyWebTarget webTarget = (ResteasyWebTarget)
                ClientBuilder.newClient().target(BANDCAMP_API_URL);
        this.hub = webTarget.proxy(HubApi.class);
        this.mobile = webTarget.proxy(MobileApi.class);
    }

    @Override
    public DigDeeperRs digDeeper(DigDeeperRq rq) {

        return hub.hub2DigDeeperPost(rq);
    }

    @Override
    public TralbumDetailsRs tralbumDetails(Long bandId,
                                           Long tralbumId,
                                           String tralbumType) {

        return mobile.mobile24TralbumDetailsGet(bandId, tralbumId, tralbumType);
    }
}
