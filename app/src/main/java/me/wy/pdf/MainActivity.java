package me.wy.pdf;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;

/**
 * Created by WangYi
 *
 * @Date : 2018/3/13
 * @Desc : 利用pdf.js预览文件demo
 */
public class MainActivity extends AppCompatActivity {

    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //加载本地文件
//        preView("file:///android_asset/demo.pdf");
        //加载允许跨域访问的文件
        preView("http://ldjy-static.oss-cn-shanghai.aliyuncs.com/lesson/201809/08feeb0e948f0481.pdf");
        // "http://p5grppofr.bkt.clouddn.com/pdf-js-demo.pdf"
        // "http://ldjy-static.oss-cn-shanghai.aliyuncs.com/lesson/201809/08feeb0e948f0481.pdf";
        //跨域加载文件 先将pdf下载到本地在加载
//        download("http://p5grppofr.bkt.clouddn.com/pdf-js-demo.pdf");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mWebView = findViewById(R.id.webView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
    }

    /**
     * 下载pdf文件到本地
     *
     * @param url 文件url
     */
    private void download(String url) {
        DownloadUtil.download(url, getCacheDir() + "/temp.pdf",
                new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(final String path) {
                        Log.d("MainActivity", "onDownloadSuccess: " + path);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                preView(path);
                            }
                        });
                    }

                    @Override
                    public void onDownloading(int progress) {
                        Log.d("MainActivity", "onDownloading: " + progress);
                    }

                    @Override
                    public void onDownloadFailed(String msg) {
                        Log.d("MainActivity", "onDownloadFailed: " + msg);
                    }
                });
    }

    /**
     * 预览pdf
     *
     * @param pdfUrl url或者本地文件路径
     */
    private void preView(String pdfUrl) {
        //1.只使用pdf.js渲染功能，自定义预览UI界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//api >= 19
            mWebView.loadUrl("file:///android_asset/index.html?" + pdfUrl);
        }else{
            if (!TextUtils.isEmpty(pdfUrl)) {

                byte[] bytes = null;

                try {// 获取以字符编码为utf-8的字符

                    bytes = pdfUrl.getBytes("UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();

                }

                if (bytes != null) {

                    pdfUrl = new BASE64Encoder().encode(bytes);// BASE64转码

                }

            }

            mWebView.loadUrl("file:///android_asset/pdf/web/viewer.html?file=" + pdfUrl);

        }
        //2.使用mozilla官方demo加载在线pdf
//        mWebView.loadUrl("http://mozilla.github.io/pdf.js/web/viewer.html?file=" + pdfUrl);
        //3.pdf.js放到本地
//        mWebView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + pdfUrl);
        //4.使用谷歌文档服务
//        mWebView.loadUrl("http://docs.google.com/gviewembedded=true&url=" + pdfUrl);
    }
}
