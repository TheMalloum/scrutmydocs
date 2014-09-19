package org.scrutmydocs.contract;

import org.scrutmydocs.plugins.SMDAbstractPlugin;

import java.util.Date;
import java.util.List;

public class SMDSettings {
    public final List<SMDAbstractPlugin> smdAbstractPlugins;
   
    public Date lastScan;

    public SMDSettings(List<SMDAbstractPlugin> smdAbstractPlugins) {
        this.smdAbstractPlugins = smdAbstractPlugins;
    }
}
