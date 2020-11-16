package com.anos.covid19.views.country

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.utils.getIntThousandFormat
import kotlinx.android.synthetic.main.layout_select_country_item.view.*
import timber.log.Timber

class CountriesSearchAdapter(
        private var countries: List<Country>,
        private val interaction: Interaction? = null
) : RecyclerView.Adapter<CountriesSearchAdapter.CountryItemViewHolder>() {

    private var selectedCountry: String? = null

    fun update(currentList: List<Country>, currentCountryCode: String) {
        countries = currentList
        selectedCountry = currentCountryCode
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryItemViewHolder {
        return CountryItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_select_country_item,
                        parent,
                        false
                ),
                interaction
        )
    }

    override fun onBindViewHolder(holder: CountryItemViewHolder, position: Int) {
        holder.bind(position, countries[position], selectedCountry)
    }

    override fun getItemCount(): Int {
        return countries.size
    }


    class CountryItemViewHolder constructor(
            itemView: View,
            private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(pos: Int, item: Country, selectedCountry: String?) = with(itemView) {
            itemView.setOnClickListener {
                Timber.e("========== ${item.countryCode}")
                interaction?.onItemSelected(adapterPosition, item.countryCode ?: "")
            }
            tv_country_name.text = "${pos + 1}. ${item.country}"
            tv_number_cases.text = getIntThousandFormat(item.totalConfirmed ?: 0)

            selectedCountry?.let {
                if (item.countryCode == it) {
                    imv_check.visibility = View.VISIBLE
                } else {
                    imv_check.visibility = View.INVISIBLE
                }
            } ?: kotlin.run {
                imv_check.visibility = View.INVISIBLE
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, code: String)
    }
}