package com.backtolife.survey.server;

import com.backtolife.survey.util.Timing;
import com.backtolife.survey.ble.PacketV1WihtRssi;
import com.backtolife.survey.util.Id;

import java.util.List;

public interface ServerInterface {
    /**
     * An approximate offset (1 second precision), in seconds, between the local timeFromEpoh(),
     * and server timeFroEpoch()
     */
    double clockOffsetFromServer();

    /**
     * List of packets (received in BLE) to send to the server
     * @param packets packets received
     * @return
     */
    boolean sendPackets(Id id, List<PacketV1WihtRssi> packets);

    /**
     *
     * @param sent the accurate timings of signals sent by this instance.
     * @param received the accurate timings of signals received by this instance.
     * @return
     */
    boolean sendTimings(Id id, List<Timing> sent, List<Timing> received);

    /**
     * Sets the
     * @param id the id of this
     * @param deviceInfo information about the device, like model, kernel build etc.
     * @param serverAddress http address of the server.
     */
    void initServer(Id id, String deviceInfo, String serverAddress);
}
