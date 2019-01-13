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

            this.em.getTransaction().begin();

            BikeRentEntity be = new BikeRentEntity();
            be.setUser_id(request.user_id);
            be.setBike_id(request.bike_id);
            be.setStart_station(request.start_station);
            be.setStart_time(request.start_time);

            this.em.persist(be);
            this.em.getTransaction().commit();
            this.em.refresh(be);
            return Response.status(Response.Status.CREATED).build();


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
            //TypedQuery<ProfileEntity> pe = this.em.createQuery("SELECT p FROM ProfileEntity p WHERE p.user_uuid = :user ORDER BY p.created_at DESC LIMIT 1", ProfileEntity.class);
            //TypedQuery<BikeRent> be = this.em.createQuery("SELECT b FROM BikeRentResource b WHERE b.user_id = :user LIMIT 1", BikeRent.class);
            // TODO: Get end_time & end_station_id from request
            // TODO: Calculate total, with_vat & write everything to DB


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
