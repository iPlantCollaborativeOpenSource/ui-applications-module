package org.iplantc.core.uiapps.client.views;

import java.util.List;

import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.CellClickEvent.CellClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeAppearance;

/**
 *
 * @author jstroot
 *
 */
public class AppsViewImpl implements AppsView {
    /**
     * FIXME CORE-2992: Add an ID to the Categories panel collapse tool to assist QA.
     */
    private static String WEST_COLLAPSE_BTN_ID = "idCategoryCollapseBtn"; //$NON-NLS-1$
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("AppsView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, AppsViewImpl> {
    }

    private Presenter presenter;

    @UiField(provided = true)
    Tree<AppGroup, String> tree;

    @UiField(provided = true)
    TreeStore<AppGroup> treeStore;

    @UiField
    Grid<App> grid;

    @UiField
    GridView<App> gridView;

    @UiField
    ListStore<App> listStore;

    @UiField
    ColumnModel<App> cm;

    @UiField
    BorderLayoutContainer con;

    @UiField
    ContentPanel westPanel;
    @UiField
    ContentPanel centerPanel;
    @UiField
    ContentPanel eastPanel;

    @UiField
    BorderLayoutData northData;
    @UiField
    BorderLayoutData eastData;

    private final Widget widget;

    @Inject
    public AppsViewImpl(final Tree<AppGroup, String> tree, final TreeStore<AppGroup> treeStore) {
        this.tree = tree;
        this.treeStore = treeStore;
        this.widget = uiBinder.createAndBindUi(this);
        grid.addCellClickHandler(new CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent arg0) {
                // TODO Auto-generated method stub

            }
        });

        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<App>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<App> event) {
                if ((event.getSelection() != null) && !event.getSelection().isEmpty()) {
                    presenter.onAppSelected(event.getSelection().get(0));
                }
            }
        });

        tree.getSelectionModel().addSelectionChangedHandler(
                new SelectionChangedHandler<AppGroup>() {
                    @Override
                    public void onSelectionChanged(SelectionChangedEvent<AppGroup> event) {
                        if ((event.getSelection() != null) && !event.getSelection().isEmpty()) {
                            presenter.onAppGroupSelected(event.getSelection().get(0));
                    }
                    }
                });
        setTreeIcons();
        new QuickTip(grid).getToolTipConfig().setTrackMouse(true);
    }


    @UiFactory
    ListStore<App> createListStore() {
        return new ListStore<App>(new ModelKeyProvider<App>() {
            @Override
            public String getKey(App item) {
                return item.getId();
            }

        });
    }

    @UiFactory
    ColumnModel<App> createColumnModel() {
        return new AppColumnModel(this);
    }

    /**
     * FIXME JDS This needs to be implemented in an {@link TreeAppearance}
     */
    private void setTreeIcons() {
        com.sencha.gxt.widget.core.client.tree.TreeStyle style = tree.getStyle();
        style.setNodeCloseIcon(IplantResources.RESOURCES.category());
        style.setNodeOpenIcon(IplantResources.RESOURCES.category_open());
        style.setLeafIcon(IplantResources.RESOURCES.subCategory());
    }


    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ListStore<App> getListStore() {
        return listStore;
    }

    @Override
    public void setTreeLoader(final TreeLoader<AppGroup> treeLoader) {
        this.tree.setLoader(treeLoader);
    }

    @Override
    public void setCenterPanelHeading(final String name) {
        centerPanel.setHeadingText(name);
    }

    @Override
    public void maskCenterPanel(final String loadingMask) {
        centerPanel.mask(loadingMask);
    }

    @Override
    public void unMaskCenterPanel() {
        centerPanel.unmask();
    }

    @Override
    public void maskWestPanel(String loadingMask) {
        westPanel.mask(loadingMask);
    }

    @Override
    public void unMaskWestPanel() {
        westPanel.unmask();
    }

    @Override
    public void setListLoader(ListLoader<ListLoadConfig, ListLoadResult<App>> listLoader) {
        grid.setLoader(listLoader);
    }

    @Override
    public void selectApp(String appId) {
        App app = listStore.findModelWithKey(appId);
        if (app != null) {
            grid.getSelectionModel().select(app, false);
        }
    }

    @Override
    public void selectAppGroup(String appGroupId) {
        if (Strings.isNullOrEmpty(appGroupId)) {
            tree.getSelectionModel().deselectAll();
        } else {
            AppGroup ag = treeStore.findModelWithKey(appGroupId);
            if (ag != null) {
                tree.getSelectionModel().select(ag, false);
                tree.scrollIntoView(ag);
                // Set heading
                setCenterPanelHeading(ag.getName());
            }
        }
    }

    @Override
    public App getSelectedApp() {
        return grid.getSelectionModel().getSelectedItem();
    }

    @Override
    public AppGroup getSelectedAppGroup() {
        return tree.getSelectionModel().getSelectedItem();
    }

    @Override
    public void setApps(final List<App> apps) {
        listStore.clear();
        listStore.addAll(apps);
    }


	@Override
    public void setNorthWidget(IsWidget widget) {
        northData.setHidden(false);
        con.setNorthWidget(widget, northData);
	}

    @Override
    public void setEastWidget(IsWidget widget) {
        eastData.setHidden(false);
        con.setEastWidget(widget, eastData);
    }

    @Override
    public void selectFirstApp() {
        grid.getSelectionModel().select(0, false);
    }

    @Override
    public void selectFirstAppGroup() {
        AppGroup ag = treeStore.getRootItems().get(0);
        tree.getSelectionModel().select(ag, false);
        tree.scrollIntoView(ag);
    }

    @Override
    public void addAppGroup(AppGroup parent, AppGroup child) {
        if (child == null) {
            return;
        }

        if (parent == null) {
            treeStore.add(child);
        } else {
            treeStore.add(parent, child);
        }
    }

    @Override
    public void addAppGroups(AppGroup parent, List<AppGroup> children) {
        if ((children == null) || children.isEmpty()) {
            return;
        }
        if (parent == null) {
            treeStore.add(children);
        } else {
            treeStore.add(parent, children);
        }

        for (AppGroup ag : children) {
            addAppGroups(ag, ag.getGroups());
        }
    }

    @Override
    public void removeApp(App app) {
        grid.getSelectionModel().deselectAll();
        presenter.onAppSelected(null);
        listStore.remove(app);
    }

    @Override
    public void deSelectAllAppGroups() {
        tree.getSelectionModel().deselectAll();
    }

    @Override
    public void updateAppGroup(AppGroup appGroup) {
        treeStore.update(appGroup);
    }

    @Override
    public AppGroup findAppGroup(String id) {
        return treeStore.findModelWithKey(id);
    }

    @Override
    public AppGroup findAppGroupByName(String name) {
        for (AppGroup appGroup : treeStore.getAll()) {
            if (appGroup.getName().equalsIgnoreCase(name)) {
                return appGroup;
            }
        }

        return null;
    }

    @Override
    public void updateAppGroupAppCount(AppGroup appGroup, int newCount) {
        int difference = appGroup.getAppCount() - newCount;

        while (appGroup != null) {
            appGroup.setAppCount(appGroup.getAppCount() - difference);
            updateAppGroup(appGroup);
            appGroup = treeStore.getParent(appGroup);
        }

    }

    @Override
    public App findApp(String appId) {
        return listStore.findModelWithKey(appId);
    }

    @Override
    public void onAppInfoClick(App app) {
        presenter.onAppInfoClick(app);
    }

    @Override
    public Grid<App> getAppsGrid() {
        return grid;
    }

    @Override
    public void expandAppGroups() {
        tree.expandAll();
    }

    @Override
    public void onAppNameSelected(final App app) {
        presenter.onAppNameSelected(app);
    }
}
