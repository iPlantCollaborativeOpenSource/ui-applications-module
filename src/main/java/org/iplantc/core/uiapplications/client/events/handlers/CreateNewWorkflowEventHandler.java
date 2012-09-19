package org.iplantc.core.uiapplications.client.events.handlers;

import com.google.gwt.event.shared.EventHandler;

public interface CreateNewWorkflowEventHandler extends EventHandler {

    /**
     * Fire when a user wants to create a new workflow.
     */
    void createNewWorkflow();

}
