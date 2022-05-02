package com.movile.ceiba.data

import javax.inject.Inject



class Repository @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val localDataSource: LocalDataSource
)