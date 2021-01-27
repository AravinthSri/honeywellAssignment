package com.honeywell.assignment.model

data class SearchResponse(
    val exhaustiveNbHits: Boolean,
    val hits: List<Hit>,
    val hitsPerPage: Int,
    val nbHits: Int,
    val nbPages: Int,
    val page: Int,
    val params: String,
    val processingTimeMS: Int,
    val query: String
)

data class Hit(
    val _highlightResult: HighlightResult,
    val _tags: List<String>,
    val author: String,
    val comment_text: String,
    val created_at: String,
    val created_at_i: Int,
    val num_comments: Int,
    val objectID: String,
    val parent_id: String,
    val points: Int,
    val relevancy_score: Int,
    val story_id: String,
    val story_text: String,
    val story_title: String,
    val story_url: String,
    val title: String,
    val url: String
)

data class HighlightResult(
    val author: Author,
    val story_text: StoryText,
    val title: Title,
    val url: Url
)

data class Author(
    val matchLevel: String,
    val matchedWords: List<String>,
    val value: String
)

data class StoryText(
    val fullyHighlighted: Boolean,
    val matchLevel: String,
    val matchedWords: List<String>,
    val value: String
)

data class Title(
    val fullyHighlighted: Boolean,
    val matchLevel: String,
    val matchedWords: List<String>,
    val value: String
)

data class Url(
    val fullyHighlighted: Boolean,
    val matchLevel: String,
    val matchedWords: List<String>,
    val value: String
)