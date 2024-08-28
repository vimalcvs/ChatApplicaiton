package com.example.chatapplicaiton.model

class ModelMessage {
    var message: String? = null
    var senderId: String? = null

    constructor()

    constructor(message: String?, senderId: String?) {
        this.message = message
        this.senderId = senderId
    }
}