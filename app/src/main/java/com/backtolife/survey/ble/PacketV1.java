package com.backtolife.survey.ble;

import com.backtolife.survey.util.Timing;
import com.backtolife.survey.util.Id;

import java.util.List;

public class PacketV1 {
    public Id id;
    /// Maximal total length of timings (sent+received)=6
    public List<Timing> sentTimings;
    public List<Timing> receivedTimings;
}
