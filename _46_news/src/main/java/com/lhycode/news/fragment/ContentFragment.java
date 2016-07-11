package com.lhycode.news.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lhycode.news.R;

import Base.BasePager;
import Base.HealthPager;
import Base.HotTalkPager;
import Base.NewsCenterPager;
import Base.TravelPager;
import Base.FunnyPager;
import android.R.integer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContentFragment extends BaseFragment {

	private RadioGroup radioGroup;
	private ViewPager viewPager;
	private List<BasePager> basePagerList;

	@Override
	public View initView() {
		// TODO Auto-generated method stub
		View view = View.inflate(activity, R.layout.contnet_fragment, null);
		radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
		viewPager = (ViewPager) view.findViewById(R.id.vp_content);
		return view;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		radioGroup.check(R.id.radiobutton1);
		basePagerList = new ArrayList<BasePager>();

		basePagerList.add(new NewsCenterPager(activity));
		basePagerList.add(new HotTalkPager(activity));
		basePagerList.add(new FunnyPager(activity));
		basePagerList.add(new HealthPager(activity));
		basePagerList.add(new TravelPager(activity));
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radiobutton1:
					viewPager.setCurrentItem(0);
					break;
				case R.id.radiobutton2:
					viewPager.setCurrentItem(1);
					break;
				case R.id.radiobutton3:
					viewPager.setCurrentItem(2);
					break;
				case R.id.radiobutton4:
					viewPager.setCurrentItem(3);
					break;
				case R.id.radiobutton5:
					viewPager.setCurrentItem(4);
					break;

				}
			}
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				basePagerList.get(position).initData();
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		viewPager.setAdapter(new ContentAdapter());
		basePagerList.get(0).initData();
	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return basePagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			BasePager pager = basePagerList.get(position);
			container.addView(pager.rootView);
//			pager.initData();viewPager��Ԥ��������ҳ��
			return pager.rootView;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method tub
			container.removeView((View) object);
		}
	}
	public NewsCenterPager getNewsCenterPager() {
		return (NewsCenterPager) basePagerList.get(0);
	}
}
