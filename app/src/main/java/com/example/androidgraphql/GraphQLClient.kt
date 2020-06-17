package com.example.androidgraphql

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

class GraphQLClient {

    companion object {
        var okHttpClient = OkHttpClient.Builder().build()
        var BASE_URL:String = "http://192.168.1.19:8089/graphql"
        var apolloClient = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .build()
    }
}