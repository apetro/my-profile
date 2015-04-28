package edu.wisc.my.profile.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.wisc.hr.dao.bnsemail.BusinessEmailUpdateDao;
import edu.wisc.hr.dao.person.ContactInfoDao;
import edu.wisc.hr.dm.bnsemail.PreferredEmail;
import edu.wisc.hr.dm.person.Address;
import edu.wisc.hr.dm.person.PersonInformation;
import edu.wisc.hr.dm.prefname.PreferredName;
import edu.wisc.hr.service.PreferredNameService;
import edu.wisc.my.profile.isis.dao.IsisDao;
import edu.wisc.my.profile.model.ContactAddress;
import edu.wisc.my.profile.model.ContactInformation;
import edu.wisc.my.profile.model.TypeValue;

@Service
public class ContactInformationServiceImpl implements ContactInformationService {
  
  protected static final Logger logger = LoggerFactory.getLogger(ContactInformationServiceImpl.class);
  
  private String businessEmailRolesPreferences = "businessEmailRoles";
  private static enum AddrTypes {Home, Office};
  private ContactInfoDao contactInfoDao;
  private BusinessEmailUpdateDao businessEmailUpdateDao;
  private IsisDao isisDao;
  private PreferredNameService preferredNameService;
  
  public void setBusinessEmailRolesPreferences(String businessEmailRolesPreferences) {
      this.businessEmailRolesPreferences = businessEmailRolesPreferences;
  }

  @Autowired
  public void setBusinessEmailUpdateDao(BusinessEmailUpdateDao businessEmailUpdateDao) {
      this.businessEmailUpdateDao = businessEmailUpdateDao;
  }
  
  @Autowired
  public void setIsisDao(IsisDao dao) {
    isisDao = dao;
  }
  
  @Autowired
  public void setPreferredNameService(PreferredNameService service) {
      this.preferredNameService = service;
  }

  @Autowired
  public void setContactInfoDao(ContactInfoDao contactInfoDao) {
      this.contactInfoDao = contactInfoDao;
  }
  
  

  @Override
  public ContactInformation getContactInfo(String username, String emplId, String pvi) {
    ContactInformation contactInformation = new ContactInformation();
    
    try {
      //get basic information from HRS
      PersonInformation personalData = contactInfoDao.getPersonalData(emplId);
      contactInformation.setLegalName(personalData.getName());
      contactInformation.setId(emplId);
      
      //get address info and phone info from HRS
      if(personalData.getOfficeAddress() != null) {
        mapAddressToContactAddress(contactInformation, personalData.getOfficeAddress(), AddrTypes.Office);
      }
  
      if(personalData.getHomeAddress() != null) {
        mapAddressToContactAddress(contactInformation, personalData.getHomeAddress(), AddrTypes.Home);
      }
    } catch (Exception e) {
      logger.warn("Had issue gathering HRS data for " + emplId, e);
    }
    
    //preferred name
    if(!StringUtils.isBlank(pvi)) {
      PreferredName preferredName = preferredNameService.getPreferredName(pvi);
      if(preferredName != null) {
        contactInformation.setPreferredName(preferredName.getFirstName() + (StringUtils.isBlank(preferredName.getMiddleName()) ? "" : " " + preferredName.getMiddleName()));
      }
    }
    
    //preferred email
    PreferredEmail preferedEmail = businessEmailUpdateDao.getPreferedEmail(emplId);
    if(preferedEmail != null && !StringUtils.isBlank(preferedEmail.getEmail())) {
      contactInformation.getEmails().add(new TypeValue(null,preferedEmail.getEmail()));
    }
    
    //student isis information
    ContactAddress address = isisDao.getAddress(emplId);
    if(address != null && address.getAddressLines().size() > 0) {
      contactInformation.getAddresses().add(address);
    }
    
    contactInformation.getPhoneNumbers().addAll(isisDao.getPhone(emplId));
    
    return contactInformation;
  }
  
  private void mapAddressToContactAddress(ContactInformation info, Address address, AddrTypes type) {
    ContactAddress newAddr = new ContactAddress();
    if(!StringUtils.isBlank(address.getRoomNumber())) {
      newAddr.getAddressLines().add("Room " + address.getRoomNumber());
    }
    
    if(!StringUtils.isBlank(address.getAddress1())) {
      newAddr.getAddressLines().add(address.getAddress1());
    }
    if(!StringUtils.isBlank(address.getAddress2())) {
      newAddr.getAddressLines().add(address.getAddress2());
    }
    if(!StringUtils.isBlank(address.getAddress3())) {
      newAddr.getAddressLines().add(address.getAddress3());
    }
    
    newAddr.setCity(address.getCity());
    newAddr.setPostalCode(address.getZip());
    newAddr.setState(address.getState());
    newAddr.setType(type.toString());
    
    info.getAddresses().add(newAddr);
    
    if(!StringUtils.isBlank(address.getPrimaryPhone())) {
      info.getPhoneNumbers().add(new TypeValue(type.toString(),address.getPrimaryPhone()));
      
    }
  }

}
