package com.cmdv.core.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat

inline fun <reified T : Activity> Activity.navigateTo(bundle: Bundle?, options: ActivityOptionsCompat?, finish: Boolean) {
    val intent = Intent(this, T::class.java)
    bundle?.let { intent.putExtras(bundle) }
    overridePendingTransition(0, 0)
    ActivityCompat.startActivity(this, intent, options?.toBundle())
    if (finish) this.finish()
}

inline fun <reified T : Activity> Activity.navigateForResultTo(bundle: Bundle?, options: ActivityOptionsCompat?, requestCode: Int, finish: Boolean) {
    val intent = Intent(this, T::class.java)
    bundle?.let { intent.putExtras(bundle) }
    overridePendingTransition(0, 0)
    ActivityCompat.startActivityForResult(this, intent, requestCode, options?.toBundle())
    if (finish) this.finish()
}