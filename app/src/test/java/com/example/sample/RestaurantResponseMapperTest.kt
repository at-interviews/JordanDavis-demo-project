package com.example.sample

import com.example.sample.network.model.RestaurantResponse
import com.example.sample.repository.RestaurantResponseMapper
import com.example.sample.repository.RestaurantsResult
import com.example.sample.ui.main.model.Restaurant
import org.junit.Test

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
        val data = emptyList<RestaurantResponse>()
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
                distanceInMeters = "100",
                phoneNumber = "1234",
                isOpen = false
            ),
            Restaurant(
                name = "name2",
                distanceInMeters = "2500",
                phoneNumber = "12345",
                isOpen = true
            )
        )

        val actual = RestaurantResponseMapper.mapToRestaurant(createSuccessRestaurantResponseList())

        assert(areRestaurantsEqual(actual[0], expected[0]))
    }

    private fun areRestaurantsEqual(restaurantOne: Restaurant, restaurantTwo: Restaurant): Boolean {
        return restaurantOne.name == restaurantTwo.name
                && restaurantOne.phoneNumber == restaurantTwo.phoneNumber
    }

    private fun createSuccessRestaurantResponseList(): List<RestaurantResponse> {
        return listOf(
            RestaurantResponse(
                id = "id",
                name = "name",
                phoneNumber = "1234",
                distanceInMeters = 100f,
                isClosed = false,
            ),
            RestaurantResponse(
                id = "id2",
                name = "name2",
                phoneNumber = "12345",
                distanceInMeters = 2500f,
                isClosed = true,
            )
        )
    }
}