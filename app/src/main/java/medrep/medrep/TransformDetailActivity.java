package medrep.medrep;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
        LinearLayout imageLayout = (LinearLayout)findViewById(R.id.imageLayout);

        Log.i("UUUURRRRRLLLLLLLLLLLL", getIntent().getExtras().getString("videoUrl")+"");
        if(getIntent().getExtras().containsKey("innerImgUrl")){
            String imageUrl = getIntent().getExtras().getString("innerImgUrl");
            Bitmap bitmap;
            try{
                InputStream in = new URL(imageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                BitmapDrawable bdrawable = new BitmapDrawable(getResources(),bitmap);
                imageLayout.setBackground(bdrawable);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(getIntent().getExtras().containsKey("videoUrl")) {
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
                    String videoPath = getIntent().getExtras().getString("videoUrl");
                    Log.i("VideoURLLLLLLL", videoPath+"");
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

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoPath));
                    intent.setDataAndType(Uri.parse(videoPath), "video/mp4");
                    startActivity(intent);
                }
            });
        } else if(getIntent().getExtras().get("pdf") != null) {
            Log.i("PDF", "COMING HERE &&&&&&&&&&");
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
//                        Intent intent = new Intent(TransformDetailActivity.this, ShareActivity.class);
//                        startActivity(intent);
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
        LayoutInflater inflater  = (LayoutInflater)TransformDetailActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view1 = inflater.inflate(R.layout.custom_popup, null);
        LinearLayout inOutList = (LinearLayout)view1.findViewById(R.id.popupwindow);
        WebView webview = (WebView)view1.findViewById(R.id.website_webview);
        PopupWindow popupWindow = new PopupWindow(view1, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(view1, Gravity.CENTER, 0, 0);
//        popupWindow.setClippingEnabled(true);
        popupWindow.setTouchable(true);



//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        PopupWindow window = new PopupWindow(this);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        WebView wv = new WebView(this);
        webview.loadUrl("http://www.artofliving.org/in-en");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
//        alert.setView(wv);
//        window.setContentView(wv);
//        window.showAsDropDown(wv);
//        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//            }
//        });
//        alert.show();
    }
}
