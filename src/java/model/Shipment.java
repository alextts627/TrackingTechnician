package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import utilities.UPSInfo;

public class Shipment {

    private static String FEDEXADDRESS;
    private static String USPSADDRESS;
    private static String UPSADDRESS;
    private String trackingNumber;
    private boolean delivered;
    private String carrier;
    private String date_input;
    private String start_location;
    private String current_location;
    private String final_location;
    /* IMPORTANT:
     * transitLocations is in order from final location to starting location,
     * will need to be parsed backwardings when printing the history
     */
    private List<String> transitLocations;
    private String googleMapURL;
    public static final int USPS_IN1 = 25;
    public static final int USPS_IN2 = 10;
    public static final int FEDEX_IN1 = 12;

    private void parseTrackingNumber() {
        /*
         * Sets the carrier variable.
         * carrier = "something"
         * carrier = FEDEX | UPS | USPS
         */
        String temp = trackingNumber.substring(0, 2);
        if (temp.contains("1Z")) {
            carrier = "UPS";
//        } else if (temp.contains("EA") || temp.contains("EC") || temp.contains("CP") || temp.contains("RA")) {
//            carrier = "USPS";
//        } else if ((temp.length() == USPS_IN1) || (temp.length() == USPS_IN2)) {
//            carrier = "USPS";
//        } else if (temp.length() == FEDEX_IN1) {
//            carrier = "FEDEX";
        } else {
            carrier = "NOT FOUND";
        }
    }

    private void parseCarrierSite() {
        UPSInfo upsUtility = new UPSInfo(trackingNumber);
        delivered = upsUtility.isDelivered();
        start_location = upsUtility.getStartLocation();
        current_location = upsUtility.getCurrentLocation();
        final_location = upsUtility.getFinalLocation();
        transitLocations = upsUtility.getTransitLocations();
        /*TODO: 
         * set date_input
         */
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        date_input = dateFormat.format(date);

    }

    private void setStaticAddresses() {
        /*TODO: get webaddresses for the following*/
        FEDEXADDRESS = "";
        USPSADDRESS = "";
        UPSADDRESS = "";
    }

    public Shipment() {
        /*Function that doesn't initialize anything
         * so that it can be used in the DATABASE
         */
        //These must be initialized becuase they are static
        parseCarrierSite();
        setStaticAddresses();
    }

    public Shipment(String trackingNumber) {
        //Build a shipment from a tracking number
        setStaticAddresses();
        this.trackingNumber = trackingNumber;
        parseTrackingNumber();
        parseCarrierSite();
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getStatus() {
        if (delivered) {
            return "Delivered";
        } else {
            return "In Transit";
        }
    }

    public String getCarrier() {
        return carrier;
    }

    public String getDateInput() {
        return date_input;
    }

    public String getStartLocation() {
        return start_location;
    }

    public String getCurrentLocation() {
        return current_location;
    }

    public String getFinalLocation() {
        return final_location;
    }

    public void setGoogleMapURL(String url) {
        googleMapURL = url;
    }

    public String getGoogleMapURL() {
        int num = 2;
        StringBuilder url = new StringBuilder();
        url.append("http://maps.googleapis.com/maps/api/staticmap?");
        url.append("size=600x600&maptype=roadmap&markers=color:blue|label:");
        url.append(1);
        url.append("|");
        url.append(getStartLocation());
        StringBuilder path = new StringBuilder();
        path.append("&path=color:0x0000ff|weight:5|");
        path.append(getStartLocation());
        path.append("|");

        for (int i = transitLocations.size() - 1; i >= 0; i--) {
            url.append("&markers=color:green|label:");
            url.append(num);
            num++;
            url.append("|");
            url.append(transitLocations.get(i));
            path.append(transitLocations.get(i));
            path.append("|");
        }
        url.append("&markers=color:red|label:");
        url.append(num);
        url.append("|");
        num++;
        url.append(getCurrentLocation());
        if (!getCurrentLocation().equals(getFinalLocation())) {
            url.append("&markers=color:purple|label:");
            url.append(num);
            url.append("|");
            url.append(getFinalLocation());
        }
        
        path.append(getCurrentLocation());
        url.append(path.toString());
        url.append("&sensor=false");

        return url.toString();
    }

    public List<String> getTransitLocations() {
        return transitLocations;
    }
}
