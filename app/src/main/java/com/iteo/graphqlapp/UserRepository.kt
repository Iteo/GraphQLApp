package com.iteo.graphqlapp

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import com.iteo.graphqlapp.graphql.SubscribeUserMutation
import com.iteo.graphqlapp.graphql.type.SubscribeInput

class UserRepository(private val apolloClient: ApolloClient) {

    suspend fun register(name: String, email: String): Response<SubscribeUserMutation.Data> {
        val user = SubscribeInput(name, email)
        return apolloClient.mutate(SubscribeUserMutation(user)).toDeferred().await()
    }
}