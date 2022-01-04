package be;

public class Song {
    private double lengthOfSong;
    private String artist;
    private String title;
    private String category;
    private String filePath;
    private final int ID;
    private final String timeToString;
    private int locationInList;
    private int IDinsideList = 0;

    public Song(String title, String artist, String category, int lengthOfSong, String filePath, int ID) {
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.lengthOfSong = lengthOfSong;
        this.filePath = filePath;
        this.ID = ID;
        timeToString = getTimeToString();
    }

    public int getID() {
        return ID;
    }

    public int getTimetoInt() {
        int timeInt = (int) lengthOfSong;
        return timeInt;
    }

    public double getLengthOfSong() {
        return lengthOfSong;
    }

    public void setLengthOfSong(double lengthOfSong) {
        this.lengthOfSong = lengthOfSong;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getTimeToString() {

        String hoursString;
        String minutesString;
        String secondsString;

        String timeToString;

        int hours = (int) (lengthOfSong / 60 / 60);

        if (hours < 10) {
            hoursString = "0" + hours;
        } else {
            hoursString = "" + hours;
        }

        int minutes = (int) ((lengthOfSong - (hours * 60 * 60)) / 60);
        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }
        int seconds = (int) (lengthOfSong % 60);
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }


        timeToString = hoursString + ":" + minutesString + ":" + secondsString;

        return timeToString;
    }

    @Override
    public String toString() {
        return "Song{" +
                "lengthOfSong=" + lengthOfSong +
                ", artist='" + artist +
                ", title='" + title +
                ", category='" + category +
                ", filePath='" + filePath +
                ", ID=" + ID +
                '}';
    }

    public int getLocationInList() {
        return locationInList;
    }

    public void setLocationInList(int locationInList) {
        this.locationInList = locationInList;
    }

    public int getIDinsideList() {
        return IDinsideList;
    }

    public void setIDinsideList(int IDinsideList) {
        this.IDinsideList = IDinsideList;
    }
}
