package com.irpyte.server.services

import java.util.UUID

import com.irpyte.server.Config
import com.irpyte.server.form.SettingsForm

object MainService {

  def createNewUser(settingsForm: SettingsForm): NewUser = {
    val id = UUID.randomUUID().toString
    val settings = Config.objectMapper.writeValueAsBytes(settingsForm)
    AzureBlob.upload(settings, id, overrideIfExists = false)
    NewUser(id)
  }

  def updateUser(settingsForm: SettingsForm, userId: String) = {

  }

  case class NewUser(id: String)

}


