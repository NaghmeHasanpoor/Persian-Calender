package com.arpitas.persiancalender.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.connections.APIClient;
import com.arpitas.persiancalender.connections.APIInterface;
import com.arpitas.persiancalender.entity.Ad;
import com.arpitas.persiancalender.entity.AdItem;
import com.arpitas.persiancalender.interfaces.ShowInterListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageAdsManager {
    private static ImageAdsManager mInstance;
    public static boolean AD_STATE;
    ShowInterListener listener;
    Context mContext;
    ViewGroup parent;
    RelativeLayout layout;
    ImageButton btnClose;
    ImageView imageView;
    SharedPrefManager shared;
    int index;

    public static ImageAdsManager getInstance(final Context context) {
        if (null == mInstance) {
            ImageAdsManager.mInstance = new ImageAdsManager(context);
        }
        return ImageAdsManager.mInstance;
    }

    private ImageAdsManager(final Context context) {
        this.shared = new SharedPrefManager(context);
        this.mContext = context;
    }


    public void displayImageAd(ViewGroup parent, ShowInterListener listener) {
        this.parent = parent;
        this.listener = listener;
        this.layout = new RelativeLayout(parent.getContext());
        this.imageView = new ImageView(parent.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.layout.setLayoutParams(params);
        this.imageView.setLayoutParams(params);
        this.layout.addView(this.imageView);
        parent.addView(this.layout);


//        parent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

//        ImageView imv = ((Activity)parent.getContext()).findViewById(R.id.imv);
        this.btnClose = new ImageButton(parent.getContext());
        final RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(40, 40, 40, 40);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        this.btnClose.setLayoutParams(buttonParams);
        this.btnClose.setImageResource(R.drawable.ic_close);
        this.btnClose.setBackgroundColor(parent.getContext().getResources().getColor(R.color.white));
        this.btnClose.setPadding(15, 15, 15, 15);
        parent.addView(this.btnClose);
        layout.setVisibility(View.INVISIBLE);
        btnClose.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layout.setTranslationZ(50f);
            btnClose.setTranslationZ(52f);
            layout.bringToFront();
        }

        Ad ad = shared.get_ad_object();
        if (ad != null) {
            List<AdItem> adItems = ad.getAds();
            this.index = this.shared.getInterIndex();

            this.shared.setStateShowInterBefore(true);
            Picasso.get()
                    .load(adItems.get(this.index).getImage())
                    .into(this.imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            AD_STATE = true;
                            shared.setStateShowInterBefore(true);
                            shared.set_boolean_value(SharedPrefManager.SHOW_APP_BRAIN_OR_PANDORA_2, true);

                            Utils.showProgressBar(parent.getContext());
                            new Handler().postDelayed(() -> {
                                Utils.dismissProgress();
                                layout.setVisibility(View.VISIBLE);
                                btnClose.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                            }, 800);
//                             Toast.makeText(mContext, "load success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Exception e) {
                            AD_STATE = false;
                            layout.setVisibility(View.INVISIBLE);
                            btnClose.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                            listener.onShowInter(true);
                            parent.removeView(layout);
                            parent.removeView(btnClose);
                            shared.setStateShowInterBefore(false);
//                             Toast.makeText(mContext, "load failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


            this.btnClose.setOnClickListener(view -> {
                ++this.index;
                this.shared.setInterIndex(this.index);
                Picasso.get().load(shared.get_ad_object().getAds()
                        .get(this.shared.get_int_value(SharedPrefManager.index_for_display_inter)).getImage());

                ImageAdsManager.AD_STATE = false;
                listener.onShowInter(true);
                parent.removeView(this.layout);
                parent.removeView(this.btnClose);
            });

            this.imageView.setOnClickListener(view -> {
                parent.removeView(this.layout);
                parent.removeView(this.btnClose);
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(ad.getAds().get(this.index).getAction()));
                if (!ad.getAds().get(this.index).getPackageNametoExist().isEmpty()
                        && Utils.isPackageInstalled(ad.getAds().get(this.index).getPackageNametoExist(), this.mContext)) {
                    intent.setPackage(ad.getAds().get(this.index).getPackageNametoExist());
                }

                ImageAdsManager.AD_STATE = false;
                listener.onShowInter(true);
                this.mContext.startActivity(intent);
            });

            this.requestViewUrl(adItems.get(this.index).getView());
        } else {
            listener.onShowInter(false);
        }
    }

//    public void displayyImageAd( ViewGroup parent) {
//        if (AD_STATE)
//
//    }

    private void requestViewUrl(final String viewUrl) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.requestView(viewUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(final Call<ResponseBody> call, final Response<ResponseBody> response) {
                Log.e("TAG", "onSuccess_requestView");
            }

            @Override
            public void onFailure(final Call<ResponseBody> call, final Throwable t) {
                Log.e("TAG", "onFailure_requestView" + t.getMessage());
            }
        });
    }

    public void hideImageAd() {
        this.parent.removeView(this.btnClose);
        this.parent.removeView(this.layout);

        ImageAdsManager.AD_STATE = false;
        this.listener.onShowInter(false);

    }
}
