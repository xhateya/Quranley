package com.xhateya.idn.quranley.activity
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.google.android.gms.location.*
import com.xhateya.idn.quranley.R
import com.xhateya.idn.quranley.adapter.SurahAdapter
import com.xhateya.idn.quranley.model.ModelSurah
import com.xhateya.idn.quranley.networking.Api
import kotlinx.android.synthetic.main.activity_list_surah.*
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList

class ListSurahActivity : AppCompatActivity(), SurahAdapter.OnSelectedData {


    private var surahAdapter: SurahAdapter? = null
    var progressDialog: ProgressDialog? = null
    var modelSurah: MutableList<ModelSurah> = ArrayList()
    private var hariIni: String? = null
    private var tanggal: String? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_surah)

        supportActionBar?.hide()

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Showing Data...")


        //nampilin tanggal
        val dateNow = Calendar.getInstance().time
        hariIni = DateFormat.format("EEEE", dateNow) as String
        tanggal = DateFormat.format("d MMMM yyyy", dateNow) as String
        tvToday.text = "$hariIni,"
        tvDate.text = tanggal

        rvSurah.layoutManager = LinearLayoutManager(this)
        rvSurah.setHasFixedSize(true)


        listSurah()
    }

    private fun listSurah() {
        progressDialog!!.show()
        AndroidNetworking.get(Api.URL_LIST_SURAH)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    for (i in 0 until response.length()) {
                        try {
                            progressDialog!!.dismiss()
                            val dataApi = ModelSurah()
                            val jsonObject = response.getJSONObject(i)
                            dataApi.nomor = jsonObject.getString("nomor")
                            dataApi.nama = jsonObject.getString("nama")
                            dataApi.type = jsonObject.getString("type")
                            dataApi.ayat = jsonObject.getString("ayat")
                            dataApi.asma = jsonObject.getString("asma")
                            dataApi.arti = jsonObject.getString("arti")
                            dataApi.keterangan = jsonObject.getString("keterangan")
                            modelSurah.add(dataApi)
                            showListSurah()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@ListSurahActivity, "Failed to show data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onError(anError: ANError) {
                    progressDialog!!.dismiss()
                    Toast.makeText(
                        this@ListSurahActivity, "No internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun showListSurah() {
        surahAdapter = SurahAdapter(modelSurah, this)
        rvSurah!!.adapter = surahAdapter
    }

    override fun onSelected(modelSurah: ModelSurah?) {
        val intent = Intent(this, DetailSurahActivity::class.java)
        intent.putExtra("detailSurah", modelSurah)
        startActivity(intent)
    }

}