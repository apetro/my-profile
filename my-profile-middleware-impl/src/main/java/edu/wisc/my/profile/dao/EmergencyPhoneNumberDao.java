package edu.wisc.my.profile.dao;

import edu.wisc.my.profile.model.TypeValue;

public interface EmergencyPhoneNumberDao {
    
    /**
     * Gets a phone number
     * @param netid
     * @return a TypeValue representing a phone number
     */
    TypeValue[] getPhoneNumber(String netId);
    
    
    /**
     * Sets a phone number returning the saved phone number
     * @param netid
     * @param phoneNumber
     * @return the saved phone number
     * @throws Exception
     */
    TypeValue[] setPhoneNumber(String netId, TypeValue[] phoneNumber) throws Exception;
    
}