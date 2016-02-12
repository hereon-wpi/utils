package hzg.wpn.xenv;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Queues;
import sun.misc.Queue;

import java.lang.Object;import java.lang.String;
import java.util.List;
import java.util.Map;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 2/11/16
 */
public class Experiment {
    public String id;
    public int totalNumberOfMonitoredAttributes;
    public List<String> monitoredAttributes;
    public Map<String, Long> monitoredAttributesUpdates;
    public Map<String, Object> data;
    public EvictingQueue<Message> lastMessages = EvictingQueue.create(15);
}
