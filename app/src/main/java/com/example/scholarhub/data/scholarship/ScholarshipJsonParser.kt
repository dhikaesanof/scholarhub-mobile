package com.example.scholarhub.data.scholarship

import com.example.scholarhub.domain.scholarship.EligibilityRule
import com.example.scholarhub.domain.scholarship.PreferenceOption
import com.example.scholarhub.domain.scholarship.RuleOperator
import com.example.scholarhub.domain.scholarship.RuleValue
import com.example.scholarhub.domain.scholarship.Scholarship
import com.example.scholarhub.domain.scholarship.ScholarshipBenefit
import com.example.scholarhub.domain.scholarship.ScholarshipCatalog
import com.example.scholarhub.domain.scholarship.ScholarshipEligibility
import com.example.scholarhub.domain.scholarship.ScholarshipGuide
import com.example.scholarhub.domain.scholarship.ScholarshipRegistration
import com.example.scholarhub.domain.scholarship.ScholarshipSection
import com.example.scholarhub.domain.scholarship.ScholarshipSources
import com.example.scholarhub.domain.scholarship.ScholarshipSummary
import com.example.scholarhub.domain.scholarship.ScholarshipTrack
import java.math.BigDecimal
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

internal object ScholarshipJsonParser {
    private const val SCHEMA_VERSION = 1

    fun parse(json: String): ScholarshipCatalog = try {
        val tokens = JSONTokener(json)
        val root = tokens.nextValue() as? JSONObject ?: throw IllegalArgumentException("Katalog harus berupa objek JSON")
        require(tokens.nextClean() == '\u0000') { "Ada data tambahan setelah katalog" }
        require(root.get("schemaVersion") == SCHEMA_VERSION) { "Versi skema katalog tidak didukung" }

        val preferences = root.getJSONArray("preferenceOptions").mapItems { getJSONObject(it).preference() }
        val scholarships = root.getJSONArray("scholarships").mapItems { getJSONObject(it).scholarship() }
        require(preferences.distinctBy { it.id }.size == preferences.size) { "ID prioritas duplikat" }
        require(scholarships.distinctBy { it.id }.size == scholarships.size) { "ID beasiswa duplikat" }

        ScholarshipCatalog(
            scheduleAsOf = LocalDate.parse(root.string("scheduleAsOf")),
            timeZone = ZoneId.of(root.string("timeZone")),
            preferenceOptions = preferences,
            scholarships = scholarships,
        )
    } catch (error: JSONException) {
        throw InvalidScholarshipCatalogException(error)
    } catch (error: IllegalArgumentException) {
        throw InvalidScholarshipCatalogException(error)
    } catch (error: DateTimeException) {
        throw InvalidScholarshipCatalogException(error)
    }

    private fun JSONObject.preference() = PreferenceOption(string("id"), string("label"))

    private fun JSONObject.scholarship() = Scholarship(
        id = string("id"),
        name = string("name"),
        provider = string("provider"),
        period = string("period"),
        registration = getJSONObject("registration").registration(),
        summary = getJSONObject("summary").summary(),
        requirements = objects("requirements") { section() },
        guide = getJSONObject("guide").guide(),
        eligibility = getJSONObject("eligibility").eligibility(),
        sources = getJSONObject("sources").sources(),
    )

    private fun JSONObject.registration() = ScholarshipRegistration(
        statusAtSnapshot = string("statusAtSnapshot"),
        opensOn = optionalString("opensOn")?.let(LocalDate::parse),
        closesOn = optionalString("closesOn")?.let(LocalDate::parse),
        closesAtLocal = optionalString("closesAtLocal")?.let(LocalTime::parse),
        timeZone = ZoneId.of(string("timeZone")),
        applicationUrl = optionalString("applicationUrl"),
    )

    private fun JSONObject.summary() = ScholarshipSummary(
        about = string("about"),
        benefits = objects("benefits") { benefit() },
        coverage = strings("coverage"),
    )

    private fun JSONObject.section() = ScholarshipSection(
        key = string("key"),
        title = string("title"),
        body = optionalString("body").orEmpty(),
        items = strings("items"),
    )

