package org.iplantc.core.uiapplications.client.views;

import java.util.List;

import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.models.autobeans.AppGroup;
import org.iplantc.core.uicommons.client.images.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
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
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * TODO JDS Need to implement means of adding to north, east and south panels.
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

    @UiField
    Tree<AppGroup, String> tree;

    @UiField(provided = true)
    final TreeStore<AppGroup> treeStore;

    @UiField
    Grid<App> grid;

    @UiField
    GridView<App> gridView;

    @UiField(provided = true)
    final ListStore<App> listStore;

    @UiField(provided = true)
    final ColumnModel<App> cm;

    @UiField
    BorderLayoutContainer con;

    // TODO JDS rename nav, main, and detail panels to east, center, west
    @UiField
    ContentPanel navPanel;
    @UiField
    ContentPanel mainPanel;
    @UiField
    ContentPanel detailPanel;

    @UiField
    BorderLayoutData northData;
    @UiField
    BorderLayoutData eastData;

    private final Widget widget;

    public AppsViewImpl(TreeStore<AppGroup> treeStore, ListStore<App> listStore,
            ColumnModel<App> cm) {
        // XXX JDS Using Dependency injection, you can get global references to stores
        this.treeStore = treeStore;
        this.listStore = listStore;
        this.cm = cm;
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
    }

    /**
     * FIXME JDS Move this implementation into the ui.xml
     */
    private void setTreeIcons() {
        com.sencha.gxt.widget.core.client.tree.TreeStyle style = tree.getStyle();
        style.setNodeCloseIcon(Resources.ICONS.category());
        style.setNodeOpenIcon(Resources.ICONS.category_open());
        style.setLeafIcon(Resources.ICONS.subCategory());
    }

    @UiFactory
    public ValueProvider<AppGroup, String> createValueProvider() {
        return new ValueProvider<AppGroup, String>() {

            @Override
            public String getValue(AppGroup object) {
                return object.getName();
            }

            @Override
            public void setValue(AppGroup object, String value) {
            }

            @Override
            public String getPath() {
                return "name";
            }
        };
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
    public TreeStore<AppGroup> getTreeStore() {
        return treeStore;
    }

    @Override
    public void setTreeLoader(final TreeLoader<AppGroup> treeLoader) {
        this.tree.setLoader(treeLoader);
    }

    @Override
    public void setCenterPanelHeading(final String name) {
        mainPanel.setHeadingText(name);
    }

    @Override
    public void maskCenterPanel(final String loadingMask) {
        mainPanel.mask(loadingMask);
    }

    @Override
    public void unMaskCenterPanel() {
        mainPanel.unmask();
    }

    @Override
    public void maskWestPanel(String loadingMask) {
        navPanel.mask(loadingMask);
    }

    @Override
    public void unMaskWestPanel() {
        navPanel.unmask();
    }

    @Override
    public void setListLoader(ListLoader<ListLoadConfig, ListLoadResult<App>> listLoader) {
        grid.setLoader(listLoader);
    }

    @Override
    public void selectApp(String appId) {
        App app = listStore.findModelWithKey(appId);
        if (app != null) {
            grid.getSelectionModel().select(app, true);
        }
    }

    @Override
    public void selectAppGroup(String appGroupId) {
        AppGroup ag = treeStore.findModelWithKey(appGroupId);
        if (ag != null) {
            tree.getSelectionModel().select(ag, true);
            // Set heading
            setCenterPanelHeading(ag.getName());
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

}
