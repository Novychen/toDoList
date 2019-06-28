package at.fhooe.mc.android;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * this class displays the toDoList and implements action that can manipulate the list
 *
 *
 * Repeat task algorithmus überlegen für Notifications
 * offline Daten
 * notificationsarten einstellen
 * wiederholgun der noti einsetellen
 * gemeinere Notis nach der Zeit
 * repeat task fixen
 *
 */
public class ActivityList extends ListActivity implements IFirebaseCallback{

    private final static String TAG = "at.fhooe.mc.toDoList :: ActivityList";
    static DataAdapter adapter;
    List <String> mSnarkyMotivation = new LinkedList<>();
    List <String> mFunnyMotivation = new LinkedList<>();
    List <String> mCuteMotivation = new LinkedList<>();
    List <String> mMotivation = new LinkedList<>();
    List <String> mBrutalMotivation = new LinkedList<>();
    int mGotIntoData;
    List<String> tRep;
    List<String> tDead;
    List<Integer> day;
    List<Integer> month;
    List<Integer> year;
    List<Integer> hour;
    List<Integer> min ;
    List<Integer> task;
    List<String> desRep;
    List<String> refRep;
    List<String> desDead;
    List<String> refDead;
    List<Boolean> mBrutalDead;
    List<Boolean> mSnarkyDead;
    List<Boolean> mFunnnyDead;
    List<Boolean> mCuteDead;
    List<Boolean> mNormalDead;
    List<Boolean> mNoNotiDead;

    List<String> mDescriptionRep;
    List<List<String>> mRepeatLabel;
    List<List<String>> mDeadLabel;
    List<Integer> mRepeats;
    List<String> mCircle;
    List<String> mCircleRepeat;
    List<Boolean> mBrutalRepeat;
    List<Boolean> mSnarkyRepeat;
    List<Boolean> mFunnnyRepeat;
    List<Boolean> mCuteRepeat;
    List<Boolean> mNormalRepeat;
    List<Boolean> mNoNotiRepeat;
    List<List<String>> label;
    List<Integer> repeats;
    List<String> circle;
    int[] rep;
    String[] cir;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       /* setContentView(R.layout.activity_list);*/

