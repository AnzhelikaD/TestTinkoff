package com.example.testtinkoff.view.listPoints

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testtinkoff.view.info.PointInfoActivity
import com.example.testtinkoff.R
import com.example.testtinkoff.mvp.PointsPresenter
import com.example.testtinkoff.network.models.PayloadModel
import com.example.testtinkoff.utils.ViewingController
import kotlinx.android.synthetic.main.fragment_list_points.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ListPointsFragment: Fragment() {
    companion object {
        private const val TAG = "ListPointsFragment"

        fun newInstance(): ListPointsFragment {
            return ListPointsFragment()
        }
    }

    private var points: List<PayloadModel>? = null
    private var adapter: PointsAdapter? = null

    private var pointsLoadedSubscription: Subscription? = null
    private val pointsLoadedBus = PointsPresenter.pointsLoadedBus

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_points, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PointsAdapter(context) {
            if (!it?.externalId.isNullOrEmpty()) {
                ViewingController.addPoint(it?.externalId!!)
            }

            val intent = Intent(activity, PointInfoActivity::class.java)
            intent.putExtra(PointInfoActivity.POINT_INFO_AGR, it)
            activity?.startActivity(intent)
        }

        rvPoints.layoutManager = LinearLayoutManager(context)
        rvPoints.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        adapter?.notifyDataSetChanged()

        if (pointsLoadedSubscription == null) {
            pointsLoadedSubscription = pointsLoadedBus.observeEvents()
                .subscribeOn(Schedulers.io())
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { event ->
                    when (event) {
                        is PointsPresenter.PointsLoadedEvent -> {
                            adapter?.clear()
                            if (event.points != null) {
                                adapter?.addAll(event.points)
                            }
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
}