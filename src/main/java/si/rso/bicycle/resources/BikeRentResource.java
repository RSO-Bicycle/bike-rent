package si.rso.bicycle.resources;

import com.kumuluz.ee.streaming.common.annotations.StreamProducer;
import org.apache.kafka.clients.producer.Producer;
import si.rso.bicycle.entity.BikeRentEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.logging.Logger;


/**
 * @author Žiga Kokelj
 * @since 1.0.0
 */

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/rent")
public class BikeRentResource {
    private static final Logger log = Logger.getLogger(BikeRentResource.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    @StreamProducer
    private Producer producer;


    @GET
    @Path("/test")
    public Response testRent(@Valid AllValid request){
        return Response.status(77).build();
    }


    @POST
    @Path("/rent/start")
    public Response startRent(@Valid StartRent request) {
        //create new bike rent entry
        try{

            //TODO: Write to the database
            //TODO: Send API request to Billing service!

            //POST call to billing service
            Client client = ClientBuilder.newClient();
            WebTarget resource = client.target("http://localhost:8080").path("billing/start")
                    .queryParam("user_id", 1)
                    .queryParam("borrow_id", 5)
                    .queryParam("start_time", 5)
                    .queryParam("start_station_id", 1)
                    .queryParam("rate", 1.2)
                    .queryParam("vat", 0.22)
                    .queryParam("currency", "EUR");
            Invocation.Builder billingRequest = resource.request();
            billingRequest.accept(MediaType.APPLICATION_JSON);

            Response billingResponse = billingRequest.get();

            if (billingResponse.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                System.out.println("Success! " + billingResponse.getStatus());
                System.out.println(billingResponse.getEntity());
            } else {
                System.out.println("ERROR! " + billingResponse.getStatus());
                System.out.println(billingResponse.getEntity());
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path("/rent/stop")
    public Response stopRent(@Valid StopRent request) {
        //create new bike rent entry
        try{

            //TODO: Update the database
            //TODO: Send API request to Billing service!


        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

}


class StartRent {
    @NotNull(message = "user_id cannot be omitted")
    public Integer user_id;

    @NotNull(message = "bike_id cannot be omitted")
    public Integer bike_id;

    @NotNull(message = "start_station cannot be omitted")
    public Integer start_station;

    @NotNull(message = "start_time cannot be omitted")
    public Date start_time;

}

class StopRent {
    @NotNull(message = "id cannot be omitted")
    public Integer id;

    @NotNull(message = "stop_time cannot be omitted")
    public Date  stop_time;

    @NotNull(message = "end_station cannot be omitted")
    public Integer end_station;

}

class AllValid {
}
