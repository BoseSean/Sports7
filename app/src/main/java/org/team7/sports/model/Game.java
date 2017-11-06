package org.team7.sports.model;

/**
 * Created by sYUYx on 3/11/17.
 */

public class Game {
    private String name, typeOfSport, location;
    private String date;
    private String startTime;
    private int numberOfPlayer;
    private Boolean isPrivate;
    //private ArrayList<GamePlayer> players;
    private GamePlayer host;
    //no more arraylist player


    private String passwd;
    private String gameId;

    public Game(String password, Boolean isPrivate, String name, String typeofsport, String location, String date, String starttime, int numberofplayer, GamePlayer host) {
        this.name = name;
        this.typeOfSport = typeofsport;
        this.location = location;
        this.date = date;
        this.startTime = starttime;
        this.numberOfPlayer = numberofplayer;
        this.host = host;
        //this.players = new ArrayList<GamePlayer>(numberOfPlayer);
        //players.add(host);
        this.isPrivate = isPrivate;
        this.passwd = password;

    }

    public String getGameName() {
        return this.name;
    }

    public void setGameName(String gameName) {
        this.name = gameName;
    }

    public String getSportType() {
        return typeOfSport;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public Boolean getIsPrivate() {
        return this.isPrivate;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public String getLocation() {
        return this.location;
    }

    public int getNumberOfPlayer() {
        return this.numberOfPlayer;
    }

    public GamePlayer getHost() {
        return this.host;
    }

    public String getGameId() {
        return this.gameId;
    }

    //public void addNewPlayer(GamePlayer p){
    //this.players.add(p);
    //}
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setTypeOfSport(String sportType) {
        this.typeOfSport = sportType;
    }


}
