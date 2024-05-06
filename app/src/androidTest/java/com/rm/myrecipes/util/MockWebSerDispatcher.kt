package com.rm.myrecipes.util

import com.rm.myrecipes.data.common.ApiConstants.Companion.API_KEY
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockWebSerDispatcher {

    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when(request.path) {
                END_POINT ->
                    MockResponse().setResponseCode(200)
                    .setBody(FileReader.getJsonContent("success_response.json"))
                else -> MockResponse().setResponseCode(400)
            }
        }
    }

    internal inner class ErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400)
                .setBody(ERROR_MESSAGE)
        }
    }

    companion object {
        const val ERROR_MESSAGE = "Loading failed!"
        const val END_POINT = "/recipes/complexSearch?addRecipeInformation=true&number=20&apiKey=$API_KEY&diet=gluten%20free&type=main%20course&fillIngredients=true"
    }

}