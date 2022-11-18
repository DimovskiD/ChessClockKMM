package com.deluxe1.chessclock.data

/**Generic resource class, mostly used for handling async loading with [LiveData] objects
 * @property status - the status of the async operation, one of [Status]
 * @property data - the loaded data - can be null in case of [Status.ERROR] or [Status.LOADING]
 * @property messageRes - resource id for the string message - commonly used to indicate the problem with [Status.ERROR]
 * but it can also carry meta data for the other statuses, too*/
data class Resource<out T>(val status: Status, val data: T?, val messageRes: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = Status.SUCCESS, data = data, messageRes = null)

        fun <T> error(data: T?, messageRes: String): Resource<T> =
            Resource(status = Status.ERROR, data = data, messageRes = messageRes)

        fun <T> loading(data: T?): Resource<T> = Resource(status = Status.LOADING, data = data, messageRes = null)
    }
}