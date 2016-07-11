package com.lhycode.news;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class NewsDetailActivity extends Activity implements
		android.view.View.OnClickListener {

	private WebView webView;
	private ImageButton backButton;
	private ImageButton textsizeButton;
	private ImageButton shareButton;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_newsdetail);
		webView = (WebView) findViewById(R.id.webview);
		backButton = (ImageButton) findViewById(R.id.button_back);
		textsizeButton = (ImageButton) findViewById(R.id.button_textsize);
		shareButton = (ImageButton) findViewById(R.id.button_share);
		progressBar = (ProgressBar) findViewById(R.id.web_progress);
		shareButton.setOnClickListener(this);
		textsizeButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		String url = getIntent().getStringExtra("url");

		webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				progressBar.setVisibility(View.INVISIBLE);
			}
		});
		webView.loadUrl(url);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_back:
			finish();
			break;
		case R.id.button_share:

			break;
		case R.id.button_textsize:
			showChooseDialog();
			break;

		default:
			break;
		}
	}

	private int currentChosenItem;
	private WebSettings webSettings;
	private int lastChosenItem = 2;

	private void showChooseDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("选择字体大小");
		String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体" };
		builder.setSingleChoiceItems(items, lastChosenItem,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						currentChosenItem = which;
					}
				});
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (currentChosenItem) {
				case 0:
					webSettings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					webSettings.setTextSize(TextSize.LARGER);

					break;

				case 2:
					webSettings.setTextSize(TextSize.NORMAL);

					break;

				case 3:
					webSettings.setTextSize(TextSize.SMALLER);

					break;

				case 4:
					webSettings.setTextSize(TextSize.SMALLEST);

					break;

				default:
					break;

				}
				lastChosenItem = currentChosenItem;
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
}
