package com.app.org;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

public class LogicHandler {
    private GameActivity gameActivity;
    private Logic logic;
    private TextView resultView, infoView, inputText;
    private ImageView imageView;
    private SharedPreferences preferences;

    public LogicHandler(GameActivity gameActivity) {
        this.gameActivity = gameActivity;

        preferences = PreferenceManager.getDefaultSharedPreferences(gameActivity);

        logic = new Logic();

        infoView = (TextView) gameActivity.findViewById(R.id.infoView);
        inputText = (TextView) gameActivity.findViewById(R.id.inputText);
        resultView = (TextView) gameActivity.findViewById(R.id.resultView);
        imageView = (ImageView) gameActivity.findViewById(R.id.imageView);

        setWordList();
    }

    private void setWordList() {
        if (preferences.getBoolean("online", false)) {
            new AsyncTask<Object[], Void, Object>() {

                @Override
                protected Void doInBackground(Object[]... objects) {
                    try {
                        logic.hentOrdFraDr();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object object) {
                    resultView.setText(logic.getSynligtOrd());
                    super.onPostExecute(object);
                }
            }.execute();
        }
        else {
            logic.sætOrd();
            resultView.setText(logic.getSynligtOrd());
        }
    }

    @SuppressLint("SetTextI18n")
    public void restart() {
        logic.nulstil();
        setWordList();
        resultView.setText(logic.getSynligtOrd());
        infoView.setText("Gæt et bogstav..");
        imageView.setImageResource(R.drawable.hang);
        inputText.setEnabled(true);
    }

    @SuppressLint("SetTextI18n")
    public void checkAnswer() {

        if (!logic.erSpilletSlut()) {

            CharSequence answer = inputText.getText();
            inputText.setText("");

            if (logic.getBrugteBogstaver().contains(answer.toString()))
                infoView.setText("Allerede brugt!");

            else {
                logic.gætBogstav(answer.toString());

                if (!logic.erSidsteBogstavKorrekt()) {

                    if (preferences.getBoolean("sound", false) && !logic.erSpilletSlut()) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(gameActivity, R.raw.wrong);
                        mediaPlayer.start();
                    }
                    infoView.setText("Forkert!");

                    switch (logic.getAntalForkerteBogstaver()) {
                        case 0:  imageView.setImageResource(R.drawable.hang);
                            break;
                        case 1:  imageView.setImageResource(R.drawable.hang0);
                            break;
                        case 2:  imageView.setImageResource(R.drawable.hang1);
                            break;
                        case 3:  imageView.setImageResource(R.drawable.hang2);
                            break;
                        case 4:  imageView.setImageResource(R.drawable.hang3);
                            break;
                        case 5:  imageView.setImageResource(R.drawable.hang4);
                            break;
                        case 6:  imageView.setImageResource(R.drawable.hang5);
                            break;
                        case 7:  imageView.setImageResource(R.drawable.hang6);
                            break;
                    }
                }
                else {
                    if (preferences.getBoolean("sound", false) && !logic.erSpilletSlut()) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(gameActivity, R.raw.correct);
                        mediaPlayer.start();
                    }
                    infoView.setText("Korrekt!");
                }
                resultView.setText(logic.getSynligtOrd());
            }

            if (logic.erSpilletTabt()) {
                if (preferences.getBoolean("sound", false)) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(gameActivity, R.raw.bad);
                    mediaPlayer.start();
                }
                infoView.setText("Spillet er slut og du har tabt");
                resultView.setText(logic.getOrdet());
                inputText.setEnabled(false);
            }

            else if (logic.erSpilletVundet()) {
                if (preferences.getBoolean("sound", false)) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(gameActivity, R.raw.good);
                    mediaPlayer.start();
                }
                infoView.setText("Spillet er slut og du har vundet");
                resultView.setText(logic.getOrdet());
                inputText.setEnabled(false);
            }
        }
    }
}
