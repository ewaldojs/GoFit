package com.ewaldo.gofit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ewaldo.gofit.api.LoginApi
import com.ewaldo.gofit.databinding.FragmentInstrukturBinding
import com.ewaldo.gofit.databinding.FragmentMemberBinding
import com.ewaldo.gofit.model.UserModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_m_o.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class FragmentMember : Fragment() {
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences
    //    private lateinit var btnLogout: Button
    private var _binding: FragmentMemberBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        queue = Volley.newRequestQueue(activity)

        binding.btnLogout.setOnClickListener{
            logout()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMemberBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    private fun logout(){
        val auth = UserModel(
            "",
            "")

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, LoginApi.LOGOUT_URL, Response.Listener { response ->
                val gson = Gson()
                var user_logout = gson.fromJson(response, UserModel::class.java)

                if(user_logout != null) {
                    val intent = Intent(activity, MainActivity::class.java)
                    sharedPreferences.edit()
                        .putInt("id",-1)
                        .putString("id", null)
                        .putString("role",null)
                        .putString("token",null)
                        .apply()
                    startActivity(intent)
                }
                else {

                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)

                }catch (e: Exception) {
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(auth)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}