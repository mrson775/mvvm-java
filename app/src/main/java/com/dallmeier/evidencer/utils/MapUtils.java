package com.dallmeier.evidencer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.utils.customview.CustomOverlayItem;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.dallmeier.evidencer.common.Constant.DOMAIN;

public class MapUtils {
    /**
     * init map
     *
     * @param context      Context
     * @param minZoomLevel int
     * @param maxZoomLevel int
     * @param mapContainer View
     * @return MapView
     */
    public static MapView createMapView(Context context, int minZoomLevel, int maxZoomLevel, RelativeLayout mapContainer) {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        MapView mMapView = new MapView(context);
        mMapView.setTilesScaledToDpi(true);
        mapContainer.addView(mMapView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mMapView.getZoomController().setVisibility(
                CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);

        // Add tiles layer with custom tile source
        final MapTileProviderBasic tileProvider = new MapTileProviderBasic(context);
        final ITileSource tileSource = new XYTileSource("FietsRegionaal", minZoomLevel, maxZoomLevel, 256, ".png",
                new String[]{DOMAIN + "/osm/", "http://tile.openstreetmap.org/"});
        tileProvider.setTileSource(tileSource);
        tileProvider.setTileRequestCompleteHandler(mMapView.getTileRequestCompleteHandler());
        final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, context);
        tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
        mMapView.getOverlays().add(tilesOverlay);
        return mMapView;
    }

    /**
     * zoomToCurrentLocation
     *
     * @param mapView   MapView
     * @param mLocation Location
     */
    public static void zoomToCurrentLocation(Context context, MapView mapView, GeoPoint mLocation) {
        try {
            mapView.getController().setZoom(15D);
            mapView.getController().setCenter(mLocation);
            Marker m = new Marker(mapView);
            m.setIcon(Utils.getIcon(context, R.drawable.ic_current_location));
            m.setPosition(mLocation);
            m.setOnMarkerClickListener((marker, mapView1) -> false);
            mapView.getOverlays().add(m);
            mapView.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void addSingleMarkerToMapView(Context context, MapView mapView, IncidentEntity incidentParent) {
        GeoPoint point = new GeoPoint(incidentParent.getLocation().getCoordinates().get(1), incidentParent.getLocation().getCoordinates().get(0), incidentParent.getLocation().getCoordinates().get(2));
        Marker m = new Marker(mapView);
        m.setIcon(Utils.getIcon(context, R.drawable.ic_marker_online));
        m.setPosition(point);
        mapView.getOverlays().add(m);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void addMarkerToMapWithCurrentUser(Context context, EvidentOSMOverlay evidentOSMOverlay, MapView mapView, IncidentEntity incident) {
        GeoPoint point = new GeoPoint(incident.getLastLatitude(), incident.getLastLongitude());
        CustomOverlayItem overlayItem = new CustomOverlayItem("Incident", " incidentEntity.getShortDescription()", point, incident);
        Drawable incidentMarker = Utils.getIcon(context, R.drawable.ic_marker_online);
        overlayItem.setMarker(incidentMarker);
        evidentOSMOverlay.addOverlay(overlayItem);
        if (mapView != null) {
            mapView.getOverlays().add(evidentOSMOverlay);
            // BoundingBox boundingBox = BoundingBox.fromGeoPoints((ArrayList<GeoPoint>) result);
            /*mapView.zoomToBoundingBox(boundingBox);*/
            mapView.invalidate();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void addMarkerSingleWithAllUser(Context context, EvidentOSMOverlay evidentOSMOverlay, MapView mapView, IncidentEntity incidentEntity) {
        if (incidentEntity == null) {
            return;
        }
        GeoPoint point = new GeoPoint(incidentEntity.getLastLatitude(), incidentEntity.getLastLongitude());
        CustomOverlayItem overlayItem = new CustomOverlayItem("Incident", " incidentEntity.getShortDescription()", point, incidentEntity);
        Drawable incidentMarker = Utils.getIcon(context, R.drawable.ic_marker_online);
        overlayItem.setMarker(incidentMarker);
        evidentOSMOverlay.addOverlay(overlayItem);
        if (mapView != null) {
            mapView.getController().setZoom(15D);
            mapView.getController().setCenter(point);
            mapView.getOverlays().add(evidentOSMOverlay);
            // BoundingBox boundingBox = BoundingBox.fromGeoPoints((ArrayList<GeoPoint>) result);
            /*mapView.zoomToBoundingBox(boundingBox);*/
            mapView.invalidate();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void addMarkerWithCreateIncident(Context context, EvidentOSMOverlay evidentOSMOverlay, MapView mapView, Double latitude, Double longitude) {
        GeoPoint point = new GeoPoint(latitude, longitude);
        CustomOverlayItem overlayItem = new CustomOverlayItem("Incident", " incidentEntity.getShortDescription()", point);
        Drawable incidentMarker = Utils.getIcon(context, R.drawable.ic_marker_place_red);
        overlayItem.setMarker(incidentMarker);
        evidentOSMOverlay.addOverlay(overlayItem);
        if (mapView != null) {
            mapView.getController().setZoom(15D);
            mapView.getController().setCenter(point);
            mapView.getOverlays().add(evidentOSMOverlay);
            // BoundingBox boundingBox = BoundingBox.fromGeoPoints((ArrayList<GeoPoint>) result);
            /*mapView.zoomToBoundingBox(boundingBox);*/
            mapView.invalidate();
        }
    }

    public static String getAddressFromLatLon(Context context, double currentLat, double currentLon) {
        Geocoder geocoder;
        List<Address> yourAddresses;
        String yourAddress = "";
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            yourAddresses = geocoder.getFromLocation(currentLat, currentLon, 1);
            if (yourAddresses.size() > 0) {
                yourAddress = yourAddresses.get(0).getAddressLine(0);
               /* String yourCity = yourAddresses.get(0).getAddressLine(1);
                String yourCountry = yourAddresses.get(0).getAddressLine(2);*/
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return yourAddress;
    }
}
