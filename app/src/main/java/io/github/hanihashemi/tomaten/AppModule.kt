package io.github.hanihashemi.tomaten

import io.github.hanihashemi.tomaten.data.repository.TagRepository
import io.github.hanihashemi.tomaten.data.repository.TimerSessionRepository
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        singleOf(::TimerSessionRepository)
        singleOf(::TagRepository)
        viewModel { MainViewModel(get(), get(), shouldFetchCurrentUser = true) }
    }
