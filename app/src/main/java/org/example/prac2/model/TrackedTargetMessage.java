package org.example.prac2.model;

import java.sql.Timestamp;

public class TrackedTargetMessage {

    private Timestamp msgRecTime;
    private Long msgTime;
    private Integer targetNumber;
    private Double distance;
    private Double bearing;
    private Double course;
    private Double speed;
    private TargetType type;
    private TargetStatus status;
    private IFF iff;


    public TrackedTargetMessage(Timestamp msgRecTime, Long msgTime, Integer targetNumber, Double distance, Double bearing, Double course, Double speed, TargetType type, TargetStatus status, IFF iff) {
        this.msgRecTime = msgRecTime;
        this.msgTime = msgTime;
        this.targetNumber = targetNumber;
        this.distance = distance;
        this.bearing = bearing;
        this.course = course;
        this.speed = speed;
        this.type = type;
        this.status = status;
        this.iff = iff;
    }

    public Timestamp getMsgRecTime() {
        return msgRecTime;
    }

    public void setMsgRecTime(Timestamp msgRecTime) {
        this.msgRecTime = msgRecTime;
    }

    public Long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Long msgTime) {
        this.msgTime = msgTime;
    }

    public Integer getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(Integer targetNumber) {
        this.targetNumber = targetNumber;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getBearing() {
        return bearing;
    }

    public void setBearing(Double bearing) {
        this.bearing = bearing;
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

    public TargetType getType() {
        return type;
    }

    public void setType(TargetType type) {
        this.type = type;
    }

    public TargetStatus getStatus() {
        return status;
    }

    public void setStatus(TargetStatus status) {
        this.status = status;
    }

    public IFF getIff() {
        return iff;
    }

    public void setIff(IFF iff) {
        this.iff = iff;
    }

    @Override
    public String toString() {
        return "TrackedTargetMessage{" +
                "msgRecTime=" + getMsgRecTime() +
                ", msgTime=" + getMsgTime() +
                ", targetNumber=" + targetNumber +
                ", distance=" + distance +
                ", bearing=" + bearing +
                ", course=" + course +
                ", speed=" + speed +
                ", type=" + type +
                ", status=" + status +
                ", iff=" + iff +
                '}';
    }
}
