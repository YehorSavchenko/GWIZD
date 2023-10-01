package com.centarius.gwizd.model;

import androidx.annotation.NonNull;

public class AnimalSpotted {

    public AnimalSpotted() {
    }

    private String animalType;

    private AnimalStatus animalStatus;
    private boolean isHurt;
    private String imageName;
    private Location location;
    private String userId;
    private String timestamp;

    public AnimalSpotted(String animalType, AnimalStatus animalStatus, boolean isHurt, String imageName, Location location, String userId, String timestamp) {
        this.animalType = animalType;
        this.animalStatus = animalStatus;
        this.isHurt = isHurt;
        this.imageName = imageName;
        this.location = location;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public void setAnimalStatus(AnimalStatus animalStatus) {
        this.animalStatus = animalStatus;
    }

    public void setHurt(boolean hurt) {
        isHurt = hurt;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAnimalType() {
        return animalType;
    }

    public AnimalStatus getAnimalStatus() {
        return animalStatus;
    }

    public boolean isHurt() {
        return isHurt;
    }

    public String getImageName() {
        return imageName;
    }

    public Location getLocation() {
        return location;
    }

    public String getUserId() {
        return userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "AnimalSpotted{" +
                "animalType='" + animalType + '\'' +
                ", animalStatus=" + animalStatus +
                ", isHurt=" + isHurt +
                ", imageName='" + imageName + '\'' +
                ", location=" + location +
                ", userId='" + userId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimalSpotted that = (AnimalSpotted) o;

        if (isHurt != that.isHurt) return false;
        if (!animalType.equals(that.animalType)) return false;
        if (animalStatus != that.animalStatus) return false;
        if (!imageName.equals(that.imageName)) return false;
        if (!location.equals(that.location)) return false;
        if (!userId.equals(that.userId)) return false;
        return timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        int result = animalType.hashCode();
        result = 31 * result + animalStatus.hashCode();
        result = 31 * result + (isHurt ? 1 : 0);
        result = 31 * result + imageName.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + timestamp.hashCode();
        return result;
    }

    public enum AnimalStatus {
        ANIMAL_WILD,
        ANIMAL_DEAD,
        ANIMAL_OWNED,
        NO_STATUS
    }
}
