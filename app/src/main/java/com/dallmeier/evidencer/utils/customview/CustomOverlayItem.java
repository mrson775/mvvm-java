package com.dallmeier.evidencer.utils.customview;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

public class CustomOverlayItem extends OverlayItem {
    private Object data;

    public CustomOverlayItem(String aTitle, String aSnippet, GeoPoint aGeoPoint) {
        super(aTitle, aSnippet, aGeoPoint);
    }

    public CustomOverlayItem(String aUid, String aTitle, String aDescription, GeoPoint aGeoPoint) {
        super(aUid, aTitle, aDescription, aGeoPoint);
    }

    public CustomOverlayItem(String aTitle, String aSnippet, GeoPoint aGeoPoint, Object data) {
        super(aTitle, aSnippet, aGeoPoint);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }

        if (data == null) {
            return false;
        }

        if (!data.getClass().equals(o.getClass())) {
            return false;
        }
        CustomOverlayItem o1 = (CustomOverlayItem) o;
        if (data.equals(o1.getData())) {
            return true;
        }
        return false;
    }
}
