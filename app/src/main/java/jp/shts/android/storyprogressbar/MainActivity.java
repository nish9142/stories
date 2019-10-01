package jp.shts.android.storyprogressbar;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class MainActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {



    private StoriesProgressView storiesProgressView;
    private ImageView image;
    String TAG="Swipe";
    private VideoView video;

    private int counter = 0;
    private static final int[] resources = new int[]{R.drawable.image1, R.drawable.image2,
            R.drawable.image3, R.drawable.image4, R.drawable.image5,
            R.drawable.image6, R.drawable.image7, R.drawable.image8,
            R.drawable.image9, R.drawable.image10};

    private Media[] mediaarray = new  Media[]{
            new Media(resources[0],null),
            new Media(resources[1],null),
            new Media(resources[2],null),
            new Media(resources[3],null),
            new Media(resources[4],null),
            new Media(resources[5],null),
            new Media(resources[6],null),
            new Media(resources[7],null),
            new Media(resources[8],null),
            new Media(resources[9],null)
    };

    private static final int PROGRESS_COUNT = resources.length;



    private final long[] durations = new long[]{
            500L, 1000L, 1500L, 4000L, 5000L, 1000,
    };

    long pressTime = 0L;
    long limit = 500L;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
        storiesProgressView.setStoryDuration(3000L);
        // or
        // storiesProgressView.setStoriesCountWithDurations(durations);
        storiesProgressView.setStoriesListener(this);
//        storiesProgressView.startStories();
        counter = 2;
        storiesProgressView.startStories(counter);


        image = (ImageView) findViewById(R.id.image);
        image.setImageResource(resources[counter]);
         video = findViewById(R.id.video);
         image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

             }
         });



        LinearLayout linearLayout = findViewById(R.id.layout);
        linearLayout.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onSwipeTop: ");
            }
            @Override
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onSwipeRight: ");
            }
            @Override
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onSwipeLeft: ");
            }
            @Override
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onSwipeBottom: ");
            }

        });







        // bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
//        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
//        skip.setOnTouchListener(onTouchListener);
    }



    @Override
    public void onNext() {
        counter = counter +1;
        if(mediaarray[counter].Image!=0){
            video.setVisibility(View.INVISIBLE);
            image.setVisibility(View.VISIBLE);
            image.setImageResource(mediaarray[counter].Image);
        }
        else {
            image.setVisibility(View.INVISIBLE);
            video.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(mediaarray[counter].Video);
            video.setVideoURI(uri);
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    video.start();
                }
            });
        }



//        image.setImageResource(resources[++counter]);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        image.setImageResource(resources[--counter]);
    }

    @Override
    public void onComplete() {
    }

    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();
    }
}
