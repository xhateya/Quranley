package com.xhateya.idn.quranley.model

import java.io.Serializable


//sum -- object -> byte stream
class ModelSurah : Serializable {

    var arti: String? = null

    @JvmField
    var asma: String? = null

    @JvmField
    var ayat: String? = null

    @JvmField
    var nama: String? = null

    @JvmField
    var type: String? = null

    @JvmField
    var nomor: String? = null
    var keterangan: String? = null

}