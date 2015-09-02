package hzg.wpn.xenv;

import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class ResourceManagerTest {
    @BeforeClass
    public static void beforeClass() {
        System.setProperty("XENV_ROOT", Paths.get("").toAbsolutePath().toString());
    }

    @Test
    public void testLoadResource_fromClassPath() throws Exception {
        Properties result = ResourceManager.loadResource("loadProperties.properties");

        assertEquals("world", result.getProperty("hello"));
    }

    @Test
    public void testLoadResource_fromFileSystem() throws Exception {
        Properties result = ResourceManager.loadResource("src/test", "loadProperties_from_FileSystem.properties");

        assertEquals("kitty!!!", result.getProperty("hello"));
    }
}