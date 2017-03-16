package com.app.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import medrep.medrep.R;

/**
 * Created by kishore on 18/10/15.
 */
public class DoctorSurveyFragment extends Fragment {

    private String url = null;
    private WebView surveyWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.doc_survey, container, false);
        surveyWebView = (WebView) v.findViewById(R.id.survey_list);
        ImageView back = (ImageView) v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

       // v.findViewById(R.id.bell_image).setVisibility(View.GONE);
        ((TextView) v.findViewById(R.id.notification_company)).setText("Survey");

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WebSettings settings = surveyWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        surveyWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        final ProgressDialog progressBar = ProgressDialog.show(getActivity(), "WebView ", "Loading...");


        surveyWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("TAG", "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i("TAG", "Finished loading URL: " + url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("TAG", "Error: " + description);
                Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });


        if(url != null && !url.trim().equals("")){
            surveyWebView.loadUrl(url);
        }
    }

    public void setUrl(String url){
        this.url = url;
    }
}
