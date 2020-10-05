package com.iteo.graphqlapp.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.iteo.graphqlapp.R
import com.iteo.graphqlapp.graphql.JobDetailsQuery
import com.iteo.graphqlapp.graphql.fragment.LocationFieldsFragment
import kotlinx.android.synthetic.main.view_country_list_item.view.*

class CountryAdapter : RecyclerView.Adapter<CountryAdapter.Item>() {

    private var items = emptyList<JobDetailsQuery.Country>()

    fun updateData(countries: List<JobDetailsQuery.Country>) {
        items = countries
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item =
        Item(
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.view_country_list_item, parent, false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Item, position: Int) {
        holder.bind(items[position])
    }

    class Item(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(country: JobDetailsQuery.Country) {
            with(country.fragments.locationFieldsFragment) {
                itemView.name.text = name
                itemView.isoCode.text = isoCode

                showJobs(itemView, jobs ?: emptyList())
            }
        }
        private fun showJobs(view: View, jobs: List<LocationFieldsFragment.Job>) {
            when(jobs.size) {
                0 -> {}
                1 -> {
                    view.otherJob1.isVisible = true
                    view.otherJob1.text = jobs[0].title
                }
                2 -> {
                    view.otherJob1.isVisible = true
                    view.otherJob1.text = jobs[0].title

                    view.otherJob2.isVisible = true
                    view.otherJob2.text = jobs[1].title
                }
                else -> {
                    view.otherJob1.isVisible = true
                    view.otherJob1.text = jobs[0].title

                    view.otherJob2.isVisible = true
                    view.otherJob2.text = jobs[1].title

                    view.otherJob3.isVisible = true
                    view.otherJob3.text = jobs[2].title
                }
            }
        }
    }
}