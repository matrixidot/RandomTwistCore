package me.neo.randomtwistcore;

import me.neo.randomtwistcore.commands.Commands;

public class RTCAPI {
    public static void onEnable() {
        new Commands(RTC.plugin);
    }
}
