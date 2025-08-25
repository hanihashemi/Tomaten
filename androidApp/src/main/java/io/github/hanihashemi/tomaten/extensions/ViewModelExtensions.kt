package io.github.hanihashemi.tomaten.extensions

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private val coroutineExceptionHandler = CoroutineExceptionHandler { _, t -> Logger.e("Coroutine error", t) }

internal fun CoroutineScope.launchSafely(
    context: CoroutineContext,
    launchBody: suspend CoroutineScope.() -> Unit,
): Job = launch(context + coroutineExceptionHandler) { launchBody.invoke(this) }
