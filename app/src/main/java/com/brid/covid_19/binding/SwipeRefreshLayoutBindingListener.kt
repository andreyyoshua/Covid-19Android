package com.brid.covid_19.binding

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


@BindingAdapter("valueAttrChanged")
fun setListener(view: SwipeRefreshLayout, listener: InverseBindingListener?) {
    if (listener != null) {
        view.setOnRefreshListener {
            listener.onChange()
        }
    }
}

@BindingAdapter("value")
fun setChip(view: SwipeRefreshLayout, newValue: Boolean?) {
    // Important to break potential infinite loops.
    if (newValue != null) {
        view.isRefreshing = newValue
    }
}

@InverseBindingAdapter(attribute = "value")
fun getChip(view: SwipeRefreshLayout): Boolean {
    return view.isRefreshing
}