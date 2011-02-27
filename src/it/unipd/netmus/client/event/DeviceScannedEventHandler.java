/**
 * 
 */
package it.unipd.netmus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author ValterTexasGroup
 *
 */
public interface DeviceScannedEventHandler extends EventHandler {
    void onScanDevice(DeviceScannedEvent event);
}
