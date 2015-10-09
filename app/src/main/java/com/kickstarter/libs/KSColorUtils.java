package com.kickstarter.libs;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;

import com.kickstarter.R;

public class KSColorUtils {
  private KSColorUtils() {}

  private final static float KICKSTARTER_LIGHTNESS_THRESHOLD = 0.72f;

  /**
   * Set the alpha portion of the color.
   *
   * @param color   the (a)rgb color to set an alpha for.
   * @param alpha   the new alpha value, between 0 and 255.
   */
  public static @ColorInt int setAlpha(final int color, @IntRange(from=0, to=255) final int alpha) {
    return color | (alpha << 24);
  }

  public static @ColorRes int darkColorId() {
    return R.color.text_dark;
  }

  /**
   * Darken the argb color by a percentage.
   *
   * @param color   the argb color to lighten.
   * @param percent percentage to darken by, between 0.0 and 1.0.
   */
  public static @ColorInt int darken(@ColorInt final int color, @FloatRange(from=0.0, to=1.0) final float percent) {
    final float[] hsl = new float[3];
    ColorUtils.colorToHSL(color, hsl);
    hsl[2] -= (hsl[2] * percent);
    // HSLToColor sets alpha to fully opaque, so pluck the alpha from the original color.
    return (color & 0xFF000000) | (ColorUtils.HSLToColor(hsl) & 0x00FFFFFF);
  }

  public static @ColorRes int lightColorId() {
    return R.color.white;
  }

  /**
   * Lighten the argb color by a percentage.
   *
   * @param color   the argb color to lighten.
   * @param percent percentage to lighten by, between 0.0 and 1.0.
   */
  public static @ColorInt int lighten(@ColorInt final int color, @FloatRange(from=0.0, to=1.0) final float percent) {
    final float[] hsl = new float[3];
    ColorUtils.colorToHSL(color, hsl);
    hsl[2] += (1.0f - hsl[2]) * percent;
    // HSLToColor sets alpha to fully opaque, so pluck the alpha from the original color.
    return (color & 0xFF000000) | (ColorUtils.HSLToColor(hsl) & 0x00FFFFFF);
  }

  /**
   * Check whether a color is light.
   *
   * @param color   the argb color to check.
   */
  public static boolean isLight(@ColorInt final int color) {
    return weightedLightness(color) >= KICKSTARTER_LIGHTNESS_THRESHOLD;
  }

  /**
   * Check whether a color is dark.
   *
   * @param color   the argb color to check.
   */
  public static boolean isDark(@ColorInt final int color) {
    return !isLight(color);
  }

  public static @ColorInt int foregroundColor(final int backgroundColor, @NonNull final Context context) {
    final @ColorRes int colorId = isLight(backgroundColor) ? darkColorId() : lightColorId();
    return context.getResources().getColor(colorId);
  }

  /*
   * Return a value between 0.0 and 1.0 representing the perceived lightness of the color.
   * More info here: https://robots.thoughtbot.com/closer-look-color-lightness
   */
  private static double weightedLightness(@ColorInt final int color) {
    return ((Color.red(color) * 212.6 + Color.green(color) * 715.2 + Color.blue(color) * 72.2) / 1000) / 255;
  }
}
