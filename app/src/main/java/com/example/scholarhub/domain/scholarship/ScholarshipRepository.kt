package com.example.scholarhub.domain.scholarship

interface ScholarshipRepository {
    suspend fun getCatalog(): Result<ScholarshipCatalog>

    /** A successful null means the catalog loaded but has no entry for this ID. */
    suspend fun getById(id: String): Result<Scholarship?>

    suspend fun getPreferenceOptions(): Result<List<PreferenceOption>>
}
