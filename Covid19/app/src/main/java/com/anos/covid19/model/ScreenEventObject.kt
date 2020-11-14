package com.anos.covid19.model

class ScreenEventObject(
    val screenType: ScreenType,
    val screenName: String,
    val data: Any? = null
) {
    enum class ScreenType {
        FRAGMENT, ACTIVITY
    }
}