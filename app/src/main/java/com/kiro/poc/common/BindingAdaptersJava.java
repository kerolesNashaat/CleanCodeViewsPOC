package com.kiro.poc.common;

import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import coil.Coil;
import coil.request.ImageRequest;
import com.kiro.poc.common.UiText;

public class BindingAdaptersJava {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        if (url != null) {
            ImageRequest request = new ImageRequest.Builder(view.getContext())
                    .data(url)
                    .target(view)
                    .build();
            Coil.imageLoader(view.getContext()).enqueue(request);
        }
    }

    @BindingAdapter("uiText")
    public static void setUiText(android.widget.TextView view, UiText uiText) {
        if (uiText != null) {
            view.setText(uiText.asString(view.getContext()));
        }
    }

    @BindingAdapter("statusColor")
    public static void setStatusColor(android.view.View view, String status) {
        if (status == null) return;
        int color;
        switch (status.toLowerCase()) {
            case "alive":
                color = android.graphics.Color.GREEN;
                break;
            case "dead":
                color = android.graphics.Color.RED;
                break;
            default:
                color = android.graphics.Color.GRAY;
                break;
        }
        android.graphics.drawable.GradientDrawable shape = new android.graphics.drawable.GradientDrawable();
        shape.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        shape.setColor(color);
        view.setBackground(shape);
    }
}
