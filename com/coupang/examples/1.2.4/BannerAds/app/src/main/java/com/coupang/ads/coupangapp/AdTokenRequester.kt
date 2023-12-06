package com.coupang.ads.coupangapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.WorkerThread
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit


class AdTokenRequester(
    private val context: Context
) {
    data class TokenResponse(
        val token: String?,
        val createdTime: Long = 0,
        val ttl: Long = 0,
        val tokenId: String? = null,
        val code: Int = 0,
        val message: String,
        val appVersion: String
    ) {
        fun isUnexpired(): Boolean {
            return token != null && System.currentTimeMillis() < (createdTime + ttl) * 1000
        }
    }

    companion object {
        const val CONTENT_PROVIDER = "content://com.coupang.mobile.offsite/"
        const val COUPANG_PACKAGE_NAME = "com.coupang.mobile"
        const val COUPANG_ACTIVITY_CLASS_NAME = "com.coupang.mobile.domain.advertising.offsite.AdActivity"
        const val CP_KEY_TOKEN = "token"
        const val CP_KEY_CREATED_TIME = "created_time"
        const val CP_KEY_TTL = "ttl"
        const val CP_KEY_CODE = "code"
        const val CP_KEY_MESSAGE = "message"
        const val CP_KEY_VERSION = "version"
        const val CP_KEY_REQUEST_ID = "request_id"
    }

    private val uri = Uri.parse(CONTENT_PROVIDER)

    @Volatile
    private var tokenResponse: TokenResponse? = null

    private val singleExecutor by lazy {
        Executors.newSingleThreadExecutor()
    }

    /**
     * Get the ad token from the coupang app
     * This method blocks until it either timeout or gets the token
     * If the token in memory has not expired, it will get the token after a maximum of 200ms of trying to obtain the token.
     */
    @WorkerThread
    fun getAdToken(): Result<TokenResponse> {
        return (tokenResponse?.takeIf { it.isUnexpired() }?.let {
            val future = FutureTask(queryTokenCallable)
            singleExecutor.execute(future)
            Result.success(kotlin.runCatching {
                future.get(200, TimeUnit.MILLISECONDS)
            }.getOrNull()?: it)
        } ?: kotlin.runCatching {
            queryTokenCallable.call()
        })
    }

    private fun wakeupCoupangApp() {
        kotlin.runCatching {
            val intent = Intent()
            intent.setClassName(COUPANG_PACKAGE_NAME, COUPANG_ACTIVITY_CLASS_NAME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    private val queryTokenCallable = Callable {
        var r = getByContentProvider()
        if (r == null) {
            wakeupCoupangApp()
            r = getByContentProvider()
        }
        r?.also {
            tokenResponse = it
        } ?: throw Exception("get Token from Coupang App failed")
    }

    private fun getByContentProvider(): TokenResponse? {
        return context.contentResolver.query(uri, null, null, null, null)?.let {
            it.moveToFirst()
            val token = it.getString(it.getColumnIndexOrThrow(CP_KEY_TOKEN))
            val createdTime = it.getLong(it.getColumnIndexOrThrow(CP_KEY_CREATED_TIME))
            val ttl = it.getLong(it.getColumnIndexOrThrow(CP_KEY_TTL))
            val tokenId = it.getString(it.getColumnIndexOrThrow(CP_KEY_REQUEST_ID))
            val code = it.getInt(it.getColumnIndexOrThrow(CP_KEY_CODE))
            val message = it.getString(it.getColumnIndexOrThrow(CP_KEY_MESSAGE))
            val version = it.getString(it.getColumnIndexOrThrow(CP_KEY_VERSION))
            it.close()

            TokenResponse(token, createdTime, ttl, tokenId, code, message, version)
        }
    }
}