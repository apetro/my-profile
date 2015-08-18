package edu.wisc.my.profile.service;

import edu.wisc.my.profile.model.TypeValue;

public interface EmergencyPhoneNumberService{
    
    /**
     * Gets a user emergency Phone Number
     * @param netid the uid or net id of user
     * @return a TypeValue object representing the phone number
     */
    public TypeValue[] getEmergencyPhoneNumber(String netid);
    
    /**
     * Sets the emergency phone number for a given user
     * @param phoneNumber
     * @return the phone number of that individual
     * @throws Exception 
     */
    public TypeValue[] setEmergencyPhoneNumber(String netid, TypeValue[] phoneNumber) throws Exception;
    
}