    private fun JSONObject.benefit() = ScholarshipBenefit(
        title = string("title"),
        amountIdr = if (isNull("amountIdr")) null else get("amountIdr").decimal(),
        amountKind = optionalString("amountKind"),
        unitOrCoverage = optionalString("unitOrCoverage"),
        duration = optionalString("duration"),
        description = optionalString("description"),
        tags = strings("tags").toSet(),
        evidence = string("evidence"),
        conditional = flag("conditional"),
    )

    private fun JSONObject.guide() = ScholarshipGuide(
        sections = objects("sections") { section() },
        officialGuideUrl = optionalString("officialGuideUrl"),
    )

    private fun JSONObject.sources() = ScholarshipSources(
        primaryUrl = string("primaryUrl"),
        supportingUrls = strings("supportingUrls"),
        sourceType = string("sourceType"),
        verificationNote = optionalString("verificationNote"),
    )

    private fun JSONObject.eligibility() = ScholarshipEligibility(
        commonRule = if (isNull("commonRule")) null else getJSONObject("commonRule").rule(),
        tracks = objects("tracks") { track() },
        manualChecks = strings("manualChecks"),
    )

    private fun JSONObject.track() = ScholarshipTrack(
        id = string("id"),
        name = string("name"),
        rule = if (isNull("rule")) null else getJSONObject("rule").rule(),
        manualChecks = strings("manualChecks"),
    )

    private fun JSONObject.rule(): EligibilityRule {
        val value = string("operator")
        if (value == "all" || value == "any") {
            val rules = getJSONArray("rules").mapItems { getJSONObject(it).rule() }
            require(rules.isNotEmpty()) { "Grup aturan tidak boleh kosong" }
            return if (value == "all") EligibilityRule.All(rules) else EligibilityRule.AnyOf(rules)
        }

        val operator = when (value) {
            "eq" -> RuleOperator.EQ
            "in" -> RuleOperator.IN
            "gt" -> RuleOperator.GT
            "gte" -> RuleOperator.GTE
            "lte" -> RuleOperator.LTE
            else -> throw IllegalArgumentException("Operator aturan tidak didukung: $value")
        }
        val values = when (operator) {
            RuleOperator.IN -> getJSONArray("value").mapItems { get(it).ruleValue() }
            RuleOperator.EQ -> listOf(get("value").ruleValue())
            else -> listOf(RuleValue.Number(get("value").decimal()))
        }
        require(values.isNotEmpty()) { "Nilai aturan tidak boleh kosong" }
        return EligibilityRule.Condition(string("field"), operator, values, optionalString("reason"))
    }

    private fun Any.ruleValue(): RuleValue = when (this) {
        is String -> RuleValue.Text(this)
        is Number -> RuleValue.Number(decimal())
        is Boolean -> RuleValue.Flag(this)
        else -> throw IllegalArgumentException("Nilai aturan harus berupa teks, angka, atau boolean")
    }

    private fun Any.decimal(): BigDecimal {
        require(this is Number) { "Nilai harus berupa angka" }
        return toString().toBigDecimal()
    }

    private fun JSONObject.string(key: String): String =
        (get(key) as? String)?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("$key harus berupa teks yang tidak kosong")

    private fun JSONObject.optionalString(key: String): String? =
        if (isNull(key)) null else string(key)

    private fun JSONObject.flag(key: String): Boolean {
        if (isNull(key)) return false
        return get(key) as? Boolean ?: throw IllegalArgumentException("$key harus berupa boolean")
    }

    private fun JSONObject.strings(key: String): List<String> =
        if (isNull(key)) emptyList() else getJSONArray(key).mapItems {
            (get(it) as? String)?.takeIf(String::isNotBlank)
                ?: throw IllegalArgumentException("$key harus berisi teks yang tidak kosong")
        }

    private fun <T> JSONObject.objects(key: String, convert: JSONObject.() -> T): List<T> =
        if (isNull(key)) emptyList() else getJSONArray(key).mapItems { getJSONObject(it).convert() }

    private inline fun <T> JSONArray.mapItems(convert: JSONArray.(Int) -> T): List<T> =
        List(length()) { convert(it) }
}

internal class InvalidScholarshipCatalogException(cause: Exception) :
    Exception("Format katalog beasiswa tidak valid. ${cause.message}", cause)
