package com.webapk.lovelyroleplayv1

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.userAgentString = "Mozilla/5.0 (Linux; Android 12; Mobile) AppleWebKit/537.36 Chrome/120 Mobile Safari/537.36"
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                injectCSS(view)
                injectJS(view)
            }
        }
        webView.webChromeClient = WebChromeClient()
        webView.loadUrl("https://link.prod.sekai.chat/wqryZu")
    }

    private fun injectCSS(view: WebView?) {
        view?.evaluateJavascript("""
            (function(){
                var s=document.createElement('style');
                s.textContent='[class*=cookie],[id*=cookie],[class*=popup],[class*=modal],[class*=overlay],[class*=banner],[class*=gdpr]{display:none!important}';
                document.head.appendChild(s);
                new MutationObserver(function(ms){ms.forEach(function(m){m.addedNodes.forEach(function(n){if(n.nodeType===1&&/cookie|popup|modal|overlay|banner|gdpr/i.test((n.className||'')+(n.id||''))){n.style.setProperty('display','none','important')}})})}).observe(document.body,{childList:true,subtree:true});
            })();
        """.trimIndent(), null)
    }

    private fun injectJS(view: WebView?) {
        view?.evaluateJavascript("(function(){window.open=function(u){window.location.href=u};})();", null)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}
