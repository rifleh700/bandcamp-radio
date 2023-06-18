package io.rifleh700.bcradio.client;

import io.rifleh700.bcapi.model.DigDeeperRq;
import io.rifleh700.bcapi.model.DigDeeperRs;
import io.rifleh700.bcapi.model.TralbumDetailsRs;

public interface BandcampClient {

    DigDeeperRs digDeeper(DigDeeperRq rq);

    TralbumDetailsRs tralbumDetails(Long bandId,
                                    Long tralbumId,
                                    String tralbumType);

}
