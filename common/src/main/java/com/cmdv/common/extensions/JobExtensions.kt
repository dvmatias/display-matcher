package com.cmdv.common.extensions

import kotlinx.coroutines.Job

fun Job?.cancelIfActive() {
    this?.let { if (isActive) cancel() }
}