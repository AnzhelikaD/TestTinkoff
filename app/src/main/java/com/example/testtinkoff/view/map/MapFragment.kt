package com.example.testtinkoff.view.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.testtinkoff.R
import com.example.testtinkoff.application.GlideApp
import com.example.testtinkoff.mvp.PointsPresenter
import com.example.testtinkoff.network.models.PayloadModel
import com.example.testtinkoff.utils.DisplayManager
import com.example.testtinkoff.utils.ViewingController
import com.example.testtinkoff.view.info.PointInfoActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.item_points_list.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MapFragment: Fragment(), GoogleMap.OnMarkerClickListener {
    companion object {
        private const val TAG = "MapFragment"

        const val REQUEST_CODE_LOCATION = 10

        fun newInstance() : MapFragment {
            return MapFragment()
        }
    }

    private lateinit var map: GoogleMap
    private lateinit var mapView: MapView

    private var pointsLoadedSubscription: Subscription? = null
    private val pointsLoadedBus = PointsPresenter.pointsLoadedBus

    private var location: Location? = null

    private var points: List<PayloadModel>? = null

    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PointsPresenter.getPartners()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState ?: Bundle())
        mapView.onResume()

        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView.getMapAsync {
            map = it

            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
            map.uiSettings.isMapToolbarEnabled = false

            map.setPadding(0, 0, 0, 200)

            if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

                map.isMyLocationEnabled = true

                val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (location?.latitude != null && location?.longitude != null) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location?.latitude!!, location?.longitude!!), 15f))
                }

            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_LOCATION
                )
            }

            map.setOnCameraIdleListener {
                getPoints()
            }

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun getPoints() {
        val visibleRegion = map.projection.visibleRegion
        val center = visibleRegion.latLngBounds.center
        val leftCorner = visibleRegion.latLngBounds.northeast

        val results = FloatArray(1)
        Location.distanceBetween(center.latitude, center.longitude, leftCorner.latitude, leftCorner.longitude, results)

        PointsPresenter.loadPoints(center?.latitude, center?.longitude, results[0].toLong())
    }

    private fun onPointsLoaded(points: List<PayloadModel>?) {
        if (points == null) {

            Toast.makeText(context, resources.getString(R.string.error_load_points), Toast.LENGTH_SHORT).show()

        } else {
            this.points = points

            map.clear()

            if (!points.isNullOrEmpty()) {
                for (i in 0 until  points.size) {
                    val lat = points[i].location?.latitude
                    val lng = points[i].location?.longitude

                    if (lat != null && lng != null) {
                        val marker = map.addMarker(MarkerOptions()
                            .position(LatLng(lat, lng))
                            .title(points[i].partnerName ?: ""))
                        marker.tag = i
                    }
                }
            }

            map.setOnMarkerClickListener(this)
            map.setOnMapClickListener {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker?.tag is Int) {
            val point = points?.get(marker.tag as Int)
            if (point?.partnerName != null) {
                val partner = PointsPresenter.getPartner(point.partnerName!!)

                GlideApp.with(context!!)
                    .load(DisplayManager.getBasePictureUrl() + partner?.picture)
                    .centerInside()
                    .circleCrop()
                    .into(ivIcon)

                tvName.text = partner?.name ?: point.partnerName
                tvAddress.text = point.addressInfo ?: ""
            }

            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

            bottomSheet.setOnClickListener {
                onBottomSheetClick(point)
            }
        }

        return false
    }

    private fun onBottomSheetClick(point: PayloadModel?) {
        if (!point?.externalId.isNullOrEmpty()) {
            ViewingController.addPoint(point?.externalId!!)
        }

        val intent = Intent(activity, PointInfoActivity::class.java)
        intent.putExtra(PointInfoActivity.POINT_INFO_AGR, point)
        activity?.startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_LOCATION
            && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            map.isMyLocationEnabled = true
        }
    }

    override fun onStart() {
        super.onStart()

        if (pointsLoadedSubscription == null) {
            pointsLoadedSubscription = pointsLoadedBus.observeEvents()
                .subscribeOn(Schedulers.io())
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { event ->
                    when (event) {
                        is PointsPresenter.PointsLoadedEvent -> {
                            onPointsLoaded(event.points)
                        }
                    }
                }
        }
    }

    override fun onStop() {
        super.onStop()

        pointsLoadedSubscription?.unsubscribe()
        pointsLoadedSubscription = null
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        //presenter?.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
