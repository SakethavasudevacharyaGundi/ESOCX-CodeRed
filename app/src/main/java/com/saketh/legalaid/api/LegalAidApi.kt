package com.saketh.legalaid.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LegalAidApi {
    @POST("api/legal-aid-requests/")
    suspend fun submitRequest(@Body request: LegalAidRequest): Response<ApiResponse>
}

data class LegalAidRequest(
    val case_type: String,
    val urgency_level: String,
    val description: String,
    val contact_info: String
)

data class ApiResponse(
    val status: String,
    val message: String,
    val data: LegalAidRequest? = null,
    val errors: Map<String, List<String>>? = null
) 