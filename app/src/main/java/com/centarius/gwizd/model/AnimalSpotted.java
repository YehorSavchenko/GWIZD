package com.centarius.gwizd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimalSpotted {
    private String tips;  // Tips on what to do
    private boolean isHurt;  // Is the animal hurt?
    private String imageUrl;  // URL to the image of the animal
    private Location location;  // Location where the animal was spotted
    private String userId;  // User who reported it
}
