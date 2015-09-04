package hzg.wpn.xenv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 02.09.2015
 */
public class ResourceManager {
    public static final String PROP_XENV_ROOT = "XENV_ROOT";
    public static final String XENV_ROOT = System.getenv(PROP_XENV_ROOT) != null ? System.getenv(PROP_XENV_ROOT) :
            System.getProperty(PROP_XENV_ROOT) != null ? System.getProperty(PROP_XENV_ROOT) : "";


    private static final Logger logger = LoggerFactory.getLogger(ResourceManager.class);

    static {
        logger.debug("XENV_ROOT=" + XENV_ROOT);
    }

    /**
     * Loads {@link Properties} from file specified by name either from the file system or classpath
     * <p/>
     *
     * @param name a file name
     * @return a new properties loaded from file
     * @throws IOException
     */
    public static Properties loadProperties(String name) throws IOException {
        return loadProperties("", name);
    }

    /**
     * Loads {@link Properties} from file specified by name either from the file system or classpath
     * <p/>
     *
     * @param name a file name
     * @return a new properties loaded from file
     * @throws IOException
     */
    public static Properties loadProperties(String prefix, String name) throws IOException {
        Properties result = new Properties();
        InputStream resource = loadResource(prefix, name);

        result.load(resource);
        logger.debug("Successfully load properties!");
        return result;
    }

    public static InputStream loadResource(String prefix, String name) throws IOException {
        logger.debug(String.format("Trying to load resource %s/%s/%s", XENV_ROOT, prefix, name));
        Path p = Paths.get(XENV_ROOT, prefix, name);
        if (Files.exists(p)) {
            logger.debug("Reading from the file system...");
            return Files.newInputStream(p);
        } else {
            logger.debug("Reading from the classpath...");
            InputStream resource = ResourceManager.class.getClassLoader().getResourceAsStream(name);
            if (resource == null) throw new IOException(String.format("Resource %s/%s not found", prefix, name));
            return resource;
        }
    }
}
