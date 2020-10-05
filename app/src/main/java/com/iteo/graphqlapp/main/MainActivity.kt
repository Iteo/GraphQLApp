package com.iteo.graphqlapp.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iteo.graphqlapp.R
import com.iteo.graphqlapp.details.showJobDetails
import com.iteo.graphqlapp.graphql.JobsQuery
import com.iteo.graphqlapp.user.showLoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private val adapter by lazy {
        JobAdapter(
            clickListener = { job -> showJobDetails(this, job) },
            glide = Glide.with(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.adapter = adapter

        observerViewModel()
    }

    private fun observerViewModel() {
        viewModel.viewState.observe(this, Observer { state ->
            when (state) {
                Loading -> onLoading()
                is Loaded -> onLoaded(state.list)
                is Error -> onError(state.error)
            }
        })
    }

    private fun onLoading() {
        loading.isVisible = true
        list.isVisible = false
        error.isVisible = false
    }

    private fun onLoaded(jobs: List<JobsQuery.Job>) {
        loading.isVisible = false
        list.isVisible = true
        error.isVisible = false

        adapter.updateData(jobs)
    }

    private fun onError(errorMessage: String) {
        loading.isVisible = false
        list.isVisible = false
        error.isVisible = true

        error.text = errorMessage
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == R.id.accountAction) {
            showLoginActivity(this)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
}
