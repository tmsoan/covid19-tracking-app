package com.anos.covid19.views.topcountry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.utils.getIntThousandFormat
import kotlinx.android.synthetic.main.layout_top_country_item.view.*
import timber.log.Timber

class TopCountriesAdapter(
        private var countries: List<Country>,
        private val interaction: Interaction? = null
) : RecyclerView.Adapter<TopCountriesAdapter.CountryItemViewHolder>() {

    fun update(currentList: List<Country>) {
        countries = currentList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryItemViewHolder {
        return CountryItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_top_country_item,
                        parent,
                        false
                ),
                interaction
        )
    }

    override fun onBindViewHolder(holder: CountryItemViewHolder, position: Int) {
        holder.bind(position, countries[position])
    }

    override fun getItemCount(): Int {
        return countries.size
    }


    class CountryItemViewHolder constructor(
            itemView: View,
            private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(pos: Int, item: Country) = with(itemView) {
            itemView.setOnClickListener {
                Timber.e("${item.countryCode}")
                interaction?.onTopCountryItemSelected(item)
            }
            tv_no.text = "${pos + 1}."
            tv_country_name.text = item.country
            tv_num_case.text = getIntThousandFormat(item.totalConfirmed ?: 0)
            tv_new_case.text = "+${getIntThousandFormat(item.newConfirmed ?: 0)}"
        }
    }

    interface Interaction {
        fun onTopCountryItemSelected(country: Country)
    }
}