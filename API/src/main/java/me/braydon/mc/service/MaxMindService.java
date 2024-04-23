/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.braydon.mc.service;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Braydon
 */
@Service @Log4j2(topic = "MaxMind")
public final class MaxMindService {
    /**
     * The directory to store databases.
     */
    private static final File DATABASES_DIRECTORY = new File("databases");

    /**
     * The endpoint to download database files from.
     */
    private static final String DATABASE_DOWNLOAD_ENDPOINT = "https://download.maxmind.com/app/geoip_download?edition_id=%s&license_key=%s&suffix=tar.gz";

    @Value("${maxmind.license}")
    private String license;

    /**
     * The currently loaded databases.
     */
    private final Map<Database, DatabaseReader> databases = new HashMap<>();

    @PostConstruct
    public void onInitialize() {
        // Load the databases
        if (!license.equals("CHANGE_ME")) {
            loadDatabases();
        }
    }

    /**
     * Load the databases.
     */
    @SneakyThrows
    private void loadDatabases() {
        log.info("Loading databases...");

        // Create the directory if it doesn't exist
        if (!DATABASES_DIRECTORY.exists()) {
            DATABASES_DIRECTORY.mkdirs();
        }

        // Download missing databases
        for (Database database : Database.values()) {
            File databaseFile = new File(DATABASES_DIRECTORY, database.getEdition() + ".mmdb");
            if (!databaseFile.exists()) { // Doesn't exist, download it
                downloadDatabase(database, databaseFile);
            }
            // Load the database and store it
            databases.put(database, new DatabaseReader.Builder(databaseFile)
                    .withCache(new CHMCache()) // Enable caching
                    .build()
            );
            log.info("Loaded database {}", database.getEdition());
        }
        log.info("Loaded {} database(s)", databases.size());
    }

    /**
     * Lookup a city by the given address.
     *
     * @param address the address
     * @return the city response
     */
    @SneakyThrows @NonNull
    public CityResponse lookupCity(@NonNull InetAddress address) {
        return getDatabase(Database.CITY).city(address);
    }

    /**
     * Download the required files
     * for the given database.
     *
     * @param database the database to download
     * @param databaseFile the file for the database
     */
    @SneakyThrows
    private void downloadDatabase(@NonNull Database database, @NonNull File databaseFile) {
        File downloadedFile = new File(DATABASES_DIRECTORY, database.getEdition() + ".tar.gz"); // The downloaded file

        // Download the database if required
        if (!downloadedFile.exists()) {
            log.info("Downloading database {}...", database.getEdition());
            long before = System.currentTimeMillis();
            try (
                    BufferedInputStream inputStream = new BufferedInputStream(new URL(DATABASE_DOWNLOAD_ENDPOINT.formatted(database.getEdition(), license)).openStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(downloadedFile)
            ) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            }
            log.info("Downloaded database {} in {}ms", database.getEdition(), System.currentTimeMillis() - before);
        }

        // Extract the database once downloaded
        log.info("Extracting database {}...", database.getEdition());
        TarGZipUnArchiver archiver = new TarGZipUnArchiver();
        archiver.setSourceFile(downloadedFile);
        archiver.setDestDirectory(DATABASES_DIRECTORY);
        archiver.extract();
        log.info("Extracted database {}", database.getEdition());

        // Locate the database file in the extracted directory
        File[] files = DATABASES_DIRECTORY.listFiles();
        assert files != null; // Ensure files is present
        dirLoop: for (File directory : files) {
            if (!directory.isDirectory() || !directory.getName().startsWith(database.getEdition())) {
                continue;
            }
            File[] downloadedFiles = directory.listFiles();
            assert downloadedFiles != null; // Ensures downloaded files is present

            // Find the file for the database, move it to the
            // correct directory, and delete the downloaded contents
            for (File file : downloadedFiles) {
                if (file.isFile() && file.getName().equals(databaseFile.getName())) {
                    Files.move(file.toPath(), databaseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    // Delete the downloaded contents
                    FileUtils.deleteDirectory(directory);
                    FileUtils.deleteQuietly(downloadedFile);

                    break dirLoop; // We're done here
                }
            }
        }
    }

    /**
     * Get the reader for the given database.
     *
     * @param database the database to get
     * @return the database reader
     */
    @NonNull
    public DatabaseReader getDatabase(@NonNull Database database) {
        return databases.get(database);
    }

    /**
     * Cleanup when the app is destroyed.
     */
    @PreDestroy @SneakyThrows
    public void cleanup() {
        for (DatabaseReader database : databases.values()) {
            database.close();
        }
        databases.clear();
    }

    /**
     * A database for MaxMind.
     */
    @AllArgsConstructor @Getter @ToString
    public enum Database {
        CITY("GeoLite2-City");

        /**
         * The edition of this database.
         */
        @NonNull private final String edition;
    }
}