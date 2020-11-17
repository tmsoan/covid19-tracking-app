package com.anos.covid19.views.country

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.anos.covid19.R
import com.anos.covid19.model.Country
import com.anos.covid19.utils.getIntThousandFormat
import kotlinx.android.synthetic.main.layout_select_country_item.view.*
import kotlinx.android.synthetic.main.layout_select_country_item.view.tv_country_name
import kotlinx.android.synthetic.main.layout_select_country_item.view.tv_new_case
import kotlinx.android.synthetic.main.layout_top_country_item.view.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class CountriesSearchAdapter(
        private var countries: List<Country>,
        private val interaction: Interaction? = null
) : RecyclerView.Adapter<CountriesSearchAdapter.CountryItemViewHolder>(), Filterable {

    private var selectedCountry: String? = null
    private lateinit var countriesFiltered: List<Country>

    init {
        countriesFiltered = ArrayList()
        countriesFiltered = countries
    }

    fun update(currentList: List<Country>, currentCountryCode: String) {
        countries = currentList
        countriesFiltered = currentList
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
        holder.bind(position, countriesFiltered[position], selectedCountry)
    }

    override fun getItemCount(): Int {
        return countriesFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                countriesFiltered = if (charString.isEmpty()) {
                    countries
                } else {
                    val filteredList: MutableList<Country> = ArrayList()
                    for (row in countries) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.country?.toLowerCase()?.contains(charString.toLowerCase()) == true) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = countriesFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                notifyDataSetChanged()
            }
        }
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
            tv_country_name.text = "${item.order}. ${item.country}"
            tv_number_cases.text = getIntThousandFormat(item.totalConfirmed ?: 0)
            tv_new_case.text = "+${getIntThousandFormat(item.newConfirmed ?: 0)}"

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