package com.example.security.encryption

import com.example.data.models.Subscriber
import java.util.Timer
import java.util.TimerTask


const val oldKeyAlias:String = "oldKeyAlias"
const val newKeyAlias:String = "newKeyAlias"

class DailyKeyGenerator(private val subs: List<Subscriber>): TimerTask() {
    init {
        Timer().schedule(this, 0,1000L * 60L * 60L * 24L)
    }

    fun getSubs(): List<Subscriber> {return subs}

    override  fun run() {

        val cryptoService = CryptoService()
        //prima volta in assoluto verifica che esista una chiave, se non esiste la genera
        if(!cryptoService.keyStore.containsAlias(oldKeyAlias)) {
            for(sub in subs) {
                for(t in sub.tickets){
                    val encQrcode = cryptoService.encrypt(t.qrcode, oldKeyAlias,System.getenv("OLDAILY_PASSWD"))
                    t.qrcode = encQrcode
                    t.iv = cryptoService.iv
                }
            }


        }else {
            for(sub in subs) {
                for(t in sub.tickets){
                    val decQrcode = cryptoService.decrypt(t.qrcode, oldKeyAlias,System.getenv("OLDAILY_PASSWD"))
                    cryptoService.iv = t.iv
                    val reEncQrCode = cryptoService.encrypt(decQrcode, newKeyAlias,System.getenv("NEWDAILY_PASSWD"))
                    t.qrcode = reEncQrCode
                    t.iv = cryptoService.iv
                    //
                }
            }

        }



    }

}