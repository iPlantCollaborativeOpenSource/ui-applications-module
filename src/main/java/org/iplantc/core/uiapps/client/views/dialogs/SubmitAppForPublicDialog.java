/**
 * 
 */
package org.iplantc.core.uiapps.client.views.dialogs;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.events.AppGroupCountUpdateEvent;
import org.iplantc.core.uiapps.client.events.AppGroupCountUpdateEvent.AppGroupType;
import org.iplantc.core.uiapps.client.gin.AppsInjector;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView.Presenter;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.info.SuccessAnnouncementConfig;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * @author sriram
 * 
 */
public class SubmitAppForPublicDialog extends IPlantDialog {


    private final class SubmitAppForPublicCallbackImpl implements AsyncCallback<String> {
        @Override
        public void onSuccess(String url) {
            hide();

            IplantAnnouncer.getInstance().schedule(
                    new SuccessAnnouncementConfig(SafeHtmlUtils.fromTrustedString(I18N.DISPLAY.makePublicSuccessMessage(url))));

            // Create and fire event
            AppGroupCountUpdateEvent event = new AppGroupCountUpdateEvent(false,
                    AppGroupType.BETA);
            EventBus.getInstance().fireEvent(event);
        }

        @Override
        public void onFailure(Throwable caught) {
            hide();
            if (caught != null) {
                ErrorHandler.post(I18N.DISPLAY.makePublicFail(), caught);
            }
        }
    }

    public SubmitAppForPublicDialog(final App selectedApp) {
        initDialog();
        final Presenter p = AppsInjector.INSTANCE.getSubmitAppForPublixUsePresenter();
        p.go(this, selectedApp, new SubmitAppForPublicCallbackImpl());
        setOkButtonText(I18N.DISPLAY.submit());
        addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                p.onSubmit();
            }
        });
        addCancelButtonSelectHandler(new SelectHandler() {
            
            @Override
            public void onSelect(SelectEvent event) {
                hide();
                
            }
        });
    }

    private void initDialog() {
        setHeadingText(I18N.DISPLAY.publicSubmissionForm()); //$NON-NLS-1$
        setPixelSize(615, 480);
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        setHideOnButtonClick(false);
    }

}
