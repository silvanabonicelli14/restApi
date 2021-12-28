package demo.application.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
class Customer(
    @JsonProperty("id")
    val id: String,

    @JsonProperty("telephone")
    val telephone: String,

    @JsonProperty("favorites")
    val favorites: List<String>
)
