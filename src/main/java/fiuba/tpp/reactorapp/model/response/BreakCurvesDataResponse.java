package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.entities.BreakCurvesData;
import fiuba.tpp.reactorapp.model.dto.BreakCurvesThomasDTO;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BreakCurvesDataResponse {

    private  Long id;

    @JsonProperty("nombre")
    private String name;

    private ThomasRequest thomasRequest;

    private ThomasResponse thomasResponse;

    private YoonNelsonRequest yoonNelsonRequest;

    private YoonNelsonResponse yoonNelsonResponse;


    @JsonProperty("sistema")
    private ProcessResponse processResponse;

    @JsonProperty("fecha")
    private Date uploadDate;


    public BreakCurvesDataResponse(BreakCurvesData data, BreakCurvesThomasDTO dto){
        this.id = data.getId();
        this.name = data.getName();
        if(data.getProcess() != null){
            this.processResponse = new ProcessResponse(data.getProcess());
        }
        this.thomasRequest = dto.getRequest();
        this.thomasResponse = dto.getResponse();

        this.uploadDate = data.getUploadDate();
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

    public ThomasRequest getThomasRequest() {
        return thomasRequest;
    }

    public void setThomasRequest(ThomasRequest thomasRequest) {
        this.thomasRequest = thomasRequest;
    }

    public ThomasResponse getThomasResponse() {
        return thomasResponse;
    }

    public void setThomasResponse(ThomasResponse thomasResponse) {
        this.thomasResponse = thomasResponse;
    }

    public YoonNelsonRequest getYoonNelsonRequest() {
        return yoonNelsonRequest;
    }

    public void setYoonNelsonRequest(YoonNelsonRequest yoonNelsonRequest) {
        this.yoonNelsonRequest = yoonNelsonRequest;
    }

    public YoonNelsonResponse getYoonNelsonResponse() {
        return yoonNelsonResponse;
    }

    public void setYoonNelsonResponse(YoonNelsonResponse yoonNelsonResponse) {
        this.yoonNelsonResponse = yoonNelsonResponse;
    }
}
