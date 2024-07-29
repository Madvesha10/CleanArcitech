package com.example.feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.ui.common.Resource
import com.example.feature.domain.entity.UsersEntity
import com.example.feature.presentation.StateHolder.StateHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

open class BaseViewModel : ViewModel() {
    private val usersStateHolder = MutableStateFlow(StateHolder(isLoading = false))
    fun executeApi(call: suspend () -> Flow<Resource<List<UsersEntity>>>): Flow<Resource<List<UsersEntity>>> {
        return flow {
            call.invoke().collect {
                when (it) {
                    is Resource.Loading -> {
                        usersStateHolder.value = StateHolder(isLoading = true)
                        emit(value = it)
                    }

                    is Resource.Success -> {
                        usersStateHolder.value = StateHolder(data = it.data)
                        emit(value = it)
                    }

                    is Resource.Error -> {
                        usersStateHolder.value = StateHolder(error = it.message.toString())
                        emit(value = it)
                    }
                }
            }
        }
    }
}
