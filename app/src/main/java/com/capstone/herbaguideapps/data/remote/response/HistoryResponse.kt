package com.capstone.herbaguideapps.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

    @field:SerializedName("history")
    val history: List<HistoryItem>,

    @field:SerializedName("message")
    val message: String
)

data class HistoryItem(

    @field:SerializedName("confidence")
    val confidence: Any,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("tanaman_herbal")
    val tanamanHerbal: TanamanHerbal
)

data class TanamanHerbal(

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("mengobati_apa")
    val mengobatiApa: List<MengobatiApaItem>,

    @field:SerializedName("photo_url")
    val photoUrl: String,

    @field:SerializedName("deskripsi")
    val deskripsi: String
)

data class MengobatiApaItem(

    @field:SerializedName("penyakit")
    val penyakit: String,

    @field:SerializedName("resep")
    val resep: List<String>
)
