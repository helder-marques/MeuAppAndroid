import java.util.Properties
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.heldermarques.appreceitas"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.heldermarques.appreceitas"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField(type = "String", name = "SUPABASE_URL", value = "\"${localProperties["SUPABASE_URL"]}\"")
        buildConfigField(type = "String", name = "SUPABASE_ANON_KEY", value = "\"${localProperties["SUPABASE_ANON_KEY"]}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures{
        viewBinding=true
        buildConfig = true
    }


}

dependencies {
    implementation("com.google.android.material:material:1.12.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.cronet.embedded)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Plataforma Supabase BOM (gerencia versões de dependências Supabase)
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.4"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")
    implementation("io.github.jan-tennert.supabase:storage-kt")

    // Ktor Client (usado por Supabase)
    implementation("io.ktor:ktor-client-okhttp:3.1.2")

    // Retrofit e Gson (para TheMealDB e APIs customizadas)
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Glide (para carregamento de imagens)
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    // Coroutines do Kotlin para Android
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // OkHttp Logging Interceptor (para depuração de requisições de rede)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Kotlin Stdlib (essencial para compatibilidade com versões do Kotlin)
    implementation(kotlin("stdlib", "1.9.24")) // <<< REINTRODUZIDO E FORÇADO

    // Google ML Kit para tradução no dispositivo
    implementation("com.google.mlkit:translate:17.0.0") // <<< MANTIDO, SEM DUPLICAÇÃO

    // Coroutines para Google Play Services Tasks (essencial para .await() do ML Kit)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
}

// <<< REINTRODUZIDO O BLOCO resolutionStrategy >>>
configurations.all {
    resolutionStrategy {
        // Força a versão do kotlin-stdlib para ser compatível com o compilador
        // Isso ajuda a resolver conflitos de metadata com bibliotecas compiladas com versões mais novas
        force("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.24")
        // Se você usa kotlin-stdlib-common, adicione também
        // force("org.jetbrains.kotlin:kotlin-stdlib-common:1.9.24")
    }
}

