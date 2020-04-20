package com.backtolife.survey.ble;

import java.util.List;

public interface BLEInterface {
    void setCurrentPacket(PacketV1 packet);
    List<PacketV1> popReceivedPackets();
}
