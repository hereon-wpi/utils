package hzg.wpn.xenv;

import java.io.IOException;
import java.nio.charset.Charset;
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

    /**
     * Loads {@link Properties} from file specified by name either from the file system or classpath
     * <p/>
     *
     * @param name a file name
     * @return a new properties loaded from file
     * @throws IOException
     */
    public static Properties loadResource(String name) throws IOException {
        return loadResource("", name);
    }

    /**
     * Loads {@link Properties} from file specified by name either from the file system or classpath
     * <p/>
     *
     * @param name a file name
     * @return a new properties loaded from file
     * @throws IOException
     */
    public static Properties loadResource(String prefix, String name) throws IOException {
        Properties result = new Properties();

        Path p = Paths.get(XENV_ROOT, prefix, name);
        if (Files.exists(p)) {
            result.load(Files.newBufferedReader(p, Charset.defaultCharset()));
        } else {
            result.load(ResourceManager.class.getClassLoader().getResourceAsStream(name));
        }

        return result;
    }
}
