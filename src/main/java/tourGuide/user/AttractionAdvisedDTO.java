package tourGuide.user;

import gpsUtil.location.Location;

import java.util.List;

public class AttractionAdvisedDTO {
    private Location userLocation;
    private List<AttractionAdvised> attractionList;

    public AttractionAdvisedDTO() {
    }

    public AttractionAdvisedDTO(Location userLocation, List<AttractionAdvised> attractionList) {
        this.userLocation = userLocation;
        this.attractionList = attractionList;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public List<AttractionAdvised> getAttractionList() {
        return attractionList;
    }

    public void setAttractionList(List<AttractionAdvised> attractionList) {
        this.attractionList = attractionList;
    }
}
