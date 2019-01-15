package si.rso.bicycle.resources;

import com.kumuluz.ee.streaming.common.annotations.StreamProducer;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.kafka.clients.producer.Producer;
import org.json.JSONObject;
import si.rso.bicycle.entity.BikeRentEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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
    public Response testRent(){
        return Response.status(77).build();
    }


    @POST
    @Path("/start")
    public Response startRent(@Valid StartRent request) {
        //create new bike rent entry
        try{
            this.em.getTransaction().begin();

            BikeRentEntity br = new BikeRentEntity();
            br.setUser_id(request.user_id);
            br.setBike_id(request.bike_id);
            br.setStart_station(request.start_station);
            br.setStart_time(new Date());


            this.em.persist(br);
            this.em.getTransaction().commit();
            this.em.refresh(br);

            String jsonString = new JSONObject()
                    .put("user_id", br.getUser_id())
                    .put("borrow_id", br.getId())
                    .put("start_station_id", br.getStart_station())
                    .toString();

            HttpResponse<JsonNode> response = Unirest.post("https://http://billing.bicycle/start")
                    .body(jsonString)
                    .asJson();

            return Response.status(Response.Status.CREATED).build();


        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path("/stop")
    public Response stopRent(@Valid StopRent request) {
        //create new bike rent entry
        try{

            //Update the database
            TypedQuery<BikeRentEntity> tq = this.em.createQuery("SELECT b FROM BikeRentEntity b WHERE b.id = :id", BikeRentEntity.class);
            tq.setParameter("id", request.id);

            BikeRentEntity br = tq.getSingleResult();

            br.setEnd_time(new Date());
            br.setEnd_station(request.end_station);

            this.em.persist(br);
            this.em.getTransaction().commit();
            this.em.refresh(br);

            //Send API request to Billing service!
            String jsonString = new JSONObject()
                    .put("borrow_id", br.getId())
                    .put("stop_station_id", request.end_station)
                    .toString();

            HttpResponse<JsonNode> response = Unirest.post("https://http://billing.bicycle/stop")
                    .body(jsonString)
                    .asJson();

            return Response.status(Response.Status.CREATED).build();

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

}

class StopRent {
    @NotNull(message = "id cannot be omitted")
    public Integer id;

    @NotNull(message = "end_station cannot be omitted")
    public Integer end_station;

}

class AllValid {
}
