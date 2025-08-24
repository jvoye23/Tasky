package com.jvoye.tasky.core.presentation.mappers

import com.jvoye.tasky.core.domain.model.AuthInfo
import com.jvoye.tasky.core.presentation.model.UserUi

fun AuthInfo.toUserUi(): UserUi {

    val initials = getInitials(username)

    return UserUi(
        username = username,
        userInitials = initials
    )
}

private fun getInitials(fullName: String): String {
    val names = fullName.split(" ").filter { it.isNotEmpty() }
    val initials: String

    when (names.size) {
        1 -> {
            initials = names[0].substring(0,2)
        }
        2 -> {
            initials = names.joinToString("") { name ->
                name.first().toString()
            }
        }
        else -> {
            val firstInitial = names[0].first()
            val secondInitial = names[names.lastIndex].first()
            initials = firstInitial + secondInitial.toString()
        }
    }
    return initials.uppercase()
}