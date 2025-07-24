package io.rifleh700.bcradio.cli;

import io.rifleh700.bcapi.api.HubApi;
import io.rifleh700.bcapi.api.MobileApi;
import io.rifleh700.bcapi.model.*;
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
    private final HubApi hub;
    private final MobileApi mobile;
    private final Random random;

    public PlayByTagArgsHandler(SingleTrackPlayer player,
                                HubApi hub,
                                MobileApi mobile) {

        this.player = player;
        this.hub = hub;
        this.mobile = mobile;
        this.random = new Random();
    }

    @Override
    public void handle(String[] args) {

        if (args.length < 2) {
            printSyntax();
            return;
        }

        DigDeeperRqFilters.SortEnum sort = null;
        try {
            sort = DigDeeperRqFilters.SortEnum.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
            printSyntax();
            return;
        }

        execute(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), sort);
    }

    private void printSyntax() {

        System.out.println("syntax is");
        System.out.println(" [sort(pop/date/random)] [tags...]");
        System.out.println(" example: date vaporwave");
    }

    private void execute(List<String> tags,
                         DigDeeperRqFilters.SortEnum sort) {

        TralbumDetails tralbumDetailsRs = fetchRandomTralbumDetails(tags, sort);
        int trackNumber = random.nextInt(tralbumDetailsRs.getTracks().size());
        TralbumTrack track = tralbumDetailsRs.getTracks().get(trackNumber);

        player.play(
                track.getStreamingUrl().get(StreamingFormat.MP3_128.toString()),
                () -> printTralbumTrackInfo(tralbumDetailsRs, trackNumber),
                () -> execute(tags, sort));
    }

    private TralbumDetails fetchRandomTralbumDetails(List<String> tags,
                                                       DigDeeperRqFilters.SortEnum sort) {

        List<DigDeeperItem> items = hub.hub2DigDeeperPost(new DigDeeperRq()
                .page(random.nextInt(DIG_DEEPER_PAGE_MAX - 1) + 1)
                .filters(new DigDeeperRqFilters()
                        .tags(tags)
                        .format(Format.ALL)
                        .sort(sort)
                        .location(0L))).getItems();
        DigDeeperItem item = items.get(random.nextInt(items.size()));

        return mobile.mobile24TralbumDetailsGet(item.getBandId(),
                item.getTralbumId(),
                item.getTralbumType());
    }

    private void printTralbumTrackInfo(TralbumDetails tralbumDetailsRs, int trackNumber) {

        System.out.println("band: \t" + tralbumDetailsRs.getBand().getName() + (
                tralbumDetailsRs.getBand().getLocation() != null ?
                        " (" + tralbumDetailsRs.getBand().getLocation() + ")"
                        : ""));
        System.out.println(
                "album: \t" + tralbumDetailsRs.getTralbumArtist() + " - " + tralbumDetailsRs.getAlbumTitle() + " (" + Instant.ofEpochSecond(
                        tralbumDetailsRs.getReleaseDate()).atZone(
                        ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE) + ")");

        TralbumTrack track = tralbumDetailsRs.getTracks().get(trackNumber);
        System.out.println("track: \t" + track.getBandName() + " - " + track.getTitle());
        System.out.print("tags: \t");
        tralbumDetailsRs.getTags().forEach(t -> System.out.print(t.getNormName() + "; "));
        System.out.println("");
        System.out.println("url: \t" + tralbumDetailsRs.getBandcampUrl());
        System.out.println("");
    }
}
