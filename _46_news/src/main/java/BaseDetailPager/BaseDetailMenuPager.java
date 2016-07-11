package BaseDetailPager;

import android.app.Activity;
import android.view.View;

public abstract class BaseDetailMenuPager {
	public Activity activity;
	public View rootView;
	public BaseDetailMenuPager(Activity activity){
		this.activity=activity;
		rootView=initViews();
	}
	public abstract View initViews();
	public void initData() {
		
	}

}
