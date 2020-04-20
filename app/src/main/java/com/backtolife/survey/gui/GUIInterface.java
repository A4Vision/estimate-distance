package com.backtolife.survey.gui;

import androidx.core.util.Pair;

import com.backtolife.survey.ble.PacketsStatistics;
import com.backtolife.survey.signal.SignalsStatistics;

import java.util.List;

public interface GUIInterface {
    void setSignalsStatistics(SignalsStatistics statistics);
    void setPacketsStatistics(PacketsStatistics statistics);
    void setDistancesInfo(List<Pair<String, Double>> distances);
}
