package com.demo.baseproject.ads.listeners

interface BannerAdListener {
    fun load()
    fun pause()
    fun resume()
    fun destroy()
}