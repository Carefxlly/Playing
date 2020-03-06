package com.example.jingbin.cloudreader.app;

import android.content.Context;

import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;

import java.io.File;

/**
 * @author One
 * @version 1.0
 * @time 2019/12/27 14:34
 */
class OneDiskLruCacheFactory extends DiskLruCacheFactory {

    OneDiskLruCacheFactory(Context context) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR,
                DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
    }

    OneDiskLruCacheFactory(Context context, long diskCacheSize) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR, diskCacheSize);
    }

    OneDiskLruCacheFactory(final Context context, final String diskCacheName, long diskCacheSize) {
        super(() -> {
            File cacheDirectory = context.getExternalCacheDir();
            if (cacheDirectory == null) {
                return null;
            }
            if (diskCacheName != null) {
                return new File(cacheDirectory, diskCacheName);
            }
            return cacheDirectory;
        }, diskCacheSize);
    }
}
