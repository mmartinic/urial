package org.mmartinic.urial.model;

import org.joda.time.LocalDate;

public class Episode {

    private String episodeName;
    private String showName;
    private Integer episodeNumber;
    private Integer seasonNumber;
    private LocalDate airDate;

    public Episode() {

    }

    public Episode(String showName, String episodeName, Integer seasonNumber, Integer episodeNumber, LocalDate airDate) {
        this.episodeName = episodeName;
        this.showName = showName;
        this.episodeNumber = episodeNumber;
        this.seasonNumber = seasonNumber;
        this.airDate = airDate;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public LocalDate getAirDate() {
        return airDate;
    }

    public void setAirDate(LocalDate airDate) {
        this.airDate = airDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((airDate == null) ? 0 : airDate.hashCode());
        result = prime * result + ((episodeName == null) ? 0 : episodeName.hashCode());
        result = prime * result + ((episodeNumber == null) ? 0 : episodeNumber.hashCode());
        result = prime * result + ((seasonNumber == null) ? 0 : seasonNumber.hashCode());
        result = prime * result + ((showName == null) ? 0 : showName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Episode other = (Episode) obj;
        if (airDate == null) {
            if (other.airDate != null)
                return false;
        }
        else if (!airDate.equals(other.airDate))
            return false;
        if (episodeName == null) {
            if (other.episodeName != null)
                return false;
        }
        else if (!episodeName.equals(other.episodeName))
            return false;
        if (episodeNumber == null) {
            if (other.episodeNumber != null)
                return false;
        }
        else if (!episodeNumber.equals(other.episodeNumber))
            return false;
        if (seasonNumber == null) {
            if (other.seasonNumber != null)
                return false;
        }
        else if (!seasonNumber.equals(other.seasonNumber))
            return false;
        if (showName == null) {
            if (other.showName != null)
                return false;
        }
        else if (!showName.equals(other.showName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Episode [episodeName=" + episodeName + ", showName=" + showName + ", episodeNumber=" + episodeNumber + ", seasonNumber="
                + seasonNumber + ", airDate=" + airDate + "]";
    }

}
