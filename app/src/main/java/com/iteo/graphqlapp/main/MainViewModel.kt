package com.iteo.graphqlapp.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.iteo.graphqlapp.JobRepository
import com.iteo.graphqlapp.graphql.JobsQuery
import org.koin.core.KoinComponent

private const val TAG = "MainViewModel"

class MainViewModel(jobRepository: JobRepository) : ViewModel(), KoinComponent {

    val viewState = liveData {
        emit(Loading)

        try {
            val response = jobRepository.getJobs()
            emit(Loaded(response.data?.jobs ?: emptyList()))
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
            emit(Error(e.message ?: "Unknown error"))
        }
    }
}

sealed class MainViewState
object Loading : MainViewState()
data class Loaded(val list: List<JobsQuery.Job>) : MainViewState()
data class Error(val error: String) : MainViewState()