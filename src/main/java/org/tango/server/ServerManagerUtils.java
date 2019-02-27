package org.tango.server;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import fr.esrf.Tango.DevFailed;
import org.tango.client.ez.util.TangoUtils;
import org.tango.server.export.TangoExporter;
import org.tango.server.servant.DeviceImpl;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 04.02.2016
 */
public class ServerManagerUtils {
    private ServerManagerUtils(){}

    public static void writePidFile(@Nullable Path prefix) throws IOException {
        ServerManager serverManager = ServerManager.getInstance();

        Preconditions.checkState(serverManager.isStarted(), "Server must be started first!");

        String pid = serverManager.getPid();

        File pidFile = Optional.ofNullable(prefix)
                .orElse(Paths.get(System.getProperty("user.dir")))
                .resolve(Paths.get(serverManager.getExecName() + ".pid")).toFile();

        Files.write(pid.getBytes(),
                pidFile);

        pidFile.deleteOnExit();
    }

    /**
     *
     * @param instance
     * @param clazz
     * @param <T>
     * @return collections that contains business objects of the Tango instance
     * @throws java.lang.RuntimeException
     */
    public static <T> List<T> getBusinessObjects(String instance, final Class<T> clazz){
        try {
            Field tangoExporterField = ServerManager.getInstance().getClass().getDeclaredField("tangoExporter");
            tangoExporterField.setAccessible(true);
            final TangoExporter tangoExporter = (TangoExporter) tangoExporterField.get(ServerManager.getInstance());

            final String[] deviceList = tangoExporter.getDevicesOfClass(clazz.getSimpleName());

            if (deviceList.length == 0) //No tango devices were found. Simply skip the following
                return Collections.emptyList();

            return Arrays.stream(deviceList)
                    .map(s -> {
                        try {
                            DeviceImpl deviceImpl = tangoExporter.getDevice(clazz.getSimpleName(), s);
                            return (T) deviceImpl.getBusinessObject();
                        } catch (DevFailed devFailed) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (DevFailed devFailed) {
            throw new RuntimeException(TangoUtils.convertDevFailedToException(devFailed));
        }
    }
}
