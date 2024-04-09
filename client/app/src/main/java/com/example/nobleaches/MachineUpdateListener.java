package com.example.nobleaches;

import java.util.List;

public interface MachineUpdateListener {
    void onMachineDataUpdate(List<MachineData> newData);
}
