package com.romeat.smashup.network

import com.romeat.smashup.data.dto.ChangeEmailRequest
import com.romeat.smashup.data.dto.ChangePasswordRequest
import com.romeat.smashup.data.dto.ChangeUsernameRequest
import com.romeat.smashup.data.dto.UpdateAvatarRequest
import javax.inject.Inject

class SmashupRemoteData @Inject constructor(
    private val mainService : MainService
) {

    //suspend fun getUserInfo(username: String) = mainService.getCurrentUser(username)
    suspend fun getMyLikes() = mainService.getMyLikes()

    suspend fun getUserProfileList(ids: List<Int>) = mainService.getUserProfileList(ids.joinToString(","))

    suspend fun getAuthorInfo(id: Int) = mainService.getUserProfile(id)

    suspend fun getMashupsList(ids: List<Int>) = mainService.getMashupsList(ids.joinToString(","))

    suspend fun getSourcesList(ids: List<Int>) = mainService.getSourcesList(ids.joinToString(","))

    suspend fun getMashupsWithSource(id: Int) = mainService.getMashupsWithSource(id)

    //suspend fun getCompilationsIds() = mainService.getCompilationsIds()

    suspend fun getRecommendationsMashupIds() = mainService.getRecommendationsMashupIds()

    suspend fun getPlaylists(ids: List<Int>) = mainService.getPlaylists(ids.joinToString(","))


    suspend fun getMashupsWithName(searchQuery: String) = mainService.getMashupsWithName(searchQuery)

    suspend fun getPlaylistsWithName(searchQuery: String) = mainService.getPlaylistsWithName(searchQuery)

    suspend fun getSourcesWithName(searchQuery: String) = mainService.getSourcesWithName(searchQuery)

    suspend fun getUsersWithName(searchQuery: String) = mainService.getUsersWithName(searchQuery)


    suspend fun addLikeToMashup(id: Int) = mainService.addLikeToMashup(id)

    suspend fun removeLikeFromMashup(id: Int) = mainService.removeLikeFromMashup(id)

    suspend fun addStreamToMashup(id: Int) = mainService.addStreamToMashup(id)


    suspend fun updateFcmToken(token: String) = mainService.updateFcmToken(token)

    suspend fun deleteFcmToken(token: String) = mainService.deleteFcmToken(token)

    suspend fun getUserSettings() = mainService.getUserSettingsBits()

    suspend fun updateMultisessionBit(allowed: Boolean) = mainService.changeUserSettingsBit(bit = 1, value = allowed.compareTo(false))

    suspend fun changeUserEmail(password: String, newEmail: String) =
        mainService.changeUserEmail(ChangeEmailRequest(password, newEmail))

    suspend fun changeUserEmailConfirm(token: String) = mainService.changeUserEmailConfirm(token)

    suspend fun changeUserPassword(password: String, newPassword: String) =
        mainService.changeUserPassword(ChangePasswordRequest(password, newPassword))

    suspend fun changeUserPasswordConfirm(token: String) = mainService.changeUserPasswordConfirm(token)

    suspend fun changeUserName(password: String, newUsername: String) =
        mainService.changeUserName(ChangeUsernameRequest(password, newUsername))

    suspend fun changeUserNameConfirm(token: String) = mainService.changeUserNameConfirm(token)

    suspend fun updateAvatar(encodedAvatar: String) = mainService.updateAvatar(UpdateAvatarRequest(encodedAvatar))
}