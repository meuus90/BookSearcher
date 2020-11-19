package com.meuus90.booksearcher.base.view.util

import com.bumptech.glide.load.Key
import java.nio.ByteBuffer
import java.security.MessageDigest

class IntegerVersionSignature(private val currentVersion: Int) : Key {
    override fun equals(other: Any?): Boolean {
        if (other is IntegerVersionSignature) {
            val o = other as IntegerVersionSignature?
            return currentVersion == o!!.currentVersion
        }

        return false
    }

    override fun hashCode(): Int {
        return currentVersion
    }

    override fun updateDiskCacheKey(md: MessageDigest) {
        md.update(ByteBuffer.allocate(Integer.SIZE).putInt(currentVersion).array())
    }
}
