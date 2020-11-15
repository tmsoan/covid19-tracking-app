package com.anos.covid19.utils

import com.anos.covid19.R
import com.ncapdevi.fragnav.FragNavTransactionOptions

object AppConst {

    val fragNavTransactionOptions = FragNavTransactionOptions.newBuilder().customAnimations(
        R.anim.slide_in_from_right,
        R.anim.slide_out_to_left,
        R.anim.slide_in_from_left,
        R.anim.slide_out_to_right)
        .build()
    val fragFlashNavTransactionOptions = FragNavTransactionOptions.newBuilder().customAnimations(
        R.anim.slide_in_from_right_flash,
        R.anim.slide_out_to_left_flash,
        R.anim.slide_in_from_left_flash,
        R.anim.slide_out_to_right_flash)
        .build()

    val fragPopTransactionOptions = FragNavTransactionOptions.newBuilder().customAnimations(
        R.anim.slide_in_from_right,
        R.anim.slide_out_to_left,
        R.anim.slide_in_from_left,
        R.anim.slide_out_to_right)
        .build()
    val fragFlashPopTransactionOptions = FragNavTransactionOptions.newBuilder().customAnimations(
        R.anim.slide_in_from_right_flash,
        R.anim.slide_out_to_left_flash,
        R.anim.slide_in_from_left_flash,
        R.anim.slide_out_to_right_flash)
        .build()

}