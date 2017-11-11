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
    private String hostName;
    private String hostEmail;
    //no more arraylist player
    private int nowNumOfPlayer;


    private String passwd;
    private String gameId;

    public Game(String password, String gameid, Boolean isPrivate, String name, String typeofsport, String location, String date, String starttime, int numberofplayer, String hostName, String hEmail) {
        this.name = name;
        this.gameId = gameid;
        this.typeOfSport = typeofsport;
        this.location = location;
        this.date = date;
        this.startTime = starttime;
        this.numberOfPlayer = numberofplayer;
        this.hostEmail = hEmail;
        this.hostName = hostName;
        //this.players = new ArrayList<GamePlayer>(numberOfPlayer);
        //players.add(host);
        this.isPrivate = isPrivate;
        this.passwd = password;
        this.nowNumOfPlayer = 1;

    }

    public Game() {

    }

    public int getNowNumOfPlayer() {
        return this.nowNumOfPlayer;
    }

    public void setNowNumOfPlayer(int num) {
        this.nowNumOfPlayer = num;
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

    //public GamePlayer getHost() {
    //   return this.host;
    //}
    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hName) {
        this.hostName = hName;
    }

    public String getHostEmail() {
        return this.hostEmail;
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
