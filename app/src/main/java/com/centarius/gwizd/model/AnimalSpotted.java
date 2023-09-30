package com.centarius.gwizd.model;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AnimalSpotted {
    private String animalType;
    private AnimalStatus animalStatus;
    private String tips;  // Tips on what to do
    private boolean isHurt;  // Is the animal hurt?
    private String imageName;  // URL to the image of the animal
    private Location location;  // Location where the animal was spotted
    private String userId;  // User who reported it

    private String timestamp;

    public AnimalSpotted(String animalType, AnimalStatus animalStatus, String tips, boolean isHurt, String imageName, Location location, String userId, String timestamp) {
        this.animalType = animalType;
        this.animalStatus = animalStatus;
        this.tips = tips;
        this.isHurt = isHurt;
        this.imageName = imageName;
        this.location = location;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public AnimalSpotted() {
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public void setAnimalStatus(AnimalStatus animalStatus) {
        this.animalStatus = animalStatus;
    }

    public void setTips(String tips) {
        this.tips = tips;
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

    public String getTips() {
        return tips;
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
                ", tips='" + tips + '\'' +
                ", isHurt=" + isHurt +
                ", imageName='" + imageName + '\'' +
                ", location=" + location +
                ", userId='" + userId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public enum AnimalStatus {
        ANIMAL_WILD,
        ANIMAL_DEAD,
        ANIMAL_OWNED
    }
}
