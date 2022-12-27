package com.example.security.encryption

import org.apache.commons.codec.binary.Hex
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoService {

    private lateinit var key: SecretKey
    lateinit var iv: ByteArray

    private val path: String = "C:/Users/angel/IdeaProjects/authAPIs/src/main/resources/sealedtrainKeystore"
    private val passwd: CharArray = System.getenv("KEYST_PW").toCharArray()
    private val fileInputStream = FileInputStream(path)
    private val keyStore: KeyStore = KeyStore.getInstance("JKS")

    init {
        keyStore.load(fileInputStream,passwd)
    }

    fun encrypt(qrcode: String,id: String, password: String): String {
        val plainText: ByteArray = qrcode.toByteArray()

        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        key = keygen.generateKey()

        //keystore


      //  keyStore.load(fileInputStream,passwd)

        val secretKeyEntry = KeyStore.SecretKeyEntry(key)

        keyStore.setEntry(id,secretKeyEntry, KeyStore.PasswordProtection(password.toCharArray()))


        val fileOutputStream = FileOutputStream(path)






        //salva
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val cipherText: ByteArray = cipher.doFinal(plainText)
        iv = cipher.iv

        keyStore.store(fileOutputStream,passwd)

        return Hex.encodeHexString(cipherText)
      //  return Hex.getEncoder().encodeToString(cipherText)
    }



    fun decrypt(str: String,id: String, password: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        val ivSpec = IvParameterSpec(iv)

        //recupero chiave da keystore

        // val protectionParameter = KeyStore.PasswordProtection(passwd)

        // val mySecretKey = SecretKeySpec("ciao".toByteArray(),"AES")
        // val secretKeyEntry = KeyStore.SecretKeyEntry(mySecretKey)





        val mykey = keyStore.getKey(id,password.toCharArray())




        cipher.init(Cipher.DECRYPT_MODE, mykey, ivSpec)

        //val cipherText = cipher.doFinal(Base64.getDecoder().decode(str))
        var cipherText: ByteArray
        try {
            cipherText = cipher.doFinal(Hex.decodeHex(str))

        } catch(e: BadPaddingException) {
            cipherText = ByteArray(1)
        }
        return String(cipherText)

    }









}

