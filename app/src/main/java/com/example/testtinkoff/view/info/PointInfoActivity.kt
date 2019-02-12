package com.example.testtinkoff.view.info

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.example.testtinkoff.application.GlideApp
import com.example.testtinkoff.mvp.PointsPresenter
import com.example.testtinkoff.utils.DisplayManager
import com.example.testtinkoff.network.models.PartnerModel
import com.example.testtinkoff.network.models.PayloadModel
import kotlinx.android.synthetic.main.activity_point_info.*
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.AppCompatTextView
import com.example.testtinkoff.R

class PointInfoActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "PointInfoActivity"

        const val POINT_INFO_AGR = "point_info_arg"
    }

    private var partner: PartnerModel? = null
    private var point: PayloadModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_info)

        point = intent.getParcelableExtra(POINT_INFO_AGR)
        if (!point?.partnerName.isNullOrEmpty()) {
            partner = PointsPresenter.getPartner(point?.partnerName!!)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.title = partner?.name ?: point?.partnerName ?: ""

        setImage(partner?.picture)
        setSite(partner?.url)
        setPaymentInfo()
    }

    private fun setImage(imageUrl: String?) {
        GlideApp.with(this)
            .load(DisplayManager.getBasePictureUrl() + imageUrl)
            .centerInside()
            .into(ivIcon)
    }

    private fun setSite(url: String?) {
        if (url.isNullOrEmpty()) {

            ivWebIcon.visibility = View.GONE
            tvSite.visibility = View.GONE

        } else {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val address = Uri.parse(url)
                    val openlinkIntent = Intent(Intent.ACTION_VIEW, address)

                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(openlinkIntent)
                    }
                }
            }

            val spannableString = SpannableString(url)
            spannableString.setSpan(clickableSpan, 0, url.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            tvSite.text = spannableString
            tvSite.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun setPaymentInfo() {
        setTextViewInfo(tvHoursTitle, tvHours, point?.workHours)
        setTextViewInfo(tvDurationTitle, tvDuration, partner?.depositionDuration)
        setTextViewInfo(tvLimitsTitle, tvLimitations, partner?.limitations)
        setTextViewInfo(tvDescriptionTitle, tvDescription, partner?.description)

        tvPointType.text = formatText(partner?.pointType) ?: ""
    }

    private fun formatText(text: String?): String? {
        return text?.replace("&nbsp;", " ")
            ?.replace("<br>", " ")
    }

    private fun setTextViewInfo(titleView: AppCompatTextView, infoView: AppCompatTextView, info: String?) {
        if (info.isNullOrEmpty()) {
            titleView.visibility = View.GONE
            infoView.visibility = View.GONE
        } else {
            infoView.text = formatText(info)
        }
    }
}