        Log.i(TAG, " :: onCreate I was here");
        Log.i(TAG, "OnCreate");

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child(Repository.getInstance().getUserId());
        Repository.getInstance().getData(ref2, this);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Repository.getInstance().getUserId()).child("CurrentTask");
        Repository.getInstance().getLongData(ref);

        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child(Repository.getInstance().getUserId());
        Repository.getInstance().getNotificationData(ref3,this);

        adapter = new DataAdapter(this);
        setListAdapter(adapter);


        mFunnyMotivation.add("People often say that motivation doesn’t last. Well, neither does bathing — that’s why we recommend it daily");
        mSnarkyMotivation.add("Anything's possible if you've got enough nerve.");
        mSnarkyMotivation.add("If you're waiting for a SIGN ... THIS IS IT");
        mSnarkyMotivation.add("It's never too late to get your shit together");

        mBrutalMotivation.add("Get your shit together!");
        mBrutalMotivation.add("Just do it!");
        mBrutalMotivation.add("You have time to think up excuses? Get to work!");
        mBrutalMotivation.add("You don't want to? so what?");
        mBrutalMotivation.add("Get your ass up");
        mBrutalMotivation.add("Don't find excuses and keep moving forward!");

        mMotivation.add("The expert in anything was once a beginner");
        mMotivation.add("Tough times don't last; Tough people do.");
        mMotivation.add("Worrying will never change the outcome");
        mMotivation.add("You can and you will");
        mMotivation.add("If not you, who? If not now. when?");
        mMotivation.add("you have to go through the worst, to get to the best");
        mMotivation.add("the struggle you're in today is developing the strength you need for tomorrow");
        mMotivation.add("To be the best, you must be able to handle the worst.");
        mMotivation.add("The best way out is always through.");

        mCuteMotivation.add("Believe you can and you’re halfway there.");
        mCuteMotivation.add("You can totally do it!");
        mCuteMotivation.add("Life is not a problem to be solved but a gift to be enjoyed. Make the best of this day!");
        mCuteMotivation.add("There's hope for tomorrow, cheer up :D !");
        mCuteMotivation.add("Just a little bit longer - you can do it!");
        mCuteMotivation.add("Don't worry - be happy");
        mCuteMotivation.add("you are so much stronger than you think");
        mCuteMotivation.add("After the rain comes the rainbow");
        mCuteMotivation.add("You are doing great! Keep pushing!");

        final ActionBar ab = getActionBar();
        ab.setHomeButtonEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int _position, long id) {
        Log.i(TAG,"repeatsSIZE---------------------->"+rep.length);
        Log.i(TAG,"CircleSIZE---------------------->"+circle.size());
        ListAdapter list = getListAdapter();
        ListData       item = (ListData) list.getItem(_position);
        Log.i(TAG, "Position ------> " + _position );
        Log.i(TAG, "clicked item " + item);
        Intent i = new Intent(this, TaskDue.class);
        Log.i(TAG,"TASK SIZE after SetALL-------->"+task.size());
        i.putExtra("task", task.get(_position));
        int deadp =0;
        int repp =0;
        for (int p  = 0; p!=_position;p++){
            if(task.get(p)==0) {
                deadp++;
            }else {
                repp++;
            }
        }
        Log.i(TAG, "dead " + deadp);
        Log.i(TAG, "rep " + repp);
        if(task.get(_position)==0){
            i.putExtra("title",tDead.get(deadp));
            i.putExtra("ref",refDead.get(deadp));
            String h = String.valueOf(hour.get(deadp));
            String m = String.valueOf(min.get(deadp));
            //   s.append(day.get(deadp)+"."+month.get(deadp)+"."+year.get(deadp));
            i.putExtra("date",day.get(deadp) + "." + month.get(deadp) + "." + year.get(deadp));
            if(hour.get(deadp) < 10){
                h = "0" + h;
            }if(min.get(deadp) < 10){
                m = "0" + m;
            }
            i.putExtra("time",h + ":" + m);
            i.putExtra("des",desDead.get(deadp));
            Log.i(TAG,"title,ref,time,des---------->"+ tDead.get(deadp)+" ,"+ refDead.get(deadp)+" ,"+ hour + ":" + min +" ,"+ desDead.get(deadp));

            i.putExtra("brutal", mBrutalDead.get(deadp));
            i.putExtra("snarky", mSnarkyDead.get(deadp));
            i.putExtra("funny", mFunnnyDead.get(deadp));
            i.putExtra("cute", mCuteDead.get(deadp));
            i.putExtra("normal", mNormalDead.get(deadp));
            i.putExtra("noNoti", mNoNotiDead.get(deadp));

            i.putExtra("label1", mDeadLabel.get(deadp).get(0));
            i.putExtra("label2", mDeadLabel.get(deadp).get(1));
            i.putExtra("label3", mDeadLabel.get(deadp).get(2));


        }else {

            i.putExtra("title", tRep.get(repp));
            i.putExtra("ref",refRep.get(repp));
            StringBuilder s= new StringBuilder();
            //s.append(repeats.get(repp)+" times per "+circle.get(repp));
            Log.i(TAG,repeats.size()+" times per "+circle.size());
            s.append("hello");
            i.putExtra("date",rep[repp]+" times per "+cir[repp]);
            i.putExtra("des",desRep.get(repp));
            Log.i(TAG,"title,ref,time,des---------->"+ tRep.get(repp)+" ,"+refRep.get(repp)+" ,"+s+" ,"+desRep.get(repp));

            i.putExtra("brutal", mBrutalRepeat.get(repp));
            i.putExtra("snarky", mSnarkyRepeat.get(repp));
            i.putExtra("funny", mFunnnyRepeat.get(repp));
            i.putExtra("cute", mCuteRepeat.get(repp));
            i.putExtra("normal", mNormalRepeat.get(repp));
            i.putExtra("noNoti", mNoNotiRepeat.get(repp));

            i.putExtra("label1", mRepeatLabel.get(repp).get(0));
            i.putExtra("label2", mRepeatLabel.get(repp).get(1));
            i.putExtra("label3", mRepeatLabel.get(repp).get(2));
        }
        startActivity(i);
     }


    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.menu_arlog, _menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem _item) {
        switch (_item.getItemId()){
            case R.id.menu_arlog_logout:{
                Log.i(TAG, "::onClick logOut Button was pressed");
                logOut();
                finish();}break;
            case R.id.menu_arlog_add_dead: {
                Log.i(TAG, "::onClick add Button was pressed");
                Intent i = new Intent(this, ActivityDeadlineTask.class);
                startActivity(i);
            }break;

            case R.id.menu_arlog_add_rep: {
                Log.i(TAG, "::onClick add Button was pressed");
                Intent i = new Intent(this, ActivityRepeatTask.class);
                startActivity(i);
            }break;

            default:
                Log.e(TAG, "::onClick unexpected ID encountered");
        }return true;
    }


    /**
     * logs the User out
     */
    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void setNotificationDeadlineData(List <Integer>_task, List<Integer> _d, List<Integer> _m, List<Integer> _y, List<Integer> _h, List<Integer> _min, List<String> _t, List<Boolean> _norm, List<Boolean> _funny, List<Boolean> _snarky, List<Boolean> _cute, List<Boolean> _brutal,List<Boolean> _notification, List<Integer> _count, List<String> _ref,List<String> _des, List<List<String>> _label) {
        {
        Log.i(TAG, "setNotificationDeadlineData :: I was here");
            List <String> motivation = new LinkedList<>();

            for (int i = 0; i < _d.size(); i++) {
                if(_notification.get(i) && _task.get(i) == 0) {
                    Calendar calendar = Calendar.getInstance();
                    if (_d.get(i) == null) {
                        _d.set(i, 0);
                    }
                    if (_m.get(i) == null) {
                        _m.set(i, 0);
                    }
                    if (_y.get(i) == null) {
                        _y.set(i, 0);
                    }
                    if (_h.get(i) == null) {
                        _h.set(i, 0);
                    }
                    if (_min.get(i) == null) {
                        _min.set(i, 0);
                    }
                    int year = _y.get(i);
                    int month = _m.get(i) - 1;
                    int day = _d.get(i);
                    int hour = _h.get(i);
                    int minute = _min.get(i);

                    calendar.set(year, month, day, hour, minute);

                    Random r = new Random();
                    Intent intent = new Intent(this, NotificationAlarm.class);
                    intent.putExtra("NotificationTitle", "The time for your task: " + _t.get(i) + " is up!");
                    intent.putExtra("text", "You better have your things done");
                    intent.putExtra("task", 0);
                    intent.putExtra("title", _t.get(i));


                    int m = _min.get(i);
                    int h = _h.get(i);
                    String stringMin = String.valueOf(m);
                    String stringHour = String.valueOf(h);
                    if(m < 10){
                        stringMin = "0" + m;
                    }
                    if(h < 10){
                        stringHour = "0" + h;
                    }
                    intent.putExtra("time",stringHour + ":" + stringMin);
                    intent.putExtra("date", _d.get(i) + "." + _m.get(i) + "." + _y.get(i));
                    intent.putExtra("des", _des.get(i));
                    intent.putExtra("label1", _label.get(i).get(0));
                    intent.putExtra("label2", _label.get(i).get(1));
                    intent.putExtra("label3", _label.get(i).get(2));
                    intent.putExtra("brutal", _brutal.get(i));
                    intent.putExtra("snarky", _snarky.get(i));
                    intent.putExtra("funny", _funny.get(i));
                    intent.putExtra("cute", _cute.get(i));
                    intent.putExtra("normal", _norm.get(i));
                    intent.putExtra("noNoti", _notification.get(i));
                    //Repository.getInstance().saveData(_count.get(i) +1,_ref.get(i));
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    PendingIntent pi = PendingIntent.getBroadcast(this, r.nextInt(1000), intent, 0);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

                    if (_norm.get(i)) {
                        motivation.addAll(mMotivation);
                    } else if (_brutal.get(i)) {
                        motivation.addAll(mBrutalMotivation);
                    } else if (_cute.get(i)) {
                        motivation.addAll(mCuteMotivation);
                    } else if (_funny.get(i)) {
                        motivation.addAll(mFunnyMotivation);
                    } else if (_snarky.get(i)) {
                        motivation.addAll(mSnarkyMotivation);
                    }
                }
            }

            Random r = new Random();
            Calendar calendar = Calendar.getInstance();
            int size = motivation.size();
            if(size != 0) {
                if (mGotIntoData != 1) {
                    for (int i = 0; i < 4; i++) {
                        calendar.setTime(new Date());
                        calendar.add(Calendar.HOUR_OF_DAY, r.nextInt(12));
                        calendar.add(Calendar.MINUTE, r.nextInt(60));
                        Intent intent = new Intent(this, NotificationAlarm.class);
                        intent.putExtra("title", "Motivation coming through");
                        intent.putExtra("text", motivation.get(r.nextInt(size)));
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        PendingIntent pi = PendingIntent.getBroadcast(this, i, intent, 0);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                    }
                }
                mGotIntoData++;
            }
        }
    }

    @Override
    public void setNotificationRepeatData(List <Integer>_task,List<Integer> _r, List<String> _c, List<String> _t, List<Boolean> _norm, List<Boolean> _funny, List<Boolean> _snarky, List<Boolean> _cute, List<Boolean> _brutal ,List<Boolean> _notification, List<String> _des, List<List<String>> _label) {

        NotificationAlarm.mGroupEnabled = true;

        try {
            if (_t.size() != 0) {
                _c.remove(_c.size() - 1);
                _r.remove(_r.size() - 1);
            }

            for (int i = 0; i < _r.size(); i++) {

                if(_notification.get(i) && _task.get(i) == 1) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    if (_r.get(i) == null) {
                        _r.set(i, 0);
                    }
                    if (_c.get(i).equals("year")) {

                    }
                    if (_c.get(i).equals("month")) {

                    }
                    if (_c.get(i).equals("week")) {
                        calendar.add(Calendar.DAY_OF_MONTH, 2);
                    }

                    int year = 0;
                    int month = 0;
                    int day = 0;
                    int hour = 0;
                    int minute = 0;

                    calendar.set(year, month, day, hour, minute);

                    Random rand = new Random();
                    Intent intent = new Intent(this, NotificationAlarm.class);
                    intent.putExtra("NotificationTitle", "The time for your task: " + _t.get(i) + " is up!");
                    intent.putExtra("text", "You better have your things done");
                    intent.putExtra("task", 1);
                    intent.putExtra("title", _t.get(i));
                    intent.putExtra("date", _r.get(i) + " times per " + _c.get(i));
                    intent.putExtra("des", _des.get(i));
                    intent.putExtra("label1", _label.get(i).get(0));
                    intent.putExtra("label2", _label.get(i).get(1));
                    intent.putExtra("label3", _label.get(i).get(2));
                    intent.putExtra("brutal", _brutal.get(i));
                    intent.putExtra("snarky", _snarky.get(i));
                    intent.putExtra("funny", _funny.get(i));
                    intent.putExtra("cute", _cute.get(i));
                    intent.putExtra("normal", _norm.get(i));
                    intent.putExtra("noNoti", _notification.get(i));
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    PendingIntent pi = PendingIntent.getBroadcast(this, rand.nextInt(1000), intent, 0);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                }
            }
        }catch (ClassCastException e) {
            Log.e(TAG, "setNotificationData: " + e.getMessage());
        }catch(NullPointerException e){
            Log.e(TAG, "setNotificationData: " + e.getMessage());
        }catch(IndexOutOfBoundsException e){
            Log.e(TAG, "setNotificationData: " + e.getMessage());
        }
    }

    @Override
    public void setTitle(List<String> _repeatT, List<String> _deadT, List<Integer> _task, List<Integer> _d, List<Integer> _m, List<Integer> _y, List<Integer> _repeats, List<String> _repeatCircle) {

            Log.i(TAG, "setTitle :: LIST size --> " + _task.size());
            try {
                adapter.clear();

                int deadi= 0;
                int repi = 0;
                for (int i = 0; i < _task.size(); i++) {
                    if(_task.get(i)==0){
                        adapter.add(new ListData(_deadT.get(deadi),_d.get(deadi),_m.get(deadi),_y.get(deadi)));
                        Log.i(TAG, "setTitle :: " + adapter.getCount() + " deadLine tile,ref,des"+i+" --> " + _deadT.get(deadi));
                        deadi++;
                    }
                    if(_task.get(i)==1){
                        adapter.add(new ListData(_repeatT.get(repi),_repeats.get(repi),_repeatCircle.get(repi)));
                        Log.i(TAG, "setTitle :: " + adapter.getCount() + " deadLine tile,ref,des"+i+" --> " + _repeatT.get(repi));
                        repi++;
                    }
                }
                adapter.notifyDataSetChanged();

            } catch (ClassCastException e) {
                Log.e(TAG, "setTitle: " + e.getMessage());
            } catch (NullPointerException e) {
                Log.e(TAG, "setTitle: " + e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                Log.e(TAG, "setTitle: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "setTitle: " + e.getMessage());
            }
    }

    @Override
    public void setAll(List<String> _repeatT, List<String> _deadT, List<Integer> _d, List<Integer> _mo, List<Integer> _y, List<Integer> _h, List<Integer> _mi, List<Integer> _t, List<List<String>> _labelRep, List<List<String>> _labelDead,List<String> _desrep,List<String> _desdead,List<String> _refrep,List<String> _refdead,List<Integer> _repeats, List<String> _repeatCircle, List<Boolean> _normRep, List<Boolean> _funnyRep, List<Boolean> _snarkyRep, List<Boolean> _cuteRep, List<Boolean> _brutalRep,List<Boolean> _notificationRep, List<Boolean> _normDead, List<Boolean> _funnyDead, List<Boolean> _snarkyDead, List<Boolean> _cuteDead, List<Boolean> _brutalDead,List<Boolean> _notificationDead) {
        repeats = _repeats;
        circle = _repeatCircle;
        tDead = _deadT;
        tRep =_repeatT;
        task = _t;
        day =_d;
        month = _mo;
        year=_y;
        hour =_h;
        min = _mi;
        desDead=_desdead;
        desRep=_desrep;
        refDead=_refdead;
        refRep=_refrep;
        mRepeatLabel =_labelRep;
        mDeadLabel = _labelDead;
        mBrutalDead = _brutalDead;
        mSnarkyDead = _snarkyDead;
        mFunnnyDead = _funnyDead;
        mCuteDead = _cuteDead;
        mNormalDead = _normDead;
        mNoNotiDead = _notificationDead;
        mBrutalRepeat =_brutalRep;
        mSnarkyRepeat = _snarkyRep;
        mFunnnyRepeat = _funnyRep;
        mCuteRepeat = _cuteRep;
        mNormalRepeat = _normRep;
        mNoNotiRepeat = _notificationRep;
        rep = new int[repeats.size()];
        cir = new String[circle.size()];
        for(int i = 0; i<circle.size();i++){
        rep[i]= repeats.get(i);
        cir[i] = circle.get(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
