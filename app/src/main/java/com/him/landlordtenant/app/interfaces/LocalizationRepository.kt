package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * LOCALIZATION REPOSITORY
 * =============================================================
 *
 * Handles:
 *
 * - Languages
 * - Translations
 * - User language preferences
 * - Organization regional settings
 * - Currencies
 * - Currency conversion
 * - Time zones
 * - Date/time formatting preferences
 * - Number formatting
 * - Kenyan counties
 * - Constituencies
 * - Sub-counties
 * - Wards
 * - Locations
 * - Address formatting
 * - Regional configuration
 *
 * The domain layer must remain independent from Android
 * Resources, Locale, java.text, Google Maps, etc.
 *
 * =============================================================
 */

interface LocalizationRepository {

    /*
     * ---------------------------------------------------------
     * LOCALIZATION DASHBOARD
     * ---------------------------------------------------------
     */

    suspend fun getLocalizationDashboard(
        organizationId: String
    ): Result<LocalizationDashboardData>

    fun observeLocalizationDashboard(
        organizationId: String
    ): Flow<Result<LocalizationDashboardData>>


    /*
     * ---------------------------------------------------------
     * LANGUAGES
     * ---------------------------------------------------------
     */

    suspend fun getSupportedLanguages(): Result<List<LanguageData>>

    suspend fun getLanguage(
        languageCode: String
    ): Result<LanguageData>

    suspend fun getUserLanguage(
        userId: String
    ): Result<LanguageData>

    suspend fun setUserLanguage(
        userId: String,
        languageCode: String
    ): Result<Unit>

    suspend fun getOrganizationLanguage(
        organizationId: String
    ): Result<LanguageData>

