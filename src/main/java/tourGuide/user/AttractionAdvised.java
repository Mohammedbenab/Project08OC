package tourGuide.user;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import rewardCentral.RewardCentral;
import tourGuide.service.RewardsService;

import java.io.Serializable;
import java.util.List;

public class AttractionAdvised implements Serializable {

    private String name;
    private Location location;
    private double distance;
    private int rewardPoints;

    public AttractionAdvised() {
    }

    public AttractionAdvised(String name, Location location, double distance, int rewardPoints) {
        this.name = name;
        this.location = location;
        this.distance = distance;
        this.rewardPoints = rewardPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public void setLocation(double latitude, double longitude) {
        this.location = new Location(latitude, longitude);
    }
}
