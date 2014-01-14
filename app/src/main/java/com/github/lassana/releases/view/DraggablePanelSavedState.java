package com.github.lassana.releases.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;

/**
 * @author lassana
 * @since 1/14/14
 */
public class DraggablePanelSavedState extends Preference.BaseSavedState {

    protected boolean isOpened;

    protected DraggablePanelSavedState(Parcel source) {
        super(source);
    }

    protected DraggablePanelSavedState(Parcelable superState) {
        super(superState);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(isOpened);
    }


    public static final Parcelable.Creator<DraggablePanelSavedState> CREATOR
            = new Parcelable.Creator<DraggablePanelSavedState>() {
        public DraggablePanelSavedState createFromParcel(Parcel in) {
            return new DraggablePanelSavedState(in);
        }

        public DraggablePanelSavedState[] newArray(int size) {
            return new DraggablePanelSavedState[size];
        }
    };
}
