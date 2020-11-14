package com.anos.covid19.model
import com.google.gson.annotations.SerializedName


data class CountryStats(
    @SerializedName("CountryISO")
    var countryISO: String? = "",
    @SerializedName("Country")
    var country: String? = "",
    @SerializedName("Continent")
    var continent: String? = "",
    @SerializedName("Population")
    var population: Int? = 0,
    @SerializedName("PopulationDensity")
    var populationDensity: Double? = 0.0,
    @SerializedName("MedianAge")
    var medianAge: Double? = 0.0,
    @SerializedName("Aged65Older")
    var aged65Older: Double? = 0.0,
    @SerializedName("Aged70Older")
    var aged70Older: Double? = 0.0,
    @SerializedName("ExtremePoverty")
    var extremePoverty: Int? = 0,
    @SerializedName("GdpPerCapita")
    var gdpPerCapita: Double? = 0.0,
    @SerializedName("CvdDeathRate")
    var cvdDeathRate: Double? = 0.0,
    @SerializedName("DiabetesPrevalence")
    var diabetesPrevalence: Double? = 0.0,
    @SerializedName("HandwashingFacilities")
    var handwashingFacilities: Double? = 0.0,
    @SerializedName("HospitalBedsPerThousand")
    var hospitalBedsPerThousand: Double? = 0.0,
    @SerializedName("LifeExpectancy")
    var lifeExpectancy: Double? = 0.0,
    @SerializedName("FemaleSmokers")
    var femaleSmokers: Int? = 0,
    @SerializedName("MaleSmokers")
    var maleSmokers: Int? = 0
)