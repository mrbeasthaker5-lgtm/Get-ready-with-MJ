package com.example.data.remote

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

data class ApiFaceShape(val primary: String, val confidenceLevel: String)
data class ApiMeasurements(val faceRatio: Float, val jawRatio: Float)
data class ApiHair(val length: String, val texture: String)

data class FaceAnalyzeResponse(
    val status: String,
    val faceShape: ApiFaceShape?,
    val measurements: ApiMeasurements?,
    val hair: ApiHair?
)

data class HaircutRecommendRequest(
    val faceShape: String,
    val hairTexture: String,
    val styleProfile: String
)

data class HaircutDto(
    val haircutId: String,
    val name: String,
    val score: Float,
    val reason: String,
    val maintenance: String,
    val stylingDifficulty: String,
    val barberInstructions: String
)

data class HaircutRecommendResponse(
    val recommendations: List<HaircutDto>
)

data class OutfitRecommendRequest(
    val heightCm: Float,
    val bodyProportions: String,
    val styleProfile: String,
    val stylePreferences: List<String>,
    val occasion: String,
    val weather: String
)

data class OutfitItemDto(val name: String, val category: String)

data class OutfitDto(
    val outfitId: String,
    val name: String,
    val score: Float,
    val items: List<OutfitItemDto>,
    val reason: String,
    val trendExplanation: String
)

data class OutfitRecommendResponse(
    val recommendations: List<OutfitDto>
)

data class TrendDto(
    val id: String,
    val name: String,
    val category: String,
    val trendScore: Float
)

data class TrendsResponse(
    val trends: List<TrendDto>,
    val season: String
)

data class UserProfileDto(
    val genderPreference: String,
    val heightCm: Float,
    val preferredStyles: List<String>
)

interface MjStylingApiService {
    @Multipart
    @POST("/api/v1/face/analyze")
    suspend fun analyzeFace(
        @Part image: MultipartBody.Part
    ): Response<FaceAnalyzeResponse>

    @POST("/api/v1/haircuts/recommend")
    suspend fun recommendHaircuts(
        @Body request: HaircutRecommendRequest
    ): Response<HaircutRecommendResponse>

    @GET("/api/v1/trends/current")
    suspend fun getCurrentTrends(): Response<TrendsResponse>

    @POST("/api/v1/outfits/recommend")
    suspend fun recommendOutfits(
        @Body request: OutfitRecommendRequest
    ): Response<OutfitRecommendResponse>

    @GET("/api/v1/profile")
    suspend fun getProfile(): Response<UserProfileDto>

    @PUT("/api/v1/profile")
    suspend fun updateProfile(@Body profile: UserProfileDto): Response<UserProfileDto>

    companion object {
        private const val BASE_URL = "https://api.getreadywithmj.com/"

        fun create(): MjStylingApiService {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(MjStylingApiService::class.java)
        }
    }
}
