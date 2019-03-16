package iu9.bmstu.ru.lab10.data;

import java.util.Date;
import java.util.Objects;

public class Meeting {

    private String name;
    private String place;
    private Date meetingTime;

    public Meeting() {
    }

    public Meeting(String name, String place, Date meetingTime) {
        this.name = name;
        this.place = place;
        this.meetingTime = meetingTime;
    }

    public String getName() {
        return name ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(Date meetingTime) {
        this.meetingTime = meetingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(name, meeting.name) &&
                Objects.equals(place, meeting.place) &&
                Objects.equals(meetingTime, meeting.meetingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, place, meetingTime);
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", meetingTime=" + meetingTime +
                '}';
    }
}
