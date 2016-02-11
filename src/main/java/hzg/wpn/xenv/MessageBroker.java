package hzg.wpn.xenv;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.PipeBlob;
import org.tango.client.ez.util.TangoUtils;
import org.tango.server.device.DeviceManager;
import org.tango.server.dynamic.DynamicManager;
import org.tango.server.pipe.PipeValue;

import java.io.IOException;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 2/11/16
 */
public class MessageBroker {
    private final DeviceManager deviceManager;
    private final MessageComposer composer;

    public MessageBroker(DeviceManager deviceManager, MessageComposer composer) {
        this.deviceManager = deviceManager;
        //TODO add pipe xenv.msg
        this.composer = composer;
    }


    public void send() throws IOException {
        try {
            Message msg = composer.compose();
            deviceManager.pushPipeEvent("xenv.msg", new PipeValue(messageToPipeBlob(msg)));
        } catch (DevFailed devFailed) {
            throw new IOException(TangoUtils.convertDevFailedToException(devFailed));
        }
    }


    private PipeBlob messageToPipeBlob(Message msg){
        return null;
    }
}
