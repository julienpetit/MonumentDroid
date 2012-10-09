package com.example.droid;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SwipingActivity extends Activity {
    
    public OnTouchListener gestureListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView lv = (ListView)findViewById(R.id.mylistview);
        
        gestureListener = new View.OnTouchListener() {
            private int padding = 0;
            private int initialx = 0;
            private int currentx = 0;
            private  ViewHolder viewHolder;
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    padding = 0;
                    initialx = (int) event.getX();
                    currentx = (int) event.getX();
                    viewHolder = ((ViewHolder) v.getTag());
                }
                if ( event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    currentx = (int) event.getX();
                    padding = currentx - initialx;
                }
                
                if ( event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    padding = 0;
                    initialx = 0;
                    currentx = 0;
                }
                
                if(viewHolder != null)
                {
                    if(padding == 0)
                    {
                        v.setBackgroundColor(0xFF000000 );
                    }
                    if(padding > 75)
                    {
                        viewHolder.setRunning(true);
                    }
                    if(padding < -75)
                    {
                        viewHolder.setRunning(false);
                    }
                    v.setBackgroundColor(viewHolder.getColor());  
                    viewHolder.icon.setImageResource(viewHolder.getImageId());
                    v.setPadding(padding, 0,0, 0);
                }
                return true;
            }
        };
        
        ModelArrayAdapter adapter = new ModelArrayAdapter(this, getData(),gestureListener);
        lv.setAdapter(adapter);
    }
    
    public ArrayList<Model> getData()
    {
        ArrayList<Model> models = new ArrayList<Model>();
        for(int a=0;a<10;a++)
        {
            Model m = new Model(String.format("Item %d", a));
            models.add(m);
        }
        return models;
    }

    static class ViewHolder {
        protected TextView text;
        protected ImageView icon;
        protected CheckBox checkbox;
        protected int position;
        protected Model model;
        private int color;
        private int imageid;
        
        public ViewHolder()
        {
            position = 0;
            //imageid = R.drawable.bullet_go;
            color = 0xFFFFFFFF;
        }
        public int getColor() {
            return color;
        }
        public int getImageId() {
            return imageid;
        }
        public void setRunning(boolean running) {
            model.setRuning(running);
            if(running)
            {
                color = 0xFFffffb6;
                //imageid = R.drawable.bullet_green;
            }
            else
            {
                //imageid = R.drawable.bullet_go;
                color = 0xFFFFFFFF;
            }
        }
    }
}