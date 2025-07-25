package com.heldermarques.appreceitas

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

object SupabaseCliente {

    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_ANON_KEY



    val supabase = createSupabaseClient(supabaseUrl, supabaseKey) {
        //Already the default serializer, but you can provide a custom Json instance (optional):
        defaultSerializer = KotlinXSerializer(Json {
            //apply your custom config
        })
        install(Auth)

    }
}