package miao.kmirror.miaolibrary.demo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import miao.kmirror.miaolibrary.demo.data.local.StudentDao
import miao.kmirror.miaolibrary.demo.data.remote.api.StudentApi
import miao.kmirror.miaolibrary.demo.data.repository.StudentRepositoryImpl
import miao.kmirror.miaolibrary.demo.domain.repository.StudentRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideStudentRepository(
        dao: StudentDao,
        api: StudentApi,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): StudentRepository = StudentRepositoryImpl(dao, api, dispatcher)
}