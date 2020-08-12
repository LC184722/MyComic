package com.qc.mycomic.ui.fragment;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qc.mycomic.R;
import com.qc.mycomic.model.ComicInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.presenter.BasePresenter;

/**
 * @author LuQiChuang
 * @description
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class MyWebFragment extends BaseFragment {

    ComicInfo comicInfo;
    String url = "http://m.pufei8.com/manhua/1584";
    WebView webView;

    public MyWebFragment(ComicInfo comicInfo) {
        this.comicInfo = comicInfo;
    }

    public MyWebFragment() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_web;
    }

    @Override
    protected void initView(View rootView) {
        mTopLayout.setVisibility(View.GONE);
        webView = rootView.findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        settings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        settings.setSupportZoom(true);//是否可以缩放，默认true
        settings.setBuiltInZoomControls(false);//是否显示缩放按钮，默认false
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setAppCacheEnabled(false);//是否使用缓存
        settings.setDomStorageEnabled(true);//DOM Storage 重点是设置这个

        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(getWebViewClient());
        webView.setWebChromeClient(getWebChromeClient());
        webView.loadUrl(url);
//        MyWebActivity.newInstance(_mActivity, "???", url);
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            // 注意啦，此处就是执行了js以后 的网页源码
            Log.i(TAG, "processHTML: " + html.length());
            Document document = Jsoup.parse(html);
            Elements elements = document.select("img");
            for (Element element : elements) {
                Log.i(TAG, "processHTML: " + element.toString());
            }
        }
    }

    private WebViewClient getWebViewClient() {
        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(TAG, "onPageStarted: start");
                showLoadingPage();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(TAG, "onPageFinished: finish");
                //document.getElementsByClassName('adbox')[0].style.display = 'none'
                //document.getElementsByTagName("img")[2].style.display = 'none';
                webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                showContentPage();
            }
        };
        return client;
    }

    private WebChromeClient getWebChromeClient() {
        WebChromeClient client = new WebChromeClient() {

        };
        return client;
    }


    @Override
    public BasePresenter getPresenter() {
        return null;
    }

}
