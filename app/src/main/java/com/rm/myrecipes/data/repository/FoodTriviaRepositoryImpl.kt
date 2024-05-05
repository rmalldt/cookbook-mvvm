package com.rm.myrecipes.data.repository

import com.rm.myrecipes.data.common.handleResponse
import com.rm.myrecipes.data.di.IoDispatcher
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.mapper.ResponseMapper
import com.rm.myrecipes.domain.data.FoodTrivia
import com.rm.myrecipes.domain.repository.FoodTriviaRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodTriviaRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val mapper: ResponseMapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FoodTriviaRepository {

    override suspend fun getFoodTrivia(apiKey: String): Result<FoodTrivia> = withContext(dispatcher) {
        handleResponse(
            request = { remoteDataSource.getFoodTrivia(apiKey) },
            responseMapper = { mapper.toFoodTrivia(it) }
        )
    }
}
