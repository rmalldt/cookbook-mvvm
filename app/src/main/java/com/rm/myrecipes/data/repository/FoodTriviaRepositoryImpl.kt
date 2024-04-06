package com.rm.myrecipes.data.repository

import com.rm.myrecipes.data.common.Result
import com.rm.myrecipes.data.network.RemoteDataSource
import com.rm.myrecipes.data.network.mapper.ResponseMapper
import com.rm.myrecipes.domain.data.FoodTrivia
import com.rm.myrecipes.domain.repository.FoodTriviaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodTriviaRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val mapper: ResponseMapper
) : FoodTriviaRepository {

    override fun getFoodTrivia(apiKey: String): Flow<FoodTrivia> = flow {
        emit(getFoodTriviaRemote(apiKey))
    }

    private suspend fun getFoodTriviaRemote(apiKey: String): FoodTrivia {
        val result = call { remoteDataSource.getFoodTrivia(apiKey) }
        return when (result) {
            is Result.NetworkError -> throw result.exception
            is Result.OK -> mapper.toFoodTrivia(result.data)
        }
    }
}