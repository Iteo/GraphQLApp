package com.iteo.graphqlapp.details

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.iteo.graphqlapp.R
import com.iteo.graphqlapp.graphql.JobDetailsQuery
import com.iteo.graphqlapp.graphql.JobsQuery
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val EXTRA_JOB_SLUG = "DetailsActivity.jobSlug"
private const val EXTRA_COMPANY_SLUG = "DetailsActivity.companySlug"

class DetailsActivity : AppCompatActivity() {

    private val viewModel: DetailsViewModel by viewModel()

    private val cityAdapter by lazy { CityAdapter() }
    private val countryAdapter by lazy { CountryAdapter() }

    private val jobSlug by lazy { intent.getStringExtra(EXTRA_JOB_SLUG) }
    private val companySlug by lazy { intent.getStringExtra(EXTRA_COMPANY_SLUG) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        cityList.adapter = cityAdapter
        countryList.adapter = countryAdapter

        observerViewModel()
        viewModel.loadData(jobSlug, companySlug)
    }

    private fun observerViewModel() {
        viewModel.viewState.observe(this, Observer { state ->
            when (state) {
                Loading -> onLoading()
                is Loaded -> onLoaded(state.details)
                is Error -> onError(state.error)
            }
        })
    }

    private fun onLoading() {
        loading.isVisible = true
        dataContainer.isVisible = false
        error.isVisible = false
    }

    private fun onLoaded(details: JobDetailsQuery.Data) {
        loading.isVisible = false
        dataContainer.isVisible = true
        error.isVisible = false

        with(details) {
            jobTitle.text = job.title
            val tags = job.tags ?: emptyList()
            tag1.text = tags.getOrNull(0)?.name
            tag2.text = tags.getOrNull(1)?.name
            tag3.text = tags.getOrNull(2)?.name

            cityAdapter.updateData(cities ?: emptyList())
            countryAdapter.updateData(countries)
        }
    }

    private fun onError(errorMessage: String) {
        loading.isVisible = false
        dataContainer.isVisible = false
        error.isVisible = true

        error.text = errorMessage
    }
}

fun showJobDetails(context: Context, job: JobsQuery.Job) {
    context.startActivity(
        Intent(context, DetailsActivity::class.java).apply {
            putExtra(EXTRA_JOB_SLUG, job.slug)
            putExtra(EXTRA_COMPANY_SLUG, job.company?.slug ?: "")
        }
    )
}
