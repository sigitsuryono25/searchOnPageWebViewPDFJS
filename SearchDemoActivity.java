package com.example.sigit.demoapps.WebViewDemo;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.sigit.demoapps.R;

import java.lang.reflect.Method;

public class SearchDemoActivity extends AppCompatActivity {
    private Toolbar toolbar;

    WebView mWebView;
    private RelativeLayout container;
    private Button nextButton, closeButton;
    private EditText findBox;

    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private FrameLayout mTargetView;
    private View mCustomView;
    private ChromeClient mClient;
    private FrameLayout mContentView;
    private ImageView ic_next_search;

    ProgressDialog pd;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_web_view_demo);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWebView = (WebView) findViewById(R.id.webview);
        container = (RelativeLayout) findViewById(R.id.layoutId);

        pd = new ProgressDialog(SearchDemoActivity.this);
        pd.setMessage("Memuat Berkas...");
        pd.show();
        mClient = new ChromeClient();
        mWebView.setWebChromeClient(mClient);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient());
//      mWebView.loadUrl("https://mozilla.github.io/pdf.js/web/viewer.html");
        mWebView.loadUrl("http://sdeveloper.esy.es/pdfjsapi/web/viewer.html?" +
                "file=https://cors-anywhere.herokuapp.com/klinikngawi.esy.es/public_files/Nomor.pdf");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_web_view, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                container.setVisibility(View.VISIBLE);
                search();
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (container.getVisibility() == View.VISIBLE) {
            container.setVisibility(View.GONE);
            mWebView.clearMatches();
        } else {
            super.onBackPressed();
        }
    }

    public void search() {

        findBox = (EditText) findViewById(R.id.searchText);
        findBox.requestFocus();
        findBox.setText("");
        findBox.setMaxLines(1);
        findBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && ((keyCode == KeyEvent.KEYCODE_ENTER))) {
                    mWebView.findAllAsync(findBox.getText().toString());

                    try {
                        Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                    } catch (Exception ignored) {
                    }
                }
                return false;
            }
        });


        ic_next_search = (ImageView) findViewById(R.id.ic_next_search);
        ic_next_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.findNext(true);
            }
        });

    }

    class ChromeClient extends WebChromeClient {

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            mCustomViewCallback = callback;
            mTargetView.addView(view);
            mCustomView = view;
            mContentView.setVisibility(View.GONE);
            mTargetView.setVisibility(View.VISIBLE);
            mTargetView.bringToFront();
        }

        @Override
        public void onHideCustomView() {
            if (mCustomView == null)
                return;

            mCustomView.setVisibility(View.GONE);
            mTargetView.removeView(mCustomView);
            mCustomView = null;
            mTargetView.setVisibility(View.GONE);
            mCustomViewCallback.onCustomViewHidden();
            mContentView.setVisibility(View.VISIBLE);
        }
    }
}
