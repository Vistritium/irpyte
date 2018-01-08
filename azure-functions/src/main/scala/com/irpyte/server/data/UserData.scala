package com.irpyte.server.data

import java.util.Objects

case class UserData(
                     search: String,
                     usedIds: List[String]
                   ) {
  require(Objects.nonNull(search), "tags cannot be null")
}
