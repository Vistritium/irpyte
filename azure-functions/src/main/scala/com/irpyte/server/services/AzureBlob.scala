package com.irpyte.server.services

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.Objects

import com.irpyte.server.Logging
import com.irpyte.server.controllers.Constants
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.CloudBlobContainer
import org.apache.commons.lang3.StringUtils

object AzureBlob extends Logging {

  def download(name: String): Array[Byte] = {
    val container = getConnection()
    val blob = container.getBlobReferenceFromServer(name)
    val length = blob.getProperties.getLength
    val stream = new ByteArrayOutputStream(length.toInt)
    blob.download(stream)
    stream.toByteArray
  }

  def upload(content: Array[Byte], name: String, overrideIfExists: Boolean): Boolean = {

    val container: CloudBlobContainer = getConnection()
    val blob = container.getBlockBlobReference(name)
    //val blob = container.getBlobReferenceFromServer(name)
    if (overrideIfExists || (!blob.exists())) {
      blob.upload(new ByteArrayInputStream(content), content.length)
      logger.info(s"Uploaded $name")
      true
    } else {
      logger.info(s"Did not upload $name because it already exists")
      false
    }
  }

  private def getConnection(): CloudBlobContainer = {
    val param = "irpytedata"
    var storageConnectionString = System.getenv("CUSTOMCONNSTR_" + param)
    if(StringUtils.isBlank(storageConnectionString)){
      storageConnectionString = System.getenv(param)
    }

    val account = CloudStorageAccount.parse(storageConnectionString)
    val serviceClient = account.createCloudBlobClient
    val container = serviceClient.getContainerReference(Constants.CONTAINER)
    container
  }
}
