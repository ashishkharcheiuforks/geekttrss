/*
 * Geekttrss is a RSS feed reader application on the Android Platform.
 *
 * Copyright (C) 2017-2019 by Frederic-Charles Barthelery.
 *
 * This file is part of Geekttrss.
 *
 * Geekttrss is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Geekttrss is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Geekttrss.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.geekorum.ttrss.article_details

import android.content.ContentUris
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.doOnNextLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.geekorum.geekdroid.dagger.DaggerDelegateFragmentFactory
import com.geekorum.geekdroid.dagger.DaggerDelegateViewModelsFactory
import com.geekorum.geekdroid.network.OkHttpWebViewClient
import com.geekorum.ttrss.BaseFragment
import com.geekorum.ttrss.R
import com.geekorum.ttrss.activityViewModels
import com.geekorum.ttrss.articles_list.ArticleListActivity
import com.geekorum.ttrss.data.Article
import com.geekorum.ttrss.databinding.FragmentArticleDetailBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import java.util.Locale
import javax.inject.Inject

/**
 * A fragment representing a single Article detail screen.
 * This fragment is either contained in a [ArticleListActivity]
 * in two-pane mode (on tablets) or a [com.geekorum.ttrss.article_details.ArticleDetailActivity]
 * on handsets.
 */
