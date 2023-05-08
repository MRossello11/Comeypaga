package core

import com.google.gson.Gson
import core.model.BaseResponse
import core.model.ErrorResponse
import retrofit2.Response

/**
 * Handles a standard response (BaseResponse) consisting of a code and message
 * and handles the callback
 */
fun handleBaseResponse(
    response: Response<BaseResponse>,
    callback: (response: BaseResponse) -> Unit
    ) {
        if (response.code() in 200..299) {
            response.body()?.let {
                callback(BaseResponse(response.code(), response.message()))
            } ?: run {
                callback(BaseResponse(response.code(), "An error occurred"))
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            callback(BaseResponse(response.code(), errorResponse.message ?: "An error occurred"))
        }
    }