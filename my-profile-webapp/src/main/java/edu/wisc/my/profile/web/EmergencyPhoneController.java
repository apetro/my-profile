package edu.wisc.my.profile.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import edu.wisc.my.profile.model.TypeValue;
import edu.wisc.my.profile.service.EmergencyPhoneNumberService;

@Controller
@RequestMapping("/phoneNumber")
public class EmergencyPhoneController {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private EmergencyPhoneNumberService emPhoneNumberService;
    
    @RequestMapping(method = RequestMethod.GET, value="/get.json")
    public @ResponseBody void getPhoneNumber(HttpServletRequest request, HttpServletResponse response){
        try {
            String username = request.getHeader("uid");
            if(StringUtils.isBlank(username)){
                logger.warn("User hit get emergency phone number service w/o username set");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            TypeValue[] phoneNumbers =  emPhoneNumberService.getEmergencyPhoneNumber(username);
            Gson gson = new Gson();
            response.getWriter().write("{\"emergencyPhoneNumbers\":"+gson.toJson(phoneNumbers)+"}");
            response.setContentType("application/json");
        } catch (Exception e) {
            logger.error("Issue happened during lookup", e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/set")
    public @ResponseBody void setPhoneNumber(HttpServletRequest request, @RequestBody TypeValue[] phoneNumber, HttpServletResponse response){
        try {
          final String uid = request.getHeader("uid");
            emPhoneNumberService.setEmergencyPhoneNumber(uid, (TypeValue[]) phoneNumber);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}