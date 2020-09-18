package org.itstep.liannoi.maps.presentation.common.maps

import android.Manifest
import android.annotation.SuppressLint
import com.google.android.gms.maps.SupportMapFragment
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import org.itstep.liannoi.maps.application.common.interfaces.MapClient

class DefaultMapClient constructor(
    private val fragment: SupportMapFragment
) : MapClient {

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun request(notification: MapClient.FineLocationNotification) {
        RxPermissions(fragment).request(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { notification.onSuccess(it) }
            .addTo(disposable)
    }

    @SuppressLint("MissingPermission")
    override fun prepare() {
        fragment.getMapAsync { it.isMyLocationEnabled = true }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Dispose
    ///////////////////////////////////////////////////////////////////////////

    override fun stop() {
        disposable.clear()
    }

    override fun destroy() {
        disposable.dispose()
    }
}
