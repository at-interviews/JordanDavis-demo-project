package com.example.sample

import com.example.sample.network.model.GeometryResponse
import com.example.sample.network.model.LocationResponse
import com.example.sample.network.model.RestaurantResponse
import com.example.sample.network.model.RestaurantsResponse
import com.example.sample.repository.RestaurantResponseMapper
import com.example.sample.repository.RestaurantsResult
import com.example.sample.ui.main.model.Restaurant
import org.junit.Test
import retrofit2.Response

class RestaurantResponseMapperTest {
    @Test
    fun mapResponseToResult_Success() {
        val data = createSuccessRestaurantResponseList()
        val actual = RestaurantResponseMapper.mapResponseToResult(data)
        val expectedData = RestaurantResponseMapper.mapToRestaurant(data)
        val expected = RestaurantsResult.Success(expectedData)

        assert(actual == expected)
    }

    @Test
    fun mapResponseToResult_SuccessEmpty() {
        val data = Response.success(RestaurantsResponse(html_attributions = listOf(), status = "OK", emptyList()))
        val actual = RestaurantResponseMapper.mapResponseToResult(data)
        val expected = RestaurantsResult.Success(emptyList())

        assert(actual == expected)
    }

    @Test
    fun mapResponseToResult_Error() {
        val data = null
        val actual = RestaurantResponseMapper.mapResponseToResult(data)
        val expected = RestaurantsResult.Error("Error fetching restaurants")

        assert(actual == expected)
    }

    @Test
    fun mapResponseToRestaurant() {
        val expected = listOf(
            Restaurant(
                name = "name",
                businessStatus = "operational",
                priceLevel = 3,
                rating = 3,
                iconUrl = "url",
                longitude = 100.0,
                latitude = 100.0
            ),
        )

        val actual = RestaurantResponseMapper.mapToRestaurant(createSuccessRestaurantResponseList())

        assert(areRestaurantsEqual(actual[0], expected[0]))
    }

    private fun areRestaurantsEqual(restaurantOne: Restaurant, restaurantTwo: Restaurant): Boolean {
        return restaurantOne.name == restaurantTwo.name && restaurantOne.latitude == restaurantTwo.latitude
    }

    private fun createSuccessRestaurantResponseList(): Response<RestaurantsResponse?>? {
        return Response.success(
            RestaurantsResponse(
                listOf(),
                "OK",
                listOf(
                    RestaurantResponse(
                        name = "name",
                        businessStatus = "operational",
                        priceLevel = 3,
                        rating = 3f,
                        iconUrl = "url",
                        geometry = GeometryResponse(location = LocationResponse(100.0, 100.0))
                    )
                )
            )
        )
    }
}