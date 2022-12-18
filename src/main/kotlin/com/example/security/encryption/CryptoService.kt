package com.example.security.encryption

import com.example.data.models.*

import com.example.security.hashing.SHA256HashingService
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoService {

    lateinit var key: SecretKey
    lateinit var iv: ByteArray

    val path: String = "C:/Users/angel/IdeaProjects/authAPIs/src/main/resources/sealedtrainKeystore"
    val passwd: CharArray = "castiglione".toCharArray()
    val fileInputStream = FileInputStream(path)
    val keyStore = KeyStore.getInstance("JKS")

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



        return Base64.getEncoder().encodeToString(cipherText)
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

        val cipherTest = cipher.doFinal(Base64.getDecoder().decode(str))
        return String(cipherTest)

    }









}

fun main() {
    val cryptoService: CryptoService = CryptoService()
    val password = "forzanapoli"
    val saltedHash = SHA256HashingService().generateSaltedHash(password)
    val user = "angelo"
    val qrcode = "FULL_YEARGiuglianoNapoliWed Nov 23 14:56:33 CET 2022"

/*
    val encryptedQrCode = cryptoService.encrypt(qrcode,user,password)
    println(encryptedQrCode)
    for(i in cryptoService.iv)
        print("$i ")

 */









    val encryptedQrCode = "of/mFIVpmhjWiNeWnHLwTWfzUbaAGtY72HE0u9F9GO1N+MmyrZqHFR1nZXwkMKnel8Y8uzVO8sAeA9f1GFRxJQ=="
    println(encryptedQrCode)

    val iv = byteArrayOf(113, -43, 12, -90, 24, 93, -16, 1 ,-37, -80, -45, -57, 13 ,-113, 65, 81)

    cryptoService.iv = iv





    val decryptedQrcode = cryptoService.decrypt(encryptedQrCode,user,password)
    println()
    println(decryptedQrcode)


}