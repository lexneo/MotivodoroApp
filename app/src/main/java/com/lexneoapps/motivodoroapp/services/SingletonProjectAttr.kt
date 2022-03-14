package com.lexneoapps.motivodoroapp.services

object SingletonProjectAttr {
    var projectName = "StopWatch"
    var projectColor = -30808
    var projectId = 1

    fun setAttributes(name: String, color: Int,projectId : Int) {
        this.projectName = name
        this.projectColor = color
        this.projectId = projectId
    }
}
