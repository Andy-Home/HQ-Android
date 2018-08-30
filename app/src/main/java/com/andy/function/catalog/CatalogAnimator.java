package com.andy.function.catalog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Andy on 2018/8/30.
 * Modify time 2018/8/30
 */
public class CatalogAnimator {

    public static void funPopAnimator(View parent, View child, int position) {
        int margin = 50;
        child.setVisibility(View.VISIBLE);

        float currentPosition = parent.getTranslationX();

        float to = parent.getHeight() * position + (margin * position);
        ObjectAnimator animator = ObjectAnimator.ofFloat(child, "translationY", currentPosition, to * -1);
        animator.setDuration(500);
        animator.start();
    }

    public static void funBackAnimator(View parent, final View child, int position) {
        int margin = 50;

        float currentPosition = child.getTranslationX();

        float to = parent.getHeight() * position + (margin * position);
        ObjectAnimator animator = ObjectAnimator.ofFloat(child, "translationY", to * -1, currentPosition);
        animator.setDuration(500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                child.setVisibility(View.GONE);
            }
        });
    }
}
