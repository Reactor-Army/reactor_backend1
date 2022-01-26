package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.entities.BreakCurvesData;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BreakCurvesDataResponse {

    private  Long id;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("modelo")
    private EModelResponse model;

    private Object request;

    private Object response;

    @JsonProperty("sistema")
    private ProcessResponse processResponse;

    @JsonProperty("fecha")
    private Date uploadDate;

    @JsonProperty("esLineaBase")
    private boolean isBaseline;

    @JsonIgnore
    private double co;


    public BreakCurvesDataResponse(BreakCurvesData data, Object request, Object response, double co){
        this.id = data.getId();
        this.name = data.getName();
        if(data.getProcess() != null){
            this.processResponse = new ProcessResponse(data.getProcess());
        }
        this.request = request;
        this.response = response;
        this.uploadDate = data.getUploadDate();
        this.model = new EModelResponse(data.getModel());
        this.isBaseline = data.getBaseline();
        this.co = co;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ProcessResponse getProcessResponse() {
        return processResponse;
    }

    public void setProcessResponse(ProcessResponse processResponse) {
        this.processResponse = processResponse;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }


    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public EModelResponse getModel() {
        return model;
    }

    public void setModel(EModelResponse model) {
        this.model = model;
    }

    public double getCo() {
        return co;
    }

    public void setCo(double co) {
        this.co = co;
    }
}
