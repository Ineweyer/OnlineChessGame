package cn.ineweyer.onlinechessgame.activity;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 棋盘适配器
 * @author LQ
 *
 */
public class ImageAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Integer> imageList;
	private int height=40,width=40;

	public ImageAdapter(Context context, ArrayList<Integer> imageList) {
		this.context = context ;
		this.imageList = imageList;
	}
	
	@Override
	public int getCount() {
		return imageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageview;
		  if(convertView==null)
		  {
		   imageview=new ImageView(context);
		   imageview.setLayoutParams(new GridView.LayoutParams(width, height));
		 }
		  else
		  {
		   imageview=(ImageView) convertView;
		  }
		  imageview.setImageResource(imageList.get(position));
		  return imageview;
	}
	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

}
