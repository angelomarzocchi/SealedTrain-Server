package com.example.security.encryption

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class EncryptionService {

    fun encryptTicket(qrcode: String): String {
        val plainText: ByteArray = qrcode.toByteArray()
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val key: SecretKey = keygen.generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val cipherText: ByteArray = cipher.doFinal(plainText)
        val iv: ByteArray = cipher.iv

        val fileName = "src/resources/keys.txt"
        val file = File(fileName)

        Files.write(file.toPath(), key.toString().toByteArray(),StandardOpenOption.APPEND)


        return cipherText.toString()

    }


}