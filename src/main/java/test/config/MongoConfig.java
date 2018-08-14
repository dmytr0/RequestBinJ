package test.config;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.extract.ITempNaming;
import de.flapdoodle.embed.process.extract.UUIDTempNaming;
import de.flapdoodle.embed.process.io.directories.IDirectory;
import de.flapdoodle.embed.process.io.directories.UserHome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;
import java.nio.file.Paths;

@EnableMongoRepositories(basePackages = "test.**.repository")
@Configuration
public class MongoConfig {
    private static final String MONGO_DB_URL = "localhost";
    private static final int MONGO_DB_PORT = 27017;
    private static final String MONGO_DB_NAME = "requestedbin";
    public static final String MONGO_PATH = ".requestedbin_db";

    @Bean("mongoConf")
    public IMongodConfig getConfig() throws IOException {
        Storage replication = new Storage(Paths.get(System.getProperty("user.home")).resolve(MONGO_PATH).toString(), null, 0);
        return new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .replication(replication)
                .build();
    }

    @Bean("mongoRuntimeConf")
    public IRuntimeConfig getRuntimeConfig() {
        Command command = Command.MongoD;
        IDirectory artifactStorePath = new UserHome(MONGO_PATH);
        ITempNaming executableNaming = new UUIDTempNaming();

        return new RuntimeConfigBuilder()
                .defaults(command)
                .artifactStore(new ExtractedArtifactStoreBuilder()
                        .defaults(command)
                        .download(new DownloadConfigBuilder()
                                .defaultsForCommand(command)
                                .artifactStorePath(artifactStorePath).build())
                        .executableNaming(executableNaming))
                .build();
    }

}
