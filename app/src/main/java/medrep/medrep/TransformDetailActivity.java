package medrep.medrep;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.HorizontalScrollView;
import android.widget.MediaController;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class TransformDetailActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transform_detail);
        String newsTitleValue = getIntent().getExtras().getString("newsTitle");
        String titleDescription = getIntent().getExtras().getString("description");
        TextView description = (TextView)findViewById(R.id.description);
        description.setText(Html.fromHtml(titleDescription));
        System.out.println(getIntent().getExtras().getString("video"));
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout searchLayout = (LinearLayout)findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        Button shareArticle = (Button)findViewById(R.id.shareArticle);
        shareArticle.setOnClickListener(this);
        Button takeWebsite = (Button)findViewById(R.id.takeWebsite);
        takeWebsite.setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Details");
        if(getIntent().getExtras().getString("video") != null) {
            final int[] stopPosition = new int[1];

            final ImageView newsImage = (ImageView)findViewById(R.id.newsImage);
            newsImage.setVisibility(View.VISIBLE);
            newsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    LinearLayout imageLayout = (LinearLayout)findViewById(R.id.imageLayout);
//                    imageLayout.setVisibility(View.GONE);
//                    final VideoView videoView = (VideoView)findViewById(R.id.videoView);
//                    videoView.setVisibility(View.VISIBLE);
//                    MediaController mediaController = new MediaController(TransformDetailActivity.this);
//                    mediaController.setAnchorView(videoView);
//                    videoView.setMediaController(mediaController);
                    String path = "http://techslides.com/demos/sample-videos/small.mp4";
//                    Uri uri=Uri.parse(path);
//                    videoView.setVideoURI(uri);
                    //MediaController mc = new MediaController(TransformDetailActivity.this);

                    //videoView.start();
//                    videoView.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            System.out.print(videoView.isPlaying());
//                            if(videoView.isPlaying()) {
//                                stopPosition[0] = videoView.getCurrentPosition();
//                                videoView.pause();
//                            } else {
//                                videoView.seekTo(stopPosition[0]);
//                                videoView.start();
//                            }
//                            return false;
//                        }
//                    });
//                    String path = "android.resource://" + getPackageName() + "/" + R.raw.welcome;
//                    Uri videoUri = Uri.parse(path);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
//                    intent.setDataAndType(videoUri, "video/*");
//                    startActivity(intent);

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                    intent.setDataAndType(Uri.parse(path), "video/mp4");
                    startActivity(intent);
                }
            });
        } else if(getIntent().getExtras().get("pdf") != null) {
            Log.i("PDF", "COMING HERE &&&&&&&&&&");
            LinearLayout imageLayout = (LinearLayout)findViewById(R.id.imageLayout);
            imageLayout.setBackgroundResource(R.drawable.pdf);
            //imageLayout.setVisibility(View.GONE);
            WebView pdfWebView = (WebView)findViewById(R.id.pdfWebView);
            pdfWebView.setVisibility(View.VISIBLE);
            pdfWebView.getSettings().setJavaScriptEnabled(true);
//            pdfWebView.getSettings().setLoadWithOverviewMode(true);
//            pdfWebView.getSettings().setUseWideViewPort(true);
//            pdfWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            //pdfWebView.setWebViewClient(new Callback());
            //pdfWebView.getSettings().setPluginsEnabled(true);
            pdfWebView.getSettings().setAllowFileAccess(true);
            String path = "http://www.physiciansfoundation.org/uploads/default/2014_Physicians_Foundation_Biennial_Physician_Survey_Report.pdf";
            String adobe = "http://drive.google.com/viewerng/viewer?embedded=true&url=";
            pdfWebView.loadUrl(adobe + path);
            //setContentView(pdfWebView);
        }
        TextView newsTitle = (TextView)findViewById(R.id.newsTitle);
        newsTitle.setText(newsTitleValue);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
    }

//    private class Callback extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(
//                WebView view, String url) {
//            return(false);
//        }
//    }

    @Override
    public void onClick(View v) {
        int objectId = v.getId();
        switch (objectId) {
            case R.id.onBackClick:
                finish();
                break;
            case R.id.shareArticle:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Medrep");
                dialog.setMessage("Topic posted successfully !!!");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(TransformDetailActivity.this, ShareActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
                break;
            case R.id.takeWebsite:
//                Uri uriUrl = Uri.parse("http://www.artofliving.org/in-en");
//                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
//                startActivity(launchBrowser);
                showWebView();
                break;
        }
    }

    private void showWebView() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        WebView wv = new WebView(this);
        wv.loadUrl("http://www.artofliving.org/in-en");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
