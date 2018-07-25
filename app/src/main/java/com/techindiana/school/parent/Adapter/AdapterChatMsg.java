package com.techindiana.school.parent.Adapter;
/*
Created By: DGP 22/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.techindiana.school.parent.Module.TeacherMessagesInfo;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by TechIndiana on 26-07-2017.
 */

/**
 * Created by sab99r
 */
public class AdapterChatMsg extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    private static MediaPlayer mediaPlayer;
    private static int mediaFileLengthInMilliseconds;
    static Context context;
    List<TeacherMessagesInfo> messagesInfos;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    private static final Handler handler = new Handler();
    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */


    public AdapterChatMsg(Context context, List<TeacherMessagesInfo> messagesInfos) {
        this.context = context;
        this.messagesInfos = messagesInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new PlaceHolder(inflater.inflate(R.layout.item_chat_discussion,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.rw_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            ((PlaceHolder)holder).bindData(messagesInfos.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {

            if (messagesInfos.get(position).type1==null) {
                return TYPE_MOVIE;
            }else{
                return TYPE_LOAD;
            }

    }

    @Override
    public int getItemCount() {
        return messagesInfos.size();
    }

    /* VIEW HOLDERS */

     class PlaceHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvDes, tvDate;
        ImageView  iv_voice;
        LinearLayout llyParent,llyParent1;
        public PlaceHolder(View itemView) {
            super(itemView);
           // tvName = (TextView) itemView.findViewById(R.id.rwchatDTvUName);
            tvDes = (TextView) itemView.findViewById(R.id.rwchatDTvName);
            tvDate = (TextView) itemView.findViewById(R.id.rwchatDTvDate);
          //  img = (ImageView) itemView.findViewById(R.id.rwchatDImgU);
            iv_voice = (ImageView) itemView.findViewById(R.id.iv_voice);

            llyParent = (LinearLayout) itemView.findViewById(R.id.llRowBagpack);
            llyParent1 = (LinearLayout) itemView.findViewById(R.id.llRowBagpack1);

           // llyParent.setLayoutParams(parms);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "sansR.ttf");
          //  tvName.setTypeface(type);
            tvDes.setTypeface(type);
            tvDate.setTypeface(type); }

        void bindData(final TeacherMessagesInfo messagesInfos){
           //  tvName.setText(discussions.get(i).getUserName());
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "d MMM yyyy hh:mm a";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date sdate = null;
            Date edate = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sdate = inputFormat.parse(messagesInfos.getMsgTime());
                tvDate.setText(outputFormat.format(sdate));

            } catch (ParseException e) {
                e.printStackTrace();
            }
           //  tvDate.setText(messagesInfos.getMsgTime());
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llyParent.setLayoutParams(parms);
             if(messagesInfos.getSenderType().equals("teacher")){
                 tvDes.setTextColor(context.getResources().getColor(R.color.textClrPri));
                 tvDate.setTextColor(context.getResources().getColor(R.color.textClrSec));
                 tvDes.setText(messagesInfos.getMsgContent());
                 llyParent1.setGravity(Gravity.LEFT);
                            llyParent.setBackgroundResource(R.drawable.chat_backgroung_teacher);
             }else{
                 String fileName = messagesInfos.getMsgContent().substring(messagesInfos.getMsgContent().lastIndexOf('/') + 1);
                 tvDes.setText(fileName);
                 tvDes.setTextColor(context.getResources().getColor(R.color.white));
                 tvDate.setTextColor(context.getResources().getColor(R.color.bg1));
                 llyParent1.setGravity(Gravity.RIGHT);

                 llyParent.setBackgroundResource(R.drawable.chat_backgroung_parent);
             }
            try {

                if (messagesInfos.getSenderType() != null) {
                    if (messagesInfos.getType().equals("text")) {

                        iv_voice.setVisibility(View.INVISIBLE);
                    } else {

                        iv_voice.setVisibility(View.VISIBLE);
                    }
                } else {

                    iv_voice.setVisibility(View.INVISIBLE);
                }

                iv_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogPlay(Constant.webImgPath + messagesInfos.getMsgContent());
                    }
                });

            } catch (Exception e) {
                e.getMessage();
            }
        }

 }
    //TODO play audio file...

    void DialogPlay(String path) {
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.font_regular));
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_audio_play);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);

            final SeekBar seekBarProgress = (SeekBar) dialog.findViewById(R.id.dSeekBarTestPlay);
            seekBarProgress.setMax(99); // It means 100% .0-99
            seekBarProgress.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (v.getId() == R.id.dSeekBarTestPlay) {
                        /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
                        if (mediaPlayer.isPlaying()) {
                            SeekBar sb = (SeekBar) v;
                            int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                            mediaPlayer.seekTo(playPositionInMillisecconds);
                        }
                    }
                    return false;
                }
            });
            final TextView btnStop = (TextView) dialog.findViewById(R.id.dExitNo);
            TextView btnOk = (TextView) dialog.findViewById(R.id.dExitYes);
            btnStop.setTypeface(tf);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    seekBarProgress.setSecondaryProgress(percent);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    btnStop.setText("Play");
                }
            });



            /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
            try {
                mediaPlayer.setDataSource(path); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                //  mediaPlayer.setDataSource("https://www.ssaurel.com/tmp/mymusic.mp3"); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

            if (!mediaPlayer.isPlaying()) {

                mediaPlayer.start();
                btnStop.setText("Stop");
            } else {
                mediaPlayer.pause();
                btnStop.setText("Play");
            }

            primarySeekBarProgressUpdater(seekBarProgress);

            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        primarySeekBarProgressUpdater(seekBarProgress);
                        btnStop.setText("Stop");
                    } else {
                        mediaPlayer.pause();
                        btnStop.setText("Play");
                    }

                    //  dialog.dismiss();
                }
            });
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.pause();
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    void primarySeekBarProgressUpdater(final SeekBar seekBarProgress) {
        seekBarProgress.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater(seekBarProgress);
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }


    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
