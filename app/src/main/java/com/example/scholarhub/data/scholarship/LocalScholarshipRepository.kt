package com.example.scholarhub.data.scholarship

import android.content.res.AssetManager
import com.example.scholarhub.domain.scholarship.PreferenceOption
import com.example.scholarhub.domain.scholarship.Scholarship
import com.example.scholarhub.domain.scholarship.ScholarshipCatalog
import com.example.scholarhub.domain.scholarship.ScholarshipRepository
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalScholarshipRepository internal constructor(
    private val readCatalog: () -> String,
) : ScholarshipRepository {
    constructor(assets: AssetManager) : this({
        assets.open("scholarships.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
    })

    private val catalog by lazy {
        ScholarshipJsonParser.parse(readCatalog())
    }

    override suspend fun getCatalog(): Result<ScholarshipCatalog> = withContext(Dispatchers.IO) {
        try {
            Result.success(catalog)
        } catch (error: IOException) {
            Result.failure(error)
        } catch (error: InvalidScholarshipCatalogException) {
            Result.failure(error)
        }
    }

    override suspend fun getById(id: String): Result<Scholarship?> =
        getCatalog().map { catalog -> catalog.scholarships.firstOrNull { it.id == id } }

    override suspend fun getPreferenceOptions(): Result<List<PreferenceOption>> =
        getCatalog().map { it.preferenceOptions }
}
