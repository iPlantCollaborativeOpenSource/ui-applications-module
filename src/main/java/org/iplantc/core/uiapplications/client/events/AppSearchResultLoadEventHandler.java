package org.iplantc.core.uiapplications.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * An EventHandler interface for AppSearchResultLoadEvents.
 * 
 * @author psarando
 * 
 */
public interface AppSearchResultLoadEventHandler extends EventHandler {

    void onLoad(AppSearchResultLoadEvent event);
}
