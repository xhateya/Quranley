@file:Suppress("DEPRECATION")

package com.xhateya.idn.quranley.activity
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.BuildConfig
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.xhateya.idn.quranley.R
import com.xhateya.idn.quranley.adapter.AyatAdapter
import com.xhateya.idn.quranley.model.ModelAyat
import com.xhateya.idn.quranley.model.ModelSurah
import com.xhateya.idn.quranley.networking.Api
import kotlinx.android.synthetic.main.activity_detail_surah.*
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList

class DetailSurahActivity : AppCompatActivity() {

    private var nomor: String? = null
    private var nama: String? = null
    private var arti: String? = null
    private var type: String? = null
    private var ayat: String? = null
    private var keterangan: String? = null
    private var modelSurah: ModelSurah? = null
    private var ayatAdapter: AyatAdapter? = null
    var progressDialog: ProgressDialog? = null
    var modelAyat: MutableList<ModelAyat> = ArrayList()
    private var mHandler: Handler? = null

    @SuppressLint("RestrictedApi", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_surah)

        //Buat Set Toolbar top
        toolbar_detail.title = null
        setSupportActionBar(toolbar_detail)
        if (BuildConfig.DEBUG && supportActionBar == null) {
            error("Assertion failed")
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mHandler = Handler()

        //dapetin data dari ListSurah
        modelSurah = intent.getSerializableExtra("detailSurah") as ModelSurah
        if (modelSurah != null) {
            nomor = modelSurah!!.nomor
            nama = modelSurah!!.nama
            arti = modelSurah!!.arti
            type = modelSurah!!.type
            ayat = modelSurah!!.ayat
            keterangan = modelSurah!!.keterangan


            //Buat Set Text
            tvHeader.text = nama
            tvTitle.text = nama
            tvSubTitle.text = arti
            tvInfo.text = "$type - $ayat Ayat "

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) tvKet.text = Html.fromHtml(
                keterangan,
                Html.FROM_HTML_MODE_COMPACT
            )
            else {
                tvKet.text = Html.fromHtml(keterangan)
            }

        }

        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Showing Data...")

        rvAyat.layoutManager = LinearLayoutManager(this)
        rvAyat.setHasFixedSize(true)

        //Methods get data
        listAyat()
    }

    private fun listAyat() {
        progressDialog!!.show()
        AndroidNetworking.get(Api.URL_LIST_AYAT)
            .addPathParameter("nomor", nomor)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    for (i in 0 until response.length()) {
                        try {
                            progressDialog!!.dismiss()
                            val dataApi = ModelAyat()
                            val jsonObject = response.getJSONObject(i)
                            dataApi.nomor = jsonObject.getString("nomor")
                            dataApi.arab = jsonObject.getString("ar")
                            dataApi.indo = jsonObject.getString("id")
                            dataApi.terjemahan = jsonObject.getString("tr")
                            modelAyat.add(dataApi)
                            showListAyat()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@DetailSurahActivity, "Failed to show data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onError(anError: ANError) {
                    progressDialog!!.dismiss()
                    Toast.makeText(
                        this@DetailSurahActivity, "No internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun showListAyat() {
        ayatAdapter = AyatAdapter(modelAyat)
        rvAyat!!.adapter = ayatAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}