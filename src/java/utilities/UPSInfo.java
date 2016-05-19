package utilities;

import com.ups.xmlschema.xoltws.track.v2.*;
import com.ups.xmlschema.xoltws.upss.v1.UPSSecurity;
import com.ups.xmlschema.xoltws.upss.v1.UPSSecurity.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ups.wsdl.xoltws.track.v2.TrackErrorMessage;
import com.ups.wsdl.xoltws.track.v2.TrackPortType;
import com.ups.wsdl.xoltws.track.v2.TrackService;
import com.ups.xmlschema.xoltws.common.v1.RequestType;
import java.util.LinkedList;

public class UPSInfo {
  
    //required variables for UPS api

    private UPSSecurity upsSec;
    private ServiceAccessToken key;
    private UsernameToken userName;
    private TrackResponse trackResponse;
    private TrackService upsService;
    private TrackRequest trackRequest;
    private TrackPortType trackPort;
    //variables need specificly for this class
    private boolean initialized;
    private String final_location;
    private String current_location;
    private String start_location;
    private String status;
    private boolean delivered;
    private List<String> transitLocations;
    private String trackingNumber;
    
    public UPSInfo(String trackingNumber){
        this.trackingNumber = trackingNumber;
    }

    private void initialize() {
        //Initizlize variables needed by the UPS API
        upsSec = new UPSSecurity();
        key = new ServiceAccessToken();
        userName = new UsernameToken();
        upsService = new TrackService();
        trackRequest = new TrackRequest();
        //Do some api specific stuff
        userName.setUsername("rsprabery"); //api userName
        userName.setPassword("hWndtx3gNiZ5u8D"); //api password
        upsSec.setServiceAccessToken(key);
        upsSec.setUsernameToken(userName);
        key.setAccessLicenseNumber("9C89B38FE55B29B0"); //api key
        //Set the tracking number passed into the constructor
        trackRequest.setInquiryNumber(trackingNumber);
        RequestType request = new RequestType();
        List<String> requestOption = request.getRequestOption();
        requestOption.add("1"); //option specific to the type of packages
        trackRequest.setTrackingOption("01");//specific to the type of info we want
        trackRequest.setRequest(request);
        trackPort = upsService.getTrackPort();

        //initialize class specific variables
        initialized = false;
        final_location = null;
        current_location = null;
        start_location = null;
        status = "In route";
        delivered = false;
        transitLocations = new LinkedList<String>();
        initialized = true;
    }

    private void getData() {
        initialize();

        try {
            trackResponse = trackPort.processTrack(trackRequest, upsSec);

            List<ShipmentType> shipmentList = trackResponse.getShipment();

            for (ShipmentType shipment : shipmentList) {

                List<ShipmentAddressType> shipmentAddressList = shipment.getShipmentAddress();
                for (ShipmentAddressType location : shipmentAddressList) {
                    AddressType address = location.getAddress();
                    String city = address.getCity();
                    String state = address.getStateProvinceCode();
//                    System.out.println("City: " + city);
//                    System.out.println("State Code:" + state);
                    CommonCodeDescriptionType description = location.getType();
//                    System.out.println("Description Code: " + description.getCode());
//                    System.out.println("Description Description: " + description.getDescription() + "\n");

                    if (description.getCode().equals("01")) {//then this is the shipper address
                        start_location = city + "," + state;
                    }
                    if (description.getCode().equals("02")) {
                        final_location = city + "," + state;
                    }
                }

//                System.out.println("Start Location: " + start_location);
//                System.out.println("Final Location: " + final_location + "\n");

//                System.out.println("Service Description: " + shipment.getService().getDescription()); //Ground, air, whatever             

                List<PackageType> packageList = shipment.getPackage();
                System.out.println("Package List: " + packageList);
                for (PackageType pt : packageList) {

                    
                    if (pt.getTrackingNumber().equals(trackingNumber)) {//TODO: check the tracking number verus the one entered.
                        List<ActivityType> activityList = pt.getActivity();
                        for (ActivityType activity : activityList) {
//                            System.out.println("City: " + activity.getActivityLocation().getAddress().getCity());
//                            System.out.println("Activity Location Code: " + activity.getActivityLocation().getCode());
//                            System.out.println("Description: " + activity.getActivityLocation().getDescription());
//                            System.out.println("Date:" + activity.getDate());
//                            System.out.println("Time: " + activity.getTime());
//                            System.out.println("Status Code: " + activity.getStatus().getCode());
//                            System.out.println("Status Type: " + activity.getStatus().getType());
//                            System.out.println("");

                            String city = activity.getActivityLocation().getAddress().getCity();
                            String state = activity.getActivityLocation().getAddress().getStateProvinceCode();
                            if (activity.getStatus().getType().equals("I")) {//Then this is a transit location
                                String location = city + "," + state;
                                if (transitLocations.isEmpty() && !delivered) {
                                    current_location = city + "," + state;
                                }
                                if (!transitLocations.contains(location)) {
                                    transitLocations.add(location);
                                }
                            }
                            if (activity.getStatus().getType().equals("D")) {//Then the package has been delivered
                                if (final_location.contains(city)) {
                                    delivered = true;
                                    current_location = city + "," + state;
                                } else {
                                    System.err.println("Something is messed up with the delivery status portion of your code");
                                }
                            }


                        }

//                        System.out.println("Delivery Date: " + pt.getDeliveryDetail());
                    }

//                    System.out.println(pt.getTrackingNumber());
//                    System.out.println(pt.getActivity().get(1).getActivityLocation().getAddress().getCity());
//                    System.out.println(pt.getDeliveryDetail());

//                    List<PackageAddressType> packageAddressList = pt.getPackageAddress();
//                    for (PackageAddressType packageAddress : packageAddressList) {
//                        AddressType address = packageAddress.getAddress();
//                        System.out.println("PackageAddress: " + address.getCity() + "\n");
//                    }
                }
            }

//            System.out.println("In Order read in: ");
//            for (int i = 0; i < transitLocations.size(); i++) {
//                String location = transitLocations.get(i);
//                System.out.println("Transit Location: " + location);
//            }
//
//            System.out.println("Reverse Order:");
//            for (int i = (transitLocations.size() - 1); i >= 0; i--) {
//                String location = transitLocations.get(i);
//                System.out.println("Transit Location: " + location);
//            }
//
//            if (delivered) {
//                status = "Delivered";
//            }
//            System.out.println("Status: " + status);
//            System.out.println("Current Location: " + current_location);

        } catch (TrackErrorMessage ex) {
            System.err.println("Something is not right with the ups api\n");
            //Logger.getLogger(WSDLUsage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean isDelivered() {
        if (!initialized) {
            getData();
        }
        return delivered;
    }

    public String getStartLocation() {
        if (!initialized) {
            getData();
        }
        return start_location;
    }

    public String getCurrentLocation() {
        if (!initialized) {
            getData();
        }
        return current_location;
    }

    public String getFinalLocation() {
        if (!initialized) {
            getData();
        }
        return final_location;
    }
    
    public List<String> getTransitLocations(){
        return transitLocations;
    }
}
