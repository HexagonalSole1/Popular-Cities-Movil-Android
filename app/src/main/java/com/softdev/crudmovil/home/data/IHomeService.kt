import com.softdev.crudmovil.core.network.RetrofitClient
import com.softdev.crudmovil.home.domain.dtos.request.CityRequest
import com.softdev.crudmovil.home.domain.dtos.response.CitiesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IHomeService {
    @GET("cities") // Endpoint para obtener todas las ciudades
    suspend fun getCities(): Response<List<CitiesResponse>>

    @POST("cities") // Endpoint para crear una nueva ciudad
    suspend fun createCity(@Body city: CityRequest): Response<Unit>

    companion object {
        fun create(): IHomeService {
            return RetrofitClient.instance.create(IHomeService::class.java)
        }
    }
}
