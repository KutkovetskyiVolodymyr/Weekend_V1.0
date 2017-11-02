package weekend.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "holidays")
public class Weekend {

    private Long id;
    private Date weekend;



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getWeekend() {
        return weekend;
    }

    public void setWeekend(Date weekend) {
        this.weekend = weekend;
    }
}

