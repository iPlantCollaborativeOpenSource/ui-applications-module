package org.iplantc.core.uiapps.client.presenter.proxy;

import java.util.List;

import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.services.AppUserServiceFacade;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.sencha.gxt.data.client.loader.RpcProxy;

public class PublicAppGroupProxy extends RpcProxy<AppGroup, List<AppGroup>> {

    private final AppUserServiceFacade appService;

    @Inject
    public PublicAppGroupProxy(AppUserServiceFacade appService) {
        this.appService = appService;
    }

    @Override
    public void load(AppGroup loadConfig, final AsyncCallback<List<AppGroup>> callback) {
        appService.getPublicAppGroups(callback);
    }
}
