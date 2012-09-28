package org.iplantc.core.uiapplications.client.views.widgets.proxy;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapplications.client.models.autobeans.AppList;
import org.iplantc.core.uiapplications.client.services.AppServiceFacade;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;

public class AppSearchRpcProxy3 extends RpcProxy<AppLoadConfig, AppListLoadResult> {

    private final AppServiceFacade templateService;
    private String lastQueryText;

    public AppSearchRpcProxy3() {
        this.templateService = Services.APP_SERVICE;
    }

    @Override
    public void load(final AppLoadConfig loadConfig,
            final AsyncCallback<AppListLoadResult> callback) {
        lastQueryText = loadConfig.getQuery();
        if (lastQueryText == null) {
            return;
        }
        templateService.searchApp(lastQueryText.toLowerCase(), new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(String result) {
                AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);
                AutoBean<AppList> bean = AutoBeanCodex.decode(factory, AppList.class, result);

                List<App> apps = bean.as().getApps();

                Collections.sort(apps, new AppComparator());
                
                AppListLoadResult searchResult = AppSearchAutoBeanFactory.instance.dataLoadResult()
                        .as();
                searchResult.setData(apps);
                callback.onSuccess(searchResult);
            }
        });
    }

    public String getLastQueryText() {
        return lastQueryText;
    }

    private final class AppComparator implements Comparator<App> {
        @Override
        public int compare(App app1, App app2) {
            String app1Name = app1.getName();
            String app2Name = app2.getName();

            boolean app1NameMatches = app1Name.toLowerCase().contains(getLastQueryText().toLowerCase());
            boolean app2NameMatches = app2Name.toLowerCase().contains(getLastQueryText().toLowerCase());

            if (app1NameMatches && !app2NameMatches) {
                // Only app1's name contains the search term, so order it before app2
                return -1;
            }
            if (!app1NameMatches && app2NameMatches) {
                // Only app2's name contains the search term, so order it before app1
                return 1;
            }

            return app1Name.compareToIgnoreCase(app2Name);
        }
    }
}
