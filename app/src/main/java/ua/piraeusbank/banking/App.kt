package ua.piraeusbank.banking

import android.app.Application


class App : Application() {

    companion object {
        lateinit var component: MainComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        component = MainComponent(this)
    }
}
