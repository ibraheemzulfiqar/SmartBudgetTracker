import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.google.hilt) apply false
}

allprojects {
    tasks.withType(KotlinCompile::class).all {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=androidx.media3.common.util.UnstableApi",
                "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi",
            )
        }
    }
}