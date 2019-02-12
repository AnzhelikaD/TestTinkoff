package com.example.testtinkoff.view.listPoints

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.testtinkoff.R
import com.example.testtinkoff.application.GlideApp
import com.example.testtinkoff.mvp.PointsPresenter
import com.example.testtinkoff.network.models.PayloadModel
import com.example.testtinkoff.utils.DisplayManager
import com.example.testtinkoff.utils.ViewingController

class PointsAdapter(val context: Context?, val callback: (item: PayloadModel?) -> Unit) :
    RecyclerView.Adapter<PointsAdapter.Holder>() {

    private var points: ArrayList<PayloadModel>? = ArrayList()

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            inflater.inflate(
                R.layout.item_points_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return points?.size ?: 0
        //return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = points?.get(position)
        val partner = if (item?.partnerName != null) {
            PointsPresenter.getPartner(item.partnerName!!)
        } else {
            null
        }

        with(holder) {
            GlideApp
                .with(context!!)
                .load(DisplayManager.getBasePictureUrl() + partner?.picture)
                .centerCrop()
                .circleCrop()
                .into(ivIcon)

            tvName.text = partner?.name ?: item?.partnerName ?: ""
            tvAddress.text = item?.fullAddress

            itemView.setOnClickListener {
                callback(item)
            }

            if (ViewingController.contains(item?.externalId)) {
                flViewed.visibility = View.VISIBLE
            } else {
                flViewed.visibility = View.GONE
            }
        }
    }

    fun addAll(items: List<PayloadModel>) {
        addAll(items, points?.size ?: 0)
    }

    fun addAll(items: List<PayloadModel>?, startPosition: Int) {
        if (items.isNullOrEmpty()) {
            return
        }
        val initialSize = points?.size
        points?.addAll(startPosition, items)

        notifyDataSetChanged()

        if (initialSize == 0) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(startPosition, items.size)
        }
    }

    fun clear() {
        val size = points?.size
        points?.clear()
        notifyItemRangeRemoved(0, size ?: 0)
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: AppCompatImageView = view.findViewById(R.id.ivIcon)
        val tvName: AppCompatTextView = view.findViewById(R.id.tvName)
        val tvAddress: AppCompatTextView = view.findViewById(R.id.tvAddress)
        val flViewed: FrameLayout = view.findViewById(R.id.flViewed)
    }
}