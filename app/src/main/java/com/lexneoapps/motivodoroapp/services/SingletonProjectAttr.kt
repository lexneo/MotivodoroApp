package com.lexneoapps.motivodoroapp.services

object SingletonProjectAttr {
    var projectName = "StopWatch"
    var projectColor = -30808

    fun setAttributes(name: String, color: Int) {
        this.projectName = name
        this.projectColor = color
    }
}
