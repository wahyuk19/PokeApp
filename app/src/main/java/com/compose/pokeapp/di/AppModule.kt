package com.compose.pokeapp.di

import android.content.Context
import androidx.room.Room
import com.compose.pokeapp.BuildConfig
import com.compose.pokeapp.db.PokemonDao
import com.compose.pokeapp.db.PokemonRoomDatabase
import com.compose.pokeapp.model.Pokemon
import com.compose.pokeapp.network.PokeApi
import com.compose.pokeapp.repository.FireRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideFirePokeRepository()
            = FireRepository(queryPoke = FirebaseFirestore.getInstance()
        .collection("pokemon"))

    @Singleton
    @Provides
    fun providePokeApi(): PokeApi {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient().newBuilder()

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient.build())
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApi::class.java)
    }

    @Singleton
    @Provides
    fun providePokemonDao(pokemonRoomDatabase: PokemonRoomDatabase): PokemonDao = pokemonRoomDatabase.pokemonDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context):PokemonRoomDatabase = Room.databaseBuilder(
        context,PokemonRoomDatabase::class.java,"pokemon_db").fallbackToDestructiveMigration().build()
}