package ua.piraeusbank.banking.util

import com.jakewharton.rxbinding2.widget.AdapterViewItemSelectionEvent
import com.jakewharton.rxbinding2.widget.AdapterViewSelectionEvent
import io.reactivex.Observable


val toSelectedItemTransformation: (Observable<AdapterViewSelectionEvent>) ->
Observable<AdapterViewItemSelectionEvent> =
    { upstream ->
        upstream.map { it as? AdapterViewItemSelectionEvent }
            .map { it }
    }