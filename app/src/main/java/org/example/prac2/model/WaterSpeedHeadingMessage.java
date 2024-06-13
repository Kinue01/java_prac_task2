package org.example.prac2.model;

import java.sql.Timestamp;

/**
 * Based on NMEA Water speed and heading (VHW)
 */
public class WaterSpeedHeadingMessage {

    private Timestamp msgRecTime;
    private Double course;
    private Double speed;


    public WaterSpeedHeadingMessage(Timestamp msgRecTime, Double course, Double speed) {
        this.msgRecTime = msgRecTime;
        this.course = course;
        this.speed = speed;
    }


    public Timestamp getMsgRecTime() {
        return msgRecTime;
    }

    public void setMsgRecTime(Timestamp msgRecTime) {
        this.msgRecTime = msgRecTime;
    }

    public Double getCourse() {
        return course;
    }

    public void setCourse(Double course) {
        this.course = course;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "WaterSpeedHeadingMessage{" +
                "msgRecTime=" + getMsgRecTime() +
                ", course=" + course +
                ", courseAttr=" + "T" +
                ", speed=" + speed +
                ", speedUnit=" + "N" +
                '}';
    }
}
