package com.wise.common.commonutils;

import android.app.ActivityManager;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.wise.common.Global;
import com.wise.common.R;

/**
 * Created by sunpeng on 17/8/3.
 */

public class MyGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ViewTarget.setTagId(R.id.glide_tag_id);
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "glide_cache", Global.MAX_CACHE_DISK_SIZE));
        //指定内存缓存大小
        int memoryClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        builder.setMemoryCache(new LruResourceCache((int) (memoryClass * Global.MAX_CACHE_MEMORY_SIZE)));
        //全部的内存缓存用来作为图片缓存
        builder.setBitmapPool(new LruBitmapPool((int) (memoryClass * Global.MAX_CACHE_MEMORY_SIZE)));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }

}
