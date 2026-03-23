package com.gsm.taskmaster.di.module

import com.gsm.taskmaster.ui.adapter.TodoAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {
    @Provides
    fun providesTodoAdapter(): TodoAdapter = TodoAdapter()
}