    suspend fun setOrganizationLanguage(
        actorId: String,
        organizationId: String,
        languageCode: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * TRANSLATIONS
     * ---------------------------------------------------------
     */

    suspend fun getTranslation(
        languageCode: String,
        key: String
    ): Result<String>

    suspend fun getTranslations(
        languageCode: String,
        keys: List<String>
    ): Result<Map<String, String>>

    suspend fun getTranslationGroup(
        languageCode: String,
        group: String
    ): Result<Map<String, String>>

    suspend fun saveTranslation(
        actorId: String,
        languageCode: String,
        key: String,
        value: String
    ): Result<Unit>

    suspend fun deleteTranslation(
        actorId: String,
        languageCode: String,
        key: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * CURRENCIES
     * ---------------------------------------------------------
     */

    suspend fun getSupportedCurrencies(): Result<List<CurrencyData>>

    suspend fun getCurrency(
        currencyCode: String
    ): Result<CurrencyData>

    suspend fun getUserCurrency(
        userId: String
    ): Result<CurrencyData>

    suspend fun setUserCurrency(
        userId: String,
        currencyCode: String
    ): Result<Unit>

    suspend fun getOrganizationCurrency(
        organizationId: String
    ): Result<CurrencyData>

    suspend fun setOrganizationCurrency(
        actorId: String,
        organizationId: String,
        currencyCode: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * CURRENCY CONVERSION
     * ---------------------------------------------------------
     */

    suspend fun getExchangeRate(
        fromCurrency: String,
        toCurrency: String
    ): Result<ExchangeRateData>

    suspend fun convertCurrency(
        amount: Double,
        fromCurrency: String,
        toCurrency: String
    ): Result<CurrencyConversionData>

    suspend fun refreshExchangeRates(): Result<Unit>


    /*
     * ---------------------------------------------------------
     * TIME ZONES
     * ---------------------------------------------------------
     */

    suspend fun getSupportedTimeZones(): Result<List<TimeZoneData>>

    suspend fun getUserTimeZone(
        userId: String
    ): Result<TimeZoneData>

    suspend fun setUserTimeZone(
        userId: String,
        timeZoneId: String
    ): Result<Unit>

    suspend fun getOrganizationTimeZone(
        organizationId: String
    ): Result<TimeZoneData>

    suspend fun setOrganizationTimeZone(
        actorId: String,
        organizationId: String,
        timeZoneId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * FORMATTING
     * ---------------------------------------------------------
     */

    suspend fun getUserFormattingPreferences(
        userId: String
    ): Result<FormattingPreferencesData>

    suspend fun updateFormattingPreferences(
        userId: String,
        preferences: UpdateFormattingPreferencesData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * KENYAN LOCATIONS
     * ---------------------------------------------------------
     */

    suspend fun getCounties(): Result<List<CountyData>>

    suspend fun getCounty(
        countyId: String
    ): Result<CountyData>

    suspend fun getSubCounties(
        countyId: String
    ): Result<List<SubCountyData>>

    suspend fun getSubCounty(
        subCountyId: String
    ): Result<SubCountyData>

    suspend fun getWards(
        subCountyId: String
    ): Result<List<WardData>>

    suspend fun getWard(
        wardId: String
    ): Result<WardData>

    suspend fun getLocations(
        wardId: String
    ): Result<List<LocationData>>

    suspend fun searchLocations(
        query: String,
        countyId: String?
    ): Result<List<LocationSearchResultData>>


    /*
     * ---------------------------------------------------------
     * ADDRESS
     * ---------------------------------------------------------
     */

    suspend fun formatAddress(
        address: AddressData
    ): Result<String>

    suspend fun validateAddress(
        address: AddressData
    ): Result<AddressValidationData>

    suspend fun normalizeAddress(
        address: AddressData
    ): Result<AddressData>


    /*
     * ---------------------------------------------------------
     * REGIONAL SETTINGS
     * ---------------------------------------------------------
     */

    suspend fun getRegionalSettings(
        organizationId: String
    ): Result<RegionalSettingsData>

    suspend fun updateRegionalSettings(
        actorId: String,
        organizationId: String,
        settings: UpdateRegionalSettingsData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * LOCALIZED CONTENT
     * ---------------------------------------------------------
     */

    suspend fun getLocalizedPropertyTypes(
        languageCode: String
    ): Result<List<LocalizedOptionData>>

    suspend fun getLocalizedLeaseTypes(
        languageCode: String
    ): Result<List<LocalizedOptionData>>

    suspend fun getLocalizedPaymentMethods(
        languageCode: String
    ): Result<List<LocalizedOptionData>>

    suspend fun getLocalizedMaintenanceCategories(
        languageCode: String
    ): Result<List<LocalizedOptionData>>


    /*
     * ---------------------------------------------------------
     * REGIONAL CONFIGURATION
     * ---------------------------------------------------------
     */

    suspend fun getCountryConfiguration(
        countryCode: String
    ): Result<CountryConfigurationData>

    suspend fun getSupportedCountries(): Result<List<CountryConfigurationData>>


    /*
     * ---------------------------------------------------------
     * CACHE
     * ---------------------------------------------------------
     */

    suspend fun refreshLocalizationCache(): Result<Unit>

    suspend fun clearLocalizationCache(): Result<Unit>
}


/*
 * =============================================================
 * DASHBOARD
 * =============================================================
 */

data class LocalizationDashboardData(
    val organizationId: String,
    val language: LanguageData,
    val currency: CurrencyData,
    val timeZone: TimeZoneData,
    val country: CountryConfigurationData,
    val supportedLanguages: Int,
    val supportedCurrencies: Int
)


/*
 * =============================================================
 * LANGUAGE
 * =============================================================
 */

data class LanguageData(
    val code: String,
    val name: String,
    val nativeName: String,
    val direction: TextDirection,
    val enabled: Boolean
)


/*
 * =============================================================
 * CURRENCY
 * =============================================================
 */

data class CurrencyData(
    val code: String,
    val name: String,
    val symbol: String,
    val decimalPlaces: Int,
    val enabled: Boolean
)

data class ExchangeRateData(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val source: String?,
    val updatedAt: String
)

data class CurrencyConversionData(
    val originalAmount: Double,
    val convertedAmount: Double,
    val fromCurrency: String,
    val toCurrency: String,
    val exchangeRate: Double,
    val convertedAt: String
)


/*
 * =============================================================
 * TIME ZONE
 * =============================================================
 */

data class TimeZoneData(
    val id: String,
    val name: String,
    val displayName: String,
    val utcOffset: String
)


/*
 * =============================================================
 * FORMATTING
 * =============================================================
 */

data class FormattingPreferencesData(
    val dateFormat: String,
    val timeFormat: String,
    val firstDayOfWeek: Int,
    val decimalSeparator: String,
    val thousandsSeparator: String,
    val use24HourTime: Boolean
)

data class UpdateFormattingPreferencesData(
    val dateFormat: String?,
    val timeFormat: String?,
    val firstDayOfWeek: Int?,
    val decimalSeparator: String?,
    val thousandsSeparator: String?,
    val use24HourTime: Boolean?
)


/*
 * =============================================================
 * KENYAN ADMINISTRATIVE AREAS
 * =============================================================
 */

data class CountyData(
    val id: String,
    val code: String,
    val name: String,
    val latitude: Double?,
    val longitude: Double?
)

data class SubCountyData(
    val id: String,
    val countyId: String,
    val name: String,
    val code: String?
)

data class WardData(
    val id: String,
    val subCountyId: String,
    val name: String,
    val code: String?
)

data class LocationData(
    val id: String,
    val wardId: String,
    val name: String,
    val postalCode: String?,
    val latitude: Double?,
    val longitude: Double?
)

data class LocationSearchResultData(
    val id: String,
    val name: String,
    val type: LocationType,
    val countyName: String?,
    val subCountyName: String?,
    val wardName: String?
)


/*
 * =============================================================
 * ADDRESS
 * =============================================================
 */

data class AddressData(
    val countryCode: String,
    val county: String?,
    val subCounty: String?,
    val ward: String?,
    val location: String?,
    val estate: String?,
    val street: String?,
    val building: String?,
    val houseNumber: String?,
    val postalCode: String?,
    val latitude: Double?,
    val longitude: Double?
)

data class AddressValidationData(
    val valid: Boolean,
    val normalized: AddressData?,
    val errors: List<String>,
    val warnings: List<String>
)


/*
 * =============================================================
 * REGIONAL SETTINGS
 * =============================================================
 */

data class RegionalSettingsData(
    val organizationId: String,
    val countryCode: String,
    val languageCode: String,
    val currencyCode: String,
    val timeZoneId: String,
    val dateFormat: String,
    val timeFormat: String,
    val measurementSystem: MeasurementSystem,
    val firstDayOfWeek: Int
)

data class UpdateRegionalSettingsData(
    val countryCode: String?,
    val languageCode: String?,
    val currencyCode: String?,
    val timeZoneId: String?,
    val dateFormat: String?,
    val timeFormat: String?,
    val measurementSystem: MeasurementSystem?,
    val firstDayOfWeek: Int?
)


/*
 * =============================================================
 * LOCALIZED OPTIONS
 * =============================================================
 */

data class LocalizedOptionData(
    val id: String,
    val key: String,
    val label: String,
    val description: String?
)


/*
 * =============================================================
 * COUNTRY CONFIGURATION
 * =============================================================
 */

data class CountryConfigurationData(
    val countryCode: String,
    val countryName: String,
    val currencyCode: String,
    val defaultLanguageCode: String,
    val defaultTimeZone: String,
    val phoneCode: String,
    val measurementSystem: MeasurementSystem,
    val addressFormat: String,
    val enabled: Boolean
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class TextDirection {
    LTR,
    RTL
}

enum class LocationType {
    COUNTRY,
    COUNTY,
    SUB_COUNTY,
    WARD,
    LOCATION,
    ESTATE,
    STREET
}

enum class MeasurementSystem {
    METRIC,
    IMPERIAL
}