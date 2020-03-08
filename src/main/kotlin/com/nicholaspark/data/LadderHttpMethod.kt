package com.nicholaspark.data

enum class LadderHttpMethod {
    POST,
    GET,
    DELETE,
    PUT,
    PATCH;

    override fun toString() = this.name.toUpperCase()
}