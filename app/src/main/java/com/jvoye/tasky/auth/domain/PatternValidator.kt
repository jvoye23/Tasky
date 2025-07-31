package com.jvoye.tasky.auth.domain

interface PatternValidator {

    fun matches(value: String): Boolean
}