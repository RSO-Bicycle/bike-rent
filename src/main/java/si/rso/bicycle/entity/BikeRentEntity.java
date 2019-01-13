package si.rso.bicycle.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
@Table(name = "bike_rents")
public class BikeRentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "bike_id")
    private Integer bike_id;

    @Column(name = "start_station")
    private Integer start_station;

    @Column(name = "end_station")
    private Integer end_station;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private @NotNull(message = "start_time cannot be omitted") Date start_time;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date end_time;



    /*
    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getBike_id() {
        return bike_id;
    }

    public void setBike_id(Integer bike_id) {
        this.bike_id = bike_id;
    }

    public Integer getStart_station() {
        return start_station;
    }

    public void setStart_station(Integer start_station) {
        this.start_station = start_station;
    }

    public Integer getEnd_station() {
        return end_station;
    }

    public void setEnd_station(Integer end_station) {
        this.end_station = end_station;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
}