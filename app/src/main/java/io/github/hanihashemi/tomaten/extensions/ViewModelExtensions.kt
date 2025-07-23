package io.github.hanihashemi.tomaten.extensions

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

private val coroutineExceptionHandler = CoroutineExceptionHandler { _, t -> Timber.e(t) }

internal fun CoroutineScope.launchSafely(
    context: CoroutineContext,
    launchBody: suspend CoroutineScope.() -> Unit,
): Job = launch(context + coroutineExceptionHandler) { launchBody.invoke(this) }
