package com.anos.covid19.views.countrydetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.model.CountryStatus
import com.anos.covid19.utils.getIntThousandFormat
import com.anos.covid19.utils.getUpdatedDateString
import kotlinx.android.synthetic.main.layout_filtered_country_item.view.*
import timber.log.Timber

class FilteredCountriesAdapter(
        private var countries: List<CountryStatus>,
        private val interaction: Interaction? = null
) : RecyclerView.Adapter<FilteredCountriesAdapter.CountryItemViewHolder>() {

    fun update(currentList: List<CountryStatus>) {
        countries = currentList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryItemViewHolder {
        return CountryItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_filtered_country_item,
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

        fun bind(pos: Int, item: CountryStatus) = with(itemView) {
            itemView.setOnClickListener {
                Timber.e("${item.countryCode}")
                interaction?.onTopCountryItemSelected(item)
            }
            item.date?.let {
                tv_date.text = "${getUpdatedDateString(it)}"
            } ?: kotlin.run {
                tv_date.text = "Unknown date"
            }
            tv_confirmed.text = "${getIntThousandFormat(item.confirmed ?: 0)}"
            tv_active.text = "${getIntThousandFormat(item.active ?: 0)}"
            tv_recovered.text = "${getIntThousandFormat(item.recovered ?: 0)}"
            tv_death.text = "${getIntThousandFormat(item.deaths ?: 0)}"
        }
    }

    interface Interaction {
        fun onTopCountryItemSelected(country: CountryStatus)
    }
}