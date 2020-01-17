package com.tisoft_id.elearningbimbel.core

import com.google.gson.GsonBuilder
import com.tisoft_id.elearningbimbel.models.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface APIretrofit {

    companion object {
        fun create(): APIretrofit {
            val logging=HttpLoggingInterceptor()
            logging.level=HttpLoggingInterceptor.Level.BODY
            val xclient=OkHttpClient.Builder()
            xclient.readTimeout(30,TimeUnit.SECONDS)
            xclient.writeTimeout(60,TimeUnit.SECONDS)
            xclient.connectTimeout(60,TimeUnit.SECONDS)
            xclient.addInterceptor(logging)
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapterFactory( ItemTypeAdapterFactory())
                .create()
            val retrofit = Retrofit.Builder()
                .client(xclient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create(gson))
                .baseUrl("https://bimbel/api/")
                .build()
            return retrofit.create(APIretrofit::class.java)
        }
        fun view(): APIretrofit {
            val logging=HttpLoggingInterceptor()
            logging.level=HttpLoggingInterceptor.Level.BODY
            val xclient=OkHttpClient.Builder()
            xclient.addInterceptor(logging)
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapterFactory( ItemTypeAdapterFactory())
                .create()
            val retrofit = Retrofit.Builder()
                .client(xclient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create(gson))
                .baseUrl("https://bimbel.tigaselaras.co.id/")
                .build()
            return retrofit.create(APIretrofit::class.java)
        }
    }

    @Headers("x-api-key: elearningbimbel")
    @GET("{linkurl}")
    fun getPelajaran(@Path("linkurl") auth: String): Observable<List<MataPelajaran>>

    @Headers("x-api-key: elearningbimbel")
    @GET("{linkurl}")
    fun getExercise(@Path("linkurl") auth: String): Observable<List<Latihan>>
    /*Observable<MataPelajaran> untuk jsonobject*/

    @Headers("x-api-key: elearningbimbel")
    @GET("results/guru/{linkurl}")
    fun getJawabanguru(@Path("linkurl") auth: String): Observable<List<Exercises>>

    @Headers("x-api-key: elearningbimbel")
    @GET("results/siswa/{linkurl}")
    fun getJawabansiswa(@Path("linkurl") auth: String): Observable<List<Exercises>>

    @Headers("x-api-key: elearningbimbel")
    @POST("users/login")
    @FormUrlEncoded
    fun postLogin( @Field("username") xuser:String,@Field("password")xpass:String ):Observable<List<LoginData>>

    @Headers("x-api-key: elearningbimbel")
    @POST("users/register")
    @FormUrlEncoded
    fun postdaftar( @Field("jenis_user") xjenis:Int,
                    @Field("nama_lengkap") xnama:String,
                    @Field("alamat")xalamat:String,
                    @Field("email")xemail:String ,
                    @Field("no_hp")xnotelp:String ,
                    @Field("username")xuser:String ,
                    @Field("password")xpass: String ,
                    @Field("status")xstatus:Int  ):Observable<ResponseReturn>

    @Headers("x-api-key: elearningbimbel")
    @GET("users/list")
    fun getUser(): Observable<List<UserList>>

    @Headers("x-api-key: elearningbimbel")
    @GET("users/aktivasi/{iduser}")
    fun getActivasi(@Path("iduser") iduser: Int): Observable<ResponseReturn>

    @Headers("x-api-key: elearningbimbel")
    @GET("subjects/delete/{idmateri}")
    fun hapusmateri(@Path("idmateri") idmateri: Int): Observable<ResponseReturn>

    @Headers("x-api-key: elearningbimbel")
    @POST("subjects/insert")
    @Multipart
    fun postTambah( @Part("guruid") xguruid:RequestBody,
                    @Part("materialid") xmaterialid:RequestBody,
                    @Part("chapter") xjudul:RequestBody,
                    @Part("deskripsi") xdeskripsi:RequestBody,
                    @Part xfile:MultipartBody.Part ):Observable<ResponseReturn>

    @Headers("x-api-key: elearningbimbel")
    @POST("subjects/edit")
    @Multipart
    fun postEdit( @Part("guruid") xguruid:RequestBody,
                  @Part("materialid") xmaterialid:RequestBody,
                  @Part("chapter") xjudul:RequestBody,
                  @Part("deskripsi") xdeskripsi:RequestBody,
                  @Part("id") xidchapter:RequestBody,
                  @Part xfile:MultipartBody.Part ):Observable<ResponseReturn>

    /*@Headers("x-api-key: elearningbimbel")
    @GET("exercises/delete/{idmateri}")
    fun exercisedelete(@Path("idmateri") idmateri: Int): Observable<ResponseReturn>*/

    @Headers("x-api-key: elearningbimbel")
    @POST("exercises/insert")
    @Multipart
    fun exerciseadd( @Part("guruid") xguruid:RequestBody,@Part("materialid") xmaterialid:RequestBody,@Part("exercise") xjudul:RequestBody,@Part xfile:MultipartBody.Part ):Observable<ResponseReturn>

    @Headers("x-api-key: elearningbimbel")
    @POST("exercises/edit")
    @Multipart
    fun exerciseEdit( @Part("guruid") xguruid:RequestBody,@Part("materialid") xmaterialid:RequestBody,@Part("exercise") xjudul:RequestBody,@Part("id") xidchapter:RequestBody,@Part xfile:MultipartBody.Part ):Observable<ResponseReturn>

    @Headers("x-api-key: elearningbimbel")
    @GET("latihan/{fileName}")
    fun exercisesave(@Path("fileName") fileName: String): Call<ResponseBody>

    @Headers("x-api-key: elearningbimbel")
    @GET("jawaban/{fileName}")
    fun jawabansave(@Path("fileName") fileName: String): Call<ResponseBody>

    @Headers("x-api-key: elearningbimbel")
    @GET("modul/{fileName}")
    fun simpan(@Path("fileName") fileName: String): Call<ResponseBody>


    @Headers("x-api-key: elearningbimbel")
    @POST("results/insert")
    @Multipart
    fun postJawaban( @Part("siswaid") xsiswaid:RequestBody,
                    @Part("exerciseid") xexerciseid:RequestBody,
                    @Part xfile:MultipartBody.Part ):Observable<ResponseReturn>

    @Headers("x-api-key: elearningbimbel")
    @POST("results/addnilai")
    @Multipart
    fun postNilai( @Part("id") xid:RequestBody,
                    @Part("nilai") xnilai:RequestBody,
                    @Part("keterangan") xketerangan:RequestBody):Observable<ResponseReturn>

    @Headers("x-api-key: elearningbimbel")
    @GET("chats/list")
    fun chating(): Observable<List<Chats>>

    @Headers("x-api-key: elearningbimbel")
    @POST("chats/insert")
    @FormUrlEncoded
    fun newchat( @Field("userid") xuser:Int,@Field("chat")xchat:String ):Observable<List<Chats>>
}