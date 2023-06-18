package io.rifleh700.bcradio.cli;

import io.rifleh700.bcapi.model.*;
import io.rifleh700.bcradio.client.BandcampClient;
import io.rifleh700.bcradio.player.SingleTrackPlayer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlayByTagArgsHandler implements ArgsHandler {

    private static final int DIG_DEEPER_PAGE_MAX = 30;

    private final SingleTrackPlayer player;
    private final BandcampClient bandcampClient;
    private final Random random;

    public PlayByTagArgsHandler(SingleTrackPlayer player,
                                BandcampClient bandcampClient) {

        this.player = player;
        this.bandcampClient = bandcampClient;
        this.random = new Random();
    }

    @Override
    public void handle(String[] args) {

        if (args.length < 2) {
            printIllegalMessage();
            return;
        }

        DigDeeperRqFilters.SortEnum sort = null;
        try {
            sort = DigDeeperRqFilters.SortEnum.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
            printIllegalMessage();
            return;
        }

        execute(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), sort);
    }

    private void printIllegalMessage() {

        System.out.println("illegal command, syntax is");
        System.out.println(" [sort(pop/date/random)] [tags...]");
        System.out.println(" example: date vaporwave");
    }

    private void execute(List<String> tags,
                         DigDeeperRqFilters.SortEnum sort) {

        TralbumDetailsRs tralbumDetailsRs = fetchRandomTralbumDetails(tags, sort);
        int trackNumber = random.nextInt(tralbumDetailsRs.getTracks().size());
        TralbumDetailsRsTracksInner track = tralbumDetailsRs.getTracks().get(trackNumber);

        player.play(
                track.getStreamingUrl().getMp3128(),
                () -> printTralbumTrackInfo(tralbumDetailsRs, trackNumber),
                () -> execute(tags, sort));
    }

    private TralbumDetailsRs fetchRandomTralbumDetails(List<String> tags,
                                                       DigDeeperRqFilters.SortEnum sort) {

        List<DigDeeperRsItemsInner> items = bandcampClient.digDeeper(new DigDeeperRq()
                .page(random.nextInt(DIG_DEEPER_PAGE_MAX - 1) + 1)
                .filters(new DigDeeperRqFilters()
                        .tags(tags)
                        .format(DigDeeperRqFilters.FormatEnum.ALL)
                        .sort(sort)
                        .location(0L))).getItems();
        DigDeeperRsItemsInner item = items.get(random.nextInt(items.size()));

        return bandcampClient.tralbumDetails(item.getBandId(),
                item.getTralbumId(),
                item.getTralbumType());
    }

    private void printTralbumTrackInfo(TralbumDetailsRs tralbumDetailsRs, int trackNumber) {

        System.out.println("band: \t" + tralbumDetailsRs.getBand().getName() + (
                tralbumDetailsRs.getBand().getLocation() != null ?
                        " (" + tralbumDetailsRs.getBand().getLocation() + ")"
                        : ""));
        System.out.println(
                "album: \t" + tralbumDetailsRs.getTralbumArtist() + " - " + tralbumDetailsRs.getAlbumTitle() + " (" + Instant.ofEpochSecond(
                        tralbumDetailsRs.getReleaseDate()).atZone(
                        ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE) + ")");

        TralbumDetailsRsTracksInner track = tralbumDetailsRs.getTracks().get(trackNumber);
        System.out.println("track: \t" + track.getBandName() + " - " + track.getTitle());
        System.out.print("tags: \t");
        tralbumDetailsRs.getTags().forEach(t -> System.out.print(t.getNormName() + "; "));
        System.out.println("");
        System.out.println("url: \t" + tralbumDetailsRs.getBandcampUrl());
        System.out.println("");
    }
}
