package com.cmdv.core.extensions

import kotlinx.coroutines.Job

fun Job?.cancelIfActive() {
    this?.let { if (isActive) cancel() }
}