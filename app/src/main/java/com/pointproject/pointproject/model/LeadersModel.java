package com.pointproject.pointproject.model;

/**
 * Created by xdewnik on 30.12.2017.
 */

public class LeadersModel {
    private String nick;
    private String wins;
    private String image;

    public LeadersModel(String nick, String wins, String image){
        this.image=image;
        this.nick = nick;
        this.wins = wins;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
