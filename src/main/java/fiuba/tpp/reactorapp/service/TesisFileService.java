package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.entities.TesisFile;
import fiuba.tpp.reactorapp.model.dto.FileTemplateDTO;
import fiuba.tpp.reactorapp.model.exception.FileNotFoundException;
import fiuba.tpp.reactorapp.model.exception.TesisNotFoundException;
import fiuba.tpp.reactorapp.model.request.TesisFileRequest;
import fiuba.tpp.reactorapp.model.response.TesisFileResponse;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
import fiuba.tpp.reactorapp.repository.TesisFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TesisFileService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private TesisFileRepository tesisFileRepository;


    public TesisFileResponse uploadFile(TesisFileRequest request) throws IOException {
        TesisFile tesisFile = generateTesisFromRequest(request);
        tesisFileRepository.save(tesisFile);
        return new TesisFileResponse(tesisFile);
    }

    public TesisFileResponse changeFile(Long id, TesisFileRequest request) throws IOException {
        Optional<TesisFile> tesis = tesisFileRepository.findById(id);
        if(tesis.isPresent()){
            TesisFile file = tesis.get().update(request);
            file.setProcesses(getProcessesFromTesis(request.getProcesosIds()));
            tesisFileRepository.save(file);
            return new TesisFileResponse(file);
        }
        throw new TesisNotFoundException();
    }

    @Transactional
    public void deleteFile(Long id){
        Optional<TesisFile> tesis = tesisFileRepository.findById(id);
        if(tesis.isPresent()){
            tesisFileRepository.delete(tesis.get());
        }else{
            throw new TesisNotFoundException();
        }
    }

    private TesisFile generateTesisFromRequest(TesisFileRequest request) throws IOException {
        TesisFile tesisFile = new TesisFile(request);
        tesisFile.setProcesses(getProcessesFromTesis(request.getProcesosIds()));
        return tesisFile;
    }


    private List<Process> getProcessesFromTesis(String ids){
        if(ids != null && !ids.isEmpty()){
            String[] idsArray = ids.split(",");
            List<Long> processesIds = new ArrayList<>();
            for (String id: idsArray) {
                processesIds.add(Long.valueOf(id));
            }
            return processRepository.getProcessesByIds(processesIds);
        }
        return new ArrayList<>();
    }

    public FileTemplateDTO downloadFile(Long id){
        Optional<TesisFile> tesis = tesisFileRepository.findById(id);
        if(tesis.isPresent()){
            TesisFile file =  tesis.get();
            return new FileTemplateDTO(new ByteArrayResource(file.getData()),buildFileName(file),file.getData().length);
        }
        throw new FileNotFoundException();
    }

    private String buildFileName(TesisFile file){
        return file.getName().concat(".").concat(file.getType());
    }
}
