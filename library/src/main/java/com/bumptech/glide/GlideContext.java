package com.bumptech.glide;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.ViewTarget;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Global context for all loads in Glide containing and exposing the various多种多样的 registries登记。注册 and classes
 * required to load resources.
 */
public class GlideContext extends ContextWrapper {
  @VisibleForTesting
  static final TransitionOptions<?, ?> DEFAULT_TRANSITION_OPTIONS =
      new GenericTransitionOptions<>();
  private final ArrayPool arrayPool;
  private final Registry registry;
  private final ImageViewTargetFactory imageViewTargetFactory;
  private final RequestOptions defaultRequestOptions;//默认图片加载配置  占位图、错误图片等
  private final List<RequestListener<Object>> defaultRequestListeners;//全局资源加载监听器
  private final Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions;//默认图片转场动画
  private final Engine engine;
  private final boolean isLoggingRequestOriginsEnabled;
  private final int logLevel;//日志级别

  public GlideContext(
      @NonNull Context context,
      @NonNull ArrayPool arrayPool,
      @NonNull Registry registry,
      @NonNull ImageViewTargetFactory imageViewTargetFactory,
      @NonNull RequestOptions defaultRequestOptions,
      @NonNull Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions,
      @NonNull List<RequestListener<Object>> defaultRequestListeners,
      @NonNull Engine engine,
      boolean isLoggingRequestOriginsEnabled,
      int logLevel) {
    super(context.getApplicationContext());
    this.arrayPool = arrayPool;
    this.registry = registry;
    this.imageViewTargetFactory = imageViewTargetFactory;
    this.defaultRequestOptions = defaultRequestOptions;
    this.defaultRequestListeners = defaultRequestListeners;
    this.defaultTransitionOptions = defaultTransitionOptions;
    this.engine = engine;
    this.isLoggingRequestOriginsEnabled = isLoggingRequestOriginsEnabled;
    this.logLevel = logLevel;
  }

  public List<RequestListener<Object>> getDefaultRequestListeners() {
    return defaultRequestListeners;
  }

  public RequestOptions getDefaultRequestOptions() {
    return defaultRequestOptions;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public <T> TransitionOptions<?, T> getDefaultTransitionOptions(@NonNull Class<T> transcodeClass) {
    TransitionOptions<?, ?> result = defaultTransitionOptions.get(transcodeClass);
    if (result == null) {
      for (Entry<Class<?>, TransitionOptions<?, ?>> value : defaultTransitionOptions.entrySet()) {
        if (value.getKey().isAssignableFrom(transcodeClass)) {
          result = value.getValue();
        }
      }
    }
    if (result == null) {
      result = DEFAULT_TRANSITION_OPTIONS;
    }
    return (TransitionOptions<?, T>) result;
  }

  @NonNull
  public <X> ViewTarget<ImageView, X> buildImageViewTarget(
      @NonNull ImageView imageView, @NonNull Class<X> transcodeClass) {
    return imageViewTargetFactory.buildTarget(imageView, transcodeClass);
  }

  @NonNull
  public Engine getEngine() {
    return engine;
  }

  @NonNull
  public Registry getRegistry() {
    return registry;
  }

  public int getLogLevel() {
    return logLevel;
  }

  @NonNull
  public ArrayPool getArrayPool() {
    return arrayPool;
  }

  /**
   * Returns {@code true} if Glide should populate
   * {@link com.bumptech.glide.load.engine.GlideException#setOrigin(Exception)} for failed requests.
   *
   * <p>This is an experimental API that may be removed in the future.
   */
  public boolean isLoggingRequestOriginsEnabled() {
    return isLoggingRequestOriginsEnabled;
  }
}
