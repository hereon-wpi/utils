package hzg.wpn;

import sun.misc.Unsafe;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 7/4/16
 */
public class NativeSupport {
    private static final Unsafe UNSAFE = UnsafeSupport.UNSAFE;

    static {
        int addressSize = UNSAFE.addressSize();
        if (addressSize != 8)
            throw new Error("Invalid address size [= " + addressSize + " Byte]! Address size must be equal to 8 Byte (64 bits). Probably using unsupported JVM... try x64 JVM");

        String os_name = System.getProperty("os.name");

        if (!"linux".equalsIgnoreCase(os_name)) throw new Error("Currently only linux is supported!");
    }

    /**
     * Loads library into Java's classpath
     *
     * @param libraryName the library
     */
    public static void loadLibrary(String libraryName){
        System.setProperty("java.library.path", Paths.get("./lib/native/x86_64-linux-gnu").toAbsolutePath().toString());

        resetClassLoader();

        System.loadLibrary(libraryName);
    }

    /**
     * Extracts specified library from the .jar file. The library will be extracted into {cwd}/lib/native/x86_64-linux-gnu
     *
     * NOTE: Library must be at /lib/native/x86_64-linux-gnu/
     *
     * @param libraryName the library
     */
    public static void extractLibrary(String libraryName) {
        InputStream lib = NativeSupport.class.getResourceAsStream("/lib/native/x86_64-linux-gnu/lib"+libraryName+".so");

        Path cwd = Paths.get(".");

        try {
            Files.copy(lib,
                    Files.createDirectories(cwd.resolve("lib/native/x86_64-linux-gnu")).resolve("lib"+libraryName+".so"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Unable to extract native library.", e);
        }
    }


    private static void resetClassLoader() {
        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
