package io.rifleh700.bcradio.cli;

import java.io.InputStream;
import java.util.Scanner;

public class InputStreamArgsListener implements Runnable {

    private final InputStream is;
    private final ArgsHandler handler;

    public InputStreamArgsListener(InputStream is,
                                   ArgsHandler handler) {

        this.is = is;
        this.handler = handler;
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(is);

        while (true) {

            if (Thread.interrupted()) {
                Thread.currentThread().interrupt();
                break;
            }

            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue;

            String[] args = line.split("\\s+");
            handler.handle(args);
        }

        scanner.close();
    }
}
