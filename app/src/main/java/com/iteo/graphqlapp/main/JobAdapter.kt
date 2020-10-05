package com.iteo.graphqlapp.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.iteo.graphqlapp.R
import com.iteo.graphqlapp.graphql.JobsQuery
import kotlinx.android.synthetic.main.view_job_list_item.view.*

class JobAdapter(
    private val clickListener: (JobsQuery.Job) -> Unit,
    private val glide: RequestManager
) : RecyclerView.Adapter<JobAdapter.Item>() {

    private var items = emptyList<JobsQuery.Job>()

    fun updateData(jobs: List<JobsQuery.Job>) {
        items = jobs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item =
        Item(
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.view_job_list_item, parent, false
            ),
            clickListener,
            glide
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Item, position: Int) {
        holder.bind(items[position])
    }

    class Item(
        view: View,
        private val clickListener: (JobsQuery.Job) -> Unit,
        private val glide: RequestManager
    ) : RecyclerView.ViewHolder(view) {
        fun bind(job: JobsQuery.Job) = with(itemView) {

            setOnClickListener { clickListener(job) }

            title.text = job.title
            country.text = job.countries?.firstOrNull()?.name ?: "Unknown country"
            city.text = job.cities?.firstOrNull()?.name ?: "Unknown city"
            with(glide) {
                clear(image)
                load(job.company?.logoUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(image)
            }

        }
    }
}