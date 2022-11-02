package com.dallmeier.evidencer.utils;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.dallmeier.evidencer.utils.customview.CustomOverlayItem;

import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;

import java.util.ArrayList;

public class EvidentOSMOverlay extends ItemizedOverlay<CustomOverlayItem> {
    protected ArrayList<CustomOverlayItem> mOverlays = new ArrayList<CustomOverlayItem>();
    private OnMarkerTapListener mOnMakerTapListener;
    protected OnSingleMarkerTapListener mOnSingleMarkerTapListener;

    public EvidentOSMOverlay(Drawable pDefaultMarker) {
        super(pDefaultMarker);
    }

    public static interface OnMarkerTapListener {
        public void onTap(int index);
    }

    public static interface OnSingleMarkerTapListener {
        public boolean onSingleTap(MotionEvent arg0, MapView arg1);
    }

    public static interface OnSingleMapTouchListener {
        public boolean onSingleTouch(MotionEvent arg0, MapView arg1);
    }

    public void addOverlay(CustomOverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }

    @Override
    protected CustomOverlayItem createItem(int i) {
        return mOverlays.get(i);
    }

    @Override
    public int size() {
        return mOverlays.size();
    }

    protected boolean onTap(int index) {
        if (mOnMakerTapListener != null) {
            mOnMakerTapListener.onTap(index);
        }
        return true;
    }

    /*@Override
    public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
        if (mOnSingleMarkerTapListener != null) {
            return mOnSingleMarkerTapListener.onSingleTap(e, mapView);
        }
        return false;
    }
*/
    @Override
    public void onDetach(MapView mapView) {
        clearOverlay();
        super.onDetach(mapView);
    }

    @Override
    public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3) {
        return false;
    }

    public void setOnMakerTapListener(OnMarkerTapListener markerListener) {
        mOnMakerTapListener = markerListener;
    }

    public void setOnSingleMakerTapListener(OnSingleMarkerTapListener markerListener) {
        mOnSingleMarkerTapListener = markerListener;
    }

    public void clearOverlay() {
        mOverlays.clear();
        populate();
    }

    public void removeOverlay(int index) {
        mOverlays.remove(index);
        populate();
    }

    public void removeOverlay(CustomOverlayItem overlay) {
        mOverlays.remove(overlay);
        populate();
    }

    public void addOverlayPos(int index, CustomOverlayItem overlay) {
        mOverlays.add(index, overlay);
        populate();
    }

    public void setOverlay(int index, CustomOverlayItem overlay) {
        mOverlays.set(index, overlay);
        populate();
    }

    public int getOverlayItemIndex(CustomOverlayItem overlayItem) {
        return mOverlays.indexOf(overlayItem);
    }
}
