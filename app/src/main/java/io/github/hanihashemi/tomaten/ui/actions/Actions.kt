package io.github.hanihashemi.tomaten.ui.actions

import io.github.hanihashemi.tomaten.MainViewModel

class Actions(viewModel: MainViewModel) {
    val login = LoginAction(viewModel)
}

val previewActions = Actions(viewModel = FakeViewModel())

class FakeViewModel : MainViewModel()
