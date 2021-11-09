package fiuba.tpp.reactorapp.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="BREAK_CURVES_DATA")
public class BreakCurvesData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private EModel model;

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    private Process process;

    @Column(columnDefinition = "TEXT")
    private String data;

    private Date uploadDate;

    @Column(columnDefinition = "boolean default false")
    private Boolean free;

    @Column(columnDefinition = "boolean default false")
    private Boolean baseline;

    public BreakCurvesData() {
    }

    public BreakCurvesData(EModel model, String data, Date uploadDate) {
        this.model = model;
        this.data = data;
        this.uploadDate = uploadDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EModel getModel() {
        return model;
    }

    public void setModel(EModel model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public Boolean getBaseline() {
        return baseline;
    }

    public void setBaseline(Boolean baseline) {
        this.baseline = baseline;
    }
}
