package com.iteo.graphqlapp.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.iteo.graphqlapp.R
import com.iteo.graphqlapp.graphql.JobDetailsQuery
import com.iteo.graphqlapp.graphql.JobsQuery
import kotlinx.android.synthetic.main.view_city_list_item.view.*
import kotlinx.android.synthetic.main.view_job_list_item.view.*

class CityAdapter : RecyclerView.Adapter<CityAdapter.Item>() {

    private var items = emptyList<JobDetailsQuery.City>()

    fun updateData(cities: List<JobDetailsQuery.City>) {
        items = cities
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item =
        Item(
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.view_city_list_item, parent, false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Item, position: Int) {
        holder.bind(items[position])
    }

    class Item(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(city: JobDetailsQuery.City) = with(itemView) {
            name.text = city.name
        }
    }
}