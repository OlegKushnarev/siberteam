package ru.siberteam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.launcher.SortLauncher;
import ru.siberteam.sorter.Sorter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Stream;

public class Sorters {
    private static final Logger LOG = LogManager.getLogger(Sorters.class);

    private final Path inputPath;
    private final String outputFile;
    private final Set<Sorter> sorters;

    public Sorters(String inputFile, String outputFile, Set<Sorter> sorters) {
        inputPath = Paths.get(inputFile);
        this.outputFile = outputFile;
        this.sorters = sorters;
    }

    private Path getOutputPath(String fileNameSuffix) {
        if (sorters.size() > 1) {
            if (outputFile.contains(".")) {
                StringBuilder sb = new StringBuilder(outputFile);
                sb.insert(outputFile.lastIndexOf('.'), fileNameSuffix);
                return Paths.get(sb.toString());
            }
            return Paths.get(outputFile + fileNameSuffix);
        }
        return Paths.get(outputFile);
    }

    private boolean sort(SortLauncher sortLauncher) {
        try (Stream<String> stringStream = Files.lines(inputPath)) {
            Stream<String> strStream = sortLauncher.launchSort(stringStream);
            Files.write(getOutputPath(sortLauncher.getSorterName()), (Iterable<String>) strStream::iterator);
            return true;
        } catch (IOException e) {
            LOG.error("Error working with file!", e);
            return false;
        }
    }

    public boolean sortWords() {
        return sorters.stream()
                .map(SortLauncher::new)
                .allMatch(this::sort);
    }
}