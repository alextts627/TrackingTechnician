package model;


import java.util.LinkedHashMap;
/*See JavaDoc for more info, but has all the benefits of a Map (key's and
 * values, and a linked list.
 * 
 * The list is linked by insert order, so it will automatically be sorted by 
 * insert_date, which will be useful when printing stuff out.
 */

public class History extends LinkedHashMap {

    public History() {
        /*Nothing to do here*/
    }

    public void addToHistory(String userName, Shipment shipment) {
        boolean success = DBAccess.addShipment(userName, shipment);
        /*
         * Error needs to check for false and throw an exception.
         * The calling servlet needs to handle that exception by
         * redirecting the user to "error.jsp" or something
         * along those lines.
         */
        super.put(shipment.getTrackingNumber(), shipment);
    }

    public void addShipment(String userName, String trackingNumber) {
        Shipment shipment = new Shipment(trackingNumber);
        addToHistory(userName, shipment);
    }

    public boolean removeShipment(String userName, String trackingNumber){
        boolean success = DBAccess.removeShipment(userName, trackingNumber);
        /*
         * Error needs to check for false and throw an exception.
         * The calling servlet needs to handle that exception by
         * redirecting the user to "error.jsp" or something
         * along those lines.
         */
        super.remove(trackingNumber);
        return success;
    }
    
    public Shipment getShipment(String TrackingNumber){
        return (Shipment) super.get(TrackingNumber);
    }
    
    public static void main(String[] args) {
    }
}
