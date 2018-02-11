package com.github.fsanaulla.models

import java.net.InetAddress

/**
  * Created by
  * Author: fayaz.sanaulla@gmail.com
  * Date: 27.08.17
  */
final case class UdpConnection(address: InetAddress, port: Int)
