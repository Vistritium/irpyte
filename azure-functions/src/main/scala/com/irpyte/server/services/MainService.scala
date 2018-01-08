package com.irpyte.server.services

import java.util.UUID

import com.irpyte.server.Config
import com.irpyte.server.controllers.Constants
import com.irpyte.server.data.UserData
import com.irpyte.server.form.SettingsForm

object MainService {

  def createNewUser(settingsForm: SettingsForm): NewUser = {
    val id = UUID.randomUUID().toString
    val userData = UserData(settingsForm.getSearch, List())
    val userDataRaw = Config.objectMapper.writeValueAsBytes(userData)
    AzureBlob.upload(userDataRaw, Constants.getUserDataLocation(id), overrideIfExists = false)
    NewUser(id)
  }

  def updateUser(settingsForm: SettingsForm, userId: String) = {

  }

  case class NewUser(id: String)

}


