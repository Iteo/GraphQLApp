package com.iteo.graphqlapp.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.iteo.graphqlapp.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        observeViewModel()

        submitAction.setOnClickListener {
            viewModel.login(name.text.toString(), email.text.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(this, Observer { state ->
            when (state) {
                is LoggedIn -> onLogged(state.id)
                is Error -> onError(state.error)
            }
        })
    }

    private fun onLogged(id: String) {
        responseView.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
        responseView.text = "Created user with id = $id"
        responseView.isVisible = true
        mainScope.launch {
            hideResponseWithDelay()
        }
    }

    private fun onError(errorMessage: String) {
        responseView.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
        responseView.text = errorMessage
        responseView.isVisible = true
        mainScope.launch {
            hideResponseWithDelay()
        }
    }

    private suspend fun hideResponseWithDelay() {
        delay(1500)
        responseView.isVisible = false
    }
}

fun showLoginActivity(context: Context) {
    context.startActivity(Intent(context, LoginActivity::class.java))
}