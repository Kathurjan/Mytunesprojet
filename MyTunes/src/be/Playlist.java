package be;



import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String title;
    private List<Song> songlist = new ArrayList<>();
    private final int ID;


    public Playlist(String title, int ID){
        this.title = title;
        this.ID = ID;
    }

    public int getSongCount(){
        return songlist.size();
    }


    public String getTime(){
        int totalCount = 0;
        String totalCountToString;
        for (Song x: songlist) {
            totalCount += x.getTimetoInt();
        }

        String hoursString;
        String minutesString;
        String secondsString;

        int hours = (totalCount / 60 / 60);

        if (hours < 10) {
            hoursString = "0" + hours;
        } else {
            hoursString = "" + hours;
        }

        int minutes = ((totalCount - (hours * 60 * 60)) / 60);

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }

        int seconds = (totalCount % 60);

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }


        totalCountToString = hoursString + ":" + minutesString + ":" + secondsString;

        return totalCountToString;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setSonglist(List<Song> songlist){
        this.songlist = songlist;
    }

    public List<Song> getSonglist(){
        return songlist;
    }

    public int getID() {
        return ID;
    }
}
