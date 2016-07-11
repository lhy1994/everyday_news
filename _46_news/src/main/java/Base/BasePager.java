package Base;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lhycode.news.MainActivity;
import com.lhycode.news.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BasePager {
	public Activity activity;
	public View rootView;
	public TextView title;
	public FrameLayout content;
	public ImageButton buttonMenu;
	public ImageButton gridButton;

	public BasePager(Activity activity) {
		this.activity = activity;
		initViews();
	}
	public void initViews() {
		rootView = View.inflate(activity, R.layout.base_pager, null);
		title=(TextView) rootView.findViewById(R.id.title);
		content=(FrameLayout) rootView.findViewById(R.id.fl_content);
		buttonMenu=(ImageButton) rootView.findViewById(R.id.button_menu);
		gridButton = (ImageButton) rootView.findViewById(R.id.button_photo);
		buttonMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleSlidingMenu();
			}
		});
	}
	protected void toggleSlidingMenu( ) {
		MainActivity mainActivity = (MainActivity) activity;
		SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
		slidingMenu.toggle();
	}
	public void initData() {
		
	}
	public void setSlidingMenuEnable(boolean enable) {
		MainActivity mainActivity=(MainActivity) activity;
		SlidingMenu slidingMenu=mainActivity.getSlidingMenu();
		if(enable)
		{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
}
