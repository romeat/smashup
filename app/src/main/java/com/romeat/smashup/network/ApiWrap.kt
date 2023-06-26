package com.romeat.smashup.network


class ApiWrap<T>(
    // указывает статус-код. Присутствует всегда
    val status: String,
    // хранит ответ на поступивший запрос
    val response: T?,
    // код сообщения, который предполагается для того, чтобы показать пользователю (по крайней мере на вебе)
    val message: String? = null,
    // класс исключения. Появляется только при внутренней ошибке сервера
    val exceptionClass: String? = null
)