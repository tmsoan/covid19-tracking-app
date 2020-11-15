package com.anos.covid19.views.country

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.anos.covid19.R
import com.anos.covid19.model.CountryItem
import kotlinx.android.synthetic.main.layout_select_country_item.view.*
import timber.log.Timber

class CountriesSearchAdapter(
        currentList: List<CountryItem>,
        private val interaction: Interaction? = null
) : RecyclerView.Adapter<CountriesSearchAdapter.CountryItemViewHolder>() {

    var countries: List<CountryItem> = currentList
        set(lst) {
            field = lst
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
        holder.bind(countries[position])
        Timber.e("============== ${countries[position].country}")
    }

    override fun getItemCount(): Int {
        Timber.e("============== ${countries.size}")
        return countries.size
    }


    class CountryItemViewHolder constructor(
            itemView: View,
            private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CountryItem) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            tv_country_name.text = item.country
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: CountryItem)
    }
}