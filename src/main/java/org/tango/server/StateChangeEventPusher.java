package org.tango.server;

import fr.esrf.Tango.DevState;
import org.tango.DeviceState;
import org.tango.server.device.DeviceManager;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 4/18/19
 */
public class StateChangeEventPusher extends ChangeEventPusher<DevState> {
    public StateChangeEventPusher(DeviceState value, DeviceManager deviceManager) {
        super("State", value.getDevState(), deviceManager);
    }
}
