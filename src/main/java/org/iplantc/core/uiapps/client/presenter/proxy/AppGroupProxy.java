package org.iplantc.core.uiapps.client.presenter.proxy;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.Services;
import org.iplantc.core.uiapps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroupList;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;

/**
 * @author jstroot
 *
 */
public class AppGroupProxy extends RpcProxy<AppGroup, List<AppGroup>> {

    private final AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);

    public AppGroupProxy() {
    }

    @Override
    public void load(AppGroup loadConfig, final AsyncCallback<List<AppGroup>> callback) {
        Services.APP_SERVICE.getAppGroups(UserInfo.getInstance().getWorkspaceId(),
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        AutoBean<AppGroupList> bean = AutoBeanCodex.decode(factory,
                                AppGroupList.class, result);
                        AppGroupList as = bean.as();
                        List<AppGroup> groups = as.getGroups();
                        callback.onSuccess(groups);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.analysisGroupsLoadFailure(), caught);
                        callback.onFailure(caught);
                    }
                });

    }

}
