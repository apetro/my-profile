package edu.wisc.my.profile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.wisc.my.profile.dao.EmergencyPhoneNumberDao;
import edu.wisc.my.profile.model.TypeValue;

@Service
public class EmergencyPhoneNumberServiceImpl implements EmergencyPhoneNumberService {
    
    @Autowired
    private EmergencyPhoneNumberDao dao;

    @Override
    public TypeValue[] getEmergencyPhoneNumber(String netId) {
        return dao.getPhoneNumber(netId);
    }

    @Override
    public TypeValue[] setEmergencyPhoneNumber(String netId, TypeValue[] phoneNumber) throws Exception{
        return dao.setPhoneNumber(netId, phoneNumber);
    }
    
    
}