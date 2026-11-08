package com.example.scholarhub.domain.scholarship

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

data class ScholarshipCatalog(
    val scheduleAsOf: LocalDate,
    val timeZone: ZoneId,
    val preferenceOptions: List<PreferenceOption>,
    val scholarships: List<Scholarship>,
)

data class PreferenceOption(val id: String, val label: String)

data class Scholarship(
    val id: String,
    val name: String,
    val provider: String,
    val period: String,
    val registration: ScholarshipRegistration,
    val summary: ScholarshipSummary,
    val requirements: List<ScholarshipSection>,
    val guide: ScholarshipGuide,
    val eligibility: ScholarshipEligibility,
    val sources: ScholarshipSources,
)

data class ScholarshipRegistration(
    val statusAtSnapshot: String,
    val opensOn: LocalDate?,
    val closesOn: LocalDate?,
    val closesAtLocal: LocalTime?,
    val timeZone: ZoneId,
    val applicationUrl: String?,
)

data class ScholarshipSummary(
    val about: String,
    val benefits: List<ScholarshipBenefit>,
    val coverage: List<String>,
)

data class ScholarshipSection(
    val key: String,
    val title: String,
    val body: String,
    val items: List<String> = emptyList(),
)

data class ScholarshipBenefit(
    val title: String,
    val amountIdr: BigDecimal?,
    val amountKind: String?,
    val unitOrCoverage: String?,
    val duration: String?,
    val description: String?,
    val tags: Set<String>,
    val evidence: String,
    val conditional: Boolean,
)

data class ScholarshipGuide(
    val sections: List<ScholarshipSection>,
    val officialGuideUrl: String?,
)

data class ScholarshipSources(
    val primaryUrl: String,
    val supportingUrls: List<String>,
    val sourceType: String,
    val verificationNote: String?,
)

data class ScholarshipEligibility(
    val commonRule: EligibilityRule?,
    val tracks: List<ScholarshipTrack>,
    val manualChecks: List<String>,
)

data class ScholarshipTrack(
    val id: String,
    val name: String,
    val rule: EligibilityRule?,
    val manualChecks: List<String>,
)

sealed interface EligibilityRule {
    data class All(val rules: List<EligibilityRule>) : EligibilityRule
    data class AnyOf(val rules: List<EligibilityRule>) : EligibilityRule

    data class Condition(
        val field: String,
        val operator: RuleOperator,
        val values: List<RuleValue>,
        val reason: String?,
    ) : EligibilityRule
}

enum class RuleOperator { EQ, IN, GT, GTE, LTE }

sealed interface RuleValue {
    data class Text(val value: String) : RuleValue
    data class Number(val value: BigDecimal) : RuleValue
    data class Flag(val value: Boolean) : RuleValue
}
