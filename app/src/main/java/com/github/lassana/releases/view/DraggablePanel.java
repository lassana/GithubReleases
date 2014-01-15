package com.github.lassana.releases.view;

/**
 * @author lassana
 * @since 1/15/14
 */
public interface DraggablePanel {

    void setPanelListener(DraggablePanelListener panelListener);

    void switchState();

    void closeBottomPanel();

    void openBottomPanel();

    boolean isSlidingPanelOpened();

    boolean isBottomPanelOpened();
}
