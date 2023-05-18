package com.ewaldo.gofit

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ewaldo.gofit.api.LoginApi
import com.ewaldo.gofit.model.UserModel
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_ganti_password.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class GantiPasswordActivity : AppCompatActivity() {

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganti_password)
        queue = Volley.newRequestQueue(this)
        supportActionBar?.hide()
        inputUsername = findViewById(R.id.inputLayoutEmail)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        btnReset.setOnClickListener(View.OnClickListener  {
            val moveDaftar = Intent(this@GantiPasswordActivity, MainActivity::class.java)
            startActivity(moveDaftar)
        })

        btnDaftar.setOnClickListener(View.OnClickListener {

            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            val model = UserModel(username,password)

            val stringRequest: StringRequest =
                object : StringRequest(Request.Method.POST, LoginApi.ResetPassword, Response.Listener { response ->
                    val gson = Gson()
//                var user_pegawai = gson.fromJson(response, Pegawai::class.java)
//                var user_member = gson.fromJson(response, Member::class.java)

                    var user= gson.fromJson(response, UserModel::class.java)

                    var resJO = JSONObject(response.toString())
                    val userobj = resJO.getJSONObject("user")

                    if(user!=null) {
                        Toasty.success(this@GantiPasswordActivity, "Berhasil Ganti Password", Toast.LENGTH_SHORT, true).show();
                        val intent = Intent(this@GantiPasswordActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        Toasty.error(this@GantiPasswordActivity, "Tidak Berhasil Ganti Password!!", Toast.LENGTH_SHORT, true).show();
                    }
                    return@Listener
                }, Response.ErrorListener { error ->
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toasty.error(
                            this@GantiPasswordActivity,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: java.lang.Exception) {
                        Toasty.error(this@GantiPasswordActivity, "Login Tidak Berhasil!!", Toast.LENGTH_SHORT, true).show();
                    }
                }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Accept"] = "application/json"
                        return headers
                    }

                    @Throws(AuthFailureError::class)
                    override fun getBody(): ByteArray {
                        val gson = Gson()
                        val requestBody = gson.toJson(model)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json; charset=utf-8;"
                    }


                }
            queue!!.add(stringRequest)

        })

    }
}