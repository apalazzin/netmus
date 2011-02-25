/**
 * 
 */
package it.unipd.netmus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author ValterTexasGroup
 *
 */
public class DeviceScannedEvent extends GwtEvent<DeviceScannedEventHandler> {
    public static Type<DeviceScannedEventHandler> TYPE = new Type<DeviceScannedEventHandler>();

    @Override
    public Type<DeviceScannedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DeviceScannedEventHandler handler) {
        handler.onScanDevice(this);
    }

}
