
import java.util.List;
import model.Shipment;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Read
 */
public class shipmentTest {
    public static void main(String[] args) {
        Shipment shipment = new Shipment("1ZWF25470315141649");
        System.out.println("Start Location: " + shipment.getStartLocation());
        System.out.println("Current Location: " + shipment.getCurrentLocation());
        System.out.println("Final Location: " + shipment.getFinalLocation());
        
        List<String> transitLocations = shipment.getTransitLocations();
        for(int i = transitLocations.size() - 1; i >=0; i--){
            System.out.println("Location: " + transitLocations.get(i));
        }
        
        System.out.println("Status: " + shipment.getStatus());
        System.out.println("Date added to trackingTechnician: " + shipment.getDateInput());
    }
}
