package com.sudocipher.budget.tracker.common.utils

import android.os.Handler
import android.os.Looper

fun ioThread(run: Runnable) {
    Thread(run).start()
}

fun ensureMainThread(run: Runnable) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        run.run()
    } else {
        Handler(Looper.getMainLooper()).post(run)
    }
}