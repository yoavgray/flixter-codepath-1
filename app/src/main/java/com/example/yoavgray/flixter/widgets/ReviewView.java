package com.example.yoavgray.flixter.widgets;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yoavgray.flixter.R;
import com.example.yoavgray.flixter.models.Review;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewView extends LinearLayout {
    private Context context;
    private Review review;

    @BindView(R.id.text_view_review_author) TextView authorTextView;
    @BindView(R.id.text_view_content) TextView contentTextView;
    @BindView(R.id.text_view_url) TextView urlTextView;


    public ReviewView(Context context) {
        super(context);
        init(context);
    }

    public ReviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        inflate(context, R.layout.widget_review, this);
        ButterKnife.bind(this);
    }

    public void setReview(Review review) {
        this.review = review;

        if (authorTextView != null) authorTextView.setText(review.getAuthor());
        if (contentTextView != null) contentTextView.setText(review.getContent());
        if (urlTextView != null) {
            urlTextView.setText(review.getUrl());
            urlTextView.setPaintFlags(contentTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    @OnClick(R.id.text_view_url)
    public void onUrlClick() {
        Toast.makeText(context, "I'm not openning it!!",Toast.LENGTH_SHORT).show();
    }
}
