package com.iteo.graphqlapp.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iteo.graphqlapp.JobRepository
import com.iteo.graphqlapp.graphql.JobDetailsQuery
import com.iteo.graphqlapp.graphql.type.JobInput
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.java.KoinJavaComponent.inject

class DetailsViewModel(private val jobRepository: JobRepository) : ViewModel(), KoinComponent {

    private val mutableViewState = MutableLiveData<DetailsViewState>(Loading)
    val viewState: LiveData<DetailsViewState> = mutableViewState

    fun loadData(jobSlug: String, companySlug: String) {
        if (jobSlug.isBlank() || companySlug.isBlank()) {
            mutableViewState.value = Error("Cannot find matching job")
            return
        }

        viewModelScope.launch {
            try {
                val response = jobRepository.getJob(JobInput(companySlug, jobSlug))
                response.data?.let { mutableViewState.postValue(Loaded(it)) }
            } catch (e: Exception) {
                mutableViewState.postValue(Error(e.message ?: "Unknown error"))
            }
        }
    }
}

sealed class DetailsViewState
object Loading : DetailsViewState()
data class Loaded(val details: JobDetailsQuery.Data) : DetailsViewState()
data class Error(val error: String) : DetailsViewState()