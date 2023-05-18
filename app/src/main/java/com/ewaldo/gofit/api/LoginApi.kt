package com.ewaldo.gofit.api

class LoginApi {

    companion object{
        val BASE_URL = "http://10.53.3.49/API/public/api/"

//        val GET_BY_ID_URL = BASE_URL + "user/"
//        val ADD_URL = BASE_URL + "user"
//        val UPDATE_URL = BASE_URL + "user/"
        val LOGIN_URL = BASE_URL + "login"
        val ResetPassword = BASE_URL + "gantiPassword"
        val LOGOUT_URL = BASE_URL + "logout"


    }
}