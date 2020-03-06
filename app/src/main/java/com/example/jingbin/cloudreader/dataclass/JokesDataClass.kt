package com.example.jingbin.cloudreader.dataclass

/**
 * @author One
 * @time 2019/11/30 14:36
 * @version 1.0
 */
data class JokesDataClass(
        val count: Int,
        val err: Int,
        val items: List<Item>,
        val page: Int,
        val refresh: Int,
        val total: Int
)

data class Item(
        val allow_comment: Boolean,
        val comments_count: Int,
        val content: String,
        val created_at: Int,
        val format: String,
        val hot_comment: HotComment,
        val id: Int,
        val image: String,
        val is_prefer: Any,
        val is_promote: Boolean,
        val published_at: Int,
        val share_count: Int,
        val state: String,
        val tag: String,
        val topic: Topic,
        val type: String,
        val user: UserX,
        val votes: Votes
)

data class HotComment(
        val at_infos: Any,
        val content: String,
        val created_at: Int,
        val floor: Int,
        val hot_comment_type: Int,
        val id: Int,
        val like_count: Int,
        val parent_id: Int,
        val score: Int,
        val user: User
)

data class User(
        val age: Int,
        val astrology: String,
        val gender: String,
        val icon: String,
        val id: Int,
        val login: String,
        val medium: String,
        val role: String,
        val state: String,
        val talents: List<Talent>,
        val thumb: String,
        val uid: Int
)

data class Talent(
        val cmd: Int,
        val cmd_desc: String,
        val remark: String
)

data class Topic(
        val avatar: String,
        val background: String,
        val content: String,
        val id: Int,
        val introduction: String,
        val type: Int
)

data class UserX(
        val age: Int,
        val astrology: String,
        val gender: String,
        val icon: String,
        val id: Int,
        val login: String,
        val medium: String,
        val rel: String,
        val role: String,
        val state: String,
        val thumb: String,
        val uid: Int
)

data class Votes(
        val down: Int,
        val up: Int
)