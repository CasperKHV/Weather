package com.casperkhv.weather

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

internal class BindViewDelegate<T : View>(
    private val fragment: Fragment,
    @IdRes private val id: Int,
) {

    private var view: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (view == null) {
            val rootView = fragment.requireView()
            if (rootView.id == id) {
                @Suppress("UNCHECKED_CAST")
                view = rootView as T
            } else {
                view = rootView.findViewById(id)
            }
        }

        return requireNotNull(view)
    }
}

internal fun <T : View> Fragment.bindView(@IdRes id: Int): BindViewDelegate<T> {
    return BindViewDelegate(
        fragment = this,
        id = id,
    )
}

internal fun <T : View> Activity.bindView(@IdRes id: Int) = lazy<T>(LazyThreadSafetyMode.NONE) {
    findViewById(id)
}