package com.iteo.graphqlapp

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.internal.subscription.SubscriptionResponse
import com.iteo.graphqlapp.graphql.JobDetailsQuery
import com.iteo.graphqlapp.graphql.JobsQuery
import com.iteo.graphqlapp.graphql.SubscribeUserMutation
import com.iteo.graphqlapp.graphql.type.JobInput

class JobRepository(private val apolloClient: ApolloClient) {

    suspend fun getJobs(): Response<JobsQuery.Data> =
        apolloClient.query( // Ask for data
            JobsQuery() // Say which data
        ).toDeferred().await() // To coroutine

    suspend fun getJob(jobInput: JobInput): Response<JobDetailsQuery.Data> =
        apolloClient.query( // Ask for data
            JobDetailsQuery( // Say which data
                jobInput // Provide job identifier
            )
        ).toDeferred().await() // To coroutine
}