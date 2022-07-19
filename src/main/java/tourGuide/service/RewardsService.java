package tourGuide.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
//import java.util.concurrent.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import java.util.stream.Collectors;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@Service
public class RewardsService {

	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral; //fournisseur externe qui determine le nombre de recompenses offertes
	
	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	public List<UserReward> calculateRewards(User user) {
		List<Attraction> attractions = gpsUtil.getAttractions();
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<VisitedLocation> locations = new CopyOnWriteArrayList<>(userLocations);
		List<UserReward> rewardsList = new ArrayList<>();
		for(VisitedLocation visitedLocation : locations) {
			for (Attraction attraction : attractions) {
				if (nearAttraction(visitedLocation, attraction)) {
					if (user.getUserRewards().stream().noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName))) {
						UserReward userReward = new UserReward(visitedLocation, attraction, 0);
						user.addUserReward(userReward);
					}
				}
			}
		}
		return user.getUserRewards();
	}
	
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;

	}


	//RewardsCentral permet le calcul des recompenses des user
	/**
	 * 
	 * @param attraction
	 * @param user
	 * @return
	 */
	public int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	/**
	 *
	 * @param loc1
	 * @param loc2
	 * @return
	 */
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

}
