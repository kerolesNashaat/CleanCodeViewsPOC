package com.kiro.poc.util

import android.os.Handler
import android.os.Looper
import androidx.test.espresso.IdlingResource

/**
 * Generic poll-based [IdlingResource]. `PagingDataAdapter`/`ListAdapter` diff lists on a
 * background executor and post the RecyclerView update back to the main thread only once
 * diffing finishes - Espresso's default main-looper synchronization sees the main thread as
 * idle *before* that post happens, so asserting on RecyclerView content right after
 * `submitData`/`submitList` is racy. Registering one of these against a condition that only
 * becomes true once the update lands closes that gap without touching production code.
 */
class ConditionIdlingResource(
    private val resourceName: String,
    private val pollIntervalMs: Long = 50,
    private val condition: () -> Boolean
) : IdlingResource {

    private val handler = Handler(Looper.getMainLooper())

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = resourceName

    override fun isIdleNow(): Boolean {
        val idle = condition()
        if (idle) {
            callback?.onTransitionToIdle()
        } else {
            handler.postDelayed({ isIdleNow() }, pollIntervalMs)
        }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}
