package com.ewaldo.gofit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ewaldo.gofit.api.LoginApi
import com.ewaldo.gofit.databinding.ActivityMainBinding
import com.ewaldo.gofit.model.UserModel
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var binding: ActivityMainBinding
    lateinit var  mBundle: Bundle
    lateinit var vUsername: String
    lateinit var vPassword : String
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        queue = Volley.newRequestQueue(this)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)


        btnRegister.setOnClickListener {
            val moveRegister = Intent(this@MainActivity, GantiPasswordActivity::class.java)
            startActivity(moveRegister)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            val model = UserModel(username,password)



            val stringRequest: StringRequest = object :
                StringRequest(Request.Method.POST, LoginApi.LOGIN_URL,
                    Response.Listener { response ->
                        val gson = Gson()

                        val jsonObject = JSONObject(response.toString())
                        val data = jsonObject.getJSONObject("user")

//                        val user = gson.fromJson(data.toString(), User::class.java)

                        val moveHome = Intent(this@MainActivity, HomeActivity::class.java)

                        if(data.has("ID_MEMBER")){
                            val token = jsonObject.getString("access_token")
                            val move = Intent(this@MainActivity, HomeActivity::class.java)

                            sharedPreferences.edit()
                                .putString("id", data.getString("ID_MEMBER"))
                                .putString("role", "member")
                                .putString("token", token)
                                .apply()
                            startActivity(move)
                        }else if(data.has("ID_PEGAWAI")){
                            val token = jsonObject.getString("access_token")
                            sharedPreferences.edit()
                                .putInt("id", data.getInt("ID_PEGAWAI"))
                                .putString("role", "manager operational")
                                .putString("token", token)
                                .apply()

                            val move = Intent(this@MainActivity, HomeActivity::class.java)

                            startActivity(move)
                        }else if(data.has("ID_INSTRUKTUR")){
                            val token = jsonObject.getString("access_token")
                            sharedPreferences.edit()
                                .putInt("id", data.getInt("ID_INSTRUKTUR"))
                                .putString("role", "instruktur")
                                .putString("token", token)
                                .apply()
                            val move = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(move)
                        }
//                    FancyToast.makeText(this, "Data ditemukan", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()

                    }, Response.ErrorListener { error ->
                        try {
                            val responseBody =
                                String(error.networkResponse.data, StandardCharsets.UTF_8)
                            val errors = JSONObject(responseBody)
                            Toasty.error(
                                this@MainActivity,
                                errors.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: java.lang.Exception) {
                            Toasty.error(this@MainActivity, "Login Tidak Berhasil!!", Toast.LENGTH_SHORT, true).show();
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

//                override fun getParams(): Map<String, String> {
//                    val params = HashMap<String, String>()
//                    params["Email"] = binding.emailInputLogin.text.toString()
//                    params["password"] = binding.passwordInputLogin.text.toString()
//                    return params
//                }
            }
            queue!!.add(stringRequest)
        })
    }


//                        startActivity(moveHome)
//                        finish()
    
}