package com.github.sguzman.scala.scrawler.aes

import java.nio.charset.StandardCharsets.UTF_8
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AES {
  private val ALGO = "AES"

  def encrypt(keyValue: String, data: String): String = {
    val key = generateKey(keyValue)
    val c = Cipher.getInstance(ALGO)
    c.init(Cipher.ENCRYPT_MODE, key)
    val encVal = c.doFinal(data.getBytes)
    Base64.getEncoder.encodeToString(encVal)
  }

  def decrypt(keyValue: String, encryptedData: String): String = {
    val key = generateKey(keyValue)
    val c = Cipher.getInstance(ALGO)
    c.init(Cipher.DECRYPT_MODE, key)
    val decordedValue = Base64.getDecoder.decode(encryptedData)
    val decValue = c.doFinal(decordedValue)
    new String(decValue)
  }

  private def generateKey(keyValue: String) = new SecretKeySpec(keyValue.getBytes(UTF_8), ALGO)
}
