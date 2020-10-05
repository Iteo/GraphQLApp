package com.iteo.graphqlapp

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCache
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.iteo.graphqlapp.details.DetailsViewModel
import com.iteo.graphqlapp.main.MainViewModel
import com.iteo.graphqlapp.user.LoginViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    private val networkModule = module {
        single {
            OkHttpClient()
                .newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build()
        }

        single<NormalizedCacheFactory<SqlNormalizedCache>> {
            SqlNormalizedCacheFactory(get(), "apollo_cache_db")
        }

        single {
            ApolloClient
                .builder()
                .serverUrl(BuildConfig.API_URL) // Define API url
                .okHttpClient(get())
                .normalizedCache(get<NormalizedCacheFactory<SqlNormalizedCache>>())
                .build()
        }
    }

    private val repositoryModule = module {
        single { JobRepository(get()) }
        single { UserRepository(get()) }
    }

    private val viewModelModule = module {
        viewModel { MainViewModel(get()) }
        viewModel { DetailsViewModel(get()) }
        viewModel { LoginViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(networkModule, repositoryModule, viewModelModule)
        }
    }

}