class ArticleDetailFragment @Inject constructor(
    viewModelsFactory: DaggerDelegateViewModelsFactory,
    fragmentFactory: DaggerDelegateFragmentFactory,
    private val okHttpClient: OkHttpClient
) : BaseFragment(viewModelsFactory, fragmentFactory) {

    private lateinit var binding: FragmentArticleDetailBinding
    private val articleDetailsViewModel: ArticleDetailsViewModel by activityViewModels()
    private lateinit var articleUri: Uri
    private val chromeClient = FSVideoChromeClient()
    private var article: Article? = null
    private var customView: View? = null

    private var markReadJob: Job? = null

    private val cssOverride: String
        get() {
            val theme = requireActivity().theme
            val colors = WebViewColors.fromTheme(theme)
            val backgroundHexColor = colors.backgroundColor.toRgbaCall()
            val textColor = colors.textColor.toRgbaCall()
            val linkHexColor = colors.linkColor.toRgbaCall()
            var cssOverride = ("body { background : " + backgroundHexColor + "; "
                    + "color : " + textColor + "; }")
            cssOverride += " a:link {color: $linkHexColor;} a:visited { color: $linkHexColor;}"
            return cssOverride
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = articleDetailsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureWebView()

        articleDetailsViewModel.article.observe(this, Observer { this.article = it })
        articleDetailsViewModel.articleContent.observe(this, Observer { renderContent(it) })

        binding.root.setOnScrollChangeListener { v, _, _, _, _ ->
            markReadJob?.cancel()
            val root = v as NestedScrollView
            if (root.isAtEndOfArticle) {
                articleDetailsViewModel.setArticleUnread(false)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        articleUri = requireNotNull(arguments?.getParcelable(ARG_ARTICLE_URI))
        articleDetailsViewModel.init(ContentUris.parseId(articleUri))
    }

    private fun renderContent(articleContent: String) {
        val linkUri = article?.link?.toUri()
        val baseUrl = linkUri?.let { "${it.scheme}://${it.host}" }

        val content = """<html>
                |<head>
                |<meta content="text/html; charset=utf-8" http-equiv="content-type">
                |<meta name="viewport" content="width=device-width, user-scalable=no" />
                |<style type="text/css">body {
                |padding : 0px; margin : 0px; line-height : 130%;
                |}
                |img, video, iframe { max-width : 100%; width : auto; height : auto; }
                |table { width : 100%; }
                |$cssOverride
                |</style>
                |</head>
                |<body>
                |$articleContent
                |</body>
                |</html>""".trimMargin()

        /* TODO maybe reimplement this one day
       if (article.attachments != null && article.attachments.size() != 0) {
            String flatContent = articleContent.replaceAll("[\r\n]", "");
            boolean hasImages = flatContent.matches(".*?<img[^>+].*?");

            for (Attachment a : article.attachments) {
                if (a.content_type != null && a.content_url != null) {
                    try {
                        if (a.content_type.contains("image") &&
                                (!hasImages || article.always_display_attachments)) {

                            URL url = new URL(a.content_url.trim());
                            String strUrl = url.toString().trim();

                            content.append("<p><img src=\"" + strUrl.replace("\"", "\\\"") + "\"></p>");
                        }

                    } catch (MalformedURLException e) {
                        //
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }*/

        binding.articleContent.loadDataWithBaseURL(baseUrl, content, "text/html", "utf-8", null)
    }

    private fun configureWebView() {
        binding.articleContent.webViewClient = MyWebViewClient()

        with(binding.articleContent.settings) {
            setSupportZoom(false)
            javaScriptEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            mediaPlaybackRequiresUserGesture = false
        }

        binding.articleContent.webChromeClient = chromeClient
    }

    private data class WebViewColors(
        @field:ColorInt
        val backgroundColor: Int,
        @field:ColorInt
        internal val textColor: Int,
        @field:ColorInt
        internal val linkColor: Int
    ) {

        companion object {
            fun fromTheme(theme: Resources.Theme): WebViewColors {
                val typedValue = TypedValue()
                theme.resolveAttribute(R.attr.articleBackground, typedValue, true)
                val backgroundColor = typedValue.data
                theme.resolveAttribute(R.attr.articleTextColor, typedValue, true)
                val textColor = typedValue.data
                theme.resolveAttribute(R.attr.linkColor, typedValue, true)
                val linkColor = typedValue.data
                return WebViewColors(backgroundColor, textColor, linkColor)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.articleContent.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.articleContent.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.articleContent.saveState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.articleContent.restoreState(savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        chromeClient.onHideCustomView()
    }

    private inner class FSVideoChromeClient : WebChromeClient() {
        //protected View m_videoChildView;

        private var callback: WebChromeClient.CustomViewCallback? = null

        override fun onShowCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
            // if a view already exists then immediately terminate the new one
            if (customView != null) {
                callback.onCustomViewHidden()
                return
            }
            customView = view

            val activity = requireActivity() as AppCompatActivity
            activity.supportActionBar?.hide()

            val fab = activity.findViewById<View>(R.id.fab)
            fab?.visibility = View.GONE

            binding.articleFullscreenVideo.visibility = View.VISIBLE
            binding.articleFullscreenVideo.addView(view)

            this.callback = callback
        }

        override fun onHideCustomView() {
            super.onHideCustomView()
            if (customView == null) {
                return
            }

            val activity = requireActivity() as AppCompatActivity
            activity.supportActionBar?.show()
            val fab = activity.findViewById<View>(R.id.fab)
            fab?.visibility = View.VISIBLE

            binding.articleFullscreenVideo.visibility = View.GONE

            // Remove the custom view from its container.
            binding.articleFullscreenVideo.removeView(customView)
            callback?.onCustomViewHidden()

            customView = null

        }
    }

    private inner class MyWebViewClient internal constructor() : OkHttpWebViewClient(okHttpClient) {

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val intent = Intent(Intent.ACTION_VIEW, request.url)
            val context = view.context
            val activities =
                context.packageManager.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER)
                    .filterNot {
                        it.filter.isWebBrowserApp()
                    }
            if (activities.isNotEmpty()) {
                context.startActivity(intent)
            } else {
                articleDetailsViewModel.openUrlInBrowser(context, request.url)
            }
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            val root = binding.root as NestedScrollView
            // sometimes onPageFinished is called before updating the webview.
            // wait for next layout pass to calculate if we have reached end
            // and request a layout pass if not
            if (!root.isInLayout) {
                root.requestLayout()
            }
            root.doOnNextLayout {
                if (root.isAtEndOfArticle) {
                    markReadJob = viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                        delay(2000)
                        articleDetailsViewModel.setArticleUnread(false)
                    }
                }
            }
        }
    }

    private fun Int.toRgbaCall(): String {
        return "rgba(%d, %d, %d, %.2f)".format(Locale.ENGLISH,
            Color.red(this), Color.green(this), Color.blue(this), Color.alpha(this) / 255f)
    }

    private val NestedScrollView.scrollRange: Int
        get() = getChildAt(0)?.let { child ->
            val lp = child.layoutParams as ViewGroup.MarginLayoutParams
            val childSize = child.height + lp.topMargin + lp.bottomMargin
            val parentSpace = height - paddingTop - paddingBottom
            Math.max(0, childSize - parentSpace)
        } ?: 0

    private val NestedScrollView.isAtEndOfArticle: Boolean
        // consider it done when we have enough space to show bottom appbar
        get() = scrollY >= scrollRange - paddingBottom

    companion object {

        private const val ARG_ARTICLE_URI = "article_uri"

        @JvmStatic
        fun newInstance(fragmentFactory: FragmentFactory, articleUri: Uri): ArticleDetailFragment {
            return fragmentFactory.instantiate(ArticleDetailFragment::class.java.classLoader,
                ArticleDetailFragment::class.java.name).apply {
                arguments = bundleOf(ARG_ARTICLE_URI to articleUri)
            } as ArticleDetailFragment
        }
    }

    private fun IntentFilter.isWebBrowserApp(): Boolean {
        // from hidden method IntentFilter.handleAllWebDataURI()
        return hasCategory(Intent.CATEGORY_APP_BROWSER) || handlesWebUris() && countDataAuthorities() == 0
    }

    private fun IntentFilter.handlesWebUris(): Boolean {
        // adapted from hidden method IntentFilter.handleWebUris()
        val nbDataSchemes = countDataSchemes()
        // Require ACTION_VIEW, CATEGORY_BROWSEABLE, and at least one scheme
        if (!hasAction(Intent.ACTION_VIEW)
            || !hasCategory(Intent.CATEGORY_BROWSABLE)
            || nbDataSchemes == 0
        ) {
            return false
        }

        // Now allow only the schemes "http" and "https"
        for (i in 0 until nbDataSchemes) {
            val scheme = getDataScheme(i)
            val isWebScheme = "http" == scheme || "https" == scheme
            if (isWebScheme) {
                return true
            }
        }
        return false
    }
}
