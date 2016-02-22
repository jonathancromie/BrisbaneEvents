package com.jonathancromie.brisbaneevents;

/**
 * Created by jonathancromie on 8/01/15.
 */
public class Scores {
    private String _playerName;
    private int _score;

    public Scores() {
    }

    public Scores(String playerName, int score) {
        this._playerName = playerName;
        this._score = score;
    }

    public void setPlayerName(String playerName) {
        this._playerName = playerName;
    }

    public void setScore(int score) {
        this._score = score;
    }

    public String getPlayerName() {
        return _playerName;
    }

    public int getScore() {
        return _score;
    }
}
