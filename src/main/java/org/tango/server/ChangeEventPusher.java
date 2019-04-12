package org.tango.server;

import fr.esrf.Tango.DevFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tango.server.attribute.AttributeValue;
import org.tango.server.device.DeviceManager;
import org.tango.server.events.EventType;
import org.tango.utils.DevFailedUtils;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 4/12/19
 */
public class ChangeEventPusher<T> implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(ChangeEventPusher.class);

    private final AttributeValue value;
    private final String attrName;

    private final DeviceManager deviceManager;

    public ChangeEventPusher(String attrName, T value, DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
        AttributeValue attributeValue;
        try {
            attributeValue = new AttributeValue(value);
        } catch (DevFailed devFailed) {
            logger.error("Failed to create AttributeValue due to...");
            DevFailedUtils.logDevFailed(devFailed, logger);
            attributeValue = null;
        }
        this.value = attributeValue;
        this.attrName = attrName;
    }

    @Override
    public void run() {
        if (value != null)
            try {
                deviceManager.pushEvent(attrName, value, EventType.CHANGE_EVENT);
            } catch (DevFailed devFailed) {
                logger.error("Failed to push event due to...");
                DevFailedUtils.logDevFailed(devFailed, logger);
            }
    }
}
